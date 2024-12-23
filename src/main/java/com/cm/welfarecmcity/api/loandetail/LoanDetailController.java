package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/loan-detail")
public class LoanDetailController {

  @Autowired private LoanDetailService loanDetailService;

  @PostMapping("search-by-loan")
  public List<LoanDetailRes> searchLoanDetail(@RequestBody DocumentReq req) {
    return loanDetailService.searchLoanDetail(req);
  }

  @PostMapping("getLoanDetail")
  public LoanDetailDto getLoanDetail(@RequestBody DocumentReq req) {
    return loanDetailService.getLoanDetail(req);
  }
}
