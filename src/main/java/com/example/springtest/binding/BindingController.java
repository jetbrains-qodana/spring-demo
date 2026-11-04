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
    // SPRING LIFECYCLE: A model-attribute producer runs before the mapped handler and supplies its form object.
    return new DangerousInput();
  }

  public DangerousInput inputWithoutAnnotations() {
    // CONTROL: Spring does not call an ordinary producer method during request binding.
    return new DangerousInput();
  }

  @PostMapping("/examples/validated-model")
  public String validatedModel(
    // SOURCE: Spring copies request form fields into this model object, so the remote caller controls command.
    // VALIDATION: @Valid invokes the custom constraint validator, but validation does not escape shell syntax.
    @Valid @ModelAttribute("input") DangerousInput input // Step 1/0
  ) throws IOException {
    // SECURITY SINK: The bound command is passed to the operating system after validation.
    Runtime.getRuntime().exec(input.getCommand()); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/0; Step 4/0
    return "validation completed";
  }

  public String validatedModelWithoutAnnotations(DangerousInput input) throws IOException {
    // CONTROL: This has the same Java body, but no HTTP mapping or model binding supplies untrusted data.
    Runtime.getRuntime().exec(input.getCommand());
    return "validation completed";
  }

  @PostMapping("/examples/model-attribute-only")
  public String mappedModelAttribute(
    @ModelAttribute("modelAttributeOnly") DangerousInput input // Step 1/1
  ) throws IOException {
    // SOURCE: A mapped handler makes Spring populate this model attribute from request form fields.
    Runtime.getRuntime().exec(input.getCommand()); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/1; Step 4/1
    return "model attribute processed";
  }

  public String unmappedModelAttribute(
    @ModelAttribute("modelAttributeOnly") DangerousInput input
  ) throws IOException {
    // CONTROL: @ModelAttribute alone does not expose an ordinary method as an HTTP handler.
    Runtime.getRuntime().exec(input.getCommand());
    return "model attribute processed";
  }

  @PostMapping("/examples/valid-only")
  public String mappedValid(
    @Valid DangerousInput input // Step 1/2
  ) throws IOException {
    // SOURCE: Spring implicitly treats this complex handler parameter as a bound model attribute.
    // VALIDATION: @Valid invokes validation but does not sanitize a value used as an operating-system command.
    Runtime.getRuntime().exec(input.getCommand()); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/2; Step 4/2
    return "validated input processed";
  }

  public String unmappedValid(
    @Valid DangerousInput input
  ) throws IOException {
    // CONTROL: @Valid requests validation; it does not make this ordinary method a request entry point.
    Runtime.getRuntime().exec(input.getCommand());
    return "validated input processed";
  }

  @PostMapping("/examples/request-body-comparison")
  public String requestBodyComparison(@RequestBody DangerousInput input) throws IOException {
    // CONTROL: A comparison result does not contain data from the bound request object.
    Runtime.getRuntime().exec(String.valueOf(input.equals(new DangerousInput())));
    return "comparison completed";
  }

  @PostMapping("/examples/bound-object-method")
  public String boundObjectMethod(@RequestBody DangerousInput input) throws IOException { // Step 1/3
    String command = input.commandText(); // Step 2/3; Step 4/3
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 5/3
    return "command completed";
  }

  @PostMapping("/examples/derived-getter")
  public String derivedGetter(@RequestBody DangerousInput input) throws IOException { // Step 1/4
    String command = input.getSummary(); // Step 2/4; Step 4/4
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 5/4
    return "command completed";
  }

  @PostMapping("/examples/overloaded-getter")
  public String overloadedGetter(@RequestBody DangerousInput input) throws IOException { // Step 1/5
    String command = input.getCommand("run "); // Step 2/5; Step 4/5
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 5/5
    return "command completed";
  }

  @PostMapping("/examples/boolean-accessor")
  public String booleanAccessor(@RequestBody DangerousInput input) {
    // CONTROL: The is-prefixed accessor reads a declared bound field without reaching a security sink.
    return String.valueOf(input.isActive());
  }

  @PostMapping(value = "/examples/escaped-getter", produces = "text/html")
  public String escapedGetter(@RequestBody DangerousInput input) {
    // CONTROL: The getter escapes the bound field before the HTML response.
    return input.getEscapedCommand();
  }

  @PostMapping("/examples/constant-getter")
  public String constantGetter(@RequestBody DangerousInput input) throws IOException {
    // CONTROL: The getter returns a constant, so the receiver does not taint its result.
    Runtime.getRuntime().exec(input.getStatus());
    return "completed";
  }

  @PostMapping("/examples/primitive-field")
  public int primitiveField(@RequestBody DangerousInput input) {
    // CONTROL: Spring can bind a primitive field without creating a text sink.
    return input.getPriority();
  }

  @PostMapping("/examples/primitive-parameter")
  public int primitiveParameter(@RequestParam int priority) {
    // CONTROL: Spring can bind a primitive parameter without creating a text sink.
    return priority;
  }
}
