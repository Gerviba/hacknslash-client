package hu.gerviba.hackandslash.client.connection;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.packets.ChatMessagePacket;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketConnectionThread extends Thread {

    private String ip;
    private String sessionId;
    private Runnable after;
    
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
            
            log.info("Subscribing");
//            websocket.subscribeChat(stomp);
            
            stomp.subscribe("/topic/chat", new StompFrameHandler() {

                public Type getPayloadType(StompHeaders stompHeaders) {
                    return byte[].class;
                }

                public void handleFrame(StompHeaders stompHeaders, Object o) {
                    log.info("Received " + new String((byte[]) o));
                    Platform.runLater(() -> {
                        for (int i = 0; i < 9; ++i)
                            IngameWindow.texts[i].setText(IngameWindow.texts[i + 1].getText());
                        ObjectMapper mapper = new ObjectMapper();
                        ChatMessagePacket msg;
                        try {
                            msg = mapper.readValue((byte[]) o, ChatMessagePacket.class);
                            IngameWindow.texts[9].setText(" " + msg.getSender() + ": " + msg.getMessage() + " ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
            
            Platform.runLater(() -> new IngameWindow().setThisToCurrentWindow());
            
            Consumer<StompSession> event;
            while (true)
                while ((event = eventBus.poll()) != null)
                    event.accept(stomp);
            
        } catch (Exception e) {
            log.error("Error in connection thread", e);
        }
    }
    
}
