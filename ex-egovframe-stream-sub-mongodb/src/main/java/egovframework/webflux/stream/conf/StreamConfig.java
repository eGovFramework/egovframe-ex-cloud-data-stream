package egovframework.webflux.stream.conf;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * 기본 송수신 테스트 설정 (사용 시 각 메소드에 @Bean 선언 후 function.definition 에 추가)
 */
@Slf4j
@Configuration
public class StreamConfig {

    public Supplier<String> basicProducer() {
        log.debug("===>>> Producer BEAN");
        return () -> "test message";
    }

    public Consumer<String> basicConsumer() {
        log.debug("===>>> Consumer BEAN");
        return msg -> log.debug("===>>> message = {}", msg);
    }

}
