package com.example.springtest.annotations;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * A {@code @Controller} is created by Spring, and its mapped method is invoked for an HTTP request.
 * The class without {@code @Controller} below has an identical method but is not a managed controller.
 */
@Controller
public class ControllerAnnotationCase {
  @GetMapping("/examples/annotations/controller")
  public String controller(
    @RequestParam String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }
}

class ControllerAnnotationControl {
  public String controller(
    String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }
}
