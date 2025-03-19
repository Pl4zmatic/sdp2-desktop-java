package repository;

import domein.machine.Report;

public class ReportDaoJpa extends GenericDaoJpa<Report>{

  public ReportDaoJpa() {
    super(Report.class);
  }

  
}
