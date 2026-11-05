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
    @RequestParam String title
  ) {
    jdbcTemplate.queryForObject("SELECT * FROM messages WHERE title = '" + title + "'", Integer.class);
    return 0;
  }

  public int sqlWithoutAnnotations(String title) {
    jdbcTemplate.queryForObject("SELECT * FROM messages WHERE title = '" + title + "'", Integer.class);
    return 0;
  }

  @GetMapping("/examples/spel")
  public Object spel(
    @RequestParam String expression
  ) {
    ExpressionParser parser = new SpelExpressionParser();
    return parser.parseExpression(expression).getValue();
  }

  public Object spelWithoutAnnotations(String expression) {
    ExpressionParser parser = new SpelExpressionParser();
    return parser.parseExpression(expression).getValue();
  }

  @GetMapping("/examples/redirect")
  public RedirectView redirect(
    @RequestParam String target
  ) {
    return new RedirectView(target);
  }

  public RedirectView redirectWithoutAnnotations(String target) {
    return new RedirectView(target);
  }

  @GetMapping("/examples/files/{*fileName}")
  public String file(
    @PathVariable String fileName
  ) throws IOException {
    try (FileInputStream input = new FileInputStream(fileName)) {
      return Integer.toString(input.read());
    }
  }

  public String fileWithoutAnnotations(String fileName) throws IOException {
    try (FileInputStream input = new FileInputStream(fileName)) {
      return Integer.toString(input.read());
    }
  }
}
