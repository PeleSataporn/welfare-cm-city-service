package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanDetailService {

  @Autowired private LoanDetailLogicRepository loanDetailLogicRepository;

  @Transactional
  public List<LoanDetailRes> searchLoanDetail(DocumentReq req) {
    StringBuilder testHistory = new StringBuilder(String.valueOf(req.getLoanId()));
    val loanHistory = loanDetailLogicRepository.loanHistory(req.getEmpId());
    //    for (LoanHistoryDto loanHistoryDto : loanHistory) {
    //      testHistory.append(',').append(loanHistoryDto.getLoanId());
    //    }
    val listLoanDetail =
        loanDetailLogicRepository.loanDetailHistory(
            testHistory.toString(), req.getMonthCurrent(), req.getYearCurrent());
    for (LoanDetailRes list : listLoanDetail) {
      if (Integer.parseInt(list.getLoanYear()) >= 2567) {
        Integer sum = 0;
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
    return listLoanDetail;
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
