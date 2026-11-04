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
    // SOURCE: Spring obtains this name from the caller's query string.
    @RequestParam String name // Step 1/0
  ) {
    // SECURITY SINK: Returning unescaped request text from an HTML endpoint is reflected XSS.
    return "<html><body>Hello " + name + "</body></html>"; // Inspection: java-basic-xss, taint.XSS; Step 2/0
  }

  public String directHtmlWithoutAnnotations(String name) {
    // CONTROL: An ordinary return value is not written to an HTTP response.
    return "<html><body>Hello " + name + "</body></html>";
  }

  @GetMapping(value = "/examples/escaped", produces = MediaType.TEXT_HTML_VALUE)
  public String escapedHtml(@RequestParam String name) throws java.io.IOException { // Step 1/3
    String escaped = HtmlUtils.htmlEscape(name); // Step 2/3
    Runtime.getRuntime().exec(escaped); // Inspection: java-basic-command-injection, taint.CMDi; Step 3/3
    // CONTROL: HTML escaping removes the XSS taint before the response.
    return escaped;
  }

  @GetMapping(value = "/examples/encoded", produces = MediaType.TEXT_HTML_VALUE)
  public String encodedHtml(@RequestParam String name) {
    // CONTROL: OWASP Encoder HTML encoding removes the XSS taint before the response.
    return "<html><body>Hello " + Encode.forHtml(name) + "</body></html>";
  }

  @GetMapping(value = "/examples/comparison", produces = MediaType.TEXT_HTML_VALUE)
  public String comparisonResult(@RequestParam String name) {
    // CONTROL: A comparison result does not contain the request value.
    return String.valueOf(java.util.List.of("known").indexOf(name));
  }

  @GetMapping(value = "/examples/direct-variable", produces = {"application/json", "TEXT/HTML;charset=UTF-8"})
  public String directVariable(@RequestParam String name) { // Step 1/1
    ResponseEntity.ok().body("unused");
    return name; // Inspection: java-basic-xss, taint.XSS; Step 2/1
  }

  @GetMapping(value = "/examples/response-entity", produces = "text/html")
  public ResponseEntity responseEntity(@RequestParam String name) { // Step 1/2
    ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
    return builder.body(name); // Inspection: java-basic-xss, taint.XSS; Step 2/2
  }

  @GetMapping(value = "/examples/static-helper", produces = MediaType.TEXT_HTML_VALUE)
  public String staticHelper(@RequestParam String name) {
    // CONTROL: A static helper does not write its argument to the HTTP response.
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
