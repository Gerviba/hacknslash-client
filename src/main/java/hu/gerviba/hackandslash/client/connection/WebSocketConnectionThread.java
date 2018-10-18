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
import hu.gerviba.hackandslash.packets.TemplatePacketBuilder;
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
        // TODO: Performace improve with lower priority
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
            
            log.info("Subscribing");
            
            stomp.subscribe("/topic/chat", new StompFrameHandler() {

                public Type getPayloadType(StompHeaders stompHeaders) {
                    return byte[].class;
                }

                public void handleFrame(StompHeaders stompHeaders, Object o) {
                    log.info("Chat " + new String((byte[]) o));
                    Platform.runLater(() -> ingame.getChatComponent().appendMessage((byte[]) o));
                }
                
            });
            
            stomp.subscribe("/topic/telemetry", new StompFrameHandler() {

                public Type getPayloadType(StompHeaders stompHeaders) {
                    return byte[].class;
                }

                public void handleFrame(StompHeaders stompHeaders, Object o) {
//                    log.info("Telemetry " + new String((byte[]) o));
                    Platform.runLater(() -> ingame.updateTelemetry((byte[]) o));
                }
                
            });
            
            Platform.runLater(() -> (ingame = new IngameWindow()).setThisToCurrentWindow());
            
            doPolling(stomp);
            log.error("Event bus stopped working");
            
        } catch (Exception e) {
            log.error("Error in connection thread", e);
        }
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
}
