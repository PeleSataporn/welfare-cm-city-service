package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.dto.LoanDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoanDetailRepository
    extends JpaRepository<LoanDetailDto, Long>, JpaSpecificationExecutor<LoanDetailDto> {
  //    List<LoanDetailDto> findAllByLoan_Id(Long loanId);
}
