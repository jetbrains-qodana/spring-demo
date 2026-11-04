package com.example.springtest.binding;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExecutesCommandValidator.class)
public @interface ExecutesCommandValidation {
  String message() default "command validation failed";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
