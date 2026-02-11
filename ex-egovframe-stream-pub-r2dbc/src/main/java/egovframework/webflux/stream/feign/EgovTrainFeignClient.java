package egovframework.webflux.stream.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 기본적으로 feign client 를 생성해서 호출 한번 해보기
 */
@FeignClient(value = "train", url = "http://swopenAPI.seoul.go.kr")
public interface EgovTrainFeignClient {

	///api/subway/(인증키)/xml/realtimePosition/0/5/1호선
    @GetMapping("/api/subway/{authKey}/json/realtimePosition/0/5/{lineNumber}호선")
    ResultTrainPosition jsonRealtimeTrainPosition(@PathVariable("authKey") String authKey,
    		@PathVariable("lineNumber") String lineNumber);

    
    @GetMapping("/api/subway/{authKey}/xml/realtimePosition/0/5/{lineNumber}호선")
    XmlResultTrainPosition xmlRealtimeTrainPosition(@PathVariable("authKey") String authKey,
    		@PathVariable("lineNumber") String lineNumber);

}
