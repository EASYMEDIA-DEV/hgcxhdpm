package com.easymedia.error.hotel.utility.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NonWhiteSpaceValidator.class)
@Documented
public @interface NonWhiteSpace {

    String message() default "공백을 사용할 수 없습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
