package egovframework.webflux.service.impl;

import java.util.concurrent.ExecutionException;

import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import egovframework.webflux.annotation.EgovService;
import egovframework.webflux.entity.Sample;
import egovframework.webflux.repository.IdsRepository;
import egovframework.webflux.repository.SampleRepository;
import egovframework.webflux.service.SampleService;
import egovframework.webflux.service.SampleVO;
import egovframework.webflux.util.EgovAppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@EgovService
@Slf4j
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

    private final IdsRepository idsRepository;

    private final SampleRepository sampleRepository;

    private final Sort sort = Sort.by("sampleId").descending();

    private int sequence = 1;

    public Flux<SampleVO> list() {
        return this.sampleRepository.findAll(this.sort).map(EgovAppUtils::entityToVo);
    }

    public Flux<SampleVO> search(SampleVO sampleVO) {
        if ("0".equals(sampleVO.getSearchCondition())) {
            return this.sampleRepository.findBySampleIdContaining(sampleVO.getSearchKeyword(), this.sort).map(EgovAppUtils::entityToVo);
        } else if ("1".equals(sampleVO.getSearchCondition())) {
            return this.sampleRepository.findByNameContaining(sampleVO.getSearchKeyword(), this.sort).map(EgovAppUtils::entityToVo);
        } else {
            return this.list();
        }
    }

    public Mono<SampleVO> detail(int id) {
        return this.sampleRepository.findById(id).map(EgovAppUtils::entityToVo);
    }

    public Mono<SampleVO> add(SampleVO sampleVO) {
        Sample sample = EgovAppUtils.voToEntity(sampleVO);

        try {
            sequence = this.idsRepository.findByTableName("sample")
                    .flatMap(result -> {
                        result.setSeq(ObjectUtils.isEmpty(result.getSeq()) ? 1 : result.getSeq() + 1);
                        return this.idsRepository.save(result);
                    }).toFuture().get().getSeq();
        } catch (InterruptedException | ExecutionException e) {
            log.error("##### R2DBC SampleServiceImpl add() Error >>> {}", e.getMessage());
        }

        sample.setSampleId("SAMPLE-".concat(String.format("%05d", sequence)));

        return this.sampleRepository.save(sample).map(EgovAppUtils::entityToVo);
    }

    public Mono<SampleVO> update(int id, SampleVO sampleVO) {
        // 본문(SampleVO)의 id는 신뢰하지 않고, 호출자가 지정한 id(경로 변수 등)만을 대상 식별자로 사용한다.
        // (mass assignment로 본문 id를 신뢰해 임의 레코드를 갱신하는 것을 방지)
        Sample sample = EgovAppUtils.voToEntity(sampleVO);
        return this.sampleRepository.findById(id)
                .flatMap(result -> {
                    result.setName(sample.getName());
                    result.setDescription(sample.getDescription());
                    result.setUseYn(sample.getUseYn());
                    result.setRegUser(sample.getRegUser());
                    return this.sampleRepository.save(result);
                })
                .map(EgovAppUtils::entityToVo);
    }

    public Mono<Void> delete(int id) {
        return this.sampleRepository.findById(id)
                .flatMap(this.sampleRepository::delete);
    }

}
