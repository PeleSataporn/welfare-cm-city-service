package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.dto.ForgetPasswordDto;
import com.cm.welfarecmcity.dto.UserDto;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySql(String username, String password) {
    val sql = new StringBuilder();

    sql
      .append(" SELECT employee.id AS id, username, password FROM employee JOIN user ON user.id = employee.user_id  WHERE username = '")
      .append(username)
      .append("' AND ")
      .append("password = '")
      .append(password)
      .append("'");
    //      .append("' AND employee.employee_status in (2,5) ");

    return sql;
  }

  public UserDto checkUserLogin(String username, String password) {
    val sql = buildQuerySql(username, password);
    try {
      return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(UserDto.class));
    } catch (EmptyResultDataAccessException e) {
      //      e.printStackTrace();
      return null;
    }
    //    return null;
    //    return jdbcTemplate.queryForObject(sql.toString(), UserDto.class);
  }

  public StringBuilder buildQuerySqlForgetPassword(String email, String idCard, String employeeCode) {
    val sql = new StringBuilder();

    sql
      .append(
        " SELECT e.id, e.id_card, c.email, e.user_id, e.employee_code FROM employee e inner join contact c on e.contact_id = c.id WHERE e.id_card = '"
      )
      .append(idCard)
      .append("' AND ")
      .append("c.email = '")
      .append(email)
      .append("' AND ")
      .append("e.employee_code = '")
      .append(employeeCode)
      .append("'");

    return sql;
  }

  public ForgetPasswordDto checkChangeForgetPassword(String email, String idCard, String employeeCode) {
    val sql = buildQuerySqlForgetPassword(email, idCard, employeeCode);
    try {
      return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(ForgetPasswordDto.class));
    } catch (EmptyResultDataAccessException e) {
      e.printStackTrace();
    }
    return null;
  }
}
