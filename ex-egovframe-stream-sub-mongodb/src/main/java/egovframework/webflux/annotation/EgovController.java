package egovframework.webflux.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

@Target(ElementType.TYPE) // 어노테이션이 생성될 수 있는 위치를 지정하는 어노테이션 >>> 타입 즉 클래스, 인터페이스, enum 선언 시
@Retention(RetentionPolicy.RUNTIME) // 어노테이션이 언제까지 유효할지 정하는 어노테이션 >>> 컴파일 이후까지 유효
@Documented // Java doc에 문서화 여부를 결정
@Controller
public @interface EgovController {

    @AliasFor(annotation = Controller.class)
    String value() default "";

}
