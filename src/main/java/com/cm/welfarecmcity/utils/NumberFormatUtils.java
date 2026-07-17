package com.cm.welfarecmcity.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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

  private static final DecimalFormat FORMAT_2 = new DecimalFormat("#,##0.00");
  private static final DecimalFormat FORMAT = new DecimalFormat("#,##0.##");

  public static String formattedNumber(String number) {

    if (number == null || number.trim().isEmpty()) {
      return "";
    }

    try {
      BigDecimal value = new BigDecimal(number.replace(",", "").trim());
      return FORMAT_2.format(value);
    } catch (NumberFormatException e) {
      return number;
    }
  }

  public static String formattedNumber2(String number) {

    if (number == null || number.trim().isEmpty()) {
      return "";
    }

    try {
      BigDecimal value = new BigDecimal(number.replace(",", "").trim());
      return FORMAT.format(value);
    } catch (NumberFormatException e) {
      return number;
    }
  }

  public static int commaParseInt(String value) {

    if (value == null || value.trim().isEmpty()) {
      return 0;
    }

    return Integer.parseInt(
            value.replace(",", "")
    );
  }

  public static String formatDecimalPercent(String value) {

    if (value == null || value.trim().isEmpty()) {
      return "";
    }

    try {

      BigDecimal number = new BigDecimal(value);

      if (number.scale() <= 1 && number.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
        return number.setScale(0).toPlainString();
      }

      return number.toPlainString();

    } catch (Exception e) {
      return value;
    }
  }

}
