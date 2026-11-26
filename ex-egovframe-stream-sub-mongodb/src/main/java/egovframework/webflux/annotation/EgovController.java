package egovframework.webflux.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

/**
 * 표준프레임워크 컨트롤러 스테레오타입
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
public @interface EgovController {

    @AliasFor(annotation = Controller.class)
    String value() default "";

}
