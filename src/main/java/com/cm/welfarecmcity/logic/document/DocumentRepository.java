package com.cm.welfarecmcity.logic.document;

import com.cm.welfarecmcity.logic.document.model.*;
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

  public StringBuilder buildQuerySqlV1(Long stockId, String monthCurrent) {
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
      //      sql.append(" stock_detail.stock_month = '").append(monthCurrent).append("'");
      //    } else {
      //      sql.append(" WHERE stock_detail.stock_month = '").append(monthCurrent).append("'");
    }

    if (monthCurrent != null) {
      sql.append(" WHERE stock_detail.stock_month = '").append(monthCurrent).append("'");
    }

    return sql;
  }

  public List<DocumentV1Res> documentInfoV1(Long stockId, String monthCurrent) {
    val sql = buildQuerySqlV1(stockId, monthCurrent);
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

  public StringBuilder sqlDocumentInfoAll() {
    val sql = new StringBuilder();
    sql.append(
      " SELECT employee.id, employee.employee_code, CONCAT(employee.first_name,' ', employee.last_name) AS fullName, employee.create_date as regisDate, " +
      " department.name as departmentName, loan_detail.interest_last_month as interestLastMonth, loan.new_loan as newLoan, " +
      " employee_type.name as employeeTypeName, positions.name as positionsName, employee.salary, stock.id AS stockId, stock.stock_value, stock.stock_accumulate, loan.id as loanId, loan.loan_value, loan.loan_time, loan.interest_percent, " +
      " guarantor_one.employee_code AS codeGuarantorOne, CONCAT(guarantor_one.first_name,' ', guarantor_one.last_name) AS fullNameGuarantorOne, " +
      " guarantor_two.employee_code AS codeGuarantorTwo, CONCAT(guarantor_two.first_name,' ', guarantor_two.last_name) AS fullNameGuarantorTwo " +
      " FROM employee LEFT JOIN department ON (employee.department_id = department.id AND department.deleted = FALSE) " +
      " LEFT JOIN employee_type ON (employee.employee_type_id = employee_type.id AND employee_type.deleted = FALSE) " +
      " LEFT JOIN stock ON (employee.stock_id = stock.id AND stock.deleted = FALSE) " +
      " LEFT JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE) " +
      " LEFT JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan.deleted = FALSE) " +
      " LEFT JOIN positions ON (employee.position_id = positions.id AND positions.deleted = FALSE) " +
      " LEFT JOIN employee guarantor_one ON (loan.guarantor_one_id = guarantor_one.id AND guarantor_one.deleted = FALSE) " +
      " LEFT JOIN employee guarantor_two ON (loan.guarantor_two_id = guarantor_two.id AND guarantor_two.deleted = FALSE) " +
      " WHERE employee.deleted = FALSE "
    );
    return sql;
  }

  public List<DocumentInfoAllRes> documentInfoAll() {
    val sql = sqlDocumentInfoAll();
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentInfoAllRes.class));
  }

  public StringBuilder buildQuerySqlV1Loan(Long loanId, String getMonthCurrent) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT department.name as departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, loan_detail.installment, loan_detail.interest_last_month as interestLastMonth, " +
      " loan.new_loan as newLoan, loan.loan_value as loanValue, loan.loan_time as loanTime, loan_detail.interest_percent as interestPercent, loan.guarantor_one_id as guarantor1, loan.guarantor_two_id as guarantor2 " +
      " FROM department JOIN employee ON employee.department_id = department.id JOIN stock ON employee.stock_id = stock.id JOIN stock_detail ON stock_detail.stock_id = stock.id " +
      " LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id "
    );
    if (loanId != null) {
      sql.append(" WHERE loan.id = ").append(loanId);
      sql.append(" AND stock_detail.stock_month = '").append(getMonthCurrent).append("'");
    } else {
      sql.append(" WHERE stock_detail.stock_month = '").append(getMonthCurrent).append("'");
    }

    return sql;
  }

  public List<DocumentV1ResLoan> documentInfoV1Loan(Long loanId, String getMonthCurrent) {
    val sql = buildQuerySqlV1Loan(loanId, getMonthCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentV1ResLoan.class));
  }

  public StringBuilder buildQuerySqlV2Loan(Long loanId, String getMonthCurrent) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT department.name as departmentName, SUM(loan.loan_value) AS loanValueTotal " +
      " FROM department " +
      " JOIN employee ON employee.department_id = department.id JOIN stock ON employee.stock_id = stock.id JOIN stock_detail ON stock_detail.stock_id = stock.id " +
      " LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id "
    );

    if (loanId != null) {
      sql.append(" WHERE loan.id = ").append(loanId);
      sql.append(" AND stock_detail.stock_month = '").append(getMonthCurrent).append("'");
    } else {
      sql.append(" WHERE stock_detail.stock_month = '").append(getMonthCurrent).append("'");
    }

    sql.append(" GROUP BY department.id ");

    return sql;
  }

  public List<DocumentV2ResLoan> documentInfoV2Loan(Long loanId, String getMonthCurrent) {
    val sql = buildQuerySqlV2Loan(loanId, getMonthCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentV2ResLoan.class));
  }

  public StringBuilder guarantee(Long empId, Boolean count) {
    val sql = new StringBuilder();
    if (count) {
      sql.append(" SELECT COUNT(*) ");
    } else {
      sql.append(
        " SELECT employee.employee_code AS codeGuarantee, CONCAT(employee.first_name,' ', employee.last_name) AS fullNameGuarantee "
      );
    }

    sql.append(" FROM employee LEFT JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE) ");

    if (empId != null) {
      sql.append(" WHERE guarantor_one_id = ").append(empId).append(" || guarantor_two_id = ").append(empId);
    }

    return sql;
  }

  public List<GuaranteeRes> documentGuarantee(Long loanId) {
    val sql = guarantee(loanId, false);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(GuaranteeRes.class));
  }

  public Integer countGuarantee(Long loanId) {
    val sql = guarantee(loanId, true);
    return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
  }

  public StringBuilder buildQuerySqlV1LoanNew(Long empId) {
    val sql = new StringBuilder();
    sql.append(
            " SELECT department.name as departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, " +
                    "employee_type.name AS employeeTypeName, stock.stock_accumulate AS stockAccumulate, loan.loan_value AS loanValue, loan.loan_balance AS loanBalance, " +
                    "loan.interest_percent AS interestPercent, employee.salary, employee.employee_type_id AS employeeTypeId " +
                    "FROM employee JOIN department ON employee.department_id = department.id JOIN employee_type ON employee_type.id = employee.employee_type_id " +
                    "JOIN stock ON employee.stock_id = stock.id JOIN stock_detail ON stock_detail.stock_id = stock.id " +
                    "LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id "
    );
    sql.append(" WHERE employee.employee_code = ").append(empId);
    return sql;
  }

  public EmployeeLoanNew searchEmployeeLoanNew(Long empId) {
    val sql = buildQuerySqlV1LoanNew(empId);
    return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(EmployeeLoanNew.class));
  }

}
