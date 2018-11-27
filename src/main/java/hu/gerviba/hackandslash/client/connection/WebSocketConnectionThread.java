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

/**
 * Class to manage the websocket connection
 * @see #appendTask(Consumer)
 * @author Gergely Szabó
 */
@Slf4j
public class WebSocketConnectionThread extends Thread {

    private String ip;
    private String sessionId;
    private Runnable after;
    
    private IngameWindow ingame;
    
    private LinkedBlockingQueue<Consumer<StompSession>> eventBus = new LinkedBlockingQueue<>();
    
    /**
     * Constructor of the class
     * @param ip Server IP to connect to
     * @param sessionId Session ID of the user
     * @param after Run this task after connected
     */
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
    
    /**
     * Append task to the event bus.
     * It will be executed from the connection thread.
     * Can be called from any thread.
     * @param task Task to execute in the connection thread
     */
    public synchronized void appendTask(Consumer<StompSession> task) {
        eventBus.add(task);
    }
    
    /**
     * Start sending telemetry updates.
     * Can be called from any thread.
     */
    public void startTelemetry() {
        scheduleNext();
    }
    
    /**
     * Sends a request packet to update user information.
     * Can be called from any thread.
     */
    public void requestForSelfInfo() {
        eventBus.add(stomp -> stomp.send("/app/self", "{}".getBytes()));
    }
    
    /**
     * The thread logic.
     */
    @Override
    public void run() {
        try {
            WebsocketHandler websocket = new WebsocketHandler();

            ListenableFuture<StompSession> f = websocket.connect(ip, sessionId);
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

    /**
     * Sends the <pre>connected</pre> packet
     */
    private void sendJustConnected() {
        eventBus.add(stomp -> {
            stomp.send("/app/connected", TemplatePacketBuilder
                    .buildJustConnected(800, 1280, 2));
        });
    }

    /**
     * Subscribes to a specified channel with an action listener.
     * @param stomp The STOMP session
     * @param channel Channel to subscribe to
     * @param action Action to perform if a message received
     */
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
    
    /**
     * Polling in the event loop
     * @param stomp STOMP session
     * @throws InterruptedException
     */
    private void doPolling(StompSession stomp) throws InterruptedException {
        Consumer<StompSession> event;
        List<Consumer<StompSession>> burst = new LinkedList<>();
        while (true) {
            while ((event = eventBus.poll()) != null) // TODO: TAKE
                burst.add(event);
            
            for (Consumer<StompSession> b : burst)
                b.accept(stomp);
            
            burst.clear();
            Thread.sleep(25);
        }
    }
    
    /**
     * Schedules the next telemetry update
     */
    private void scheduleNext() {
        eventBus.add(stomp -> updateTelemetry(stomp));
    }

    /**
     * Sends the telemetry update packet
     * @param session STOMP session
     */
    private void updateTelemetry(StompSession session) {
        session.send("/app/telemetry", TemplatePacketBuilder
                .buildTelemetry(ingame.getMe()));
        scheduleNext();
    }
    
}
