package com.example.springtest.config;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfiguredBeanController {
  private final LifecyclePageRenderer renderer;
  private final PlainLifecyclePageRenderer plainRenderer;

  public ConfiguredBeanController(LifecyclePageRenderer renderer) {
    this.renderer = renderer;
    plainRenderer = new PlainRendererConfiguration().lifecyclePageRenderer(new PlainHtmlPrefixProvider());
  }

  @GetMapping(value = "/examples/configured-bean", produces = MediaType.TEXT_HTML_VALUE)
  public String configuredBean(
    // SOURCE: Spring obtains this value from the caller's query string.
    @RequestParam String value // Step 1/0
  ) {
    // SECURITY SINK: The raw value returns as HTML after crossing configuration, bean, and lifecycle callbacks.
    return renderer.render(value); // Inspection: java-basic-xss, taint.XSS; Step 2/0; Step 5/0
  }

  public String configuredBeanWithoutAnnotations(String value) {
    // CONTROL: The same rendering path is not an HTML endpoint when the method has no mapping annotation.
    return plainRenderer.render(value);
  }
}
