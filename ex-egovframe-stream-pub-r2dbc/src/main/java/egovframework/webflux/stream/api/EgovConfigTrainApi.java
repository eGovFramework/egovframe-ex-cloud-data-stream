package egovframework.webflux.stream.api;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import egovframework.webflux.stream.feign.EgovTrainFeignClient;
import egovframework.webflux.stream.feign.ErrorMessage;
import egovframework.webflux.stream.feign.RealtimePosition;
import egovframework.webflux.stream.feign.ResultTrainPosition;
import egovframework.webflux.stream.feign.XmlResultTrainPosition;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EgovConfigTrainApi {

	@Autowired
	EgovTrainFeignClient trainFeignClient;
	
	private AtomicBoolean aBool = new AtomicBoolean(true);
	private AtomicInteger aInt = new AtomicInteger(-1);
	
	private XmlResultTrainPosition xmlResultTrainPosition;
	private static int INTERVAL = 2;

    @Bean
    public Supplier<List<RealtimePosition>> trainApiProducer() {
    	log.debug("===>>> trainPosition Producer OK");

    	
    	Supplier<List<RealtimePosition>> apiSupplier = () -> {
    		
    		log.debug("===>>> apiSupplier OK");
    		
    		if ( aInt.addAndGet(1) % INTERVAL == 0) {
    		
    			ResultTrainPosition resultTrainPosition = trainFeignClient.jsonRealtimeTrainPosition("sample","1");
	    		ErrorMessage errorMessage = resultTrainPosition.getErrorMessage();
	    		List<RealtimePosition> listRealtimePositions = resultTrainPosition.getRealtimePositionList();
	
	    		log.debug("===>>> realtimePositionList.size() = "+listRealtimePositions.size());
	    		log.debug("errorMessage.getCode() = "+errorMessage.getCode());
	    		log.debug("errorMessage.getMessage() = "+errorMessage.getMessage());
	    		log.debug("errorMessage.getStatus() = "+errorMessage.getStatus());
	    		log.debug("errorMessage.getTotal() = "+errorMessage.getTotal());
	    		return listRealtimePositions;

    		} else {
    			List<RealtimePosition> listRealtimePositions = null;
    			return listRealtimePositions;
    		}
    	};
    	
    	return apiSupplier;
    }
	
}
