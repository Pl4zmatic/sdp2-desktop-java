package main;

import java.util.Scanner;

import domein.machine.Report;

public class ReportMainTest {
  private static Report report;
  private static Scanner inputScanner;

  private static void init() {
    report = new Report();
    inputScanner = new Scanner(System.in);
  }
  public static void main(String[] args) {
    init();

    while (menu() != -1) {
      
    }
  }

  private static void printMenu() {
    System.out.printf("%s\n%s\n%s\n%s\n",
    "Reports\n" + "-".repeat(10),
      "1. Add",
      "2. Delete",
      "3. Update",
      "4. Get By Id",
      "5. Stop"
    );
  }

  private static int getMenuChoice() {
    System.out.println("Choice: ");
    return inputScanner.nextInt();
  }

  private static int menu() {
    printMenu();
    switch (getMenuChoice()) {
      case 1:
        
        break;
    
      default:
        return -1;
    }

    return 0;
  }

  private static void addReport() {
    System.out.println("");
  }

  private static void deleteReport() {

  }
}
