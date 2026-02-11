package egovframework.webflux.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SampleService {

    Flux<SampleVO> list();

    Flux<SampleVO> search(SampleVO sampleVO);

    Mono<SampleVO> detail(int id);

    Mono<SampleVO> add(SampleVO sampleVO);

    Mono<SampleVO> update(SampleVO sampleVO);

    Mono<Void> delete(int id);

}
