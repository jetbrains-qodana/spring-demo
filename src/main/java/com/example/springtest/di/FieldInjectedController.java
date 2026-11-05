package com.example.springtest.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FieldInjectedController {
  @Autowired
  private HtmlPageService pageService;

  private final PlainHtmlPageService plainPageService = new PlainHtmlPageService();

  @GetMapping(value = "/examples/field-di", produces = MediaType.TEXT_HTML_VALUE)
  public String fieldInjection(
    @RequestParam String message
  ) {
    return pageService.render(message);
  }

  public String fieldInjectionWithoutAnnotations(String message) {
    return plainPageService.render(message);
  }
}
