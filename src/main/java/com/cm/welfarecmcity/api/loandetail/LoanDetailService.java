package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loan.LoanRepository;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.StockDetailDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanDetailService {

    @Autowired
    private LoanDetailRepository loanDetailRepository;

//    @Autowired
//    private ResponseDataUtils responseDataUtils;
//    public ResponseModel<ResponseId> add(LoanDto dto) {
//        val loan = loanDetailRepository.save(dto);
//        return responseDataUtils.insertDataSuccess(loan.getId());
//    }

    public List<LoanDetailDto> getLoanDetail(Long loanId) {
        return loanDetailRepository.findAllByLoan_Id(loanId);
    }
}
