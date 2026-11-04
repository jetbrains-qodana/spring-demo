package com.example.springtest.report;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReportRepository {
  private final JdbcTemplate jdbcTemplate;

  public ReportRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public int count(ReportQuery query) { // Step 22/0
    jdbcTemplate.queryForObject(query.getSql(), Integer.class); // Inspection: java-basic-sql-injection, taint.SQLi; Step 23/0; Step 25/0
    return 0;
  }
}
