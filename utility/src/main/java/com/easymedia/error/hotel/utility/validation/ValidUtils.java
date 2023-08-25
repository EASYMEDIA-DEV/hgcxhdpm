package com.easymedia.error.hotel.utility.validation;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidUtils {

    /**
     * Dto에서 Validation 에러 조회
     * @param bindingResult
     * @return
     */
    public static Map<String, String>  getErrors(BindingResult bindingResult){

        //Field, Message
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {

            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError error : allErrors) {
                //System.out.println(error.getDefaultMessage());
                FieldError field = ((FieldError) error);
                errors.put(field.getField(), field.getDefaultMessage());
            }

            return errors;
        }

        return null;
    }

}
