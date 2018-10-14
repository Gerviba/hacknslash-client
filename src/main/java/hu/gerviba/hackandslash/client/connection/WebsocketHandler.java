package hu.gerviba.hackandslash.client.connection;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import lombok.Getter;

public class WebsocketHandler {
    
    @Getter
    private static WebsocketHandler instance;
    
    public WebsocketHandler() {
        instance = this;
    }
    
    public ListenableFuture<StompSession> connect(String host, String sessionId) throws MalformedURLException {
        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("hns-session-id", sessionId);
        
        URL url = new URL(host);
        String urlPattern = (url.getProtocol().equals("https") ? "wss" : "ws") + "://{host}:{port}/game";
        return stompClient.connect(urlPattern, headers, new SessionHandler(), url.getHost(), url.getPort());
    }
    
    
}
