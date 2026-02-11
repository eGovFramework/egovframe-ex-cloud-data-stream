package egovframework.webflux.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import egovframework.webflux.websocket.DbWebSocketHandler;
import egovframework.webflux.websocket.EchoHandler;
import egovframework.webflux.websocket.FileWebSocketHandler;
import egovframework.webflux.websocket.SensorWebSocketHandler;
import reactor.core.publisher.Sinks;

@Configuration
@EnableWebFlux
public class ReactiveWebSocketConfiguration {

	/*
	@Autowired
	private SensorWebSocketHandler swsh;
	@Autowired
	private FileWebSocketHandler fwsh;
	@Autowired
	private DbWebSocketHandler dwsh;
	*/
	
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

	@Bean
	public Sinks.Many<String> sink() {
		return Sinks.many().multicast().directBestEffort();
	}

	@Bean
	public Sinks.Many<String> sinkFile() {
		return Sinks.many().multicast().directBestEffort();
	}

	@Bean
	public Sinks.Many<String> sinkDb() {
		return Sinks.many().multicast().directBestEffort();
	}

}