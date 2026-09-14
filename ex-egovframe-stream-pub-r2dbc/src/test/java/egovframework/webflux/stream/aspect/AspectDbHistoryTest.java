package egovframework.webflux.stream.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;

import egovframework.webflux.entity.Ids;
import egovframework.webflux.entity.Sample;
import egovframework.webflux.stream.aspect.dto.SampleDTO;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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

    private void givenSaveJoinPoint(String declaringTypeName, Object arg, Object retVal) throws Throwable {
        given(thisJoinPoint.proceed()).willReturn(retVal);
        given(thisJoinPoint.getTarget()).willReturn(new Object());
        given(thisJoinPoint.getSignature()).willReturn(signature);
        given(signature.getDeclaringTypeName()).willReturn(declaringTypeName);
        given(signature.getName()).willReturn("save");
        given(thisJoinPoint.getArgs()).willReturn(new Object[] { arg });
    }

    @Test
    @DisplayName("save() - Sample 이 아닌 엔티티는 변경 이력 대상에서 제외한다")
    void save_withNonSampleEntity_skipsChangeHistory() throws Throwable {
        Ids ids = new Ids(1, "sample", 8);
        givenSaveJoinPoint("egovframework.webflux.repository.IdsRepository", ids, Mono.just(ids));

        assertThatCode(() -> aspect.aroundTargetMethod(thisJoinPoint)).doesNotThrowAnyException();
        verifyNoInteractions(streamBridge);
    }

    @Test
    @DisplayName("save() - Sample 을 저장하면 변경 이력을 발행한다")
    void save_withSample_sendsChangeHistory() throws Throwable {
        Sample sample = new Sample(1, "SAMPLE-00001", "테스트명", "테스트 설명", "Y", "admin");
        Mono<Sample> saved = Mono.just(sample);
        givenSaveJoinPoint("egovframework.webflux.repository.SampleRepository", sample, saved);

        assertThat(aspect.aroundTargetMethod(thisJoinPoint)).isSameAs(saved);
        verify(streamBridge).send(eq("historyDb"), any(SampleDTO.class));
    }
}
