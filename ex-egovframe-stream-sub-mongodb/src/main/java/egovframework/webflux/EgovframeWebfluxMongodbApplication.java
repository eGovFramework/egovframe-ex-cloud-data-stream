package egovframework.webflux;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import egovframework.webflux.repository.RealtimePositionRepository;

@SpringBootApplication
public class EgovframeWebfluxMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(EgovframeWebfluxMongodbApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(RealtimePositionRepository realtimePositionRepository) {
        return args -> {
        	realtimePositionRepository.deleteAll().doOnSuccess(result -> System.out.println("RealtimePositon Table Delete Ok!")).subscribe();

        };
    }

}
