package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import com.cm.welfarecmcity.logic.employee.EmployeeLogicRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanDetailService {

  @Autowired private LoanDetailLogicRepository loanDetailLogicRepository;

  @Autowired private EmployeeLogicRepository employeeLogicRepository;

  //  @Transactional
  //  public List<LoanDetailRes> searchLoanDetail(DocumentReq req) {
  //    StringBuilder testHistory = new StringBuilder(String.valueOf(req.getLoanId()));
  //    val loanHistory = loanDetailLogicRepository.loanHistory(req.getEmpId());
  //    //    for (LoanHistoryDto loanHistoryDto : loanHistory) {
  //    //      testHistory.append(',').append(loanHistoryDto.getLoanId());
  //    //    }
  //    val listLoanDetail =
  //            loanDetailLogicRepository.loanDetailHistory(
  //                    testHistory.toString(), req.getMonthCurrent(), req.getYearCurrent());
  //    for (LoanDetailRes list : listLoanDetail) {
  //      if (Integer.parseInt(list.getLoanYear()) >= 2567) {
  //        Integer sum = 0;
  //        if (list.getLoanBalance() > 0 && list.getInstallment() > 0) {
  //          sum = (list.getLoanBalance() + Math.round((list.getLoanOrdinary() -
  // list.getInterest())));
  //        } else {
  //          if (list.getInstallment() < 0) {
  //            sum =
  //                    (list.getLoanBalance() + Math.round((list.getLoanOrdinary() -
  // list.getInterest())));
  //          } else {
  //            sum = list.getLoanOrdinary();
  //          }
  //        }
  //        list.setLoanBalance(sum);
  //      }
  //    }
  //    return listLoanDetail;
  //  }

  @Transactional
  public List<LoanDetailRes> searchLoanDetail(DocumentReq req) {
    val emp = employeeLogicRepository.getEmployeeOfMain(req.getEmpId());

    List<LoanDetailRes> loanDetailHistories = new ArrayList<>();
    if (req.getLoanId() != null) {
      loanDetailHistories = loanDetailLogicRepository.getLoanDetailMergeHistory(req.getLoanId());
    } else {
      val loanHistory = loanDetailLogicRepository.LoanDetailHistoryList(emp.getEmployeeCode());
      loanDetailHistories =
          loanDetailLogicRepository.getLoanDetailMergeHistory(
              (!loanHistory.isEmpty() ? loanHistory.get(0).getLoanId() : req.getLoanId()));
    }

    for (val list : loanDetailHistories) {
      if (Integer.parseInt(list.getLoanYear()) >= 2567) {
        int sum = 0;
        if (list.getLoanBalance() > 0 && list.getInstallment() > 0) {
          sum = (list.getLoanBalance() + Math.round((list.getLoanOrdinary() - list.getInterest())));
        } else {
          if (list.getInstallment() < 0) {
            sum =
                (list.getLoanBalance() + Math.round((list.getLoanOrdinary() - list.getInterest())));
          } else {
            sum = list.getLoanOrdinary();
          }
        }
        list.setLoanBalance(sum);
      }
    }

    Set<String> seen = new HashSet<>();
    List<LoanDetailRes> result = new ArrayList<>();
    for (LoanDetailRes item : loanDetailHistories) {
      String key = item.getLoanYear() + "-" + item.getLoanMonth();
      if (!seen.contains(key)) {
        seen.add(key);
        result.add(item);   // เก็บตามลำดับเดิม
      }
    }
    loanDetailHistories = result;
    return loanDetailHistories;
  }

  @Transactional
  public LoanDetailDto getLoanDetail(DocumentReq req) {
    val loanDetailList = loanDetailLogicRepository.loanDetailList(req);
    if (loanDetailList.isEmpty()) {
      return null;
    } else {
      return loanDetailList.get(loanDetailList.size() - 1);
    }
  }
}
