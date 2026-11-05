package com.example.springtest.web;

import org.owasp.encoder.Encode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectController {
  @GetMapping(value = "/examples/direct", produces = MediaType.TEXT_HTML_VALUE)
  public String directHtml(
    @RequestParam String name
  ) {
    return "<html><body>Hello " + name + "</body></html>";
  }

  public String directHtmlWithoutAnnotations(String name) {
    return "<html><body>Hello " + name + "</body></html>";
  }

  @GetMapping(value = "/examples/escaped", produces = MediaType.TEXT_HTML_VALUE)
  public String escapedHtml(@RequestParam String name) throws java.io.IOException {
    String escaped = HtmlUtils.htmlEscape(name);
    Runtime.getRuntime().exec(escaped);
    return escaped;
  }

  @GetMapping(value = "/examples/encoded", produces = MediaType.TEXT_HTML_VALUE)
  public String encodedHtml(@RequestParam String name) {
    return "<html><body>Hello " + Encode.forHtml(name) + "</body></html>";
  }

  @GetMapping(value = "/examples/comparison", produces = MediaType.TEXT_HTML_VALUE)
  public String comparisonResult(@RequestParam String name) {
    return String.valueOf(java.util.List.of("known").indexOf(name));
  }

  @GetMapping(value = "/examples/direct-variable", produces = {"application/json", "TEXT/HTML;charset=UTF-8"})
  public String directVariable(@RequestParam String name) {
    ResponseEntity.ok().body("unused");
    return name;
  }

  @GetMapping(value = "/examples/response-entity", produces = "text/html")
  public ResponseEntity responseEntity(@RequestParam String name) {
    ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
    return builder.body(name);
  }

  @GetMapping(value = "/examples/static-helper", produces = MediaType.TEXT_HTML_VALUE)
  public String staticHelper(@RequestParam String name) {
    discard(name);
    return "completed";
  }

  @GetMapping(value = "/examples/plain-text", produces = MediaType.TEXT_PLAIN_VALUE)
  public String plainText(@RequestParam String name) {
    return name;
  }

  private static void discard(String value) {
  }
}
