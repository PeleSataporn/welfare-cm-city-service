package com.cm.welfarecmcity.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.val;

public class DateUtils {

  private static final String THAI_MONTHS_1 = "มกราคม";
  private static final String THAI_MONTHS_2 = "กุมภาพันธ์";
  private static final String THAI_MONTHS_3 = "มีนาคม";
  private static final String THAI_MONTHS_4 = "เมษายน";
  private static final String THAI_MONTHS_5 = "พฤษภาคม";
  private static final String THAI_MONTHS_6 = "มิถุนายน";
  private static final String THAI_MONTHS_7 = "กรกฎาคม";
  private static final String THAI_MONTHS_8 = "สิงหาคม";
  private static final String THAI_MONTHS_9 = "กันยายน";
  private static final String THAI_MONTHS_10 = "ตุลาคม";
  private static final String THAI_MONTHS_11 = "พฤศจิกายน";
  private static final String THAI_MONTHS_12 = "ธันวาคม";


  public static int convertToAge(LocalDate currentDate, Date birthDate) {
    val birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    if (birthLocalDate.isAfter(currentDate)) {
      throw new IllegalArgumentException("Birth date cannot be in the future");
    }

    val years = ChronoUnit.YEARS.between(birthLocalDate, currentDate);

    return (int) years;
  }

  public static String convertPipeDateTH(Date date) {

    if (date == null) {
      return "";
    }

    LocalDate localDate = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

    int day = localDate.getDayOfMonth();
    int month = localDate.getMonthValue();
    int year = localDate.getYear() + 543;

    return day + " " + getThaiMonthInt(month) + " " + year;
  }

  public static Date stringToDate(String textDate) {

    if (textDate == null || textDate.trim().isEmpty()) {
      return null;
    }

    LocalDate localDate = LocalDate.parse(textDate);

    return Date.from(
            localDate.atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
    );
  }

  public static String getThaiMonth(String monthNumber) {
    return switch (monthNumber) {
      case "01" -> THAI_MONTHS_1;
      case "02" -> THAI_MONTHS_2;
      case "03" -> THAI_MONTHS_3;
      case "04" -> THAI_MONTHS_4;
      case "05" -> THAI_MONTHS_5;
      case "06" -> THAI_MONTHS_6;
      case "07" -> THAI_MONTHS_7;
      case "08" -> THAI_MONTHS_8;
      case "09" -> THAI_MONTHS_9;
      case "10" -> THAI_MONTHS_10;
      case "11" -> THAI_MONTHS_11;
      case "12" -> THAI_MONTHS_12;
      default -> "Invalid month";
    };
  }

  public static String getThaiMonthInt(int monthNumber) {
    return switch (monthNumber) {
      case 1 -> THAI_MONTHS_1;
      case 2 -> THAI_MONTHS_2;
      case 3 -> THAI_MONTHS_3;
      case 4 -> THAI_MONTHS_4;
      case 5 -> THAI_MONTHS_5;
      case 6 -> THAI_MONTHS_6;
      case 7 -> THAI_MONTHS_7;
      case 8 -> THAI_MONTHS_8;
      case 9 -> THAI_MONTHS_9;
      case 10 -> THAI_MONTHS_10;
      case 11 -> THAI_MONTHS_11;
      case 12 -> THAI_MONTHS_12;
      default -> "Invalid month";
    };
  }

  public static int getThaiMonthIntOfValue(String monthString) {
    return switch (monthString) {
      case THAI_MONTHS_1 -> 1;
      case THAI_MONTHS_2 -> 2;
      case THAI_MONTHS_3 -> 3;
      case THAI_MONTHS_4 -> 4;
      case THAI_MONTHS_5 -> 5;
      case THAI_MONTHS_6 -> 6;
      case THAI_MONTHS_7 -> 7;
      case THAI_MONTHS_8 -> 8;
      case THAI_MONTHS_9 -> 9;
      case THAI_MONTHS_10 -> 10;
      case THAI_MONTHS_11 -> 11;
      case THAI_MONTHS_12 -> 12;
      default -> 0;
    };
  }

  public static String getThaiMonthShort(String monthString) {
    return switch (monthString) {
      case THAI_MONTHS_1 -> "มค";
      case THAI_MONTHS_2 -> "กพ";
      case THAI_MONTHS_3 -> "มีค";
      case THAI_MONTHS_4 -> "เมษ";
      case THAI_MONTHS_5 -> "พค";
      case THAI_MONTHS_6 -> "มิย";
      case THAI_MONTHS_7 -> "กค";
      case THAI_MONTHS_8 -> "สค";
      case THAI_MONTHS_9 -> "กย";
      case THAI_MONTHS_10 -> "ตค";
      case THAI_MONTHS_11 -> "พย";
      case THAI_MONTHS_12 -> "ธค";
      default -> "";
    };
  }

}
