package com.cm.welfarecmcity.api.loandetailhistory;

import com.cm.welfarecmcity.dto.LoanDetailHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface LoanDetailHistoryRepository
    extends JpaRepository<LoanDetailHistory, Long>, JpaSpecificationExecutor<LoanDetailHistory> {

  List<LoanDetailHistory> findByEmployeeId(Long employeeId);

  List<LoanDetailHistory> findByLoanId(Long loanId);

  @Query(
      value =
          "SELECT 1 FROM loan_detail_history WHERE loan_id = :loanId AND loan_month = :month AND loan_year = :year",
      nativeQuery = true)
  Integer existsLoanDetailHistoryByLoanIdLoanMonthLoanYear(Long loanId, String month, String year);
}
