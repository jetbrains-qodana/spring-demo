package com.example.springtest.di;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConstructorInjectedController {
  private final HtmlPageService pageService;
  private final PlainHtmlPageService plainPageService = new PlainHtmlPageService();

  // SPRING LIFECYCLE: A single constructor is used for dependency injection even without @Autowired.
  public ConstructorInjectedController(HtmlPageService pageService) {
    this.pageService = pageService;
  }

  @GetMapping(value = "/examples/constructor-di", produces = MediaType.TEXT_HTML_VALUE)
  public String constructorInjection(
    // SOURCE: Spring obtains this message from the caller's query string.
    @RequestParam String message // Step 1/0
  ) {
    // SECURITY SINK: The unescaped value returns as HTML after crossing the injected service boundary.
    return pageService.render(message); // Inspection: java-basic-xss, taint.XSS; Step 2/0; Step 5/0
  }

  public String constructorInjectionWithoutAnnotations(String message) {
    // CONTROL: Without a mapping annotation, this identical service call is not an HTTP response.
    return plainPageService.render(message);
  }
}
