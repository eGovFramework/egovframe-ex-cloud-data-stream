package egovframework.webflux.config;

import org.springframework.util.ObjectUtils;

import egovframework.webflux.annotation.EgovNotNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EgovValidation implements ConstraintValidator<EgovNotNull, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (ObjectUtils.isEmpty(value)) {
            return false;
        } else {
            return true;
        }
    }

}