package com.cm.welfarecmcity.api.loanHistory;

import com.cm.welfarecmcity.dto.LoanHistoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoanHistoryRepository
    extends JpaRepository<LoanHistoryDto, Long>, JpaSpecificationExecutor<LoanHistoryDto> {}
