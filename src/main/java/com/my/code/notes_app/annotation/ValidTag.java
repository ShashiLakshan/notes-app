package com.my.code.notes_app.annotation;

import com.my.code.notes_app.validation.TagValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TagValidator.class)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTag {

    String message() default "Invalid tags. Allowed values are BUSINESS, PERSONAL, IMPORTANT.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
