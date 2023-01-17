package com.cm.welfarecmcity.api.loan;

import com.cm.welfarecmcity.api.stock.StockRepository;
import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.StockDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ResponseDataUtils responseDataUtils;

    public ResponseModel<ResponseId> add(LoanDto dto) {
        val loan = loanRepository.save(dto);
        return responseDataUtils.insertDataSuccess(loan.getId());
    }
}
