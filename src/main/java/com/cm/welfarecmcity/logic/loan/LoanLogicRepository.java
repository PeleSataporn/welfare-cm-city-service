package com.cm.welfarecmcity.logic.loan;

import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.base.PageReq;
import com.cm.welfarecmcity.logic.loan.model.*;
import com.cm.welfarecmcity.logic.loan.model.search.LoanByAdminOrderReqDto;
import com.cm.welfarecmcity.logic.loan.model.search.LoanByAdminReqDto;
import com.cm.welfarecmcity.utils.PaginationUtils;
import java.util.List;
import java.util.StringJoiner;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoanLogicRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySql(String currentMonth, String currentYear) {
    val sql = new StringBuilder();
    sql.append(
            " SELECT loan.id, loan_value, loan.loan_balance, loan_time, loan.loan_no, prefix, employee.employee_status, employee.id_card, employee.employee_code, first_name, last_name, loan.stock_flag, loan.start_loan_date, "
                + "loan.guarantor_one_id AS guarantorOne , loan.guarantor_two_id AS guarantorTwo, "
                + "loan_detail.loan_ordinary , loan_detail.interest as interestDetail, loan_detail.loan_balance as loanBalanceDetail, loan_detail.loan_year, loan_detail.loan_month, "
                + "loan.interest_percent, stock.stock_accumulate, loan_detail.installment "
                + "FROM loan JOIN employee ON (employee.loan_id = loan.id AND employee.deleted = FALSE ) "
                + "JOIN loan_detail ON loan_detail.loan_id = loan.id "
                + "JOIN stock ON employee.stock_id = stock.id "
                + "JOIN stock_detail ON stock_detail.stock_id = stock.id "
                + "WHERE loan.deleted = FALSE and loan.active = TRUE ")
        .append(" AND loan_detail.loan_month = '" + currentMonth)
        .append("' AND loan_detail.loan_year = '")
        .append(currentYear + "'")
        .append(" GROUP BY loan.id ");
    return sql;
  }

  public List<LoanRes> searchLoan(AddLoanDetailAllReq req) {
    val sql = buildQuerySql(req.getNewMonth(), req.getNewYear());
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanRes.class));
  }

  private StringBuilder searchLoanByAdminQuerySql(
      boolean isCount, LoanByAdminReqDto criteria, LoanByAdminOrderReqDto order, PageReq page) {
    val sql = new StringBuilder();

    // select cause
    if (isCount) {
      sql.append(" SELECT COUNT(*) ");
    } else {
      sql.append(
          " SELECT l.id, l.loan_value, l.loan_balance, l.loan_time, l.loan_no, e.prefix, e.employee_status, e.id_card, e.employee_code, ");
      sql.append(
          " e.first_name, e.last_name, l.stock_flag, l.start_loan_date, l.guarantor_one_id AS guarantorOne, l.guarantor_two_id AS guarantorTwo, ");
      sql.append(
          " ld.loan_ordinary, ld.interest as interestDetail, ld.loan_balance as loanBalanceDetail, ld.loan_year, ");
      sql.append(" ld.loan_month, l.interest_percent, s.stock_accumulate, ld.installment ");
    }

    // from cause
    sql.append(" FROM loan l ");
    sql.append(" JOIN employee e ON (e.loan_id = l.id AND e.deleted = FALSE ) ");
    sql.append(" JOIN loan_detail ld ON ld.loan_id = l.id ");

    if (!isCount) {
      sql.append(" JOIN stock s ON e.stock_id = s.id ");
      sql.append(" JOIN stock_detail sd ON sd.stock_id = s.id ");
    }

    if (criteria != null) {
      sql.append(this.buildCriteriaSQL(criteria));
    }

    if (!isCount) {
      sql.append(" GROUP BY l.id ");
    }

    // order by cause
    if (!isCount && order != null) {
      sql.append(this.buildOrderSQL(order));
    }

    // pagination
    if (!isCount && page != null) {
      sql.append(PaginationUtils.pagination(page));
    }

    return sql;
  }

  public String buildCriteriaSQL(LoanByAdminReqDto criteria) {
    val statements = new StringJoiner(" AND ");

    statements.add(" l.deleted = FALSE AND l.active = TRUE ");
    statements.add(
        " ld.loan_month = '"
            + criteria.newMonth()
            + "' AND ld.loan_year = '"
            + criteria.newYear()
            + "' ");

    if (criteria.employeeCode() != null) {
      statements.add(" e.employee_code = '" + criteria.employeeCode() + "' ");
    }

    if (criteria.firstName() != null) {
      statements.add(" e.first_name LIKE '%" + criteria.firstName() + "%' ");
    }

    if (criteria.lastName() != null) {
      statements.add(" e.last_name LIKE '%" + criteria.lastName() + "%' ");
    }

    if (criteria.idCard() != null) {
      statements.add(" e.id_card = '" + criteria.idCard() + "' ");
    }

    if (criteria.loanNo() != null) {
      statements.add(" l.loan_no = '" + criteria.loanNo() + "' ");
    }

    return " WHERE " + statements;
  }

  private String buildOrderSQL(LoanByAdminOrderReqDto order) {
    val statements = new StringJoiner(",");

    if (order.id() != null) {
      statements.add(" l.id " + order.id());
    }

    if (order.createDate() != null) {
      statements.add(" e.create_date " + order.createDate());
    }

    return " ORDER BY " + statements;
  }

  public List<LoanRes> searchLoanByAdmin(
      LoanByAdminReqDto criteria, LoanByAdminOrderReqDto order, PageReq page) {
    val sql = searchLoanByAdminQuerySql(false, criteria, order, page);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanRes.class));
  }

  public Long count(LoanByAdminReqDto criteria) {
    val sql = searchLoanByAdminQuerySql(true, criteria, null, null);
    return jdbcTemplate.queryForObject(sql.toString(), Long.class);
  }

  public StringBuilder sqlGuarantor(Long employeeId) {
    val sql = new StringBuilder();
    sql.append(
            " SELECT guarantor_one.gender AS genderOne, guarantor_one.employee_code AS codeGuarantorOne, CONCAT(guarantor_one.prefix,guarantor_one.first_name,' ', guarantor_one.last_name) AS fullNameGuarantorOne, "
                + " guarantor_two.gender AS genderTwo, guarantor_two.employee_code AS codeGuarantorTwo, CONCAT(guarantor_one.prefix,guarantor_two.first_name,' ', guarantor_two.last_name) AS fullNameGuarantorTwo "
                + " FROM employee LEFT JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE) "
                + " LEFT JOIN employee guarantor_one ON (loan.guarantor_one_id = guarantor_one.id AND guarantor_one.deleted = FALSE) "
                + " LEFT JOIN employee guarantor_two ON (loan.guarantor_two_id = guarantor_two.id AND guarantor_two.deleted = FALSE) "
                + " WHERE employee.deleted = FALSE AND employee.id = ")
        .append(employeeId);
    return sql;
  }

  public GuarantorRes guarantor(Long employeeId) {
    val sql = sqlGuarantor(employeeId);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(GuarantorRes.class));
  }

  public StringBuilder beneficiarySql(Long id) {
    val sql = new StringBuilder();
    sql.append(
            " SELECT prefix, first_name, last_name, gender, relationship FROM beneficiary WHERE beneficiary.deleted = FALSE AND active = TRUE AND employee_id = ")
        .append(id);
    return sql;
  }

  public List<BeneficiaryRes> beneficiary(Long id) {
    val sql = beneficiarySql(id);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(BeneficiaryRes.class));
  }

  public StringBuilder searchBeneficiarySql(Long id) {
    val sql = new StringBuilder();
    sql.append(
            " SELECT id, prefix, first_name, last_name, gender, relationship, active FROM beneficiary WHERE beneficiary.deleted = FALSE AND employee_id = ")
        .append(id)
        .append(" AND beneficiary.life_status = 'มีชีวิต'")
        .append(" order by prefix ");
    return sql;
  }

  public List<BeneficiaryRes> searchBeneficiary(Long id) {
    val sql = searchBeneficiarySql(id);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(BeneficiaryRes.class));
  }

  public void update(Long id, boolean active) {
    val sql = searchBeneficiarySql(id);
    jdbcTemplate.update("UPDATE beneficiary SET active = ? WHERE id = ?", active, id);
  }

  public StringBuilder getLoanDetailByMonthSql(String oldMonth, String oldYear) {
    val sql = new StringBuilder();
    sql.append(
            " SELECT loan_detail.installment, loan_detail.interest, loan_detail.loan_month, loan_detail.loan_ordinary, loan_detail.loan_id, "
                + " loan_detail.interest_percent, loan_detail.loan_year, loan_detail.interest_last_month, loan.loan_time, loan.loan_value, loan.new_loan, loan.start_loan_date ")
        .append(
            " FROM loan_detail LEFT JOIN loan ON (loan.id = loan_detail.loan_id AND loan.deleted = FALSE AND loan.active = TRUE ) WHERE loan_detail.loan_month = '")
        .append(oldMonth)
        .append("' AND loan_detail.loan_year = '")
        .append(oldYear)
        .append("' AND loan_detail.deleted = FALSE ")
        .append(" AND loan_detail.active = TRUE ");
    return sql;
  }

  public List<LoanDetailRes> getLoanDetailByMonth(String oldMonth, String oldYear) {
    val sql = getLoanDetailByMonthSql(oldMonth, oldYear);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailRes.class));
  }

  public StringBuilder buildQuerySqlLoanOfEmployee(Long loanId) {
    val sql = new StringBuilder();
    sql.append(" SELECT id, employee_code, loan_id FROM employee WHERE loan_id = ").append(loanId);
    // .append("' GROUP BY loan.id ");
    return sql;
  }

  public EmployeeDto searchLoanOfEmployee(Long loanId) {
    val sql = buildQuerySqlLoanOfEmployee(loanId);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(EmployeeDto.class));
  }

  public StringBuilder buildQuerySqlLoanOfLoanDetail(Long loanId) {
    val sql = new StringBuilder();
    sql.append(" SELECT id , loan_id FROM loan_detail WHERE loan_id = ").append(loanId);
    return sql;
  }

  public List<LoanDetailDto> searchLoanOfLoanDetail(Long loanId) {
    val sql = buildQuerySqlLoanOfLoanDetail(loanId);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanDetailDto.class));
  }
}
