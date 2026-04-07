package com.bookstorage.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String field1;
    private String field2;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null) {
            return true;
        }

        try {
            Field firstField = o.getClass().getDeclaredField(field1);
            Field secondField = o.getClass().getDeclaredField(field2);
            firstField.setAccessible(true);
            secondField.setAccessible(true);

            Object firstFieldValue = firstField.get(o);
            Object secondFieldValue = secondField.get(o);

            boolean valid = Objects.equals(firstFieldValue, secondFieldValue);

            if (!valid) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        constraintValidatorContext.getDefaultConstraintMessageTemplate()
                )
                        .addPropertyNode(field2)
                        .addConstraintViolation();
            }

            return valid;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.field1 = constraintAnnotation.first();
        this.field2 = constraintAnnotation.second();
    }
}
