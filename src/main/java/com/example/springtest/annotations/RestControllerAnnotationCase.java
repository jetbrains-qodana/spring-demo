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
    @RequestParam String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }
}

class RestControllerAnnotationControl {
  public String restController(
    String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }
}
