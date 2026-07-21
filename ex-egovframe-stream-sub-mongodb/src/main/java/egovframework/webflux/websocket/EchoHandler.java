package egovframework.webflux.websocket;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.HtmlUtils;

import reactor.core.publisher.Mono;

public class EchoHandler implements WebSocketHandler {

    public EchoHandler() {
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        return session.send(
        		session.receive()
                .doOnNext(WebSocketMessage::retain)// Use retain() for Reactor Netty
                // 반사된 페이로드를 HTML 싱크로 렌더링하는 클라이언트에서 스크립트가 실행되지 않도록 이스케이프 처리
                .map(m -> session.textMessage("received:" + HtmlUtils.htmlEscape(m.getPayloadAsText())))
                );
    }
}