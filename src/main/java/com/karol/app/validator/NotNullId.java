package com.karol.app.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotNullIdValidator.class)
public @interface NotNullId {
    String message() default "Object has to contains not null id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
