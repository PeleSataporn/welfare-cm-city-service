package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.StockDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LoanDetailRepository extends JpaRepository<LoanDetailDto, Long>, JpaSpecificationExecutor<LoanDetailDto> {
//    List<LoanDetailDto> findAllByLoan_Id(Long loanId);
}
