package com.cm.welfarecmcity.logic.loan;

import com.cm.welfarecmcity.logic.loan.model.LoanRes;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanLogicService {

  @Autowired
  private LoanLogicRepository repository;

  @Transactional
  public List<LoanRes> searchLoan() {
    return repository.searchLoan();
  }
}
