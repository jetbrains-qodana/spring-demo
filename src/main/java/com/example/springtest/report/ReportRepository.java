package com.example.springtest.report;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReportRepository {
  private final JdbcTemplate jdbcTemplate;

  public ReportRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public int count(ReportQuery query) {
    jdbcTemplate.queryForObject(query.getSql(), Integer.class);
    return 0;
  }
}
