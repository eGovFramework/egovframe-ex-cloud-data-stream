package egovframework.webflux.websocket;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Mono;

public class EchoHandler implements WebSocketHandler {

    public EchoHandler() {
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        return session.send(
        		session.receive()
                .doOnNext(WebSocketMessage::retain)// Use retain() for Reactor Netty
                .map(m -> session.textMessage("received:" + m.getPayloadAsText()))
                );
    }
}