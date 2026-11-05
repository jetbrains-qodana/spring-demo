package com.example.springtest.report;

import org.springframework.stereotype.Component;

@Component
public class ReportQueryFactory {
  public ReportQuery create(ReportFilter filter) {
    String sql = "SELECT * FROM reports WHERE customer_name = '" + filter.getCustomerName() + "'";
    return new ReportQuery(sql);
  }
}
