package com.cm.welfarecmcity.api.loan;

import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.logic.document.model.EmployeeLoanNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping
    public ResponseModel<ResponseId> add(@RequestBody LoanDto dto) {
        return loanService.add(dto);
    }

    // insert loan new
    @PostMapping("add-loan-New")
    public ResponseModel<ResponseId> addLoanNew(@RequestBody EmployeeLoanNew req) {
        return loanService.addLoanNew(req);
    }

}