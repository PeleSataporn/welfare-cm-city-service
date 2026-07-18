package com.cm.welfarecmcity.Scheduled.MemberData;

import com.cm.welfarecmcity.logic.document.DocumentService;
import com.cm.welfarecmcity.logic.document.model.DocumentInfoAllRes;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import com.cm.welfarecmcity.utils.DateUtils;
import com.cm.welfarecmcity.utils.NumberFormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/document/pdf")
@RequiredArgsConstructor
public class MemberPdfController {

    @Autowired
    private MemberPdfService memberPdfService;
    @Autowired
    private DocumentService documentService;

    @PostMapping("/generatePdfMemberAll")
    public ResponseEntity<byte[]> generatePdfMemberAll() throws Exception {

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
        String fileName = DateUtils.getThaiMonthShort(monthThai) + yearThai + "-รายงานหน้า1หน้า3.pdf";
        System.out.println(" fileName : " + fileName);

        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(fileName, StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
