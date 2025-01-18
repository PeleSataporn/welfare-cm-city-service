package com.cm.welfarecmcity.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.val;

public class DateUtils {

  public static int convertToAge(LocalDate currentDate, Date birthDate) {
    val birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    if (birthLocalDate.isAfter(currentDate)) {
      throw new IllegalArgumentException("Birth date cannot be in the future");
    }

    val years = ChronoUnit.YEARS.between(birthLocalDate, currentDate);

    return (int) years;
  }

  public static String getThaiMonth(String monthNumber) {
    return switch (monthNumber) {
      case "01" -> "มกราคม";
      case "02" -> "กุมภาพันธ์";
      case "03" -> "มีนาคม";
      case "04" -> "เมษายน";
      case "05" -> "พฤษภาคม";
      case "06" -> "มิถุนายน";
      case "07" -> "กรกฎาคม";
      case "08" -> "สิงหาคม";
      case "09" -> "กันยายน";
      case "10" -> "ตุลาคม";
      case "11" -> "พฤศจิกายน";
      case "12" -> "ธันวาคม";
      default -> "Invalid month";
    };
  }

  public static String getThaiMonthInt(int monthNumber) {
    return switch (monthNumber) {
      case 1 -> "มกราคม";
      case 2 -> "กุมภาพันธ์";
      case 3 -> "มีนาคม";
      case 4 -> "เมษายน";
      case 5 -> "พฤษภาคม";
      case 6 -> "มิถุนายน";
      case 7 -> "กรกฎาคม";
      case 8 -> "สิงหาคม";
      case 9 -> "กันยายน";
      case 10 -> "ตุลาคม";
      case 11 -> "พฤศจิกายน";
      case 12 -> "ธันวาคม";
      default -> "Invalid month";
    };
  }

  public static int getThaiMonthIntOfValue(String monthString) {
    return switch (monthString) {
      case "มกราคม" -> 1;
      case "กุมภาพันธ์" -> 2;
      case "มีนาคม" -> 3;
      case "เมษายน" -> 4;
      case "พฤษภาคม" -> 5;
      case "มิถุนายน" -> 6;
      case "กรกฎาคม" -> 7;
      case "สิงหาคม" -> 8;
      case "กันยายน" -> 9;
      case "ตุลาคม" -> 10;
      case "พฤศจิกายน" -> 11;
      case "ธันวาคม" -> 12;
      default -> 0;
    };
  }
}
