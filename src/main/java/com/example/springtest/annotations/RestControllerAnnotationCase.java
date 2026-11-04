package com.example.springtest.annotations;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Demonstrates that {@code @RestController} makes the mapped method a request callback.
 * The neighboring control repeats the same Java method without any Spring annotations.
 */
@RestController
public class RestControllerAnnotationCase {
  @GetMapping("/examples/annotations/rest-controller")
  public String restController(
    @RequestParam String command // Step 1/9
  ) throws IOException {
    // SOURCE: The query parameter originates outside the application.
    // SECURITY SINK: Passing it to exec allows the remote caller to influence process creation.
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/9
    return "command submitted";
  }
}

class RestControllerAnnotationControl {
  public String restController(
    String command
  ) throws IOException {
    // CONTROL: With no Spring annotations, this identical body is not part of request dispatch.
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }
}
