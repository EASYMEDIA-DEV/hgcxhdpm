package com.easymedia.error.hotel.utility.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.charset.StandardCharsets;

public class ByteLengthValidator implements ConstraintValidator<ByteLength, String> {

    private long value;
    private String charset;

    @Override
    public void initialize(ByteLength constraintAnnotation) {
        value = constraintAnnotation.value();
        charset = constraintAnnotation.charset();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.getBytes(StandardCharsets.UTF_8).length <= this.value;
    }

}
