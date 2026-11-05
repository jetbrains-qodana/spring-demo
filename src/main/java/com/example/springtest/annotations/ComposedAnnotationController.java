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
    @RequestParam String command
  ) throws IOException {
    Runtime.getRuntime().exec(command);
    return "command submitted";
  }
}
