package com.example.springtest.config;

import org.springframework.stereotype.Component;

@Component
public class HtmlPrefixProvider {
  public String prefix() {
    return "<html><body>Configured result: ";
  }
}
