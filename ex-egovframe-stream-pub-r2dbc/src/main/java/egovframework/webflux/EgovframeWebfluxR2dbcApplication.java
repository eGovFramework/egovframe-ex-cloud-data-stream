package egovframework.webflux;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import egovframework.webflux.entity.Ids;
import egovframework.webflux.entity.Sample;
import egovframework.webflux.repository.IdsRepository;
import egovframework.webflux.repository.SampleRepository;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class EgovframeWebfluxR2dbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(EgovframeWebfluxR2dbcApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(IdsRepository idsRepository, SampleRepository sampleRepository) {
        return args -> {
            idsRepository.deleteAll().doOnSuccess(result -> System.out.println("Ids Table Delete Ok!")).subscribe();

            sampleRepository.deleteAll().doOnSuccess(result -> System.out.println("Sample Table Delete Ok!")).subscribe();

            Flux.just(
                    new Ids(null, "sample",8)
            ).flatMap(idsRepository::save)
            .thenMany(idsRepository.findAll()).subscribe(System.out::println);

            Flux.just(
                    new Sample(null, "SAMPLE-00001","Runtime Environment","Foundation Layer","Y","eGov"),
                    new Sample(null, "SAMPLE-00002","Runtime Environment","Persistence Layer","Y","eGov"),
                    new Sample(null, "SAMPLE-00003","Runtime Environment","Presentation Layer","Y","eGov"),
                    new Sample(null, "SAMPLE-00004","Runtime Environment","Business Layer","Y","eGov"),
                    new Sample(null, "SAMPLE-00005","Runtime Environment","Batch Layer","Y","eGov"),
                    new Sample(null, "SAMPLE-00006","Runtime Environment","Integration Layer","Y","eGov"),
                    new Sample(null, "SAMPLE-00007","Development Environment","Implementation Tool","Y","eGov"),
                    new Sample(null, "SAMPLE-00008","Development Environment","Test Tool","Y","eGov")
            )
            .flatMap(sampleRepository::save)
            .thenMany(sampleRepository.findAll()).subscribe(System.out::println);
        };
    }

}
