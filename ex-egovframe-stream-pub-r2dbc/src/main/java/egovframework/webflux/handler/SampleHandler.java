package egovframework.webflux.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import egovframework.webflux.annotation.EgovController;
import egovframework.webflux.repository.SampleRepository;
import egovframework.webflux.service.SampleService;
import egovframework.webflux.service.SampleVO;
import egovframework.webflux.util.EgovAppUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@EgovController
@Slf4j
@RequiredArgsConstructor
@Tag(name="R2DBC(H2) 연동 API", description = "R2DBC(H2) 연동 API 입니다.")
public class SampleHandler {

    private final SampleService sampleService;

    private final SampleRepository sampleRepository;

    private final Mono<ServerResponse> response404 = ServerResponse.notFound().build();

    private final Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    public Mono<ServerResponse> list(ServerRequest request) {
        Flux<SampleVO> sampleVO = this.sampleRepository.findAll().map(EgovAppUtils::entityToVo);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(sampleVO, SampleVO.class);
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        return this.sampleService.detail(id)
                .flatMap(sampleVO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(sampleVO)))
                .switchIfEmpty(response404);
    }

    public Mono<ServerResponse> add(ServerRequest request) {
        return request.bodyToMono(SampleVO.class)
                .flatMap(this.sampleService::add)
                .flatMap(result -> ServerResponse.ok().build())
                .switchIfEmpty(response406);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        return request.bodyToMono(SampleVO.class)
                .flatMap(this.sampleService::update)
                .flatMap(result -> ServerResponse.ok().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        return this.sampleService.delete(id)
                .flatMap(result -> ServerResponse.ok().build());
    }

}
