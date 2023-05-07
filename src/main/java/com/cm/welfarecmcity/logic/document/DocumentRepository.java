package com.cm.welfarecmcity.logic.document;

import com.cm.welfarecmcity.logic.document.model.DocumentV1Res;
import com.cm.welfarecmcity.logic.document.model.DocumentV2Res;
import com.cm.welfarecmcity.logic.document.model.GrandTotalRes;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySqlV1(Long stockId) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT department.name as departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, stock_detail.installment AS stockInstallment, " +
      " stock_detail.stock_value, loan_detail.installment AS loanInstallment, loan_detail.loan_ordinary, loan_detail.interest, " +
      " CASE WHEN (loan_detail.loan_ordinary IS NOT NULL && loan_detail.interest IS NOT NULL) THEN (stock_detail.stock_value + loan_detail.loan_ordinary + loan_detail.interest) " +
      " WHEN (loan_detail.loan_ordinary IS NOT NULL && loan_detail.interest IS NULL) THEN (stock_detail.stock_value + loan_detail.loan_ordinary ) " +
      " WHEN (loan_detail.loan_ordinary IS NULL && loan_detail.interest IS NOT NULL) THEN (stock_detail.stock_value + loan_detail.interest) " +
      " ELSE (stock_detail.stock_value) END AS sumMonth, stock.stock_accumulate FROM department " +
      " JOIN employee ON employee.department_id = department.id JOIN stock ON employee.stock_id = stock.id JOIN stock_detail ON stock_detail.stock_id = stock.id " +
      " LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id "
    );

    if (stockId != null) {
      sql.append(" WHERE stock.id = ").append(stockId);
    }

    return sql;
  }

  public List<DocumentV1Res> documentInfoV1(Long stockId) {
    val sql = buildQuerySqlV1(stockId);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentV1Res.class));
  }

  public StringBuilder buildQuerySqlV2(Long stockId) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT department.name as departmentName, SUM(stock_detail.stock_value) AS stockValueTotal, SUM(stock.stock_accumulate) AS stockAccumulateTotal, " +
      " CASE WHEN (loan_detail.loan_ordinary IS NOT NULL && loan_detail.interest IS NOT NULL) THEN SUM(stock_detail.stock_value + loan_detail.loan_ordinary + loan_detail.interest) " +
      " WHEN (loan_detail.loan_ordinary IS NOT NULL && loan_detail.interest IS NULL) THEN SUM(stock_detail.stock_value + loan_detail.loan_ordinary ) " +
      " WHEN (loan_detail.loan_ordinary IS NULL && loan_detail.interest IS NOT NULL) THEN SUM(stock_detail.stock_value + loan_detail.interest) " +
      " ELSE SUM(stock_detail.stock_value) END AS totalMonth, SUM(loan_detail.loan_ordinary) AS loanDetailOrdinaryTotal, SUM(loan_detail.interest) AS loanDetailInterestTotal " +
      " FROM department " +
      " JOIN employee ON employee.department_id = department.id JOIN stock ON employee.stock_id = stock.id JOIN stock_detail ON stock_detail.stock_id = stock.id " +
      " LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id "
    );

    if (stockId != null) {
      sql.append(" WHERE stock.id = ").append(stockId);
    }

    sql.append(" GROUP BY department.id ");

    return sql;
  }

  public List<DocumentV2Res> documentInfoV2(Long stockId) {
    val sql = buildQuerySqlV2(stockId);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentV2Res.class));
  }

  public StringBuilder buildQuerySqlGrandTotal() {
    val sql = new StringBuilder();
    sql.append(
      " SELECT COUNT(employee.id) AS sumEmp, COUNT(loan.id) AS sumLoan, SUM(loan.loan_balance) AS sumLoanBalance, SUM(stock.stock_accumulate) AS sumStockAccumulate, " +
      " SUM(stock_detail.stock_value) AS sumStockValue, SUM(loan_detail.interest) AS sumLoanInterest, SUM(loan_detail.loan_ordinary) AS sumLoanOrdinary " +
      " FROM employee JOIN stock ON employee.stock_id = stock.id JOIN stock_detail ON stock_detail.stock_id = stock.id " +
      " LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id "
    );
    return sql;
  }

  public GrandTotalRes grandTotal() {
    val sql = buildQuerySqlGrandTotal();
    return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(GrandTotalRes.class));
  }
}
