package com.easymedia.error.hotel.utility.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NonWhiteSpaceValidator implements ConstraintValidator<NonWhiteSpace, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Pattern.matches("^\\S*$", value);
    }

}
