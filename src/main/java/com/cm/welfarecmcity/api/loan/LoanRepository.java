package com.cm.welfarecmcity.api.loan;

import com.cm.welfarecmcity.dto.LoanDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoanRepository extends JpaRepository<LoanDto, Long>, JpaSpecificationExecutor<LoanDto> {}
