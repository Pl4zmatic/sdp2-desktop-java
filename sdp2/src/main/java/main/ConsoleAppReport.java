package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import domein.machine.Maintenance;
import domein.machine.report.Image;
import domein.machine.report.Report;
import service.ImageService;
import service.ServiceController;


public class ConsoleAppReport {
  private static Report report;
  private static ServiceController sc;
  private static Maintenance maintenance;
  private static Scanner inputScanner;
  private static ImageService imageService;

  private static void init() {
    report = new Report();
    inputScanner = new Scanner(System.in);
    sc = ServiceController.getInstance();
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


    printDbTable(sc.getAllMaintenances());

    System.out.println("Choose a maintenance with id: ");
    long maintenanceId = inputScanner.nextLong();
    
    maintenance = maintenanceService.getMaintenanceById(maintenanceId);
  }
  
  private static void printMenu() {
    List<String> menuItems = Arrays.asList("Reports\n" + "-".repeat(10),
    "1. Add",
    "2. Delete",
    "3. Update",
    "4. Show list",
    "5. Download images from report");

    System.out.printf("%s\n".repeat(menuItems.size()), menuItems.toArray());
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
        printDbTable(sc.getAllReports());
        break;

      case 5:
        downloadImages();
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

    report = new Report(paths, notes, maintenance);
    sc.addReport(report);
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

    printDbTable(sc.getAllReports());

    System.out.println("Choose a report with id: ");
    int maintenanceId = inputScanner.nextInt();

    Report toDeleteReport = sc.getReportById(maintenanceId);
    sc.deleteReport(toDeleteReport);
  }

  private static void updateReport() {

    printDbTable(sc.getAllReports());

    System.out.println("Choose a report with id: ");
    int maintenanceId = inputScanner.nextInt();

    Report toUpdateReport = sc.getReportById(maintenanceId);

    List<String> paths = inputStringList("image path");
    String steps = "step";
    System.out.println("Notes: ");
    String notes = inputScanner.nextLine();

    if (!paths.isEmpty())
      toUpdateReport.setImagesFromStrings(paths);
    if (!steps.isEmpty())
      toUpdateReport.setSteps(steps);

    sc.updateReport(toUpdateReport);
  }

  private static void downloadImages() {

    printDbTable(sc.getAllReports());

    System.out.println("Choose a report with id: ");
    int maintenanceId = inputScanner.nextInt();
    scannerFlush();

    Report report = sc.getReportById(maintenanceId);

    System.out.println("Give a destination for the file: ");
    String destination = inputScanner.nextLine();



    for(Image image : report.getImages()) {
      sc.downloadImage(destination, image);
    }
  }

  private static void scannerFlush() {
    inputScanner.nextLine();
  }
}
