package service;

import java.util.Collections;
import java.util.List;

import domein.machine.report.Report;
import repository.ReportDaoJpa;

public class ReportService {
  private ReportDaoJpa reportDaoJpa;

  private static ReportService instance;

  public static ReportService getInstance() {
    if (instance == null) {
      instance = new ReportService();
    }

    return instance;
  }

  public ReportService() {
    this.reportDaoJpa = new ReportDaoJpa();
  }

  public void addReport(Report report) {
    try {
      ReportDaoJpa.startTransaction();
      reportDaoJpa.insert(report);
      ReportDaoJpa.commitTransaction();
    } catch (Exception e) {
      System.out.printf("%s\n%s\n%s\n", "=".repeat(20), "Adding report failed.", "=".repeat(20));
      e.printStackTrace();
    }
  }

  public void deleteReport(Report report) {
    try {
      ReportDaoJpa.startTransaction();
      reportDaoJpa.delete(report);
      ReportDaoJpa.commitTransaction();
    } catch (Exception e) {
      System.out.printf("%s\n%s\n%s\n", "=".repeat(20), "Deleting report failed.", "=".repeat(20));
      e.printStackTrace();
    }
  }

  public void updateReport(Report report) {
    try {
      ReportDaoJpa.startTransaction();
      reportDaoJpa.update(report);
      ReportDaoJpa.commitTransaction();
    } catch (Exception e) {
      System.out.printf("%s\n%s\n%\n", "=".repeat(20), "Updating report failed.", "=".repeat(20));
      e.printStackTrace();
    }
  }

  public Report getReportById(int id) {
    try {
      return reportDaoJpa.get(id);
    } catch (Exception e) {
      System.out.printf("%s\n%s%d\n%s\n", "=".repeat(20), "No report found with id = ", id, "=".repeat(20));
      e.printStackTrace();
    }

    return null;
  }

  public List<Report> getAllReports() {
    try {
      return Collections.unmodifiableList(reportDaoJpa.findAll());
    } catch (Exception e) {
      System.out.printf("%s\n%s%d\n%s\n", "=".repeat(20), "getAllReports Failed.", "=".repeat(20));
      e.printStackTrace();
    }

    return null;
  }


}
