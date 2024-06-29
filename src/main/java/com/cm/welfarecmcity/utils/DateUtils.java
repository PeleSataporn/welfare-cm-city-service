package com.cm.welfarecmcity.utils;

import lombok.val;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

    public static int convertToAge(LocalDate currentDate, Date birthDate) {
        val birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (birthLocalDate.isAfter(currentDate)) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }

        val years = ChronoUnit.YEARS.between(birthLocalDate, currentDate);

        return (int) years;
    }
}
