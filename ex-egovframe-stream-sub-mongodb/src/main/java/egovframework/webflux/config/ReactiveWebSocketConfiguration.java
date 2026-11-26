package egovframework.webflux.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import egovframework.webflux.websocket.DbWebSocketHandler;
import egovframework.webflux.websocket.EchoHandler;
import egovframework.webflux.websocket.FileWebSocketHandler;
import egovframework.webflux.websocket.SensorWebSocketHandler;
import reactor.core.publisher.Sinks;

/**
 * WebSocket 엔드포인트 매핑 및 스트림 → WebSocket 전달용 Sink 설정
 */
@Configuration
public class ReactiveWebSocketConfiguration {

	@Bean
	public HandlerMapping handlerMapping(SensorWebSocketHandler swsh, FileWebSocketHandler fwsh, DbWebSocketHandler dwsh) {
		Map<String, WebSocketHandler> map = new HashMap<>();
		map.put("/ws/echo", new EchoHandler());
		map.put("/ws/file", fwsh);
		map.put("/ws/db", dwsh);
		map.put("/ws/sensor", swsh);

		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setUrlMap(map);
		return mapping;
	}

	@Bean
	WebSocketHandlerAdapter webSocketHandlerAdapter() {
		return new WebSocketHandlerAdapter();
	}

	/** 센서용 Sink */
	@Bean
	public Sinks.Many<String> sink() {
		return Sinks.many().multicast().directBestEffort();
	}

	/** 파일 라인용 Sink */
	@Bean
	public Sinks.Many<String> sinkFile() {
		return Sinks.many().multicast().directBestEffort();
	}

	/** DB 처리 이력용 Sink */
	@Bean
	public Sinks.Many<String> sinkDb() {
		return Sinks.many().multicast().directBestEffort();
	}

}