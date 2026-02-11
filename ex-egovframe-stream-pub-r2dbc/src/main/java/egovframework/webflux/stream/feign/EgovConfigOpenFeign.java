package egovframework.webflux.stream.feign;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableFeignClients("egovframework.webflux.stream.feign")
public class EgovConfigOpenFeign {

    /**
     * WebFlux 환경에서는 HttpMessageConverters가 자동 등록되지 않음.
     * OpenFeign의 SpringDecoder가 응답 디코딩 시 이 빈을 필요로 하므로 수동 등록.
     */
    @Bean
    public HttpMessageConverters httpMessageConverters() {
        return new HttpMessageConverters();
    }

}
