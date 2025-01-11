package com.cm.welfarecmcity.Scheduled;

import com.cm.welfarecmcity.api.summary.SummaryRepository;
import com.cm.welfarecmcity.dto.SummaryDto;
import com.cm.welfarecmcity.logic.document.DocumentService;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import com.cm.welfarecmcity.logic.document.model.GrandTotalRes;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MonthlyTotalJob {

  @Autowired private DocumentService documentService;

  @Autowired private SummaryRepository summaryRepository;

  @Scheduled(
      cron =
          "0 0 0 L * ?") // Execute at midnight on the last day of the month = [ 1."0 0 0 28-31 * ?"
  // ** 2. "0 0 0 L * ?" ]  ||  0 */2 * * * * => 2 minute
  public void executeMonthlyJob() throws InterruptedException {
    // System.out.println("Monthly job executed on the last day of the month.");
    LocalDate currentDate = LocalDate.now();
    String monthThai = checkMonthToFormatThai(currentDate.getMonthValue());
    int yearThai = currentDate.getYear() + 543;
    DocumentReq req = new DocumentReq();
    req.setMonthCurrent(monthThai);
    req.setYearCurrent(String.valueOf(yearThai));
    GrandTotalRes resultTotal = documentService.grandTotal(req);

    // set data to SummaryDto
    SummaryDto summaryDto = new SummaryDto();
    summaryDto.setSumMonth(monthThai);
    summaryDto.setSumYear(String.valueOf(yearThai));
    summaryDto.setSumEmp(resultTotal.getSumEmp());
    summaryDto.setSumLoan(resultTotal.getSumLoan());
    summaryDto.setSumLoanBalance(resultTotal.getSumLoanBalance());
    //    int sumDelete =
    //        Math.round(resultTotal.getSumStockAccumulate() - resultTotal.getSumStockValue());
    int sumDelete = (int) (resultTotal.getSumStockAccumulate() - resultTotal.getSumStockValue());
    summaryDto.setSumStockAccumulate((long) sumDelete);
    summaryDto.setSumStockValue(resultTotal.getSumStockValue());
    summaryDto.setSumLoanInterest(resultTotal.getSumLoanInterest());
    summaryDto.setSumLoanOrdinary(resultTotal.getSumLoanOrdinary());
    summaryDto.setSumTotal(resultTotal.getSumTotal());
    val summaryAll = summaryRepository.save(summaryDto);

    log.info(" ======= [ Scheduled Service ] ======= ");
    log.info(" ======= [ resultTotal ] ======= ");
    log.info(" getSumLoan = " + summaryAll.getSumEmp());
    log.info(" getSumLoan = " + summaryAll.getSumLoan());
    log.info(" getSumLoanBalance = " + summaryAll.getSumLoanBalance());
    log.info(
        " getSumStockAccumulate = "
            + summaryAll.getSumStockAccumulate()); // resultTotal.getSumStockAccumulate()
    log.info(" getSumStockValue = " + summaryAll.getSumStockValue());
    log.info(" getSumLoanInterest = " + summaryAll.getSumLoanInterest());
    log.info(" getSumLoanOrdinary = " + summaryAll.getSumLoanOrdinary());
    log.info(" getSumTotal = " + summaryAll.getSumTotal());
  }

  public String checkMonthToFormatThai(int month) {
    return switch (month) {
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
      default -> "";
    };
  }

  @Bean
  public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(1);
    scheduler.setThreadNamePrefix("GRAND-TOTAL");
    return scheduler;
  }
}
