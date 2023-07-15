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

  public StringBuilder buildQuerySqlV1(Long loanId, String monthCurrent, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT loan_detail.installment, loan_detail.interest, loan_detail.loan_month, loan_detail.loan_ordinary, loan_detail.interest_percent, loan_detail.loan_year," +
              "loan.loan_balance, loan.loan_value " +
              "FROM loan_detail join loan on loan_detail.loan_id = loan.id " +
              "WHERE loan_detail.deleted = FALSE "
    );
    sql.append(" AND loan_detail.loan_id = ").append(loanId);
    if (monthCurrent != null) {
      sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
    }
    if (yearCurrent != null) {
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    }
    //sql.append(" GROUP BY loan_detail.installment ");

    return sql;
  }

  public List<LoanDetailRes> loanDetail(Long loanId, String monthCurrent, String yearCurrent) {
    val sql = buildQuerySqlV1(loanId,monthCurrent,yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailRes.class));
  }
}
