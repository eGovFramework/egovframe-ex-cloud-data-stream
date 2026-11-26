package egovframework.webflux.websocket;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.HtmlUtils;

import reactor.core.publisher.Mono;

/**
 * 수신 메시지를 HTML 이스케이프하여 응답하는 에코 핸들러
 */
public class EchoHandler implements WebSocketHandler {

    public EchoHandler() {
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        return session.send(
        		session.receive()
                .doOnNext(WebSocketMessage::retain) // Reactor Netty 버퍼 유지
                .map(m -> session.textMessage("received:" + HtmlUtils.htmlEscape(m.getPayloadAsText())))
                );
    }
}