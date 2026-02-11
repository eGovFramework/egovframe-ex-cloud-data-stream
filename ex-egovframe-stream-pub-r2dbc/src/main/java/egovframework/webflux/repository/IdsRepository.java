package egovframework.webflux.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import egovframework.webflux.annotation.EgovRepository;
import egovframework.webflux.entity.Ids;
import reactor.core.publisher.Mono;

@EgovRepository
public interface IdsRepository extends ReactiveCrudRepository<Ids, Integer> {

    Mono<Ids> findByTableName(String tableName);

}
