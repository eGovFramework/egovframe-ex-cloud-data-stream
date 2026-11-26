package egovframework.webflux;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import egovframework.webflux.repository.RealtimePositionRepository;

/**
 * 데이터 스트림 구독(Subscriber) 예제 애플리케이션
 */
@SpringBootApplication
public class EgovframeWebfluxMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(EgovframeWebfluxMongodbApplication.class, args);
    }

    /** 기동 시 수집 데이터 초기화 */
    @Bean
    public CommandLineRunner init(RealtimePositionRepository realtimePositionRepository) {
        return args -> {
        	realtimePositionRepository.deleteAll().doOnSuccess(result -> System.out.println("RealtimePosition Table Delete Ok!")).subscribe();
        };
    }

}
