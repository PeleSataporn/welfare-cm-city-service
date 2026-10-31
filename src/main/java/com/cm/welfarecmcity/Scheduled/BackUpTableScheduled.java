package com.cm.welfarecmcity.Scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class BackUpTableScheduled {

  private final BackUpTableRepository backUpTableRepository;

  @Scheduled(cron = "0 0 0 L * ?", zone = "Asia/Bangkok")
  public void executeMonthlyBackUpTable() {

    LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Bangkok"));
    int yearThai = currentDate.getYear() + 543;

    String dateSuffix = currentDate.format(
            DateTimeFormatter.ofPattern("MMdd")
    );

    String backupDate = yearThai + dateSuffix;

    log.info("Start monthly backup table: {}", backupDate);

    try {
      backUpTableRepository.backupAllTables(backupDate);

      log.info("Monthly backup table completed: {}", backupDate);

    } catch (Exception e) {
      log.error("Monthly backup table failed: {}", backupDate, e);
    }
  }
}

