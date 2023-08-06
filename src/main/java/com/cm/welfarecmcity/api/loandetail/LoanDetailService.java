package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanHistoryDto;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import jakarta.transaction.Transactional;
import lombok.val;
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
        String testHistory = "";
        testHistory = String.valueOf(req.getLoanId());
        val loanHistory = loanDetailLogicRepository.loanHistory(req.getEmpId());
        for(int i=0; i < loanHistory.size(); i++){
                testHistory = testHistory + ',' + loanHistory.get(i).getLoanId();

        }
        val loanDetail = loanDetailLogicRepository.loanDetailHistory(testHistory, req.getMonthCurrent(), req.getYearCurrent());
        return loanDetail;
    }
}
