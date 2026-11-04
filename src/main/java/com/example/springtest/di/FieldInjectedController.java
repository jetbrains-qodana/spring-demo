package com.example.springtest.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FieldInjectedController {
  // SPRING LIFECYCLE: The container assigns the managed service instance before handling requests.
  @Autowired
  private HtmlPageService pageService;

  private final PlainHtmlPageService plainPageService = new PlainHtmlPageService();

  @GetMapping(value = "/examples/field-di", produces = MediaType.TEXT_HTML_VALUE)
  public String fieldInjection(
    // SOURCE: Spring obtains this message from the caller's query string.
    @RequestParam String message // Step 1/1
  ) {
    // SECURITY SINK: The unescaped value returns as HTML through an injected dependency.
    return pageService.render(message); // Inspection: java-basic-xss, taint.XSS; Step 2/1; Step 5/1
  }

  public String fieldInjectionWithoutAnnotations(String message) {
    // CONTROL: The plain field and unannotated method are never supplied with an HTTP request value.
    return plainPageService.render(message);
  }
}
