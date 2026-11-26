package egovframework.webflux.stream.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Sort;

import egovframework.webflux.entity.Ids;
import egovframework.webflux.entity.Sample;
import egovframework.webflux.repository.IdsRepository;
import egovframework.webflux.repository.SampleRepository;
import egovframework.webflux.stream.aspect.dto.SampleDTO;
import egovframework.webflux.stream.aspect.dto.SampleListDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AspectDbHistoryTest {

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private ProceedingJoinPoint thisJoinPoint;

    @Mock
    private Signature signature;

    @InjectMocks
    private AspectDbHistory aspect;

    private void givenSampleRepositoryJoinPoint(String methodName, Object arg, Object retVal) throws Throwable {
        given(thisJoinPoint.proceed()).willReturn(retVal);
        given(thisJoinPoint.getThis()).willReturn(mock(SampleRepository.class));
        given(thisJoinPoint.getSignature()).willReturn(signature);
        given(signature.getDeclaringTypeName()).willReturn("egovframework.webflux.repository.SampleRepository");
        given(signature.getName()).willReturn(methodName);
        given(thisJoinPoint.getArgs()).willReturn(new Object[] { arg });
    }

    @Test
    @DisplayName("save() - Sample 리포지토리가 아니면 이력 대상에서 제외하고 결과를 그대로 반환한다")
    void save_withNonSampleRepository_skipsHistory() throws Throwable {
        Mono<Ids> saved = Mono.just(new Ids(1, "sample", 8));
        given(thisJoinPoint.proceed()).willReturn(saved);
        given(thisJoinPoint.getThis()).willReturn(mock(IdsRepository.class));

        assertThat(aspect.aroundTargetMethod(thisJoinPoint)).isSameAs(saved);
        verifyNoInteractions(streamBridge);
    }

    @Test
    @DisplayName("save() - Sample 저장을 구독한 시점에 변경 이력을 1회 발행한다")
    @SuppressWarnings("unchecked")
    void save_withSample_sendsChangeHistoryOnSubscribe() throws Throwable {
        Sample sample = new Sample(1, "SAMPLE-00001", "테스트명", "테스트 설명", "Y", "admin");
        givenSampleRepositoryJoinPoint("save", sample, Mono.just(sample));

        Mono<Sample> result = (Mono<Sample>) aspect.aroundTargetMethod(thisJoinPoint);
        verifyNoInteractions(streamBridge);

        StepVerifier.create(result).expectNext(sample).verifyComplete();

        ArgumentCaptor<SampleDTO> captor = ArgumentCaptor.forClass(SampleDTO.class);
        verify(streamBridge).send(eq("historyDb"), captor.capture());
        assertThat(captor.getValue().getCategory()).isEqualTo("change");
        assertThat(captor.getValue().getContent()).isSameAs(sample);
    }

    @Test
    @DisplayName("findAll() - 검색 이력 목록은 리포지토리가 방출한 순서를 유지한다")
    @SuppressWarnings("unchecked")
    void findAll_keepsEmittedOrderInHistory() throws Throwable {
        Sample first = new Sample(2, "SAMPLE-00002", "둘째", "설명", "Y", "admin");
        Sample second = new Sample(1, "SAMPLE-00001", "첫째", "설명", "Y", "admin");
        Sort sort = Sort.by("sampleId").descending();
        givenSampleRepositoryJoinPoint("findAll", sort, Flux.just(first, second));

        Flux<Sample> result = (Flux<Sample>) aspect.aroundTargetMethod(thisJoinPoint);

        StepVerifier.create(result).expectNext(first, second).verifyComplete();

        ArgumentCaptor<SampleListDTO> captor = ArgumentCaptor.forClass(SampleListDTO.class);
        verify(streamBridge).send(eq("historyDb"), captor.capture());
        assertThat(captor.getValue().getCategory()).isEqualTo("list");
        assertThat(captor.getValue().getContentList()).containsExactly(first, second);
        assertThat(captor.getValue().getSort()).isEqualTo(sort);
    }
}
