package com.cm.welfarecmcity.api.loandetailhistory;

import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import com.cm.welfarecmcity.logic.document.model.DocumentV1ResLoan;
import com.cm.welfarecmcity.logic.document.model.DocumentV2ResLoanV2;
import java.text.ParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/loan-detail-histories")
public class LoanDetailHistoryController {

  @Autowired private LoanDetailHistoryService loanDetailHistoryService;

  @PostMapping("v1/all")
  public List<DocumentV1ResLoan> searchV1LoanHistory(@RequestBody DocumentReq req)
      throws ParseException {
    return loanDetailHistoryService.searchV1LoanHistory(
        req.getMonthCurrent(), req.getYearCurrent());
  }

  @PostMapping("v2/all")
  public List<DocumentV2ResLoanV2> searchV2LoanHistory(@RequestBody DocumentReq req) {
    return loanDetailHistoryService.searchV2LoanHistory(
        req.getMonthCurrent(), req.getYearCurrent());
  }
}
