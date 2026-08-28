package egovframework.webflux.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import egovframework.webflux.entity.Ids;
import egovframework.webflux.entity.Sample;
import egovframework.webflux.repository.IdsRepository;
import egovframework.webflux.repository.SampleRepository;
import egovframework.webflux.service.impl.SampleServiceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SampleServiceImplTest {

    @Mock
    private SampleRepository sampleRepository;

    @Mock
    private IdsRepository idsRepository;

    @InjectMocks
    private SampleServiceImpl sampleService;

    private Sample sampleFixture;

    @BeforeEach
    void setUp() {
        sampleFixture = new Sample(1, "SAMPLE-00001", "테스트명", "테스트 설명", "Y", "admin");
    }

    @Test
    @DisplayName("list() - 전체 목록을 정렬하여 반환한다")
    void list_returnsSortedSamples() {
        given(sampleRepository.findAll(any(Sort.class))).willReturn(Flux.just(sampleFixture));

        StepVerifier.create(sampleService.list())
            .assertNext(vo -> {
                assertThat(vo.getSampleId()).isEqualTo("SAMPLE-00001");
                assertThat(vo.getName()).isEqualTo("테스트명");
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("search() - searchCondition=0이면 sampleId로 검색한다")
    void search_bySampleId() {
        given(sampleRepository.findBySampleIdContaining(anyString(), any(Sort.class)))
            .willReturn(Flux.just(sampleFixture));

        SampleVO cond = new SampleVO();
        cond.setSearchCondition("0");
        cond.setSearchKeyword("SAMPLE");

        StepVerifier.create(sampleService.search(cond))
            .assertNext(vo -> assertThat(vo.getSampleId()).isEqualTo("SAMPLE-00001"))
            .verifyComplete();
    }

    @Test
    @DisplayName("search() - searchCondition=1이면 name으로 검색한다")
    void search_byName() {
        given(sampleRepository.findByNameContaining(anyString(), any(Sort.class)))
            .willReturn(Flux.just(sampleFixture));

        SampleVO cond = new SampleVO();
        cond.setSearchCondition("1");
        cond.setSearchKeyword("테스트");

        StepVerifier.create(sampleService.search(cond))
            .assertNext(vo -> assertThat(vo.getName()).isEqualTo("테스트명"))
            .verifyComplete();
    }

    @Test
    @DisplayName("detail() - id로 단건 조회한다")
    void detail_returnsCorrectSample() {
        given(sampleRepository.findById(1)).willReturn(Mono.just(sampleFixture));

        StepVerifier.create(sampleService.detail(1))
            .assertNext(vo -> {
                assertThat(vo.getId()).isEqualTo(1);
                assertThat(vo.getName()).isEqualTo("테스트명");
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("update() - 변경된 필드를 반영하여 저장한다")
    void update_modifiesFields() {
        Sample saved = new Sample(1, "SAMPLE-00001", "수정명", "수정 설명", "N", "user1");
        given(sampleRepository.findById(1)).willReturn(Mono.just(sampleFixture));
        given(sampleRepository.save(any(Sample.class))).willReturn(Mono.just(saved));

        SampleVO vo = new SampleVO();
        vo.setId(saved.getId());
        vo.setName("수정명");
        vo.setDescription("수정 설명");
        vo.setUseYn("N");
        vo.setRegUser("user1");

        StepVerifier.create(sampleService.update(saved.getId(), vo))
            .assertNext(result -> {
                assertThat(result.getName()).isEqualTo("수정명");
                assertThat(result.getUseYn()).isEqualTo("N");
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("delete() - 존재하는 항목을 삭제하고 완료한다")
    void delete_completesSuccessfully() {
        given(sampleRepository.findById(1)).willReturn(Mono.just(sampleFixture));
        given(sampleRepository.delete(sampleFixture)).willReturn(Mono.empty());

        StepVerifier.create(sampleService.delete(1))
            .verifyComplete();
    }

    @Test
    @DisplayName("add() - 새 Sample을 저장하고 반환한다")
    void add_savesAndReturnsSample() {
        Ids ids = new Ids(1, "sample", 1);
        given(idsRepository.findByTableName("sample")).willReturn(Mono.just(ids));
        given(idsRepository.save(any(Ids.class))).willReturn(Mono.just(ids));
        given(sampleRepository.save(any(Sample.class))).willReturn(Mono.just(sampleFixture));

        SampleVO vo = new SampleVO();
        vo.setName("테스트명");
        vo.setDescription("테스트 설명");
        vo.setUseYn("Y");
        vo.setRegUser("admin");

        StepVerifier.create(sampleService.add(vo))
            .assertNext(result -> assertThat(result.getName()).isEqualTo("테스트명"))
            .verifyComplete();
    }
}
