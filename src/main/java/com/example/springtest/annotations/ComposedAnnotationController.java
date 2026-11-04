package com.example.springtest.annotations;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@interface HtmlController {
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = org.springframework.web.bind.annotation.RequestMethod.GET)
@interface HtmlGet {
}

@HtmlController
public class ComposedAnnotationController {
  @HtmlGet
  public String composed(
    @RequestParam String command // Step 1/10
  ) throws IOException {
    // SECURITY SINK: Composed controller and mapping annotations expose this request parameter.
    Runtime.getRuntime().exec(command); // Inspection: java-basic-command-injection, taint.CMDi; Step 2/10
    return "command submitted";
  }
}
