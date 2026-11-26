package egovframework.webflux.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Repository;

/**
 * 표준프레임워크 리포지토리 스테레오타입
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
public @interface EgovRepository {

    @AliasFor(annotation = Repository.class)
    String value() default "";

}
