package com.cm.welfarecmcity.utils;

import java.text.NumberFormat;
import java.util.Locale;
import lombok.val;

public class ThaiNumeralsUtils {
  private ThaiNumeralsUtils() {}

  public static String formatThaiWords(double amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount cannot be negative.");
    }

    val thaiLocale = new Locale("th", "TH");
    val nf = NumberFormat.getCurrencyInstance(thaiLocale);
    val thaiBaht = nf.format(amount);

    return convertCurrencyToThaiWords(thaiBaht);
  }

  private static final String[] THAI_DIGITS = {
    "ศูนย์", "หนึ่ง", "สอง", "สาม", "สี่", "ห้า", "หก", "เจ็ด", "แปด", "เก้า"
  };
  private static final String[] THAI_PLACES = {"", "สิบ", "ร้อย", "พัน", "หมื่น", "แสน", "ล้าน"};

  public static String convertCurrencyToThaiWords(String currency) {
    currency = currency.replaceAll("[฿,]", "");
    String[] currencySplit = currency.split("\\.");

    val currencyBaht = currencySplit[0];
    val currencySatang = currencySplit.length > 1 ? currencySplit[1] : "00";

    val stringBuilder = new StringBuilder();

    appendBahtAmount(stringBuilder, currencyBaht);
    appendSatangAmount(stringBuilder, currencySatang);

    return stringBuilder.toString();
  }

  private static void appendBahtAmount(StringBuilder stringBuilder, String currencyBaht) {
    for (int i = 0; i < currencyBaht.length(); i++) {
      val number = Character.getNumericValue(currencyBaht.charAt(i));
      val thaiPlace = THAI_PLACES[currencyBaht.length() - i - 1];
      if (number != 0) {
        val sumLength = currencyBaht.length() - i;
        if (number == 1 && currencyBaht.length() == 5 && i == 0) {
          stringBuilder.append("หนึ่ง");
        } else if (sumLength == 1) {
          stringBuilder.append("เอ็ด");
        } else {
          if (sumLength != 2 || (sumLength == 2 && (number != 1 && number != 2))) {
            stringBuilder.append(THAI_DIGITS[number]);
          }
        }

        if (sumLength == 2 && number == 2) {
          stringBuilder.append("ยี่สิบ");
        } else {
          stringBuilder.append(thaiPlace);
        }
      }
    }

    if (stringBuilder.isEmpty()) {
      stringBuilder.append("ศูนย์");
    }

    stringBuilder.append("บาท");
  }

  private static void appendSatangAmount(StringBuilder stringBuilder, String currencySatang) {
    if (!currencySatang.equals("00")) {
      for (int i = 0; i < currencySatang.length(); i++) {
        val number = Character.getNumericValue(currencySatang.charAt(i));
        if (number == 1 && i == 0) {
          stringBuilder.append(THAI_PLACES[number]);
        } else if (number == 2 && i == 0) {
          stringBuilder.append("ยี่สิบ");
        } else if (number != 0) {
          stringBuilder.append(THAI_DIGITS[number]);
        }
      }
      stringBuilder.append("สตางค์");
    }
    stringBuilder.append("ถ้วน");
  }
}
