package egovframework.webflux.stream.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.webflux.stream.dto.EgovTempHumiDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

@Slf4j
@RequiredArgsConstructor
//@EnableBinding(EgovSensorProcessor.class)
public class EgovSensorMessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSensorMessageListener.class);
	
	private final Sinks.Many<String> sink;
	
	//@StreamListener(EgovSensorProcessor.INPUT)
	public void handleMessage(String message) {
		log.debug("===> Received Sensor message: " + message);
		
		ObjectMapper mapper = new ObjectMapper();
		EgovTempHumiDTO tempHumiVO = new EgovTempHumiDTO();
		
		try {
			tempHumiVO = mapper.readValue(message, EgovTempHumiDTO.class);
		} catch (JsonMappingException e) {
			log.debug(e.getMessage());
		} catch (JsonProcessingException e) {
			log.debug(e.getMessage());
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		
		log.debug("===>>> result = " + tempHumiVO.toString());
    	sink.emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST);
    	
		//messagingTemplate.convertAndSend("/ws/sensor", tempHumiVO);
		/*
		WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(
          URI.create("ws://localhost:19092/ws/sensor"), 
          session -> session.send(
            Mono.just(session.textMessage("event-spring-reactive-client-websocket")))
            .thenMany(session.receive()
              .map(WebSocketMessage::getPayloadAsText)
              .log())
            .then())
            .block(Duration.ofSeconds(10L));
		*/
	}
}