package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoanDetailLogicRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySqlV1(Long loanId) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT installment, interest, loan_month, loan_ordinary, interest_percent, loan_year FROM loan_detail WHERE deleted = FALSE "
    );

    sql.append(" AND loan_id = ").append(loanId);

    return sql;
  }

  public List<LoanDetailRes> loanDetail(Long loanId) {
    val sql = buildQuerySqlV1(loanId);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailRes.class));
  }
}
