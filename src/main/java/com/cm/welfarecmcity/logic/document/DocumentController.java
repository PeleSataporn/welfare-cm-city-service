package com.cm.welfarecmcity.logic.document;

import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.logic.document.model.*;
import java.text.ParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logic")
public class DocumentController {

  @Autowired
  private DocumentService service;

  // stock
  @PostMapping("v1/document/search")
  public List<DocumentV1Res> searchDocumentV1(@RequestBody DocumentReq req) {
    return service.searchDocumentV1(req.getEmpId(), req.getMonthCurrent(), req.getYearCurrent());
  }

  @PostMapping("v2/document/search")
  public List<DocumentV2Res> searchDocumentV2(@RequestBody DocumentReq req) {
    return service.searchDocumentV2(req.getEmpId(), req.getMonthCurrent(), req.getYearCurrent());
  }

  @PostMapping("v1/document/grand-total")
  public GrandTotalRes grandTotal(@RequestBody DocumentReq req) {
    return service.grandTotal(req);
  }

  @PostMapping("v1/document/info-all")
  public List<DocumentInfoAllRes> documentInfoAll(@RequestBody DocumentReq req) {
    return service.documentInfoAll(req);
  }

  // loan
  @PostMapping("v1/document/searchLoan")
  public List<DocumentV1ResLoan> searchDocumentV1Loan(@RequestBody DocumentReq req) {
    return service.searchDocumentV1Loan(req.getLoanId(), req.getMonthCurrent(), req.getAdmin(), req.getEmpId());
  }

  @PostMapping("v2/document/searchLoan")
  public List<DocumentV2ResLoan> searchDocumentV2Loan(@RequestBody DocumentReq req) {
    return service.searchDocumentV2Loan(req.getLoanId(), req.getMonthCurrent());
  }

  @PostMapping("v1/document/searchLoan-add-new")
  public EmployeeLoanNew searchEmployeeLoanNew(@RequestBody DocumentReq req) {
    return service.searchEmployeeLoanNew(req);
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
  public List<CalculateInstallments> calculateLoanNew(@RequestBody CalculateReq req) throws ParseException {
    return service.calculateLoanNew(req);
  }

  @PostMapping("v1/document/calculate-loan-old")
  public List<CalculateInstallments> calculateLoanOld(@RequestBody CalculateReq req) throws ParseException {
    return service.calculateLoanOld(req);
  }

  // เงินปันผล
  @PostMapping("v1/document/calculate-dividend")
  public List<DocumentStockDevidend> calculateStockDividend(@RequestBody DocumentReq req) throws ParseException {
    return service.calculateStockDividend(req);
  }

}
