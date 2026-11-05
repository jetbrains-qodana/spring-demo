package com.example.springtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RendererConfiguration {
  @Bean
  public LifecyclePageRenderer lifecyclePageRenderer(HtmlPrefixProvider prefixProvider) {
    return new LifecyclePageRenderer(prefixProvider);
  }
}
