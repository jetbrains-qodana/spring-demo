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
    @RequestParam String command // Step 1/1
  ) throws IOException {
    // SOURCE: Spring reads command from the request query string.
    // SECURITY SINK: The request value controls an operating-system process.
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/1
    return "command submitted";
  }
}

class ControllerAnnotationControl {
  public String controller(
    String command
  ) throws IOException {
    // CONTROL: With no Spring annotations, this identical body is never supplied with request data.
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }
}
