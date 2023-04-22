package com.cm.welfarecmcity.logic.loan;

import com.cm.welfarecmcity.logic.loan.model.LoanRes;
import com.cm.welfarecmcity.logic.stock.StockLogicService;
import com.cm.welfarecmcity.logic.stock.model.StockRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logic/v1/loan")
public class LoanLogicController {

  @Autowired
  private LoanLogicService service;

  @PostMapping("search")
  public List<LoanRes> searchLoan() {
    return service.searchLoan();
  }
}
