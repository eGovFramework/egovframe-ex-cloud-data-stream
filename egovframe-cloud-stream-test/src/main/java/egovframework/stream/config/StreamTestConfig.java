package egovframework.stream.config;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Cloud Stream 기본 송수신 테스트 설정
 */
@Slf4j
@Configuration
public class StreamTestConfig {

    /** poller 주기마다 test-topic 으로 메시지 발행 */
    @Bean
    public Supplier<String> basicProducer() {
        log.info("===>>> producer OK");
        return () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.info("===>>> thread = {}", Thread.currentThread().getName());
            return "test message";
        };
    }

    /** 수신 메시지 로깅 (spring.cloud.function.definition 에 추가 시 동작) */
    @Bean
    public Consumer<String> basicConsumer() {
        log.info("===>>> consumer OK");
        return msg -> log.info("===>>> message = {}", msg);
    }

}
