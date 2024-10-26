package com.cm.welfarecmcity.api.loandetailhistory;

import com.cm.welfarecmcity.dto.LoanDetailHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoanDetailHistoryRepository
    extends JpaRepository<LoanDetailHistory, Long>, JpaSpecificationExecutor<LoanDetailHistory> {}
