package com.example.springtest.sinks;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class SpringSinkController {
  private final JdbcTemplate jdbcTemplate;

  public SpringSinkController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping("/examples/sql")
  public int sql(
    // SOURCE: Spring obtains the title from the caller's query string.
    @RequestParam String title // Step 1/0
  ) {
    // SECURITY SINK: Concatenating request data into a JdbcTemplate query creates SQL injection.
    jdbcTemplate.queryForObject("SELECT * FROM messages WHERE title = '" + title + "'", Integer.class); // Inspection: java-basic-sql-injection, taint.SQLi; Step 2/0
    return 0;
  }

  public int sqlWithoutAnnotations(String title) {
    // CONTROL: The same query construction has no request source when the method is not mapped.
    jdbcTemplate.queryForObject("SELECT * FROM messages WHERE title = '" + title + "'", Integer.class);
    return 0;
  }

  @GetMapping("/examples/spel")
  public Object spel(
    // SOURCE: Spring obtains the expression text from the caller's query string.
    @RequestParam String expression // Step 1/1
  ) {
    ExpressionParser parser = new SpelExpressionParser();
    // SECURITY SINK: Parsing attacker-controlled Spring Expression Language can execute unintended operations.
    return parser.parseExpression(expression).getValue(); // Inspection: java-basic-expression-injection, taint.TemplateInjection; Step 2/1
  }

  public Object spelWithoutAnnotations(String expression) {
    // CONTROL: This identical parser call is not reached through Spring request dispatch.
    ExpressionParser parser = new SpelExpressionParser();
    return parser.parseExpression(expression).getValue();
  }

  @GetMapping("/examples/redirect")
  public RedirectView redirect(
    // SOURCE: Spring obtains the redirect destination from the caller's query string.
    @RequestParam String target // Step 1/2
  ) {
    // SECURITY SINK: The application redirects to a caller-selected destination without an allowlist.
    return new RedirectView(target); // Inspection: java-basic-open-redirect, taint.OpenRedirect; Step 2/2
  }

  public RedirectView redirectWithoutAnnotations(String target) {
    // CONTROL: Returning this object from an ordinary method does not create an HTTP redirect.
    return new RedirectView(target);
  }

  @GetMapping("/examples/files/{*fileName}")
  public String file(
    // SOURCE: The route segment is supplied by the remote caller and may contain an unsafe filename.
    @PathVariable String fileName // Step 1/3
  ) throws IOException {
    // SECURITY SINK: The request path controls a filesystem read, enabling path traversal.
    try (FileInputStream input = new FileInputStream(fileName)) { // Inspection: java-basic-path-traversal, taint.PathTraversal; Step 2/3
      return Integer.toString(input.read());
    }
  }

  public String fileWithoutAnnotations(String fileName) throws IOException {
    // CONTROL: No HTTP route supplies the filename to this duplicate filesystem call.
    try (FileInputStream input = new FileInputStream(fileName)) {
      return Integer.toString(input.read());
    }
  }
}
