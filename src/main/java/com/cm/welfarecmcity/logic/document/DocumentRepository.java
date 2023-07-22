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

  public StringBuilder buildQuerySqlV1stock(Long empId, String monthCurrent, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT employee.id as empId, department.name AS departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, " +
      " stock_detail.installment AS stockInstallment, stock_detail.stock_value, stock_detail.stock_accumulate FROM employee " +
      " LEFT JOIN department ON employee.department_id = department.id LEFT JOIN stock ON employee.stock_id = stock.id " +
      " LEFT JOIN stock_detail ON stock_detail.stock_id = stock.id "
    );

    if (empId != null) {
      //      sql.append(" AND employee.id = ").append(empId);
      sql.append(" WHERE employee.id = ").append(empId);
      //      sql.append(" ORDER BY stock_detail.installment ");
    }

    if (monthCurrent != null) {
      sql.append(" AND stock_detail.stock_month = '").append(monthCurrent).append("'");
    }

    if (yearCurrent != null) {
      sql.append(" AND stock_detail.stock_year = '").append(yearCurrent).append("'");
    }
    sql.append(" GROUP BY stockInstallment ");

    return sql;
  }

  public List<DocumentV1Res> documentInfoV1stock(Long empId, String monthCurrent, String yearCurrent) {
    val sql = buildQuerySqlV1stock(empId, monthCurrent, yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentV1Res.class));
  }

  public StringBuilder buildQuerySqlV1loan(Long empId, String monthCurrent, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT loan_detail.installment AS loanInstallment, loan_detail.loan_ordinary, loan_detail.interest FROM employee " +
      " LEFT JOIN department ON employee.department_id = department.id LEFT JOIN loan ON employee.loan_id = loan.id " +
      " LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id WHERE 1=1"
    );

    if (empId != null && monthCurrent != null && yearCurrent != null) {
      sql.append(" AND employee.id = ").append(empId);
      sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    } else {
      if (empId != null) {
        sql.append(" AND employee.id = ").append(empId);
      }

      if (monthCurrent != null) {
        sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
      }

      if (yearCurrent != null) {
        sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
      }
    }
    sql.append(" GROUP BY loanInstallment ");
    return sql;
  }

  public List<DocumentLoanV1Res> documentInfoV1loan(Long empId, String monthCurrent, String yearCurrent) {
    val sql = buildQuerySqlV1loan(empId, monthCurrent, yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentLoanV1Res.class));
  }

  public StringBuilder buildQuerySqlV2(Long stockId) {
    val sql = new StringBuilder();
    // TODO: edit sql
    sql.append(
      " SELECT DISTINCT department.name as departmentName, SUM(stock_detail.stock_value) AS stockValueTotal, SUM(stock.stock_accumulate) AS stockAccumulateTotal, " +
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

  public StringBuilder buildQuerySqlGrandTotal(String yearCurrent, String monthCurrent) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT COUNT(DISTINCT employee.id) AS sumEmp, COUNT(DISTINCT loan.id) AS sumLoan, SUM(DISTINCT loan.loan_balance) AS sumLoanBalance, SUM(DISTINCT stock.stock_accumulate) AS sumStockAccumulate, " +
      " SUM(stock_detail.stock_value) AS sumStockValue, SUM(loan_detail.interest) AS sumLoanInterest, SUM(loan_detail.loan_ordinary) AS sumLoanOrdinary " +
      " FROM employee JOIN stock ON (employee.stock_id = stock.id AND stock.deleted = FALSE) JOIN stock_detail ON (stock_detail.stock_id = stock.id AND stock_detail.deleted = FALSE) " +
      " LEFT JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE) LEFT JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan_detail.deleted = FALSE) WHERE employee.deleted = FALSE "
    );
    if (yearCurrent != null) {
      sql.append(" AND stock_detail.stock_year = '").append(yearCurrent).append("'");
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    }
    if (monthCurrent != null) {
      sql.append(" AND stock_detail.stock_month = '").append(monthCurrent).append("'");
      sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
    }
    return sql;
  }

  public GrandTotalRes grandTotal(String yearCurrent, String monthCurrent) {
    val sql = buildQuerySqlGrandTotal(yearCurrent, monthCurrent);
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
      " FROM department " +
      " JOIN employee ON (employee.department_id = department.id AND employee.deleted = FALSE) " +
      " JOIN stock ON (employee.stock_id = stock.id AND stock.deleted = FALSE) " +
      " JOIN stock_detail ON (stock_detail.stock_id = stock.id AND stock_detail.deleted = FALSE) " +
      " JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE) " +
      " JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan_detail.deleted = FALSE) "
    );

    if (loanId != null) {
      sql.append(" WHERE loan.id = ").append(loanId);
      sql.append(" AND loan_detail.loan_month = '").append(getMonthCurrent).append("'");
    } else {
      sql.append(" WHERE loan_detail.loan_month = '").append(getMonthCurrent).append("'");
      sql.append(" GROUP BY employee.id ");
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
      " JOIN employee ON employee.department_id = department.id " +
      " JOIN stock ON employee.stock_id = stock.id JOIN stock_detail ON stock_detail.stock_id = stock.id " +
      " LEFT JOIN loan ON employee.loan_id = loan.id " +
      " LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id "
    );

    if (loanId != null) {
      sql.append(" WHERE loan.id = ").append(loanId);
      sql.append(" AND loan_detail.loan_month = '").append(getMonthCurrent).append("'");
    } else {
      sql.append(" WHERE loan_detail.loan_month = '").append(getMonthCurrent).append("'");
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
        " SELECT employee.gender, employee.employee_code AS codeGuarantee, CONCAT(employee.prefix,employee.first_name,' ', employee.last_name) AS fullNameGuarantee "
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

  public StringBuilder buildQuerySqlV1LoanNew(DocumentReq req) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT employee.id as empId, department.name as departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, " +
      " employee_type.name AS employeeTypeName, stock.stock_accumulate AS stockAccumulate,loan.active as loanActive, loan.loan_value AS loanValue, loan.loan_balance AS loanBalance, " +
      " loan_detail.installment, loan.loan_time AS loanTime, loan.interest_percent AS interestPercent, employee.salary, employee.employee_type_id AS employeeTypeId " +
      " FROM employee LEFT JOIN department ON employee.department_id = department.id LEFT JOIN employee_type ON employee_type.id = employee.employee_type_id " +
      " LEFT JOIN stock ON employee.stock_id = stock.id LEFT JOIN stock_detail ON stock_detail.stock_id = stock.id " +
      " LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id "
    );
    sql.append(" WHERE employee.employee_code = '").append(req.getEmpCode()).append("'");
    sql
      .append(" and stock_detail.stock_month = '")
      .append(req.getMonthCurrent())
      .append("' and stock_detail.stock_year = '")
      .append(req.getYearCurrent())
      .append("'");
    sql
      .append(" and loan_detail.loan_month = '")
      .append(req.getMonthCurrent())
      .append("' and loan_detail.loan_year = '")
      .append(req.getYearCurrent())
      .append("'");
    return sql;
  }

  public EmployeeLoanNew searchEmployeeLoanNew(DocumentReq req) {
    val sql = buildQuerySqlV1LoanNew(req);
    return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(EmployeeLoanNew.class));
  }

  public StringBuilder buildQuerySqlV1GetEmpCodeOfId(String empCode) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT employee.id AS empId, employee.employee_code AS empCode, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName  FROM employee "
    );
    sql.append(" WHERE employee.employee_code = '").append(empCode).append("'");
    return sql;
  }

  public DocumentReq getEmpCodeOfId(String empCode) {
    val sql = buildQuerySqlV1GetEmpCodeOfId(empCode);
    return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(DocumentReq.class));
  }

  public StringBuilder buildQuerySqlStockDividend(String empCode, String getYearCurrent, String getYearOld) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT employee.id as empId, department.name as departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, " +
      "employee.bank_account_receiving_number, stock.stock_accumulate " +
      "FROM employee LEFT JOIN department ON employee.department_id = department.id LEFT JOIN employee_type ON employee_type.id = employee.employee_type_id " +
      "LEFT JOIN stock ON employee.stock_id = stock.id LEFT JOIN stock_detail ON stock_detail.stock_id = stock.id " +
      "LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id " +
      "where employee.deleted = FALSE "
    );
    // , stock_detail.stock_value, stock_detail.stock_year, stock_detail.stock_month
    if (empCode != null) {
      sql.append(" AND employee.employee_code = '").append(empCode).append("'");
    }
    if (getYearCurrent != null) {
      sql.append(" AND stock_detail.stock_year = '").append(getYearCurrent).append("'");
    }
    //    if(getYearOld != null){
    //      sql.append(" AND stock_detail.stock_year = '").append(getYearOld).append("'");
    //      sql.append(" AND stock_detail.stock_month = '").append("ธันวาคม").append("'");
    //    }
    sql.append(" GROUP BY employee.id ORDER BY department.name ");

    return sql;
  }

  public List<DocumentStockDevidend> documentInfoStockDividend(String empCode, String getYearCurrent, String getYearOld) {
    val sql = buildQuerySqlStockDividend(empCode, getYearCurrent, getYearOld);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentStockDevidend.class));
  }

  public StringBuilder buildQuerySqlInterestDividend(String empCode, String getYearCurrent) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT employee.id as empId, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, " +
      "loan_detail.interest, loan_detail.loan_year, loan_detail.loan_month " +
      "FROM employee JOIN loan ON employee.loan_id = loan.id JOIN loan_detail ON loan_detail.loan_id = loan.id "
    );
    sql.append(" WHERE employee.employee_code = '").append(empCode).append("'");
    sql.append(" AND loan_detail.loan_year = '").append(getYearCurrent).append("'");
    return sql;
  }

  public List<DocumentStockDevidend> documentInfoInterestDividend(String empCode, String getYearCurrent) {
    val sql = buildQuerySqlInterestDividend(empCode, getYearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentStockDevidend.class));
  }

  public StringBuilder buildQuerySqlStockDividendV2(String empCode, String getYearCurrent, String getYearOld) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT employee.id as empId, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, " +
      "stock_detail.stock_accumulate, stock_detail.stock_value, stock_detail.stock_year, stock_detail.stock_month " +
      "FROM employee LEFT JOIN stock ON employee.stock_id = stock.id LEFT JOIN stock_detail ON stock_detail.stock_id = stock.id  "
    );
    sql.append(" WHERE employee.employee_code = '").append(empCode).append("'");
    if (getYearCurrent != null) {
      sql.append(" AND stock_detail.stock_year = '").append(getYearCurrent).append("'");
    }
    if (getYearOld != null) {
      sql.append(" AND stock_detail.stock_year = '").append(getYearOld).append("'");
      sql.append(" AND stock_detail.stock_month = '").append("ธันวาคม").append("'");
    }
    return sql;
  }

  public List<DocumentStockDevidend> documentInfoStockDividendV1(String empCode, String getYearCurrent, String getYearOld) {
    val sql = buildQuerySqlStockDividendV2(empCode, getYearCurrent, getYearOld);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentStockDevidend.class));
  }

  public StringBuilder buildQuerySqlDocumentInfoSumEmp() {
    val sql = new StringBuilder();
    sql.append(" SELECT COUNT(employee.id) AS sumEmp FROM employee where employee.deleted = false");
    return sql;
  }

  public GrandTotalRes documentInfoSumEmp() {
    val sql = buildQuerySqlDocumentInfoSumEmp();
    return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(GrandTotalRes.class));
  }

  public StringBuilder buildQuerySqlDocumentInfoSumLoanEmp() {
    val sql = new StringBuilder();
    sql.append(" SELECT COUNT(loan.id) AS sumLoan FROM loan where loan.deleted = false ");
    return sql;
  }

  public GrandTotalRes documentInfoSumLoanEmp() {
    val sql = buildQuerySqlDocumentInfoSumLoanEmp();
    return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(GrandTotalRes.class));
  }
}
