package hu.gerviba.hackandslash.client.connection;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import lombok.extern.slf4j.Slf4j;

/**
 * STOMP Session listener 
 * @author Gergely Szabó
 */
@Slf4j
public class SessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        log.info("Now connected");
    }
    
}
