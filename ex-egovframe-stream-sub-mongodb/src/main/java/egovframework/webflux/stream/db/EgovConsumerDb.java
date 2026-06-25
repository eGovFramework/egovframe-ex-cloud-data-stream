package egovframework.webflux.stream.db;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EgovConsumerDb {

	private final Sinks.Many<String> sinkDb;

	private final ObjectMapper objectMapper = new ObjectMapper();
	
    @Bean
    Consumer<String> historyDb() {
    	
    	Consumer<String> historyDbConsumer = (payload) -> {
    		log.debug("===> Received DB message: " + payload);

			String category = "";
			try {
	            JsonNode jsonNode = objectMapper.readTree(payload);
	            category = jsonNode.has("category") ? jsonNode.get("category").asText() : "";
			} catch (Exception e) {
				log.error("JSON 파싱 오류", e);
			}
    		
			log.debug("===>>> jsonObj.category = " + category);
			
			// DB History 저장
			
    		sinkDb.emitNext(payload, Sinks.EmitFailureHandler.FAIL_FAST);
    	};
    	
    	return historyDbConsumer;
    	
    }
}
