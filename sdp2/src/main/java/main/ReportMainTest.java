package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import domein.machine.Maintenance;
import domein.machine.Report;
import service.MaintenanceService;
import service.ReportService;

public class ReportMainTest {
  private static Report report;
  private static ReportService reportService;
  private static Maintenance maintenance;
  private static Scanner inputScanner;

  private static void init() {
    report = new Report();
    inputScanner = new Scanner(System.in);
    reportService = new ReportService();
  }

  public static void main(String[] args) {
    init();
    chooseMaintenance();
    while (menu() != -1) {

    }
  }

  private static <T> void printDbTable(List<T> objList) {
    System.out.println("-".repeat(20 * 8));
    objList.forEach((obj) -> {
      System.out.println(obj.toString());
      System.out.println("-".repeat(20 * 8));
    });
  }

  private static void chooseMaintenance() {
    MaintenanceService maintenanceService = new MaintenanceService();

    printDbTable(maintenanceService.getAllMaintenance());

    System.out.println("Choose a maintenance with id: ");
    int maintenanceId = inputScanner.nextInt();

    maintenance = maintenanceService.getMaintenanceById(maintenanceId);
  }

  private static void printMenu() {
    System.out.printf("%s\n".repeat(6),
        "Reports\n" + "-".repeat(10),
        "1. Add",
        "2. Delete",
        "3. Update",
        "4. Show list",
        "5. Stop");
  }

  private static int getMenuChoice() {
    System.out.println("Choice: ");
    return inputScanner.nextInt();
  }

  private static int menu() {
    printMenu();
    switch (getMenuChoice()) {
      case 1:
        addReport();
        break;

      case 2:
        deleteReport();
        break;

      case 3:
        updateReport();
        break;

      case 4:
        printDbTable(reportService.getAllReports());
        break;

      default:
        return -1;
    }

    return 0;
  }

  private static void addReport() {
    List<String> paths = inputStringList("image path");
    List<String> steps = inputStringList("step");
    System.out.println("Notes: ");
    String notes = inputScanner.nextLine();

    report = new Report(paths, steps, notes, maintenance);
    reportService.addReport(report);
  }

  private static List<String> inputStringList(String item) {
    inputScanner = new Scanner(System.in);
    List<String> list = new ArrayList<>();
    String input = "";

    System.out.printf("Please enter %s(s).\n", item);
    System.out.println("Enter nothing to stop.");

    while (true) {
      System.out.printf("%s: ", item);
      input = inputScanner.nextLine();

      if (input.isBlank() || input.isEmpty())
        break;

      list.add(input);
    }

    return list;
  }

  private static void deleteReport() {
    ReportService reportService = new ReportService();
    printDbTable(reportService.getAllReports());

    System.out.println("Choose a report with id: ");
    int maintenanceId = inputScanner.nextInt();

    Report toDeleteReport = reportService.getReportById(maintenanceId);
    reportService.deleteReport(toDeleteReport);
  }

  private static void updateReport() {
    ReportService reportService = new ReportService();
    printDbTable(reportService.getAllReports());

    System.out.println("Choose a report with id: ");
    int maintenanceId = inputScanner.nextInt();

    Report toUpdateReport = reportService.getReportById(maintenanceId);

    List<String> paths = inputStringList("image path");
    List<String> steps = inputStringList("step");
    System.out.println("Notes: ");
    String notes = inputScanner.nextLine();

    if (!paths.isEmpty())
      toUpdateReport.setImagePaths(paths);
    if(!steps.isEmpty())
      toUpdateReport.setSteps(steps);
    if(!notes.isBlank() && !notes.isEmpty())
      toUpdateReport.setNotes(notes);

    reportService.updateReport(toUpdateReport);
  }
}
