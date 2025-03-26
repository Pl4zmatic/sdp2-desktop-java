package repository;

import java.util.ArrayList;
import java.util.List;

import domein.machine.report.Image;
import domein.machine.report.Report;

public class ReportDaoJpa extends GenericDaoJpa<Report>{

  public ReportDaoJpa() {
    super(Report.class);
  }
}
