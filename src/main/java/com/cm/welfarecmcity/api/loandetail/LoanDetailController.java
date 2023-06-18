package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loan.LoanService;
import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.StockDetailDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/loan-detail")
public class LoanDetailController {

    @Autowired
    private LoanDetailService loanDetailService;

    @GetMapping("search-by-loan/{loanId}")
    public List<LoanDetailRes> getLoanDetail(@PathVariable Long loanId) {
        return loanDetailService.getLoanDetail(loanId);
    }
}