package egovframework.webflux.stream.file;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EgovConsumerFileLine {

	private final Sinks.Many<String> sinkFile;
	
    @Bean
    public Consumer<String> fileLine() {
    	
    	Consumer<String> fileLineConsumer = (payload) -> {
    		log.debug("===> Received File message: " + payload);
    		
    		ObjectMapper mapper = new ObjectMapper();
    		
    		Map<String, Object> fileStreamLineMap = new HashMap<>();
    		fileStreamLineMap.put("category", "file_line");
    		fileStreamLineMap.put("lineData", payload);
    		
    		// Map -> JSON
    		String result = "{\"category\":\"\"}";
            try {
    			result = mapper.writeValueAsString(fileStreamLineMap);
    		} catch (JsonProcessingException e) {
    			e.printStackTrace();
    		}
            log.debug("websocket send file info : "+result);
            
    		sinkFile.emitNext(result, Sinks.EmitFailureHandler.FAIL_FAST);
    	};
    	
    	return fileLineConsumer;
    	
    }
}
