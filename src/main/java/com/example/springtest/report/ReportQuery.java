package com.example.springtest.report;

public class ReportQuery {
  private final String sql;

  public ReportQuery(String sql) {
    this.sql = sql;
  }

  public String getSql() {
    return sql;
  }
}
