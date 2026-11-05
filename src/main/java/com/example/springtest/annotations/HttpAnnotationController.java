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
    @RequestParam String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  public String requestMappingWithoutAnnotations(
    String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  @GetMapping("/get-mapping")
  public String getMapping(
    @RequestParam String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  public String getMappingWithoutAnnotations(
    String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  @PostMapping("/post-mapping")
  public String postMapping(
    @RequestBody CommandPayload input
  ) throws IOException {
    Runtime.getRuntime().exec(input.getCommand());
    return "command submitted";
  }

  public String postMappingWithoutAnnotations(
    PlainCommandPayload input
  ) throws IOException {
    Runtime.getRuntime().exec(input.getCommand());
    return "command submitted";
  }

  @PutMapping("/put-mapping")
  public String putMapping(
    @RequestHeader("X-Command") String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  public String putMappingWithoutAnnotations(
    String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  @PatchMapping("/patch-mapping")
  public String patchMapping(
    @CookieValue("command") String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  public String patchMappingWithoutAnnotations(
    String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  @DeleteMapping("/delete-mapping/{command}")
  public String deleteMapping(
    @PathVariable String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  public String deleteMappingWithoutAnnotations(
    String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }

  @GetMapping("/model-attribute")
  public String modelAttribute(
    @Valid @ModelAttribute("input") CommandPayload input
  ) throws IOException {
    Runtime.getRuntime().exec(input.getCommand());
    return "command submitted";
  }

  public String modelAttributeWithoutAnnotations(PlainCommandPayload input) throws IOException {
    Runtime.getRuntime().exec(input.getCommand());
    return "command submitted";
  }

  public static class CommandPayload {
    @NotBlank
    @Size(max = 200)
    private String command;

    public String getCommand() {
      return command;
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
