package com.cm.welfarecmcity.api.loanHistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/loan-history")
public class LoanHistoryController {

  @Autowired private LoanHistoryService loanHistoryService;
}
