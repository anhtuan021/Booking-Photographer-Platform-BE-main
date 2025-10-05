package org.bookingplatform.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VietnamesePhoneValidator implements ConstraintValidator<VietnamesePhone, String> {

    private static final String PHONE_REGEX = "^(0|\\+84)[35789]\\d{8}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return value.matches(PHONE_REGEX);
    }
}