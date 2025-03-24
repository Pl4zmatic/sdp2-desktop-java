package repository;

import domein.machine.report.Report;

public class ReportDaoJpa extends GenericDaoJpa<Report>{

  public ReportDaoJpa() {
    super(Report.class);
  }

  
}
