package com.example.springtest.annotations;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Exercises every Spring MVC mapping and binding annotation from the acceptance checklist.
 *
 * <p>The annotated methods are HTTP handlers. Spring creates their arguments from URL parameters,
 * path segments, headers, cookies, request bodies, or form fields, so an unauthenticated caller can
 * choose each command value. The unannotated neighbor of every handler has the same Java body, but it
 * is an ordinary method that Spring never calls with request data.</p>
 */
@RestController
@RequestMapping("/examples/annotations")
@Validated
public class HttpAnnotationController {
  @RequestMapping("/request-mapping")
  public String requestMapping(
    @RequestParam String command // Step 1/2
  ) throws IOException {
    // SECURITY SINK: Starting an operating-system process with request data permits command injection.
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/2
    return "command submitted";
  }

  public String requestMappingWithoutAnnotations(
    String command
  ) throws IOException {
    // CONTROL: No Spring annotation exposes this method or obtains its argument from an HTTP request.
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  @GetMapping("/get-mapping")
  public String getMapping(
    @RequestParam String command // Step 1/3
  ) throws IOException {
    // SECURITY SINK: The query parameter is controlled by the caller and becomes a process command.
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/3
    return "command submitted";
  }

  public String getMappingWithoutAnnotations(
    String command
  ) throws IOException {
    // CONTROL: This duplicate body has no framework entry point and therefore no request source.
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  @PostMapping("/post-mapping")
  public String postMapping(
    @RequestBody CommandPayload input // Step 1/4
  ) throws IOException {
    // SECURITY SINK: Spring deserializes attacker-supplied JSON into input.command before this call.
    Runtime.getRuntime().exec(input.getCommand()); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/4; Step 4/4
    return "command submitted";
  }

  public String postMappingWithoutAnnotations(
    PlainCommandPayload input
  ) throws IOException {
    // CONTROL: The plain value object is supplied only by normal Java callers, not by Spring MVC.
    Runtime.getRuntime().exec(input.getCommand());
    return "command submitted";
  }

  @PutMapping("/put-mapping")
  public String putMapping(
    @RequestHeader("X-Command") String command // Step 1/5
  ) throws IOException {
    // SECURITY SINK: Any HTTP client can set this header and select the process command.
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/5
    return "command submitted";
  }

  public String putMappingWithoutAnnotations(
    String command
  ) throws IOException {
    // CONTROL: The identical process call is not tainted because the method is not an HTTP handler.
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  @PatchMapping("/patch-mapping")
  public String patchMapping(
    @CookieValue("command") String command // Step 1/6
  ) throws IOException {
    // SECURITY SINK: Cookies are client-controlled request data and must not become process commands.
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/6
    return "command submitted";
  }

  public String patchMappingWithoutAnnotations(
    String command
  ) throws IOException {
    // CONTROL: Without Spring annotations, command is merely an ordinary Java argument.
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  @DeleteMapping("/delete-mapping/{command}")
  public String deleteMapping(
    @PathVariable String command // Step 1/7
  ) throws IOException {
    // SECURITY SINK: The caller chooses the route segment that is passed to the operating system.
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/7
    return "command submitted";
  }

  public String deleteMappingWithoutAnnotations(
    String command
  ) throws IOException {
    // CONTROL: No router calls this method, even though its Java body intentionally matches the handler.
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  @GetMapping("/model-attribute")
  public String modelAttribute(
    @Valid @ModelAttribute("input") CommandPayload input // Step 1/8
  ) throws IOException {
    // SOURCE: Spring populates the model object's writable command property from request parameters.
    // VALIDATION: @Valid, @NotBlank, and @Size constrain shape; they do not make a shell command safe.
    // SECURITY SINK: Validation does not prevent command metacharacters from reaching the process API.
    Runtime.getRuntime().exec(input.getCommand()); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/8; Step 4/8
    return "command submitted";
  }

  public String modelAttributeWithoutAnnotations(PlainCommandPayload input) throws IOException {
    // CONTROL: This object was not created by web binding, and this method is not a mapped callback.
    Runtime.getRuntime().exec(input.getCommand());
    return "command submitted";
  }

  public static class CommandPayload {
    @NotBlank
    @Size(max = 200)
    private String command;

    public String getCommand() {
      return command; // Step 3/4; Step 3/8
    }

    public void setCommand(String command) {
      this.command = command;
    }
  }

  public static class PlainCommandPayload {
    private String command;

    public String getCommand() {
      return command;
    }

    public void setCommand(String command) {
      this.command = command;
    }
  }
}
