package com.easymedia.error.hotel.utility.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class DigitValidator implements ConstraintValidator<Digit, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Pattern.matches("^[0-9]+$", value);
    }

}
