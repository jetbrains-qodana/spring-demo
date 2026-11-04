package com.example.springtest.binding;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExecutesCommandValidator implements ConstraintValidator<ExecutesCommandValidation, DangerousInput> {
  @Override
  public boolean isValid(DangerousInput value, ConstraintValidatorContext context) {
    // VALIDATION: This checks only that a value exists; it neither escapes nor allowlists shell syntax.
    return value == null || value.getCommand() != null;
  }
}
