package com.example.springtest.di;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConstructorInjectedController {
  private final HtmlPageService pageService;
  private final PlainHtmlPageService plainPageService = new PlainHtmlPageService();

  public ConstructorInjectedController(HtmlPageService pageService) {
    this.pageService = pageService;
  }

  @GetMapping(value = "/examples/constructor-di", produces = MediaType.TEXT_HTML_VALUE)
  public String constructorInjection(
    @RequestParam String message
  ) {
    return pageService.render(message);
  }

  public String constructorInjectionWithoutAnnotations(String message) {
    return plainPageService.render(message);
  }
}
