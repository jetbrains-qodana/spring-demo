package com.example.springtest.report;

public class ReportFilter {
  private final String customerName;

  public ReportFilter(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerName() {
    return customerName;
  }
}
