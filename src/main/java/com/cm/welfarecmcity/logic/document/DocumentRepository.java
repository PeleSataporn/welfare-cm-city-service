package com.cm.welfarecmcity.logic.document;

import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.logic.document.model.*;
import com.cm.welfarecmcity.utils.DateUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySqlV1stock(Long empId, String monthCurrent, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.id as empId, department.name AS departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, "
            + " stock_detail.installment AS stockInstallment, stock_detail.stock_value, stock_detail.stock_accumulate FROM employee "
            + " LEFT JOIN department ON employee.department_id = department.id LEFT JOIN stock ON employee.stock_id = stock.id "
            + " LEFT JOIN stock_detail ON stock_detail.stock_id = stock.id WHERE 1=1 ");

    if (empId != null) {
      //      sql.append(" AND employee.id = ").append(empId);
      sql.append(" AND employee.id = ").append(empId);
      //      sql.append(" ORDER BY stock_detail.installment ");
    }

    if (monthCurrent != null) {
      sql.append(" AND stock_detail.stock_month = '").append(monthCurrent).append("'");
    }

    if (yearCurrent != null) {
      sql.append(" AND stock_detail.stock_year = '").append(yearCurrent).append("'");
    }
    sql.append(" AND employee.employee_status IN (2,5) GROUP BY employee.employee_code ");

    return sql;
  }

  public List<DocumentV1Res> documentInfoV1stock(
      Long empId, String monthCurrent, String yearCurrent) {
    val sql = buildQuerySqlV1stock(empId, monthCurrent, yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentV1Res.class));
  }

  public StringBuilder buildQuerySqlV1loan(Long empId, String monthCurrent, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT loan_detail.installment AS loanInstallment, loan_detail.loan_ordinary, loan_detail.interest, loan.loan_time FROM employee "
            + " LEFT JOIN department ON employee.department_id = department.id LEFT JOIN loan ON employee.loan_id = loan.id "
            + " LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id WHERE 1=1");

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
    sql.append(" GROUP BY employee.employee_code ");
    return sql;
  }

  public List<DocumentLoanV1Res> documentInfoV1loan(
      Long empId, String monthCurrent, String yearCurrent) {
    val sql = buildQuerySqlV1loan(empId, monthCurrent, yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentLoanV1Res.class));
  }

  public StringBuilder buildQuerySqlV2(Long stockId) {
    val sql = new StringBuilder();
    // TODO: edit sql
    sql.append(
        " SELECT DISTINCT department.name as departmentName, SUM(stock_detail.stock_value) AS stockValueTotal, SUM(stock.stock_accumulate) AS stockAccumulateTotal, "
            + " CASE WHEN (loan_detail.loan_ordinary IS NOT NULL && loan_detail.interest IS NOT NULL) THEN SUM(stock_detail.stock_value + loan_detail.loan_ordinary + loan_detail.interest) "
            + " WHEN (loan_detail.loan_ordinary IS NOT NULL && loan_detail.interest IS NULL) THEN SUM(stock_detail.stock_value + loan_detail.loan_ordinary ) "
            + " WHEN (loan_detail.loan_ordinary IS NULL && loan_detail.interest IS NOT NULL) THEN SUM(stock_detail.stock_value + loan_detail.interest) "
            + " ELSE SUM(stock_detail.stock_value) END AS totalMonth, SUM(loan_detail.loan_ordinary) AS loanDetailOrdinaryTotal, SUM(loan_detail.interest) AS loanDetailInterestTotal "
            + " FROM department "
            + " JOIN employee ON employee.department_id = department.id JOIN stock ON employee.stock_id = stock.id JOIN stock_detail ON stock_detail.stock_id = stock.id "
            + " LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id ");

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
        " SELECT COUNT(DISTINCT employee.id) AS sumEmp, COUNT(DISTINCT loan.id) AS sumLoan, SUM(DISTINCT loan.loan_balance) AS sumLoanBalance, SUM(DISTINCT stock.stock_accumulate) AS sumStockAccumulate, "
            + " SUM(stock_detail.stock_value) AS sumStockValue, SUM(loan_detail.interest) AS sumLoanInterest, SUM(loan_detail.loan_ordinary) AS sumLoanOrdinary "
            + " FROM employee JOIN stock ON (employee.stock_id = stock.id AND stock.deleted = FALSE) JOIN stock_detail ON (stock_detail.stock_id = stock.id AND stock_detail.deleted = FALSE) "
            + " LEFT JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE) LEFT JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan_detail.deleted = FALSE) WHERE employee.deleted = FALSE ");
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
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(GrandTotalRes.class));
  }

  public StringBuilder sqlDocumentInfoAll(String monthCurrent, String yearCurrent) {
    val currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));

    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.id, employee.employee_code, CONCAT(employee.first_name,' ', employee.last_name) AS fullName, employee.create_date as regisDate, "
            + " department.name as departmentName, loan_detail.interest_last_month as interestLastMonth, loan.new_loan as newLoan, "
            + " employee_type.name as employeeTypeName, positions.name as positionsName, employee.salary, stock.id AS stockId, stock.stock_value, stock.stock_accumulate, loan.id as loanId, loan.loan_value, loan.loan_time, loan.interest_percent, "
            + " guarantor_one.employee_code AS codeGuarantorOne, CONCAT(guarantor_one.first_name,' ', guarantor_one.last_name) AS fullNameGuarantorOne, "
            + " guarantor_two.employee_code AS codeGuarantorTwo, CONCAT(guarantor_two.first_name,' ', guarantor_two.last_name) AS fullNameGuarantorTwo, loan.start_loan_date "
            + " FROM employee LEFT JOIN department ON (employee.department_id = department.id AND department.deleted = FALSE) "
            + " LEFT JOIN employee_type ON (employee.employee_type_id = employee_type.id AND employee_type.deleted = FALSE) "
            + " LEFT JOIN stock ON (employee.stock_id = stock.id AND stock.deleted = FALSE) "
            + " LEFT JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE) "
            + " LEFT JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan.deleted = FALSE) "
            + " LEFT JOIN positions ON (employee.position_id = positions.id AND positions.deleted = FALSE) "
            + " LEFT JOIN employee guarantor_one ON (loan.guarantor_one_id = guarantor_one.id AND guarantor_one.deleted = FALSE) "
            + " LEFT JOIN employee guarantor_two ON (loan.guarantor_two_id = guarantor_two.id AND guarantor_two.deleted = FALSE) WHERE ");

    if (!monthCurrent.equals(DateUtils.getThaiMonth(currentMonth))) {
      sql.append(" employee.employee_status IN (2,3,5) ");
    } else {
      sql.append(" employee.employee_status IN (2,5) ");
    }

    sql.append(
        " AND employee.deleted = FALSE AND employee.id != 0 "
            + " and loan_detail.loan_month is null and loan_detail.loan_year is null "
            + " UNION "
            + " SELECT employee.id, employee.employee_code, CONCAT(employee.first_name,' ', employee.last_name) AS fullName, employee.create_date as regisDate, "
            + " department.name as departmentName, loan_detail.interest_last_month as interestLastMonth, loan.new_loan as newLoan, "
            + " employee_type.name as employeeTypeName, positions.name as positionsName, employee.salary, stock.id AS stockId, stock.stock_value, stock.stock_accumulate, loan.id as loanId, loan.loan_value, loan.loan_time, loan.interest_percent, "
            + " guarantor_one.employee_code AS codeGuarantorOne, CONCAT(guarantor_one.first_name,' ', guarantor_one.last_name) AS fullNameGuarantorOne, "
            + " guarantor_two.employee_code AS codeGuarantorTwo, CONCAT(guarantor_two.first_name,' ', guarantor_two.last_name) AS fullNameGuarantorTwo, loan.start_loan_date "
            + " FROM employee LEFT JOIN department ON (employee.department_id = department.id AND department.deleted = FALSE) "
            + " LEFT JOIN employee_type ON (employee.employee_type_id = employee_type.id AND employee_type.deleted = FALSE) "
            + " LEFT JOIN stock ON (employee.stock_id = stock.id AND stock.deleted = FALSE) "
            + " LEFT JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE) "
            + " LEFT JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan.deleted = FALSE) "
            + " LEFT JOIN positions ON (employee.position_id = positions.id AND positions.deleted = FALSE) "
            + " LEFT JOIN employee guarantor_one ON (loan.guarantor_one_id = guarantor_one.id AND guarantor_one.deleted = FALSE) "
            + " LEFT JOIN employee guarantor_two ON (loan.guarantor_two_id = guarantor_two.id AND guarantor_two.deleted = FALSE) "
            + " WHERE employee.deleted = FALSE AND employee.employee_status IN (2,5) AND employee.id != 0 ");

    if (monthCurrent != null && yearCurrent != null) {
      sql.append(" and loan_detail.loan_month = '")
          .append(monthCurrent)
          .append("' and loan_detail.loan_year = '")
          .append(yearCurrent)
          .append("'");
    }

    sql.append(" GROUP BY employee.id  ");
    return sql;
  }

  public List<DocumentInfoAllRes> documentInfoAll(String monthCurrent, String yearCurrent) {
    val sql = sqlDocumentInfoAll(monthCurrent, yearCurrent);
    return jdbcTemplate.query(
        sql.toString(), new BeanPropertyRowMapper<>(DocumentInfoAllRes.class));
  }

  public StringBuilder buildQuerySqlV1Loan(
      Long loanId, String getMonthCurrent, String testHistory, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT department.name as departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, loan_detail.installment, loan_detail.interest_last_month as interestLastMonth, "
            + " loan.new_loan as newLoan, loan.loan_value as loanValue, loan.loan_time as loanTime, loan_detail.interest_percent as interestPercent, loan.guarantor_one_id as guarantor1, loan.guarantor_two_id as guarantor2, "
            + " loan.start_loan_date "
            + " FROM department "
            + " JOIN employee ON (employee.department_id = department.id AND employee.deleted = FALSE) "
            + " JOIN stock ON (employee.stock_id = stock.id AND stock.deleted = FALSE) "
            + " JOIN stock_detail ON (stock_detail.stock_id = stock.id AND stock_detail.deleted = FALSE) "
            + " RIGHT JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE) "
            + " RIGHT JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan_detail.deleted = FALSE AND loan_detail.loan_ordinary != 0) WHERE 1=1 ");
    if (getMonthCurrent != null && yearCurrent != null) {
      sql.append(" AND loan_detail.loan_month = '").append(getMonthCurrent).append("'");
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
      sql.append(" AND employee.employee_status IN (2,5) AND employee.id != 0 ");
    }
    if (loanId != null) {
      if (testHistory != null) {
        sql.append(" AND loan_detail.loan_id in (").append(testHistory).append(")");
      } else {
        sql.append(" AND loan_detail.loan_id in (").append(loanId).append(")");
      }
      sql.append(
          " GROUP BY loan_detail.loan_id desc, loan_detail.loan_year desc, loan_detail.installment desc "); // installment
    } else {
      sql.append(" GROUP BY employee.id ");
    }

    return sql;
  }

  public List<DocumentV1ResLoan> documentInfoV1Loan(
      Long loanId, String getMonthCurrent, String testHistory, String yearCurrent) {
    val sql = buildQuerySqlV1Loan(loanId, getMonthCurrent, testHistory, yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentV1ResLoan.class));
  }

  public StringBuilder buildQuerySqlV2Loan(
      Long loanId, String getMonthCurrent, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT department.name as departmentName, SUM(loan.loan_value) AS loanValueTotal "
            + " FROM department "
            + " LEFT JOIN employee ON employee.department_id = department.id "
            + " LEFT JOIN loan ON employee.loan_id = loan.id  "
            + " LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id "
            + " WHERE employee.deleted = FALSE AND employee.employee_status IN (2,5) AND loan.active = TRUE  ");

    if (loanId != null) {
      sql.append(" AND loan.id = ").append(loanId);
      // sql.append(" AND loan_detail.loan_month = '").append(getMonthCurrent).append("'");
    } else {
      sql.append(" AND loan_detail.loan_month = '").append(getMonthCurrent).append("'");
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    }

    sql.append(" GROUP BY department.name ");

    return sql;
  }

  public List<DocumentV2ResLoan> documentInfoV2Loan(
      Long loanId, String getMonthCurrent, String yearCurrent) {
    val sql = buildQuerySqlV2Loan(loanId, getMonthCurrent, yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentV2ResLoan.class));
  }

  public StringBuilder guarantee(Long empId, Boolean count) {
    val sql = new StringBuilder();
    if (count) {
      sql.append(" SELECT COUNT(*) ");
    } else {
      sql.append(
          " SELECT employee.gender, employee.employee_code AS codeGuarantee, CONCAT(employee.prefix,employee.first_name,' ', employee.last_name) AS fullNameGuarantee ");
    }

    sql.append(
        " FROM employee LEFT JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE AND loan.active = TRUE) ");

    if (empId != null) {
      sql.append(" WHERE guarantor_one_id = ")
          .append(empId)
          .append(" || guarantor_two_id = ")
          .append(empId);
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
    //      " SELECT employee.id as empId, department.name as departmentName,
    // employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name)
    // AS fullName, " +
    //      " employee_type.name AS employeeTypeName, stock.stock_accumulate AS
    // stockAccumulate,loan.id as loanId, loan.active as loanActive, loan.loan_value AS loanValue,
    // loan.loan_balance AS loanBalance, " +
    //      " loan_detail.installment, loan.loan_time AS loanTime, loan.interest_percent AS
    // interestPercent, employee.salary, employee.employee_type_id AS employeeTypeId, " +
    //      " stock_detail.installment as stockDetailInstallment, loan.id as loanId, stock.id as
    // stockId, loan.start_loan_date as startDateLoan, loan.new_loan " +
    //      " FROM employee LEFT JOIN department ON employee.department_id = department.id LEFT JOIN
    // employee_type ON employee_type.id = employee.employee_type_id " +
    //      " LEFT JOIN stock ON employee.stock_id = stock.id LEFT JOIN stock_detail ON
    // stock_detail.stock_id = stock.id " +
    //      " LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON
    // loan_detail.loan_id = loan.id "
    sql.append(
        " SELECT employee.id as empId, department.name as departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, "
            + " employee_type.name AS employeeTypeName, stock_detail.stock_accumulate AS stockAccumulate,  stock_detail.stock_value, "
            + " employee.salary, employee.employee_type_id AS employeeTypeId, stock_detail.installment as stockDetailInstallment, stock.id as stockId ");
    if (req.getLoanId() != null) {
      sql.append(
          " ,loan.id as loanId, loan.active as loanActive, loan.loan_value AS loanValue, loan.loan_balance AS loanBalance, "
              + " loan.id as loanId, loan.start_loan_date as startDateLoan, loan.new_loan, "
              + " loan_detail.installment, loan.loan_time AS loanTime, loan.interest_percent AS interestPercent, loan_detail.interest as interestLoanLastMonth, loan_detail.loan_ordinary ");
    }
    sql.append(
        " FROM employee LEFT JOIN department ON employee.department_id = department.id LEFT JOIN employee_type ON employee_type.id = employee.employee_type_id "
            + " LEFT JOIN stock ON employee.stock_id = stock.id LEFT JOIN stock_detail ON stock_detail.stock_id = stock.id   ");
    if (req.getLoanId() != null) {
      sql.append(
          " LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id ");
    }
    sql.append(" WHERE employee.employee_code = '").append(req.getEmpCode()).append("'");
    if (req.getMonthCurrent() != null && req.getYearCurrent() != null) {
      if (req.getStockId() != null) {
        sql.append(" and stock_detail.stock_month = '")
            .append(req.getMonthCurrent())
            .append("' and stock_detail.stock_year = '")
            .append(req.getYearCurrent())
            .append("'");
      }
      if (req.getLoanId() != null) {
        sql.append(" and loan_detail.loan_month = '")
            .append(req.getMonthCurrent())
            .append("' and loan_detail.loan_year = '")
            .append(req.getYearCurrent())
            .append("'");
      }
    }
    sql.append(" GROUP BY employee.employee_code ");
    return sql;
  }

  public EmployeeLoanNew searchEmployeeLoanNew(DocumentReq req) {
    val sql = buildQuerySqlV1LoanNew(req);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(EmployeeLoanNew.class));
  }

  public StringBuilder buildQuerySqlV1LoanOfNull(DocumentReq req) {
    val sql = new StringBuilder();
    sql.append(" SELECT * " + " FROM loan_detail WHERE deleted = FALSE AND active = TRUE ");
    sql.append(" and loan_id = ").append(req.getLoanId());
    // if (req.getLoanId() != null) {
    sql.append(" and loan_month = '")
        .append(req.getMonthCurrent())
        .append("' and loan_year = '")
        .append(req.getYearCurrent())
        .append("'");
    // }
    // sql.append(" GROUP BY employee.employee_code ");
    return sql;
  }

  public List<LoanDetailDto> searchEmployeeLoanOfNull(DocumentReq req) {
    val sql = buildQuerySqlV1LoanOfNull(req);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailDto.class));
  }

  public StringBuilder buildQuerySqlV1LoanNewOfNull(DocumentReq req) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.id as empId, department.name as departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, "
            + " employee_type.name AS employeeTypeName, stock.stock_accumulate AS stockAccumulate, employee.salary, employee.employee_type_id AS employeeTypeId ");
    if (req.getMonthCurrent() != null && req.getYearCurrent() != null) {
      sql.append(
          " loan.id as loanId, loan.active as loanActive, loan.loan_value AS loanValue, loan.loan_balance AS loanBalance, "
              + " loan_detail.installment, loan.loan_time AS loanTime, loan.interest_percent AS interestPercent ");
    }
    sql.append(
        " FROM employee LEFT JOIN department ON employee.department_id = department.id LEFT JOIN employee_type ON employee_type.id = employee.employee_type_id "
            + " LEFT JOIN stock ON employee.stock_id = stock.id LEFT JOIN stock_detail ON stock_detail.stock_id = stock.id "
            + " LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id ");
    sql.append(" WHERE employee.employee_code = '").append(req.getEmpCode()).append("'");
    if (req.getMonthCurrent() != null && req.getYearCurrent() != null) {
      sql.append(" and stock_detail.stock_month = '")
          .append(req.getMonthCurrent())
          .append("' and stock_detail.stock_year = '")
          .append(req.getYearCurrent())
          .append("'");
      sql.append(" and loan_detail.loan_month = '")
          .append(req.getMonthCurrent())
          .append("' and loan_detail.loan_year = '")
          .append(req.getYearCurrent())
          .append("'");
    }
    sql.append(" GROUP BY employee.employee_code ");
    return sql;
  }

  public EmployeeLoanNew searchEmployeeLoanNewOfNull(DocumentReq req) {
    val sql = buildQuerySqlV1LoanNewOfNull(req);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(EmployeeLoanNew.class));
  }

  public StringBuilder buildQuerySqlV1GetEmpCodeOfId(String empCode) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.id AS empId, employee.employee_code AS empCode, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName FROM employee ");
    sql.append(" WHERE employee.employee_code = '").append(empCode).append("'");
    return sql;
  }

  public DocumentReq getEmpCodeOfId(String empCode) {
    val sql = buildQuerySqlV1GetEmpCodeOfId(empCode);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(DocumentReq.class));
  }

  public StringBuilder buildQuerySqlV1GetIdOfEmpCode(Long empId) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.id AS empId, employee.employee_code AS empCode, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName  FROM employee ");
    sql.append(" WHERE employee.id = ").append(empId);
    return sql;
  }

  public DocumentReq getIdOfEmpCode(Long empId) {
    val sql = buildQuerySqlV1GetIdOfEmpCode(empId);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(DocumentReq.class));
  }

  public StringBuilder buildQuerySqlStockDividend(
      String empCode, String getYearCurrent, String getYearOld) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.id as empId, department.name as departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, "
            + "employee.bank_account_receiving_number, stock.stock_accumulate "
            + "FROM employee LEFT JOIN department ON employee.department_id = department.id LEFT JOIN employee_type ON employee_type.id = employee.employee_type_id "
            + "LEFT JOIN stock ON employee.stock_id = stock.id LEFT JOIN stock_detail ON stock_detail.stock_id = stock.id "
            + "LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id "
            + "where employee.deleted = FALSE ");
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

  public List<DocumentStockDevidend> documentInfoStockDividend(
      String empCode, String getYearCurrent, String getYearOld) {
    val sql = buildQuerySqlStockDividend(empCode, getYearCurrent, getYearOld);
    return jdbcTemplate.query(
        sql.toString(), new BeanPropertyRowMapper<>(DocumentStockDevidend.class));
  }

  public StringBuilder buildQuerySqlInterestDividend(String empCode, String getYearCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.id as empId, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, "
            + "loan_detail.interest, loan_detail.loan_year, loan_detail.loan_month "
            + "FROM employee JOIN loan ON employee.loan_id = loan.id JOIN loan_detail ON loan_detail.loan_id = loan.id ");
    sql.append(" WHERE employee.employee_code = '").append(empCode).append("'");
    sql.append(" AND loan_detail.loan_year = '").append(getYearCurrent).append("'");
    return sql;
  }

  public List<DocumentStockDevidend> documentInfoInterestDividend(
      String empCode, String getYearCurrent) {
    val sql = buildQuerySqlInterestDividend(empCode, getYearCurrent);
    return jdbcTemplate.query(
        sql.toString(), new BeanPropertyRowMapper<>(DocumentStockDevidend.class));
  }

  public StringBuilder buildQuerySqlStockDividendV2(
      String empCode, String getYearCurrent, String getYearOld) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.id as empId, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, "
            + "stock_detail.stock_accumulate, stock_detail.stock_value, stock_detail.stock_year, stock_detail.stock_month "
            + "FROM employee LEFT JOIN stock ON employee.stock_id = stock.id LEFT JOIN stock_detail ON stock_detail.stock_id = stock.id  ");
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

  public List<DocumentStockDevidend> documentInfoStockDividendV1(
      String empCode, String getYearCurrent, String getYearOld) {
    val sql = buildQuerySqlStockDividendV2(empCode, getYearCurrent, getYearOld);
    return jdbcTemplate.query(
        sql.toString(), new BeanPropertyRowMapper<>(DocumentStockDevidend.class));
  }

  public StringBuilder buildQuerySqlDocumentInfoSumEmp() {
    val sql = new StringBuilder();
    sql.append(
        " SELECT COUNT(employee.id) AS sumEmp FROM employee WHERE employee.deleted = FALSE AND employee.employee_status IN (2,5) AND employee.id != 0 ");
    return sql;
  }

  public GrandTotalRes documentInfoSumEmp() {
    val sql = buildQuerySqlDocumentInfoSumEmp();
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(GrandTotalRes.class));
  }

  public StringBuilder buildQuerySqlDocumentInfoSumLoanEmp() {
    val sql = new StringBuilder();
    sql.append(
        " SELECT COUNT(loan.id) AS sumLoan FROM loan where loan.deleted = false AND loan.active = true ");
    return sql;
  }

  public GrandTotalRes documentInfoSumLoanEmp() {
    val sql = buildQuerySqlDocumentInfoSumLoanEmp();
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(GrandTotalRes.class));
  }

  public StringBuilder buildQuerySqlSumLoanBalance(String yearCurrent, String monthCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT SUM(loan_detail.loan_balance + (loan_detail.loan_ordinary - loan_detail.interest)) AS sumLoanBalance FROM loan "
            + " JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan_detail.deleted = FALSE) "
            + " WHERE loan.deleted = FALSE AND loan.active = TRUE AND loan_detail.active = TRUE ");
    if (yearCurrent != null) {
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    }
    if (monthCurrent != null) {
      sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
    }
    return sql;
  }

  public GrandTotalRes getSumLoanBalance(String yearCurrent, String monthCurrent) {
    val sql = buildQuerySqlSumLoanBalance(yearCurrent, monthCurrent);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(GrandTotalRes.class));
  }

  // list calulate loanBalance total //
  public StringBuilder buildQuerySqlSumLoanBalanceList(String yearCurrent, String monthCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT loan.id , loan_detail.loan_balance , loan_detail.loan_ordinary, loan_detail.interest, loan_detail.installment FROM loan "
            + " JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan_detail.deleted = FALSE) "
            + " WHERE loan.deleted = FALSE AND loan.active = TRUE AND loan_detail.active = TRUE ");
    if (yearCurrent != null) {
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    }
    if (monthCurrent != null) {
      sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
    }
    return sql;
  }

  public List<LoanDetailDto> getSumLoanBalanceList(String yearCurrent, String monthCurrent) {
    val sql = buildQuerySqlSumLoanBalanceList(yearCurrent, monthCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailDto.class));
  }

  // list calulate loanBalance total //

  public StringBuilder buildQuerySqlSumStockAccumulate(String yearCurrent, String monthCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT SUM(COALESCE(stock.stock_accumulate,0)) AS sumStockAccumulate "
            + " FROM employee "
            + " JOIN stock ON (stock.id = employee.stock_id AND stock.deleted = FALSE) "
            + " JOIN stock_detail ON (stock.id = stock_detail.stock_id AND stock_detail.deleted = FALSE) "
            + " WHERE employee.deleted = FALSE AND employee.employee_status IN (2,5) AND employee.id != 0 ");
    if (yearCurrent != null) {
      sql.append(" AND stock_detail.stock_year = '").append(yearCurrent).append("'");
    }
    if (monthCurrent != null) {
      sql.append(" AND stock_detail.stock_month = '").append(monthCurrent).append("'");
    }
    return sql;
  }

  public GrandTotalRes getSumStockAccumulate(String yearCurrent, String monthCurrent) {
    val sql = buildQuerySqlSumStockAccumulate(yearCurrent, monthCurrent);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(GrandTotalRes.class));
  }

  public StringBuilder buildQuerySqlSumStockValue(String yearCurrent, String monthCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT SUM(COALESCE(stock_detail.stock_value,0)) AS sumStockValue "
            + "FROM employee "
            + "JOIN stock ON (stock.id = employee.stock_id AND stock.deleted = FALSE) "
            + "JOIN stock_detail ON (stock.id = stock_detail.stock_id AND stock_detail.deleted = FALSE) "
            + "WHERE employee.deleted = FALSE AND employee.employee_status IN (2,5) AND employee.id != 0 ");
    if (yearCurrent != null) {
      sql.append(" AND stock_detail.stock_year = '").append(yearCurrent).append("'");
    }
    if (monthCurrent != null) {
      sql.append(" AND stock_detail.stock_month = '").append(monthCurrent).append("'");
    }
    return sql;
  }

  public GrandTotalRes getSumStockValue(String yearCurrent, String monthCurrent) {
    val sql = buildQuerySqlSumStockValue(yearCurrent, monthCurrent);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(GrandTotalRes.class));
  }

  public StringBuilder buildQuerySqlSumLoanInterest(String yearCurrent, String monthCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT SUM(COALESCE(loan_detail.interest,0)) AS sumLoanInterest "
            + " FROM loan "
            + " JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan_detail.deleted = FALSE) "
            + " WHERE loan.deleted = FALSE AND loan.active = TRUE AND loan_detail.active = TRUE  ");
    if (yearCurrent != null) {
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    }
    if (monthCurrent != null) {
      sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
    }
    return sql;
  }

  public GrandTotalRes getSumLoanInterest(String yearCurrent, String monthCurrent) {
    val sql = buildQuerySqlSumLoanInterest(yearCurrent, monthCurrent);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(GrandTotalRes.class));
  }

  public StringBuilder buildQuerySqlSumLoanOrdinary(String yearCurrent, String monthCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT SUM(loan_detail.loan_ordinary - loan_detail.interest) AS sumLoanOrdinary "
            + " FROM loan "
            + " JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan_detail.deleted = FALSE) "
            + " WHERE loan.deleted = FALSE AND loan.active = TRUE AND loan_detail.active = TRUE ");
    if (yearCurrent != null) {
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    }
    if (monthCurrent != null) {
      sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
    }
    return sql;
  }

  public GrandTotalRes getSumLoanOrdinary(String yearCurrent, String monthCurrent) {
    val sql = buildQuerySqlSumLoanOrdinary(yearCurrent, monthCurrent);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(GrandTotalRes.class));
  }

  // list calulate loanOrdinary total //
  public StringBuilder buildQuerySqlSumLoanOrdinaryList(String yearCurrent, String monthCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT loan.id , loan_detail.loan_ordinary, loan_detail.interest, loan_detail.loan_balance, loan_detail.installment "
            + " FROM loan "
            + " JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan_detail.deleted = FALSE) "
            + " WHERE loan.deleted = FALSE AND loan.active = TRUE AND loan_detail.active = TRUE ");
    if (yearCurrent != null) {
      sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
    }
    if (monthCurrent != null) {
      sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
    }
    return sql;
  }

  public List<LoanDetailDto> getSumLoanOrdinaryList(String yearCurrent, String monthCurrent) {
    val sql = buildQuerySqlSumLoanOrdinaryList(yearCurrent, monthCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailDto.class));
  }

  // list calulate loanOrdinary total //

  public StringBuilder buildQuerySqlV1GetEmpFull(String empCode) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT id as empId, active, create_date, deleted, last_update, approve_flag, approved_resignation_date, bank_account_receiving_number, "
            + " billing_start_date, birthday, civil_service_date, compensation, contract_start_date, date_of_death, description, employee_code as empCode, "
            + " employee_status, first_name, gender, id_card, last_name, marital, monthly_stock_money, password_flag, prefix, profile_flag, reason, "
            + " resignation_date, retirement_date, salary, salary_bank_account_number, affiliation_id, contact_id, employee_type_id, level_id, loan_id, "
            + " position_id, stock_id, user_id, department_id, check_stock_value_flag, profile_img_id, admin_flag FROM employee ");
    sql.append(" WHERE employee.employee_code = '").append(empCode).append("'");
    return sql;
  }

  public DocumentReq getEmpFullData(String empCode) {
    val sql = buildQuerySqlV1GetEmpFull(empCode);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(DocumentReq.class));
  }

  public StringBuilder buildQuerySqlV1OfLoanById(Long empId) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT loan_detail.installment, loan_detail.interest_last_month as interestLastMonth, loan.loan_balance, "
            + " loan.new_loan as newLoan, loan.loan_value as loanValue, loan.loan_time as loanTime, loan_detail.interest_percent as interestPercent, "
            + " loan.guarantor_one_id as guarantor1, loan.guarantor_two_id as guarantor2, loan.start_loan_date, loan_detail.loan_month, loan_detail.loan_year, "
            + " loan_detail.loan_ordinary, loan_detail.loan_balance, loan_detail.interest, loan.loan_no "
            + " FROM loan "
            + " JOIN loan_detail ON (loan_detail.loan_id = loan.id AND loan_detail.deleted = FALSE AND loan_detail.active = TRUE ) ");
    sql.append(" where loan.id = ")
        .append(empId)
        .append(" and loan.active = true ")
        .append(" order by loan_detail.id desc ");
    return sql;
  }

  public List<DocumentInfoAllLoanEmpRes> loanById(Long empId) {
    val sql = buildQuerySqlV1OfLoanById(empId);
    return jdbcTemplate.query(
        sql.toString(), new BeanPropertyRowMapper<>(DocumentInfoAllLoanEmpRes.class));
  }

  public StringBuilder buildQuerySqlV1OfEmpById(Long empId) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT department.name as departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName "
            + " FROM employee "
            + " JOIN department ON (employee.department_id = department.id) ");
    sql.append(" where employee.id = ").append(empId);
    sql.append(" and employee.deleted = FALSE AND employee.employee_status IN (2,5) ");
    return sql;
  }

  public DocumentV1ResLoan employeeById(Long empId) {
    val sql = buildQuerySqlV1OfEmpById(empId);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(DocumentV1ResLoan.class));
  }
}
