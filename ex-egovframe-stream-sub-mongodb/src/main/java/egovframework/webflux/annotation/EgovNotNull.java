package egovframework.webflux.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import egovframework.webflux.config.EgovValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 필수값 검증 어노테이션 (null·빈 문자열 불가)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EgovValidation.class)
@Documented
public @interface EgovNotNull {

    String message() default "필수항목의 값을 입력하세요.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

}
