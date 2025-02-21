package com.cm.welfarecmcity.api.loandetail;

import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import com.cm.welfarecmcity.api.loandetail.model.LoanHistoryV2Res;
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

  @Autowired private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySqlV1(Long loanId, String monthCurrent, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT loan_detail.installment, loan_detail.interest, loan_detail.loan_month, loan_detail.loan_ordinary, loan_detail.interest_percent, loan_detail.loan_year,"
            + "loan.loan_balance, loan.loan_value "
            + "FROM loan_detail join loan on loan_detail.loan_id = loan.id "
            + "WHERE loan_detail.deleted = FALSE ");
    sql.append(" AND loan_detail.loan_id = ").append(loanId);
    if (monthCurrent != null) {
      sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
    }
    if (yearCurrent != null) {
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    }
    sql.append(" ORDER BY loan_detail.loan_year , loan_detail.installment desc "); // loan.loan_no

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

  public StringBuilder buildQuerySqlV1LoanDetailHistory(
      String loanId, String monthCurrent, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT loan_detail.installment, loan_detail.interest, loan_detail.loan_month, loan_detail.loan_ordinary, loan_detail.interest_percent, loan_detail.loan_year,"
            + "loan_detail.loan_balance, loan.loan_value, loan.loan_no "
            + "FROM loan_detail join loan on loan_detail.loan_id = loan.id "
            + "WHERE loan_detail.deleted = FALSE ");
    sql.append(" AND loan_detail.loan_id in (").append(loanId).append(")");
    if (monthCurrent != null) {
      sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
    }
    if (yearCurrent != null) {
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    }
    sql.append(
        " ORDER BY loan_detail.loan_id desc, loan_detail.loan_year desc, loan_detail.installment desc "); // loan.loan_no
    return sql;
  }

  public List<LoanDetailRes> loanDetailHistory(
      String loanId, String monthCurrent, String yearCurrent) {
    val sql = buildQuerySqlV1LoanDetailHistory(loanId, monthCurrent, yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailRes.class));
  }

  public StringBuilder buildQuerySqlV1LoanDetailList(DocumentReq req) {
    val sql = new StringBuilder();
    sql.append(" SELECT * FROM loan_detail ");
    sql.append(" WHERE loan_month = '")
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

  public List<LoanDetailRes> getLoanDetailMergeHistory(Long loanId) {
    val sql = buildQuerySqlV1LoanDetailMergeHistory(loanId);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailRes.class));
  }

  public StringBuilder buildQuerySqlV1LoanDetailMergeHistory(Long loanId) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT loan_detail_history.installment, loan_detail_history.interest, loan_detail_history.loan_month, loan_detail_history.loan_ordinary, "
            + " loan_detail_history.interest_percent, loan_detail_history.loan_year, loan_detail_history.loan_balance, loan.loan_value, loan.loan_no "
            + " FROM loan_detail_history JOIN loan ON loan_detail_history.loan_id = loan.id "
            + " WHERE loan_detail_history.deleted = FALSE ");
    sql.append(" AND loan_detail_history.loan_id = ").append(loanId).append(" UNION ALL ");
    sql.append(
        " SELECT loan_detail.installment, loan_detail.interest, loan_detail.loan_month, loan_detail.loan_ordinary, "
            + " loan_detail.interest_percent, loan_detail.loan_year, loan_detail.loan_balance, loan.loan_value, loan.loan_no "
            + " FROM loan_detail JOIN loan ON loan_detail.loan_id = loan.id "
            + " WHERE loan_detail.deleted = FALSE ");
    sql.append(" AND loan_detail.loan_id = ").append(loanId).append(" ");
    sql.append(" ORDER BY loan_year DESC, installment DESC; ");
    return sql;
  }

  public StringBuilder buildQuerySqlV1LoanDetailHistoryList(String empCode) {
    val sql = new StringBuilder();
    sql.append(" SELECT employee_code, loan_id, employee_id FROM loan_detail_history ");
    sql.append(" WHERE employee_code = '").append(empCode).append("'");
    return sql;
  }

  public List<LoanHistoryDto> LoanDetailHistoryList(String empCode) {
    val sql = buildQuerySqlV1LoanDetailHistoryList(empCode);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanHistoryDto.class));
  }

  public StringBuilder buildQuerySqlV2LoanDetailHistoryList(String empCode) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT e.employee_code, e.loan_id as lLoanId, ldh.loan_id as hLoanId, ldh.employee_id FROM employee e ");
    sql.append(
            " left join loan_detail_history ldh on ldh.employee_id = e.id left join loan l on e.loan_id = l.id WHERE e.employee_code = '")
        .append(empCode)
        .append("'");
    return sql;
  }

  public List<LoanHistoryV2Res> LoanDetailHistoryListV2(String empCode) {
    val sql = buildQuerySqlV2LoanDetailHistoryList(empCode);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanHistoryV2Res.class));
  }
}
