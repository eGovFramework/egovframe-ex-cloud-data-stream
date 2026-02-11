package egovframework.webflux.stream.api;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import egovframework.webflux.repository.RealtimePositionRepository;
import egovframework.webflux.stream.api.dto.RealtimePositionDTO;
import egovframework.webflux.stream.api.entity.RealtimePosition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EgovConsumerTrainApi {

    @Bean
    public Consumer<List<RealtimePositionDTO>> trainApi(RealtimePositionRepository realtimePositionRepository) {
    	
    	Consumer<List<RealtimePositionDTO>> trainApiConsumer = (payload) -> {
    		log.debug("===> Received Train API message: " + payload);
    		
    		long currentTimeStamp = System.currentTimeMillis();
    		
    		RealtimePosition realtimePosition = new RealtimePosition();
    		
    		for ( RealtimePositionDTO realtimePositionDTO : payload ) {
                realtimePosition.setReceivedTimeStamp(currentTimeStamp);
    			log.debug("===> realtimePositionDTO : " + realtimePositionDTO);
    			
                BeanUtils.copyProperties(realtimePositionDTO, realtimePosition);
    			log.debug("===> realtimePosition : " + realtimePosition);

        		realtimePositionRepository.save(realtimePosition).subscribe();

    		}
    		
    	};
    	
    	return trainApiConsumer;
    	
    }
}