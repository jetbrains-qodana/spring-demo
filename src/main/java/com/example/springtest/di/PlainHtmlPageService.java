package com.example.springtest.di;

/** Structural control for {@link HtmlPageService}, with the same rendering behavior and no Spring stereotype. */
public class PlainHtmlPageService {
  public String render(String value) {
    return "<html><body>Service result: " + value + "</body></html>";
  }
}
