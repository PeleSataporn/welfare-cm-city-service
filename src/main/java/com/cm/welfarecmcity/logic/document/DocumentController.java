package com.cm.welfarecmcity.logic.document;

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
    return service.searchDocumentV1(req.getStockId());
  }

  @PostMapping("v2/document/search")
  public List<DocumentV2Res> searchDocumentV2(@RequestBody DocumentReq req) {
    return service.searchDocumentV2(req.getStockId());
  }

  @PostMapping("v1/document/grand-total")
  public GrandTotalRes grandTotal() {
    return service.grandTotal();
  }

  @PostMapping("v1/document/info-all")
  public List<DocumentInfoAllRes> documentInfoAll() {
    return service.documentInfoAll();
  }

  // loan
  @PostMapping("v1/document/searchLoan")
  public List<DocumentV1ResLoan> searchDocumentV1Loan(@RequestBody DocumentReq req) {
    return service.searchDocumentV1Loan(req.getLoanId(), req.getMonthCurrent());
  }

  @PostMapping("v2/document/searchLoan")
  public List<DocumentV2ResLoan> searchDocumentV2Loan(@RequestBody DocumentReq req) {
    return service.searchDocumentV2Loan(req.getLoanId());
  }

  // calculate Loan
  @PostMapping("v1/document/calculate-loan")
  public List<CalculateInstallments> calculateLoan(@RequestBody CalculateReq req) throws ParseException {
    return service.calculateLoan(req);
  }

}
