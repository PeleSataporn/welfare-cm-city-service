package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class LoanDetailService {

    @Autowired
    private LoanDetailLogicRepository loanDetailLogicRepository;

    @Transactional
    public List<LoanDetailRes> getLoanDetail(DocumentReq req) {
        return loanDetailLogicRepository.loanDetail(req.getLoanId(), req.getMonthCurrent(), req.getYearCurrent());
    }
}
