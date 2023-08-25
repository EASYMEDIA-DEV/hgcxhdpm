package com.easymedia.error.hotel.utility.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class AlphanumericValidator implements ConstraintValidator<Alphanumeric, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Pattern.matches("^[a-zA-Z0-9]+$", value);
    }

}
