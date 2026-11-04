package com.example.springtest.report;

import org.springframework.stereotype.Service;

@Service
public class ReportService {
  private final ReportQueryFactory queryFactory;
  private final ReportRepository reportRepository;

  public ReportService(ReportQueryFactory queryFactory, ReportRepository reportRepository) {
    this.queryFactory = queryFactory;
    this.reportRepository = reportRepository;
  }

  public int countRows(String customerName) { // Step 3/0
    ReportFilter filter = createFilter(customerName); // Step 4/0; Step 10/0
    ReportQuery query = queryFactory.create(filter); // Step 11/0; Step 20/0
    return reportRepository.count(query); // Step 21/0
  }

  private ReportFilter createFilter(String customerName) { // Step 5/0
    return new ReportFilter(customerName); // Step 6/0; Step 9/0
  }
}
