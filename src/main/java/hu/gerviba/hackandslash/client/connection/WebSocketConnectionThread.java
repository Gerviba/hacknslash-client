package hu.gerviba.hackandslash.client.connection;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.packets.TemplatePacketBuilder;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketConnectionThread extends Thread {

    private String ip;
    private String sessionId;
    private Runnable after;
    
    private IngameWindow ingame;
    
    private LinkedBlockingQueue<Consumer<StompSession>> eventBus = new LinkedBlockingQueue<>();
    
    public WebSocketConnectionThread(String ip, String sessionId, Runnable after) {
        this.setName("WebsocketConnection");
        this.setDaemon(true);
        this.setUncaughtExceptionHandler((thread, exception) -> {
            log.error("Exception in thread " + thread.getName(), exception);
        });
        this.ip = ip;
        this.sessionId = sessionId;
        this.after = after;
    }
    
    public synchronized void appendTask(Consumer<StompSession> task) {
        eventBus.add(task);
    }
    
    @Override
    public void run() {
        try {
            WebsocketHandler websocket = new WebsocketHandler();

            ListenableFuture<StompSession> f = websocket.connect(ip, sessionId);
            // Exception handler egy Consumer-el
            StompSession stomp = f.get();

            log.info("Received session");
            after.run();
            
            Platform.runLater(() -> {
                (ingame = new IngameWindow()).setThisToCurrentWindow();
                
                appendTask(s -> {
                    log.info("Subscribing");
                    subscribeTo(s, "/topic/chat", ingame.getChatComponent()::appendMessage);
                    subscribeTo(s, "/user/topic/chat", ingame.getChatComponent()::appendMessage);
                    subscribeTo(s, "/user/topic/telemetry", ingame::updateTelemetry);
                    subscribeTo(s, "/user/topic/map", ingame::loadMap);
                    subscribeTo(s, "/user/topic/self", ingame.getPlayerInfoComponent()::update);
                    subscribeTo(s, "/topic/skills", ingame.getSkillsComponent()::applySkill);
                    subscribeTo(s, "/user/topic/inventory", ingame.getInventoryComponent()::update);
                    
                    sendJustConnected();

                    requestForSelfInfo();
                });
            });
            
            log.info("Event bus started");
            doPolling(stomp);
            log.error("Event bus stopped working");
            
        } catch (Exception e) {
            log.error("Error in connection thread", e);
        }
    }

    private void sendJustConnected() {
        eventBus.add(stomp -> {
            stomp.send("/app/connected", TemplatePacketBuilder
                    .buildJustConnected(800, 1280, 2));
        });
    }

    private void subscribeTo(StompSession stomp, String channel, Consumer<byte[]> action) {
        log.info("Subscribe to: " + channel);
        stomp.subscribe(channel, new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                Platform.runLater(() -> action.accept((byte[]) o));
            }
            
        });
    }
    
    private void doPolling(StompSession stomp) throws InterruptedException {
        Consumer<StompSession> event;
        List<Consumer<StompSession>> burst = new LinkedList<>();
        while (true) {
            while ((event = eventBus.poll()) != null)
                burst.add(event);
            
            for (Consumer<StompSession> b : burst)
                b.accept(stomp);
            
            burst.clear();
            Thread.sleep(25);
        }
    }
    
    private void scheduleNext() {
        eventBus.add(stomp -> updateTelemetry(stomp));
    }

    private void updateTelemetry(StompSession session) {
        session.send("/app/telemetry", TemplatePacketBuilder
                .buildTelemetry(ingame.getMe()));
        scheduleNext();
    }

    public void startTelemetry() {
        scheduleNext();
    }
    
    public void requestForSelfInfo() {
        eventBus.add(stomp -> stomp.send("/app/self", "{}".getBytes()));
    }
}
