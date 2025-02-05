package com.cm.welfarecmcity.api.loandetailhistory;

import com.cm.welfarecmcity.dto.LoanDetailHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoanDetailHistoryRepository
    extends JpaRepository<LoanDetailHistory, Long>, JpaSpecificationExecutor<LoanDetailHistory> {

  List<LoanDetailHistory> findByEmployeeId(Long employeeId);

  List<LoanDetailHistory> findByLoanId(Long loanId);
}
