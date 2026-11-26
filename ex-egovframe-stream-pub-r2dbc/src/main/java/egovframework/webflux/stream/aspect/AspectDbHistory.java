package egovframework.webflux.stream.aspect;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import egovframework.webflux.entity.Sample;
import egovframework.webflux.repository.SampleRepository;
import egovframework.webflux.stream.aspect.dto.SampleDTO;
import egovframework.webflux.stream.aspect.dto.SampleListDTO;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository 호출을 가로채 처리 이력을 db-topic 으로 발행
 */
@Slf4j
@Component("aspectUsingAnnotation")
@Aspect
public class AspectDbHistory {
	
	@Autowired
	private StreamBridge streamBridge;

    @Pointcut("execution(public * egovframework.webflux..*Repository.*(..))")
    public void targetMethod() {
        // pointcut 참조용
    }

	@Before("targetMethod()")
    public void beforeTargetMethod(JoinPoint thisJoinPoint) {
    	log.debug("\nAspectUsingAnnotation.beforeTargetMethod executed.");

        @SuppressWarnings("unused")
		Class<? extends Object> clazz = thisJoinPoint.getTarget().getClass();
        String className = thisJoinPoint.getTarget().getClass().getSimpleName();
        String methodName = thisJoinPoint.getSignature().getName();

        // 클래스·메소드·인자 로깅
        StringBuffer buf = new StringBuffer();
        buf.append("\n== AspectUsingAnnotation.beforeTargetMethod : ["
            + className + "." + methodName + "()] ==");
        Object[] arguments = thisJoinPoint.getArgs();
        int argCount = 0;
        for (Object obj : arguments) {
            buf.append("\n - arg ");
            buf.append(argCount++);
            buf.append(" : ");
            buf.append(String.valueOf(obj));
        }

        log.debug(buf.toString());
    }

    @After("targetMethod()")
    public void afterTargetMethod(JoinPoint thisJoinPoint) {
    	log.debug("AspectUsingAnnotation.afterTargetMethod executed.");
    }

	@AfterReturning(pointcut = "targetMethod()", returning = "retVal")
    public void afterReturningTargetMethod(JoinPoint thisJoinPoint,
            Object retVal) {
    	log.debug("AspectUsingAnnotation.afterReturningTargetMethod executed. return value is [{}]", retVal);
    	
        StringBuffer buf = new StringBuffer();

        buf.append("\n");

        // List 결과는 건수와 전체 데이터 출력 (성능상 운영 환경에서는 비권장)
        if (retVal instanceof List) {
            List<?> resultList = (List<?>) retVal;
            buf.append("resultList size : " + resultList.size() + "\n");
            for (Object oneRow : resultList) {
                buf.append(String.valueOf(oneRow));
                buf.append("\n");
            }
        }

        log.debug(buf.toString());
    }

    @AfterThrowing(pointcut = "targetMethod()", throwing = "exception")
    public void afterThrowingTargetMethod(JoinPoint thisJoinPoint,
            Exception exception) throws Exception {
    	log.debug("AspectUsingAnnotation.afterThrowingTargetMethod executed.");
    	log.error("에러가 발생했습니다. {}", exception);

        // 원본 예외를 감싸 재전달
        throw new RuntimeException("에러가 발생했습니다.", exception);
    }

    /**
     * 반환 Publisher 에 이력 발행을 연결 (별도 구독 없이 실제 수행 시점에 1회 실행·발행).
     * 수행 시간은 구독부터 완료까지 측정
     */
    @Around("targetMethod()")
    @SuppressWarnings("unchecked")
    public Object aroundTargetMethod(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        Object retVal = thisJoinPoint.proceed();

        // Sample 리포지토리만 이력 대상
        if (!(thisJoinPoint.getThis() instanceof SampleRepository)) {
            return retVal;
        }

        String className = thisJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = thisJoinPoint.getSignature().getName();
        Object[] args = thisJoinPoint.getArgs();
        log.debug("===>>> {}.{}() args.length : {}", className, methodName, args.length);

        switch (methodName) {
            case "findAll": {
                Sort sort = args.length > 0 && args[0] instanceof Sort ? (Sort) args[0] : null;
                return withSearchHistory((Flux<Sample>) retVal, className, methodName, null, sort);
            }
            case "findBySampleIdContaining": {
                Sample search = new Sample();
                search.setSampleId(String.valueOf(args[0]));
                return withSearchHistory((Flux<Sample>) retVal, className, methodName, search, null);
            }
            case "findByNameContaining": {
                Sample search = new Sample();
                search.setName(String.valueOf(args[0]));
                return withSearchHistory((Flux<Sample>) retVal, className, methodName, search, null);
            }
            case "findById":
                return withHistory((Mono<Sample>) retVal, "view", className, methodName, null);
            case "save":
                return withHistory((Mono<Sample>) retVal, "change", className, methodName, null);
            case "delete":
                if (args.length > 0 && args[0] instanceof Sample) {
                    return withHistory((Mono<Sample>) retVal, "change", className, methodName, (Sample) args[0]);
                }
                return retVal;
            default:
                return retVal;
        }
    }

    /** 단건 조회·변경 이력 (delete 처럼 결과가 없으면 fallback 을 내용으로 발행) */
    private Mono<Sample> withHistory(Mono<Sample> result, String category, String className, String methodName, Sample fallback) {
        return Mono.defer(() -> {
            long start = System.currentTimeMillis();
            AtomicReference<Sample> content = new AtomicReference<>(fallback);
            return result.doOnNext(content::set).doOnSuccess(ignored -> {
                if (content.get() == null) {
                    return;
                }
                SampleDTO sampleDTO = new SampleDTO();
                sampleDTO.setCategory(category);
                sampleDTO.setContent(content.get());
                sampleDTO.setClassName(className);
                sampleDTO.setMethodName(methodName);
                sampleDTO.setElapsedMills(System.currentTimeMillis() - start);
                streamBridge.send("historyDb", sampleDTO);
            });
        });
    }

    /** 목록·검색 이력 (결과가 있을 때만 발행) */
    private Flux<Sample> withSearchHistory(Flux<Sample> result, String className, String methodName, Sample search, Sort sort) {
        return Flux.defer(() -> {
            long start = System.currentTimeMillis();
            List<Sample> resultList = new ArrayList<>();
            return result.doOnNext(sample -> resultList.add(sample)).doOnComplete(() -> {
                if (resultList.isEmpty()) {
                    return;
                }
                SampleListDTO sampleListDTO = new SampleListDTO();
                sampleListDTO.setCategory("list");
                sampleListDTO.setContentList(resultList);
                sampleListDTO.setClassName(className);
                sampleListDTO.setMethodName(methodName);
                sampleListDTO.setElapsedMills(System.currentTimeMillis() - start);
                sampleListDTO.setSearch(search);
                sampleListDTO.setSort(sort);
                streamBridge.send("historyDb", sampleListDTO);
            });
        });
    }

}
