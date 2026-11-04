package com.example.springtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RendererConfiguration {
  @Bean
  public LifecyclePageRenderer lifecyclePageRenderer(HtmlPrefixProvider prefixProvider) {
    // SPRING LIFECYCLE: The container calls this factory and supplies the managed prefix provider by type.
    return new LifecyclePageRenderer(prefixProvider);
  }
}
