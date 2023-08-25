package com.easymedia.error.hotel.utility.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ByteLengthValidator.class)
@Documented
public @interface ByteLength {

    long value() ;

    String charset() default "UTF-8";

    String message() default "길이는 {value}Byte 보다 작아야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
