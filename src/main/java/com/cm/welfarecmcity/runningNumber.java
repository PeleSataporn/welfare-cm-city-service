package com.cm.welfarecmcity;

import java.time.LocalDate;

public class runningNumber {

  public static void main(String[] args) {
    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();
    int currentMonth = currentDate.getMonthValue();
    int currentDay = currentDate.getDayOfMonth();

    int runningNumber = 1; // Start with 1
    String formattedRunningNumber = String.format("%04d", runningNumber);
    String runningNumberString =
        currentYear
            + "-"
            + String.format("%02d%02d", currentMonth, currentDay)
            + formattedRunningNumber;
    System.out.println(runningNumberString);
  }
}
