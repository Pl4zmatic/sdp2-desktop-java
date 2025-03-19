package service;

import domein.machine.Report;
import lombok.NoArgsConstructor;
import repository.ReportDaoJpa;

@NoArgsConstructor
public class ReportService {
  private ReportDaoJpa rapportDaoJpa;

  public void addReport(Report report) {
    try {
      ReportDaoJpa.startTransaction();
      rapportDaoJpa.insert(report);
      ReportDaoJpa.commitTransaction();
    } catch (Exception e) {
      System.out.printf("%s\n%s\n%s\n", "=".repeat(20), "Adding report failed.", "=".repeat(20));
      e.printStackTrace();
    }
  }

  public void deleteReport(Report report) {
    try {
      ReportDaoJpa.startTransaction();
      rapportDaoJpa.delete(report);
      ReportDaoJpa.commitTransaction();
    } catch (Exception e) {
      System.out.printf("%s\n%s\n%s\n", "=".repeat(20), "Deleting report failed.", "=".repeat(20));
      e.printStackTrace();
    }
  }

  public void updateReport(Report report) {
    try {
      ReportDaoJpa.startTransaction();
      rapportDaoJpa.update(report);
      ReportDaoJpa.commitTransaction();
    } catch (Exception e) {
      System.out.printf("%s\n%s\n%\n", "=".repeat(20), "Updating report failed.", "=".repeat(20));
      e.printStackTrace();
    }
  }

  public Report getReportById(int id) {
    try {
      return rapportDaoJpa.get(id);
    } catch (Exception e) {
      System.out.printf("%s\n%s%d\n%s\n", "=".repeat(20), "No report found with id = ", id, "=".repeat(20));
      e.printStackTrace();
    }

    return null;
  }


}
