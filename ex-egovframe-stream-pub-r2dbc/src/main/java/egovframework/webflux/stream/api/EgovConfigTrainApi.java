package egovframework.webflux.stream.api;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import egovframework.webflux.stream.feign.EgovTrainFeignClient;
import egovframework.webflux.stream.feign.ErrorMessage;
import egovframework.webflux.stream.feign.RealtimePosition;
import egovframework.webflux.stream.feign.ResultTrainPosition;
import lombok.extern.slf4j.Slf4j;

/**
 * 지하철 실시간 위치 Open API 를 호출하여 api-topic 으로 발행
 */
@Slf4j
@Configuration
public class EgovConfigTrainApi {

	@Autowired
	EgovTrainFeignClient trainFeignClient;

	@Value("${egov.trainApi.authKey}")
	private String authKey;

	@Value("${egov.trainApi.lineNumber}")
	private String lineNumber;
	
	private AtomicInteger aInt = new AtomicInteger(-1);
	
	private static int INTERVAL = 2;

    /** poller 호출 INTERVAL 회마다 1회 API 조회 (그 외에는 null 반환 → 미발행) */
    @Bean
    public Supplier<List<RealtimePosition>> trainApiProducer() {
    	log.debug("===>>> trainPosition Producer OK");

    	
    	Supplier<List<RealtimePosition>> apiSupplier = () -> {
    		
    		log.debug("===>>> apiSupplier OK");
    		
    		if ( aInt.addAndGet(1) % INTERVAL == 0) {
    		
    			ResultTrainPosition resultTrainPosition = trainFeignClient.jsonRealtimeTrainPosition(authKey, lineNumber);
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
