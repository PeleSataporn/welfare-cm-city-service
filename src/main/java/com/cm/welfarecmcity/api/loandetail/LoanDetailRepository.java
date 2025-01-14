package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.dto.LoanDetailDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface LoanDetailRepository
    extends JpaRepository<LoanDetailDto, Long>, JpaSpecificationExecutor<LoanDetailDto> {
  //    List<LoanDetailDto> findAllByLoan_Id(Long loanId);

  @Query(
      "SELECT ld FROM LoanDetailDto ld WHERE ld.loanMonth = :loanMonth AND ld.loanYear = :loanYear")
  List<LoanDetailDto> findByLoanMonthAndLoanYear(String loanMonth, String loanYear);
}
