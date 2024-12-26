package com.cm.welfarecmcity.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;
import lombok.val;

public class NumberFormatUtils {
  private NumberFormatUtils() {}

  public static String numberFormat(String value) {
    // Parse the string into a number
    long number = Long.parseLong(value);

    // Format the number with commas
    val numberFormat = NumberFormat.getInstance();

    if (number == 0) {
      return "-";
    }

    return numberFormat.format(number);
  }

  public static long parseNumber(String input) {
    try {
      if (Objects.equals(input, "-")) {
        input = "0";
      }

      // Remove commas and parse the number
      NumberFormat format = NumberFormat.getInstance();
      Number number = format.parse(input);
      return number.longValue();
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid input string for number format: " + input, e);
    }
  }
}
