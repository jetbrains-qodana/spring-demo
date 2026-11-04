package com.example.springtest.config;

import jakarta.annotation.PostConstruct;

public class LifecyclePageRenderer {
  private final HtmlPrefixProvider prefixProvider;
  private String prefix;

  public LifecyclePageRenderer(HtmlPrefixProvider prefixProvider) {
    this.prefixProvider = prefixProvider;
  }

  @PostConstruct
  public void initialize() {
    // SPRING LIFECYCLE: The container calls this after construction and dependency injection.
    prefix = prefixProvider.prefix();
  }

  public String render(String value) { // Step 3/0
    // PROPAGATION: The request value is embedded unchanged in an HTML document.
    return prefix + value + "</body></html>"; // Step 4/0
  }
}
