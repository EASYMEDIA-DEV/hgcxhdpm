package com.easymedia.error.hotel.utility.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DigitValidator.class)
@Documented
public @interface Digit {

    String message() default "숫자만 가능합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
