package com.example.springtest.binding;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class BindingController {
  @ModelAttribute("input")
  public DangerousInput input() {
    return new DangerousInput();
  }

  public DangerousInput inputWithoutAnnotations() {
    return new DangerousInput();
  }

  @PostMapping("/examples/validated-model")
  public String validatedModel(
    @Valid @ModelAttribute("input") DangerousInput input
  ) throws IOException {
    Runtime.getRuntime().exec(input.getCommand());
    return "validation completed";
  }

  public String validatedModelWithoutAnnotations(DangerousInput input) throws IOException {
    Runtime.getRuntime().exec(input.getCommand());
    return "validation completed";
  }

  @PostMapping("/examples/model-attribute-only")
  public String mappedModelAttribute(
    @ModelAttribute("modelAttributeOnly") DangerousInput input
  ) throws IOException {
    Runtime.getRuntime().exec(input.getCommand());
    return "model attribute processed";
  }

  public String unmappedModelAttribute(
    @ModelAttribute("modelAttributeOnly") DangerousInput input
  ) throws IOException {
    Runtime.getRuntime().exec(input.getCommand());
    return "model attribute processed";
  }

  @PostMapping("/examples/valid-only")
  public String mappedValid(
    @Valid DangerousInput input
  ) throws IOException {
    Runtime.getRuntime().exec(input.getCommand());
    return "validated input processed";
  }

  public String unmappedValid(
    @Valid DangerousInput input
  ) throws IOException {
    Runtime.getRuntime().exec(input.getCommand());
    return "validated input processed";
  }

  @PostMapping("/examples/request-body-comparison")
  public String requestBodyComparison(@RequestBody DangerousInput input) throws IOException {
    Runtime.getRuntime().exec(String.valueOf(input.equals(new DangerousInput())));
    return "comparison completed";
  }

  @PostMapping("/examples/bound-object-method")
  public String boundObjectMethod(@RequestBody DangerousInput input) throws IOException {
    String command = input.commandText();
    Runtime.getRuntime().exec(command);
    return "command completed";
  }

  @PostMapping("/examples/derived-getter")
  public String derivedGetter(@RequestBody DangerousInput input) throws IOException {
    String command = input.getSummary();
    Runtime.getRuntime().exec(command);
    return "command completed";
  }

  @PostMapping("/examples/overloaded-getter")
  public String overloadedGetter(@RequestBody DangerousInput input) throws IOException {
    String command = input.getCommand("run ");
    Runtime.getRuntime().exec(command);
    return "command completed";
  }

  @PostMapping("/examples/boolean-accessor")
  public String booleanAccessor(@RequestBody DangerousInput input) {
    return String.valueOf(input.isActive());
  }

  @PostMapping(value = "/examples/escaped-getter", produces = "text/html")
  public String escapedGetter(@RequestBody DangerousInput input) {
    return input.getEscapedCommand();
  }

  @PostMapping("/examples/constant-getter")
  public String constantGetter(@RequestBody DangerousInput input) throws IOException {
    Runtime.getRuntime().exec(input.getStatus());
    return "completed";
  }

  @PostMapping("/examples/primitive-field")
  public int primitiveField(@RequestBody DangerousInput input) {
    return input.getPriority();
  }

  @PostMapping("/examples/primitive-parameter")
  public int primitiveParameter(@RequestParam int priority) {
    return priority;
  }
}
