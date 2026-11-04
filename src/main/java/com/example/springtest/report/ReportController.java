package com.example.springtest.report;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {
  private final ReportService reportService;

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping("/examples/reports/count")
  public int count(
    @RequestParam String customerName // Step 1/0
  ) {
    return reportService.countRows(customerName); // Step 2/0
  }
}
