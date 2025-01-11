package com.cm.welfarecmcity.logic.document;

import com.cm.welfarecmcity.logic.document.model.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logic")
public class DocumentController {

  @Autowired private DocumentService service;

  // stock v1 เดือนปัจจุบัน
  @PostMapping("v1/document/search")
  public List<DocumentV1Res> searchDocumentV1(@RequestBody DocumentReq req) {
    return service.searchDocumentV1(req.getEmpId(), req.getMonthCurrent(), req.getYearCurrent());
  }

  // stock v2 เดือนปัจจุบัน
  @PostMapping("v2/document/search")
  public List<DocumentV2Res> searchDocumentV2(@RequestBody DocumentReq req) {
    return service.searchDocumentV2(req.getEmpId(), req.getMonthCurrent(), req.getYearCurrent());
  }

  // stock v1 เดือนเก่า
  @PostMapping("v1/document/old/search")
  public List<DocumentV1Res> searchDocumentOldV1(@RequestBody DocumentReq req) {
    return service.searchDocumentOldV1(req.getEmpId(), req.getMonthCurrent(), req.getYearCurrent());
  }

  // stock v2 เดือนเก่า
  @PostMapping("v2/document/old/search")
  public List<DocumentV2Res> searchDocumentOldV2(@RequestBody DocumentReq req) {
    return service.searchDocumentOldV2(req.getEmpId(), req.getMonthCurrent(), req.getYearCurrent());
  }

  @PostMapping("v1/document/grand-total")
  public GrandTotalRes grandTotal(@RequestBody DocumentReq req) {
    return service.grandTotal(req);
  }

  @PostMapping("v1/document/info-all")
  public List<DocumentInfoAllRes> documentInfoAll(@RequestBody DocumentReq req) {
    return service.documentInfoAll(req);
  }

  // loan แก้ service แล้ว
  @PostMapping("v1/document/searchLoanById")
  public List<DocumentInfoAllLoanEmpRes> searchDocumentV1LoanById(@RequestBody DocumentReq req)
      throws ParseException {
    return service.searchDocumentV1LoanById(req.getLoanId(), req.getEmpId());
  }

  @PostMapping("v1/document/searchLoan")
  public List<DocumentV1ResLoan> searchDocumentV1Loan(@RequestBody DocumentReq req) {
    return service.searchDocumentV1Loan(
        req.getLoanId(),
        req.getMonthCurrent(),
        req.getAdmin(),
        req.getEmpId(),
        req.getYearCurrent());
  }

  @PostMapping("v2/document/searchLoan")
  public List<DocumentV2ResLoan> searchDocumentV2Loan(@RequestBody DocumentReq req) {
    return service.searchDocumentV2Loan(
        req.getLoanId(), req.getMonthCurrent(), req.getYearCurrent());
  }

  // ใบเสร็จรับเงิน เดือนปัจจุบัน
  @PostMapping("v1/document/searchLoan-add-new")
  public EmployeeLoanNew searchEmployeeLoanNew(@RequestBody DocumentReq req) {
    return service.searchEmployeeLoanNew(req);
  }

  // ใบเสร็จรับเงิน เดือนเก่า
  @PostMapping("v1/document/searchLoan-old")
  public EmployeeLoanNew searchEmployeeLoanOld(@RequestBody DocumentReq req) {
    return service.searchEmployeeLoanOld(req);
  }

  @PostMapping("v1/document/searchLoan-guarantor-unique")
  public List<GuaranteeRes> searchGuarantorUnique(@RequestBody DocumentReq req) {
    return service.searchGuarantorUnique(req.getEmpCode());
  }

  @PostMapping("v1/document/search-emp-code-of-id")
  public DocumentReq searchEmpCodeOfId(@RequestBody DocumentReq req) {
    return service.searchEmpCodeOfId(req.getEmpCode());
  }

  @PostMapping("v1/document/search-id-of-emp-code")
  public DocumentReq searchIdOfEmpCode(@RequestBody DocumentReq req) {
    return service.searchIdOfEmpCode(req.getEmpId());
  }

  // calculate Loan
  @PostMapping("v1/document/calculate-loan-new")
  public List<CalculateInstallments> calculateLoanNew(@RequestBody CalculateReq req)
      throws ParseException {
    return service.calculateLoanNew(req);
  }

  @PostMapping("v1/document/calculate-loan-old")
  public List<CalculateInstallments> calculateLoanOld(@RequestBody CalculateReq req)
      throws ParseException {
    return service.calculateLoanOld(req);
  }

  // เงินปันผล
  @PostMapping("v1/document/calculate-dividend")
  public List<DocumentStockDevidend> calculateStockDividend(@RequestBody DocumentReq req) {
    return service.calculateStockDividend(req);
  }

  @PostMapping("v1/document/calculate-loanbalance")
  public String calculateLoanBalance(@RequestBody CalculateReq req) throws ParseException {
    return service.calculateLoanbalance(req);
  }

  // TODO: TEST UPDATE LOAN HISTORY MONTH 11
  @PutMapping("v1/document/test-update")
  public void update() throws ParseException {
    service.update();
  }

  @PostMapping(
      value = "v1/document/export-data/dividends",
      produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> exportDividends(@RequestBody DocumentReq req) throws IOException {
    val outputStream = service.exportDividends(req);

    if (outputStream == null || outputStream.size() == 0) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .contentLength(outputStream.size())
        .header(
            "Content-Disposition",
            "attachment;filename=export-dividends-" + System.nanoTime() + ".xlsx")
        .body(outputStream.toByteArray());
  }

  // Report PDF
  @PostMapping("v1/document/receipt-report")
  public ResponseEntity<InputStreamResource> receiptReport(@RequestBody ReportReq req)
          throws Exception {
    val pdfStream = service.receiptStock(req);

    val headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt_report.zip");

    return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfStream);
  }

  @PostMapping("v1/document/receipt-report-code")
  public ResponseEntity<InputStreamResource> receiptReportCode(@RequestBody ReportReq req)
          throws Exception {
    val pdfStream = service.receiptStockEmpCode(req);

    val headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt_report.pdf");

    return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfStream);
  }
}
