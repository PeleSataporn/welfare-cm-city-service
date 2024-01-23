package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanHistoryDto;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
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
    sql.append(" ORDER BY loan_detail.loan_year , loan_detail.installment desc "); //loan.loan_no

    return sql;
  }

  public List<LoanDetailRes> loanDetail(Long loanId, String monthCurrent, String yearCurrent) {
    val sql = buildQuerySqlV1(loanId, monthCurrent, yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailRes.class));
  }

  public StringBuilder buildQuerySqlV1OfEmpId(Long empId) {
    val sql = new StringBuilder();
    sql.append(" select employee_id,loan_id,status from loan_history lh ");
    sql.append(" where lh.employee_id = ").append(empId);
    return sql;
  }

  public List<LoanHistoryDto> loanHistory(Long empId) {
    val sql = buildQuerySqlV1OfEmpId(empId);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanHistoryDto.class));
  }

  public StringBuilder buildQuerySqlV1LoanDetailHistory(String loanId, String monthCurrent, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT loan_detail.installment, loan_detail.interest, loan_detail.loan_month, loan_detail.loan_ordinary, loan_detail.interest_percent, loan_detail.loan_year," +
      "loan_detail.loan_balance, loan.loan_value, loan.loan_no " +
      "FROM loan_detail join loan on loan_detail.loan_id = loan.id " +
      "WHERE loan_detail.deleted = FALSE "
    );
    sql.append(" AND loan_detail.loan_id in (").append(loanId).append(")");
    if (monthCurrent != null) {
      sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
    }
    if (yearCurrent != null) {
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    }
    sql.append(" ORDER BY loan_detail.loan_id desc, loan_detail.loan_year desc, loan_detail.installment desc "); //loan.loan_no
    return sql;
  }

  public List<LoanDetailRes> loanDetailHistory(String loanId, String monthCurrent, String yearCurrent) {
    val sql = buildQuerySqlV1LoanDetailHistory(loanId, monthCurrent, yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailRes.class));
  }

  public StringBuilder buildQuerySqlV1LoanDetailList(DocumentReq req) {
    val sql = new StringBuilder();
    sql.append(" SELECT * FROM loan_detail ");
    sql
      .append(" WHERE loan_month = '")
      .append(req.getMonthCurrent())
      .append("' AND loan_year = '")
      .append(req.getYearCurrent())
      .append("'");
    sql.append(" order by id ");

    return sql;
  }

  public List<LoanDetailDto> loanDetailList(DocumentReq req) {
    val sql = buildQuerySqlV1LoanDetailList(req);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailDto.class));
  }

}
