package com.cm.welfarecmcity.Scheduled;

import com.cm.welfarecmcity.Scheduled.MemberData.MemberPdfService;
import com.cm.welfarecmcity.api.document.DocumentsService;
import com.cm.welfarecmcity.api.summary.SummaryRepository;
import com.cm.welfarecmcity.dto.SummaryDto;
import com.cm.welfarecmcity.logic.document.DocumentService;
import com.cm.welfarecmcity.logic.document.model.DocumentInfoAllRes;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import com.cm.welfarecmcity.logic.document.model.GrandTotalRes;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cm.welfarecmcity.utils.DateUtils;
import com.cm.welfarecmcity.utils.NumberFormatUtils;
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

  @Autowired
  private MemberPdfService memberPdfService;

  @Autowired
  private DocumentsService DocumentsServiceAddFile;

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


  //@Scheduled(cron = "0 */10 * * * *")
  @Scheduled(cron = "0 0 0 L * ?")
  public void savePdfMemberAllForByte()  throws Exception {
    log.info("======= [ SavePdfMemberAllForByte Service ] =======");
    LocalDate currentDate = LocalDate.now();
    String monthThai = DateUtils.getThaiMonthInt(currentDate.getMonthValue());
    int yearThai = currentDate.getYear() + 543;

    DocumentReq req = new DocumentReq();
    req.setMonthCurrent(monthThai);
    req.setYearCurrent(String.valueOf(yearThai));

    List<DocumentInfoAllRes> InfoAll =  documentService.documentInfoAll(req);
    for(DocumentInfoAllRes list : InfoAll){

      // วันที่เป็นสมาชิก, regisDate
      if(list.getRegisDate() != null){
        list.setRegisDateText(DateUtils.convertPipeDateTH(list.getRegisDate()));
      }

      // อัตราเงินเดือน, salary
      if(list.getSalary() != null){
        list.setSalary(NumberFormatUtils.formattedNumber2(list.getSalary()));
      }

      //คํ้าประกันให้ codeGuaranteeOne
      if(list.getCodeGuaranteeOne() != null
              && list.getFullNameGuaranteeOne() != null){
        String fullNameCode1 = list.getCodeGuaranteeOne() + " " + list.getFullNameGuaranteeOne();
        list.setFullNameGuaranteeOneText(fullNameCode1);
      }else{
        list.setFullNameGuaranteeOneText(null);
      }

      //คํ้าประกันให้ codeGuaranteeTwo
      if(list.getCodeGuaranteeTwo() != null
              && list.getFullNameGuaranteeTwo() != null){
        String fullNameCode2 = list.getCodeGuaranteeTwo() + " " + list.getFullNameGuaranteeTwo();
        list.setFullNameGuaranteeTwoText(fullNameCode2);
      }else{
        list.setFullNameGuaranteeTwoText(null);
      }

      // ส่งค่าหุ้น, stockValue
      if(list.getStockValue() != null){
        list.setStockValue(NumberFormatUtils.formattedNumber2(list.getStockValue()));
      }

      // งวดที่, installment
      //            if(list.getInstallment() <= 1){
      //                list.setInstallment(list.getInstallment());
      //            }else{
      //                int sumInstallment = list.getInstallment() - 1;
      //                list.setInstallment(sumInstallment);
      //            }

      // หุ้นสะสม, stockAccumulate, stockValue
      int stockAcc = NumberFormatUtils.commaParseInt(list.getStockAccumulate());
      int stockVl = NumberFormatUtils.commaParseInt(list.getStockValue());
      int sumAccVl = stockAcc - stockVl;
      list.setStockAccVlText(NumberFormatUtils.formattedNumber2(String.valueOf(sumAccVl)));

      // อัตราดอกเบี้ย, interestPercent
      if(list.getInterestPercent() != null && list.getInterestPercent() >= 0){
        list.setInterestPercentText(NumberFormatUtils.formatDecimalPercent(String.valueOf(list.getInterestPercent())));
      }

      // กู้เงินจํานวน, loanValue
      if(list.getLoanValue() != null){
        list.setLoanValueText(NumberFormatUtils.formattedNumber2(String.valueOf(list.getLoanValue())));
      }

      // วันที่ทําสัญญา, startLoanDate
      if(list.getStartLoanDate() != null){
        Date dateCv = DateUtils.stringToDate(list.getStartLoanDate());
        list.setStartLoanDate(DateUtils.convertPipeDateTH(dateCv));
      }else{
        list.setStartLoanDate("-");
      }

      //ผู้คํ้าประกัน codeGuarantorOne
      if(list.getCodeGuarantorOne() != null
              && list.getFullNameGuarantorOne() != null){
        String fullNameCode1 = list.getCodeGuarantorOne() + " " + list.getFullNameGuarantorOne();
        list.setFullNameGuarantorOneText(fullNameCode1);
      }else{
        list.setFullNameGuarantorOneText(null);
      }

      //ผู้คํ้าประกัน codeGuarantorTwo
      if(list.getCodeGuarantorTwo() != null
              && list.getFullNameGuarantorTwo() != null){
        String fullNameCode2 = list.getCodeGuarantorTwo() + " " + list.getFullNameGuarantorTwo();
        list.setFullNameGuarantorTwoText(fullNameCode2);
      }else{
        list.setFullNameGuarantorTwoText(null);
      }

      // ดอกเดือนนี้, interestMonth
      if(list.getInterestMonth() >= 0){
        list.setInterestMonthText(NumberFormatUtils.formattedNumber2(String.valueOf(list.getInterestMonth())));
      }
      // ต้นเดือนนี้, earlyMonth - interestMonth
      int earlyMonth = list.getEarlyMonth();
      int interestMonth = list.getInterestMonth();
      int sumEI = earlyMonth - interestMonth;
      list.setEarlyMonthIntText(NumberFormatUtils.formattedNumber2(String.valueOf(sumEI)));
      // เดือนสุดท้าย, interestMonthLast
      if(list.getInterestMonthLast() >= 0){
        list.setInterestMonthLastText(NumberFormatUtils.formattedNumber2(String.valueOf(list.getInterestMonthLast())));
      }
      // เดือนสุดท้าย, earlyMonthLast
      if(list.getEarlyMonthLast() >= 0){
        list.setEarlyMonthLastText(NumberFormatUtils.formattedNumber2(String.valueOf(list.getEarlyMonthLast())));
      }
      // ส่งงวดที่
      if(list.getInstallmentLoan() >= 0){
        list.setInstallmentLoanText(NumberFormatUtils.formattedNumber2(String.valueOf(list.getInstallmentLoan())));
      }
      // ดอกคงค้าง, outStandInterest
      if(list.getOutStandInterest() >= 0){
        list.setOutStandInterestText(NumberFormatUtils.formattedNumber2(String.valueOf(list.getOutStandInterest())));
      }
      // ต้นคงค้าง, outStandPrinciple
      if(list.getOutStandPrinciple() >= 0){
        list.setOutStandPrincipleText(NumberFormatUtils.formattedNumber2(String.valueOf(list.getOutStandPrinciple())));
      }

    }

    byte[] pdf =  memberPdfService.generate(InfoAll, monthThai, String.valueOf(yearThai));
    // EXp --> name = มิย69-รายงานหน้า1หน้า3.pdf
    String fileName = DateUtils.getThaiMonthShort(monthThai) + yearThai + "-รายงานหน้า1หน้า3.pdf";
    log.info(" fileName : {}", fileName);
    // save file to server
    Map<String, String> respFile = DocumentsServiceAddFile.addFileForSeverPath(pdf, fileName, monthThai, String.valueOf(yearThai));
    log.info(" Resp Message : {}", respFile.get("message"));
    log.info(" Resp Path : {}", respFile.get("path"));

  }

  @Bean
  public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(2);
    scheduler.setThreadNamePrefix("GRAND-TOTAL");
    return scheduler;
  }
}
