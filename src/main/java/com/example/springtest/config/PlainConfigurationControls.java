package com.example.springtest.config;

/**
 * Mirrors the configuration, component, bean factory, and post-construction types without annotations.
 * Spring does not discover these classes or invoke their methods; they are used only by an ordinary control method.
 */
class PlainRendererConfiguration {
  public PlainLifecyclePageRenderer lifecyclePageRenderer(PlainHtmlPrefixProvider prefixProvider) {
    return new PlainLifecyclePageRenderer(prefixProvider);
  }
}

class PlainHtmlPrefixProvider {
  public String prefix() {
    return "<html><body>Configured result: ";
  }
}

class PlainLifecyclePageRenderer {
  private final PlainHtmlPrefixProvider prefixProvider;
  private String prefix;

  PlainLifecyclePageRenderer(PlainHtmlPrefixProvider prefixProvider) {
    this.prefixProvider = prefixProvider;
  }

  public void initialize() {
    prefix = prefixProvider.prefix();
  }

  public String render(String value) {
    return prefix + value + "</body></html>";
  }
}
