package egovframework.webflux.stream.sensor;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.webflux.stream.dto.EgovTempHumiDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EgovConsumerSensorThermohygrometer {
	
	private final Sinks.Many<String> sink;
	
    @Bean
    public Consumer<String> sensorThermo() {

    	Consumer<String> sensorConsumer = (payload) -> {
    		//log.debug("===>>> thermo = {}", payload);
    		log.debug("===> Received Sensor message: " + payload);
    		
    		ObjectMapper mapper = new ObjectMapper();
    		EgovTempHumiDTO tempHumiVO = new EgovTempHumiDTO();
    		
    		try {
    			tempHumiVO = mapper.readValue(payload, EgovTempHumiDTO.class);
    		} catch (JsonMappingException e) {
    			log.debug(e.getMessage());
    		} catch (JsonProcessingException e) {
    			log.debug(e.getMessage());
    		} catch (Exception e) {
    			log.debug(e.getMessage());
    		}
    		
    		log.debug("===>>> result = " + tempHumiVO.toString());
        	sink.emitNext(payload, Sinks.EmitFailureHandler.FAIL_FAST);
        	
    	};
    	return sensorConsumer;
    	
    }
}