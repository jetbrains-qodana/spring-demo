package com.example.springtest.di;

import org.springframework.stereotype.Service;

@Service
public class HtmlPageService {
  public String render(String value) {
    return "<html><body>Service result: " + value + "</body></html>";
  }
}
