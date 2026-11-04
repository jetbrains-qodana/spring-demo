package com.example.springtest.report;

import org.springframework.stereotype.Component;

@Component
public class ReportQueryFactory {
  public ReportQuery create(ReportFilter filter) { // Step 12/0
    String sql = "SELECT * FROM reports WHERE customer_name = '" + filter.getCustomerName() + "'"; // Step 13/0; Step 15/0
    return new ReportQuery(sql); // Step 16/0; Step 19/0
  }
}
