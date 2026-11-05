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
    @RequestParam String value
  ) {
    return renderer.render(value);
  }

  public String configuredBeanWithoutAnnotations(String value) {
    return plainRenderer.render(value);
  }
}
