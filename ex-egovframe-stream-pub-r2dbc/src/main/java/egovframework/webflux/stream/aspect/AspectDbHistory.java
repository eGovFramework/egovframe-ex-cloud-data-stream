package egovframework.webflux.stream.aspect;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
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
import egovframework.webflux.stream.aspect.dto.SampleDTO;
import egovframework.webflux.stream.aspect.dto.SampleListDTO;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component("aspectUsingAnnotation")
@Aspect
public class AspectDbHistory {
	
	@Autowired
	private StreamBridge streamBridge;

    @Pointcut("execution(public * egovframework.webflux..*Repository.*(..))")
    public void targetMethod() {
        // pointcut annotation 값을 참조하기 위한 dummy method
    }

	@Before("targetMethod()")
    public void beforeTargetMethod(JoinPoint thisJoinPoint) {
    	log.debug("\nAspectUsingAnnotation.beforeTargetMethod executed.");

        @SuppressWarnings("unused")
		Class<? extends Object> clazz = thisJoinPoint.getTarget().getClass();
        String className = thisJoinPoint.getTarget().getClass().getSimpleName();
        String methodName = thisJoinPoint.getSignature().getName();

        // 현재 class, method 정보 및 method arguments 로깅
        StringBuffer buf = new StringBuffer();
        buf.append("\n== AspectUsingAnnotation.beforeTargetMethod : ["
            + className + "." + methodName + "()] ==");
        Object[] arguments = thisJoinPoint.getArgs();
        int argCount = 0;
        for (Object obj : arguments) {
            buf.append("\n - arg ");
            buf.append(argCount++);
            buf.append(" : ");
            // Java 17 모듈 시스템 호환을 위해 toString() 사용
            buf.append(String.valueOf(obj));
        }

        // 대상 클래스의 logger 를 사용하여 method arguments 로깅
        // 하였음.
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

        // 결과값이 List 이면 size 와 전체 List 데이터를 풀어 출력
        // (성능상 사용않는 것이 좋음)
        if (retVal instanceof List) {
            List<?> resultList = (List<?>) retVal;
            buf.append("resultList size : " + resultList.size() + "\n");
            for (Object oneRow : resultList) {
                buf.append(String.valueOf(oneRow));
                buf.append("\n");
            }
        } else {
            // buf.append(String.valueOf(retVal));
        }

        // 대상 클래스의 logger 를 사용하여 결과값 로깅
        // 하였음.
        log.debug(buf.toString());

        // return value 의 변경은 불가함에 유의!
    }

    @AfterThrowing(pointcut = "targetMethod()", throwing = "exception")
    public void afterThrowingTargetMethod(JoinPoint thisJoinPoint,
            Exception exception) throws Exception {
    	log.debug("AspectUsingAnnotation.afterThrowingTargetMethod executed.");
    	log.error("에러가 발생했습니다. {}", exception);

        // 원본 exception 을 wrapping 하고 user-friendly 한
        // 메시지를 설정하여 새로운 Exception 으로 re-throw
        throw new RuntimeException("에러가 발생했습니다.", exception);

        // 여기서는 간단하게 작성하였지만 일반적으로 messageSource 를 사용한
        // locale 에 따른 다국어 처리 및 egov. exceptionHandler
        // 를 확장한 Biz. (ex. email 공지 등) 기능 적용이 가능함.
    }

    @Around("targetMethod()")
    public Object aroundTargetMethod(ProceedingJoinPoint thisJoinPoint) throws Throwable {
    	
    	log.debug("AspectUsingAnnotation.aroundTargetMethod start.");
        long time1 = System.currentTimeMillis();
        Object retVal = thisJoinPoint.proceed();

        long time2 = System.currentTimeMillis();
        long elapsedTimeMills = time2 - time1;
        log.debug("AspectUsingAnnotation.aroundTargetMethod end. Time({})", elapsedTimeMills);

        @SuppressWarnings("unused")
		Class<? extends Object> clazz = thisJoinPoint.getTarget().getClass();
        //String className = thisJoinPoint.getTarget().getClass().getSimpleName();
        String className = thisJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = thisJoinPoint.getSignature().getName();

        // 현재 class, method 정보 및 method arguments 로깅
        StringBuffer buf = new StringBuffer();
        buf.append("\n== AspectUsingAnnotation.afterReturningTargetMethod : ["
            + className + "." + methodName + "()] ==");
		Object[] args = thisJoinPoint.getArgs();
		log.debug("===>>> args.length : "+args.length);
        
        switch(methodName) {
        	case "findAll":

        		Sort sort = null;
        		if (args.length > 0) { 
        			sort = (Sort) args[0];
        			log.debug("===>>> sort : "+sort.toString());
        		}
        		processSearchHistory(retVal, elapsedTimeMills, className, methodName, sort);

	            /*
	            List<Sample> resultList = result.collectList().block();
	            if (resultList.size() > 0) {
	            	log.debug("===>>> resultList : "+resultList);
            		streamBridge.send("historyDb", resultList.get(0));
	            }
	            */
	            //Mono<List<Sample>> resultList = result.collectList();
	        	//log.debug("===>>> result : "+resultList);
	            break;
        	case "findById":
        		if (args.length > 0) { 
        			log.debug("===>>> param : "+String.valueOf(args[0]));
        			
        			processViewHistory(retVal, elapsedTimeMills, className, methodName);
        		}
        		break;
        	case "findBySampleIdContaining":
        		if (args.length > 0) {
        			Sample search = new Sample();
        			search.setId(Integer.parseInt(args[0].toString()));
        			
        			log.debug("===>>> param : "+String.valueOf(args[0]));
        			
        			processSearchHistory(retVal, elapsedTimeMills, className, methodName, search);
        		}
        		break;
        	case  "findByNameContaining":
        		if (args.length > 0) {
        			Sample search = new Sample();
        			search.setName(args[0].toString());
        			
        			log.debug("===>>> param : "+String.valueOf(args[0]));
        			
        			processSearchHistory(retVal, elapsedTimeMills, className, methodName, search);
        		}
        		break;
        		
        	case "save":
        		if (args.length > 0) { 
        			Sample content = (Sample) args[0];
        			log.debug("===>>> result : "+String.valueOf(content));
        			
        			processChangeHistory(elapsedTimeMills, className, methodName, content);
        		}

        		break;
        	case "delete":
        		if (args.length > 0) { 
        			log.debug("===>>> param : "+String.valueOf(args[0]));
        			Sample content = new Sample();
        			
        			content.setId(Integer.parseInt(args[0].toString()));
        			processChangeHistory(elapsedTimeMills, className, methodName, content);

        		}
        		break;
        }

        return retVal;
    }

	private void processViewHistory(Object retVal, long elapsedTimeMills, String className, String methodName) {
		Mono<Sample> result = (Mono<Sample>)retVal;
		result.subscribe(content -> {
			SampleDTO sampleDTO = new SampleDTO();
			sampleDTO.setCategory("view");
			sampleDTO.setContent(content);
			sampleDTO.setClassName(className);
			sampleDTO.setMethodName(methodName);
			sampleDTO.setElapsedMills(elapsedTimeMills);
			
			streamBridge.send("historyDb", sampleDTO);
		});
	}

	private void processChangeHistory(long elapsedTimeMills, String className, String methodName, Sample content) {
		SampleDTO sampleDTO = new SampleDTO();
		sampleDTO.setCategory("change");
		sampleDTO.setContent(content);
		sampleDTO.setClassName(className);
		sampleDTO.setMethodName(methodName);
		sampleDTO.setElapsedMills(elapsedTimeMills);
		
		streamBridge.send("historyDb", sampleDTO);
	}

	private void processSearchHistory(Object retVal, long elapsedTimeMills, String className, String methodName, Sort sort) {
		processSearchHistory(retVal, elapsedTimeMills, className, methodName, null, sort);
	}

	private void processSearchHistory(Object retVal, long elapsedTimeMills, String className, String methodName, Sample search) {
		processSearchHistory(retVal, elapsedTimeMills, className, methodName, search, null);
	}

	private void processSearchHistory(Object retVal, long elapsedTimeMills, String className, String methodName, Sample search, Sort sort) {
		Flux<Sample> result = (Flux<Sample>)retVal;
		List<Sample> resultList = new ArrayList<>();
		result.subscribe(sample -> {
			// toString()으로 출력 - 성능상 사용않는 것이 좋음
			log.debug("===>>> result : "+String.valueOf(sample));
			resultList.add(0,sample);
		    //streamBridge.send("historyDb", sample);
		});
		/*
		if (resultList.size() > 0) {
			log.debug("===>>> resultList : "+resultList);
			streamBridge.send("historyDb", resultList);
		}*/
		if (resultList.size() > 0) {
		    SampleListDTO sampleListDTO = new SampleListDTO();
		    sampleListDTO.setCategory("list");
		    sampleListDTO.setContentList(resultList);
		    sampleListDTO.setClassName(className);
		    sampleListDTO.setMethodName(methodName);
		    sampleListDTO.setElapsedMills(elapsedTimeMills);
		    if (search != null) sampleListDTO.setSearch(search);
		    if (sort !=null) sampleListDTO.setSort(sort);

			log.debug("===>>> sampleListDTO : "+sampleListDTO);
			streamBridge.send("historyDb", sampleListDTO);
		}
	}

}
