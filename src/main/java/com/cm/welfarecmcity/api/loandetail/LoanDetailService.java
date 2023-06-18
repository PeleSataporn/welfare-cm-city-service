package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanDetailService {

    @Autowired
    private LoanDetailLogicRepository loanDetailLogicRepository;

    @Transactional
    public List<LoanDetailRes> getLoanDetail(Long loanId) {
        return loanDetailLogicRepository.loanDetail(loanId);
    }
}
