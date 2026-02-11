package egovframework.webflux.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import egovframework.webflux.annotation.EgovRepository;
import egovframework.webflux.entity.Sample;
import reactor.core.publisher.Flux;

@EgovRepository
public interface SampleRepository extends ReactiveCrudRepository<Sample, Integer> {

    Flux<Sample> findAll(Sort sort);

    Flux<Sample> findBySampleIdContaining(String sampleId, Sort sort);

    Flux<Sample> findByNameContaining(String name, Sort sort);

}
