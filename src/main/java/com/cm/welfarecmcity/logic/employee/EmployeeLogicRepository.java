package com.cm.welfarecmcity.logic.employee;

import com.cm.welfarecmcity.logic.employee.model.EmployeeOfMainRes;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
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
      " SELECT employee.employee_code, employee.prefix,employee.first_name, employee.last_name, employee.gender, positions.name as positionName, stock.stock_accumulate, loan.loan_value, department.name as departmentName FROM employee " +
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
    return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(EmployeeOfMainRes.class));
  }
}
