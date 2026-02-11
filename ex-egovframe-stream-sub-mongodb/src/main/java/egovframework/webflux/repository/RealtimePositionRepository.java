package egovframework.webflux.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import egovframework.webflux.annotation.EgovRepository;
import egovframework.webflux.stream.api.entity.RealtimePosition;
import reactor.core.publisher.Flux;

@EgovRepository
public interface RealtimePositionRepository extends ReactiveCrudRepository<RealtimePosition, String> {

    Flux<RealtimePosition> findAll(Sort sort);

    Flux<RealtimePosition> findBySubwayIdContaining(String subwayId, Sort sort);

    Flux<RealtimePosition> findBySubwayNmContaining(String subwayNm, Sort sort);

}
