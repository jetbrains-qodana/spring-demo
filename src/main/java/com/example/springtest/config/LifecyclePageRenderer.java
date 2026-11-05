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
    prefix = prefixProvider.prefix();
  }

  public String render(String value) {
    return prefix + value + "</body></html>";
  }
}
