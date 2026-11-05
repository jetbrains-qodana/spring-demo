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

  public int countRows(String customerName) {
    ReportFilter filter = createFilter(customerName);
    ReportQuery query = queryFactory.create(filter);
    return reportRepository.count(query);
  }

  private ReportFilter createFilter(String customerName) {
    return new ReportFilter(customerName);
  }
}
