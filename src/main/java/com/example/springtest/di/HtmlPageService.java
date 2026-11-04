package com.example.springtest.di;

import org.springframework.stereotype.Service;

@Service
public class HtmlPageService {
  public String render(String value) { // Step 3/0; Step 3/1
    // PROPAGATION: The request value crosses a service-call boundary and is returned unchanged.
    return "<html><body>Service result: " + value + "</body></html>"; // Step 4/0; Step 4/1
  }
}
