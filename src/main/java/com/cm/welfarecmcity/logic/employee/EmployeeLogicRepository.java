package com.cm.welfarecmcity.logic.employee;

import com.cm.welfarecmcity.logic.employee.model.EmployeeOfMainRes;
import java.sql.Blob;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeLogicRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public StringBuilder employeeOfMainSql(Long empId) {
    val sql = new StringBuilder();
    sql.append(
      " SELECT employee.id, employee.employee_code, employee.prefix,employee.first_name, employee.last_name, employee.gender, employee.salary, positions.name as positionName, " +
      " stock.stock_accumulate, loan.loan_value, loan.loan_balance, department.name as departmentName, employee.profile_img_id, employee.admin_flag, employee.password_flag, " +
      " employee.billing_start_date, loan.loan_no FROM employee " +
      " LEFT JOIN stock ON (employee.stock_id = stock.id AND stock.deleted = FALSE) LEFT JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE) " +
      " LEFT JOIN positions ON (employee.position_id = positions.id AND positions.deleted = FALSE) LEFT JOIN department ON (employee.department_id = department.id AND department.deleted = FALSE) "
    );

    if (empId != null) {
      sql.append(" WHERE employee.id = ").append(empId);
    }

    return sql;
  }

  public EmployeeOfMainRes getEmployeeOfMain(Long empId) {
    val sql = employeeOfMainSql(empId);

    return jdbcTemplate.queryForObject(
      sql.toString(),
      (rs, rowNum) -> {
        EmployeeOfMainRes employee = new EmployeeOfMainRes();
        employee.setId(rs.getLong("id"));
        employee.setEmployeeCode(rs.getString("employee_code"));
        employee.setPrefix(rs.getString("prefix"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setGender(rs.getString("gender"));
        employee.setSalary(rs.getDouble("salary"));
        employee.setPositionName(rs.getString("positionName"));
        employee.setStockAccumulate(rs.getDouble("stock_accumulate"));
        employee.setLoanValue(rs.getDouble("loan_value"));
        employee.setLoanBalance(rs.getDouble("loan_balance"));
        employee.setDepartmentName(rs.getString("departmentName"));
        employee.setProfileImgId(rs.getLong("profile_img_id"));
        employee.setAdminFlag(rs.getBoolean("admin_flag"));
        employee.setPasswordFlag(rs.getBoolean("password_flag"));
        employee.setBillingStartDate(rs.getDate("billing_start_date"));
        employee.setLoanNo(rs.getString("loan_no"));

        return employee;
      }
    );
  }
}
