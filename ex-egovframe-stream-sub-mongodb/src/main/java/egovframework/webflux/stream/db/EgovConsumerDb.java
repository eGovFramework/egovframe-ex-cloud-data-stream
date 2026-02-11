package egovframework.webflux.stream.db;

import java.util.function.Consumer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EgovConsumerDb {

	private final Sinks.Many<String> sinkDb;
	
    @Bean
    public Consumer<String> historyDb() {
    	
    	Consumer<String> historyDbConsumer = (payload) -> {
    		log.debug("===> Received DB message: " + payload);
    		JSONObject jsonObj = new JSONObject();
    		
            JSONParser jsonParser = new JSONParser();
			try {
	            Object obj = jsonParser.parse(payload);
	            jsonObj = (JSONObject) obj;
			} catch (ParseException e) {
				e.printStackTrace();
			}
    		
			log.debug("===>>> jsonObj.category = "+jsonObj.get("category"));
			
			// DB History 저장
			
    		sinkDb.emitNext(payload, Sinks.EmitFailureHandler.FAIL_FAST);
    	};
    	
    	return historyDbConsumer;
    	
    }
}
