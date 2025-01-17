package com.cm.welfarecmcity.api.loandetailhistory;

import com.cm.welfarecmcity.logic.document.model.DocumentV1ResLoan;
import com.cm.welfarecmcity.logic.document.model.DocumentV2ResLoan;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoanDetailHistoryLogicRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  public List<DocumentV1ResLoan> searchV1LoanHistory(String getMonthCurrent, String yearCurrent) {
    val sql = buildQuerySqlV1Loan(getMonthCurrent, yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentV1ResLoan.class));
  }

  public StringBuilder buildQuerySqlV1Loan(String getMonthCurrent, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.id, department.name as departmentName, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, "
            + " loan_detail_history.installment, loan_detail_history.interest_last_month as interestLastMonth, loan.new_loan as newLoan, loan.loan_value as loanValue, loan.active as loanActive, "
            + " loan.loan_time as loanTime, loan_detail_history.interest_percent as interestPercent, loan.guarantor_one_id as guarantor1, loan.guarantor_two_id as guarantor2, loan.start_loan_date "
            + " ,employee.resignation_date "
            + " FROM department "
            + " JOIN employee ON (employee.department_id = department.id AND employee.deleted = FALSE) "
            + " JOIN stock ON (employee.stock_id = stock.id AND stock.deleted = FALSE) "
            + " JOIN stock_detail ON (stock_detail.stock_id = stock.id AND stock_detail.deleted = FALSE) "
            + " JOIN loan_detail_history ON (loan_detail_history.employee_id = employee.id "
            + " AND loan_detail_history.deleted = FALSE "
            + " AND loan_detail_history.loan_ordinary != 0) "
            + " left JOIN loan ON "
            + " loan_detail_history.loan_id = loan.id "
            + " WHERE 1=1 ");
    if (getMonthCurrent != null && yearCurrent != null) {
      sql.append(" AND loan_detail_history.loan_month = '").append(getMonthCurrent).append("'");
      sql.append(" AND loan_detail_history.loan_year = '").append(yearCurrent).append("'");
      sql.append(" AND employee.id != 0 "); // employee.employee_status IN (2,5) AND
      sql.append(" AND ( employee.resignation_date IS NULL ")
              .append(" OR (" +  " (YEAR(employee.resignation_date) + 543 = CAST('").append(yearCurrent)
              .append(" ' AS INT)) ").append(" AND MONTH(employee.resignation_date) > ") // >=
              .append(" CASE '").append(getMonthCurrent).append("' ")
              .append(" WHEN 'มกราคม' THEN '1' ")
              .append(" WHEN 'กุมภาพันธ์' THEN '2' ")
              .append(" WHEN 'มีนาคม' THEN '3' ")
              .append(" WHEN 'เมษายน' THEN '4'")
              .append(" WHEN 'พฤษภาคม' THEN '5'")
              .append(" WHEN 'มิถุนายน' THEN '6'")
              .append(" WHEN 'กรกฎาคม' THEN '7'")
              .append(" WHEN 'สิงหาคม' THEN '8'")
              .append(" WHEN 'กันยายน' THEN '9'")
              .append(" WHEN 'ตุลาคม' THEN '10'")
              .append(" WHEN 'พฤศจิกายน' THEN '11'")
              .append(" WHEN 'ธันวาคม' THEN '12' ").append(" END )) ");
    }
    sql.append(" GROUP BY employee.id ");
    // sql.append(" order by department.id ");

    return sql;
  }

  public List<DocumentV2ResLoan> searchV2LoanHistory(String getMonthCurrent, String yearCurrent) {
    val sql = buildQuerySqlV2Loan(getMonthCurrent, yearCurrent);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DocumentV2ResLoan.class));
  }

  public StringBuilder buildQuerySqlV2Loan(String getMonthCurrent, String yearCurrent) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.employee_code, department.name as departmentName, loan.loan_value, loan.active as loanActive, "
            + " loan_detail_history.installment, loan.loan_time as loanTime FROM department "
            + " LEFT JOIN employee ON employee.department_id = department.id "
            + " LEFT JOIN loan_detail_history ON loan_detail_history.employee_id = employee.id"
            + " LEFT JOIN loan ON loan_detail_history.loan_id = loan.id  "
            + " WHERE employee.deleted = FALSE  "); // AND loan.active = TRUE

    sql.append(" AND loan_detail_history.loan_month = '").append(getMonthCurrent).append("'");
    sql.append(" AND loan_detail_history.loan_year = '").append(yearCurrent).append("'");
    //    sql.append(" GROUP BY department.name ");
    //    sql.append(" order by department.id ");

    return sql;
  }
}
