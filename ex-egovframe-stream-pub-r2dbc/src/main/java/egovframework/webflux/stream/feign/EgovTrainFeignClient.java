package egovframework.webflux.stream.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 서울시 지하철 실시간 열차 위치 Open API 클라이언트
 * (/api/subway/{인증키}/{json|xml}/realtimePosition/0/5/{호선})
 */
@FeignClient(value = "train", url = "${egov.trainApi.url}")
public interface EgovTrainFeignClient {

    @GetMapping("/api/subway/{authKey}/json/realtimePosition/0/5/{lineNumber}호선")
    ResultTrainPosition jsonRealtimeTrainPosition(@PathVariable("authKey") String authKey,
            @PathVariable("lineNumber") String lineNumber);

    @GetMapping("/api/subway/{authKey}/xml/realtimePosition/0/5/{lineNumber}호선")
    XmlResultTrainPosition xmlRealtimeTrainPosition(@PathVariable("authKey") String authKey,
            @PathVariable("lineNumber") String lineNumber);

}
