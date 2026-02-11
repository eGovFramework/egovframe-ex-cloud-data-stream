package egovframework.webflux.websocket;

import java.util.Arrays;
import java.util.List;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class FileWebSocketHandler implements WebSocketHandler {

	private final Sinks.Many<String> sink;
	
	public FileWebSocketHandler(Sinks.Many<String> sinkFile) {
		this.sink = sinkFile;
	}
	
    @Override
    public List<String> getSubProtocols() {
        return Arrays.asList("test");
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
    	
    	Publisher<WebSocketMessage> p = sink.asFlux()
                				.map(s -> session.textMessage(s));
        
    	return session.send(p);
    }

    private Mono<Void> doSend(WebSocketSession session, Publisher<WebSocketMessage> output) {
    	return session.send(
        		session.receive()
                .doOnNext(WebSocketMessage::retain)// Use retain() for Reactor Netty
                .map(m -> session.textMessage("received:" + m.getPayloadAsText()))
                );
    }

}