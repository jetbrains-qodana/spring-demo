package com.example.springtest.binding;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExecutesCommandValidator implements ConstraintValidator<ExecutesCommandValidation, DangerousInput> {
  @Override
  public boolean isValid(DangerousInput value, ConstraintValidatorContext context) {
    return value == null || value.getCommand() != null;
  }
}
