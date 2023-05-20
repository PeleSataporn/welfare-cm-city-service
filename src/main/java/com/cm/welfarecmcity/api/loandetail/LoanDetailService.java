package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.dto.LoanDetailDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanDetailService {

    @Autowired
    private LoanDetailRepository loanDetailRepository;

    @Transactional
    public List<LoanDetailDto> getLoanDetail(Long loanId) {
        return loanDetailRepository.findAllByLoan_Id(loanId);
    }
}
