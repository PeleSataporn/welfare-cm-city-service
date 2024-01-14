package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanHistoryDto;
import com.cm.welfarecmcity.dto.StockDetailDto;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class LoanDetailService {

  @Autowired
  private LoanDetailLogicRepository loanDetailLogicRepository;

  @Transactional
  public List<LoanDetailRes> searchLoanDetail(DocumentReq req) {
    StringBuilder testHistory = new StringBuilder(String.valueOf(req.getLoanId()));
    val loanHistory = loanDetailLogicRepository.loanHistory(req.getEmpId());
    for (LoanHistoryDto loanHistoryDto : loanHistory) {
      testHistory.append(',').append(loanHistoryDto.getLoanId());
    }
    return loanDetailLogicRepository.loanDetailHistory(testHistory.toString(), req.getMonthCurrent(), req.getYearCurrent());
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
