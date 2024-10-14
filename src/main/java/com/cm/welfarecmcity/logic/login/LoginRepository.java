package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.dto.ForgetPasswordDto;
import com.cm.welfarecmcity.dto.UserDto;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySql(String username, String password) {
    val sql = new StringBuilder();

    sql.append(
            " SELECT employee.id AS id, username, password, employee.password_flag FROM employee JOIN user ON user.id = employee.user_id  WHERE username = '")
        .append(username)
        .append("' AND ")
        .append("password = '")
        .append(password)
        .append("' AND employee.employee_status not in (6,7) ");

    return sql;
  }

  public UserDto checkUserLogin(String username, String password) {
    val sql = buildQuerySql(username, password);
    try {
      return jdbcTemplate.queryForObject(
          sql.toString(), new BeanPropertyRowMapper<>(UserDto.class));
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  public StringBuilder buildQuerySqlForgetPassword(String tel, String idCard, String employeeCode) {
    val sql = new StringBuilder();

    sql.append(
            " SELECT e.id, e.id_card, c.tel, e.user_id, e.employee_code FROM employee e inner join contact c on e.contact_id = c.id WHERE e.id_card = '")
        .append(idCard)
        .append("' AND ")
        .append("c.tel = '")
        .append(tel)
        .append("' AND ")
        .append("e.employee_code = '")
        .append(employeeCode)
        .append("'");

    return sql;
  }

  public ForgetPasswordDto checkChangeForgetPassword(
      String tel, String idCard, String employeeCode) {
    val sql = buildQuerySqlForgetPassword(tel, idCard, employeeCode);
    try {
      return jdbcTemplate.queryForObject(
          sql.toString(), new BeanPropertyRowMapper<>(ForgetPasswordDto.class));
    } catch (EmptyResultDataAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public StringBuilder buildQuerySqlV1GetEmpCodeOfId(String empCode) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.id AS empId, employee.employee_code AS empCode, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName,"
            + " password_flag FROM employee ");
    sql.append(" WHERE employee.employee_code = '").append(empCode).append("'");
    return sql;
  }

  public DocumentReq getEmpCodeOfId(String empCode) {
    val sql = buildQuerySqlV1GetEmpCodeOfId(empCode);
    return jdbcTemplate.queryForObject(
        sql.toString(), new BeanPropertyRowMapper<>(DocumentReq.class));
  }
}
