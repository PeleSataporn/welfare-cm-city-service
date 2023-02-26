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
      .append(" SELECT id, username, password FROM user WHERE username = '")
      .append(username)
      .append("' AND ")
      .append("password = '")
      .append(password)
      .append("'");

    return sql;
  }

  public UserDto checkUserLogin(String username, String password) {
    val sql = buildQuerySql(username, password);
    try {
      return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(UserDto.class));
    }catch (EmptyResultDataAccessException e) {
      e.printStackTrace();
    }
    return null;
  }


  public StringBuilder buildQuerySqlForgetPassword(String email, String idCard) {
    val sql = new StringBuilder();

             sql.append(" SELECT e.id, e.id_card, c.email, e.user_id FROM employee e inner join contact c on e.contact_id = c.id WHERE e.id_card = '")
            .append(idCard)
            .append("' AND ")
            .append("c.email = '")
            .append(email)
            .append("'");

    return sql;
  }

  public ForgetPasswordDto checkChangeForgetPassword(String email, String idCard) {
    val sql = buildQuerySqlForgetPassword(email, idCard);
    try {
      return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(ForgetPasswordDto.class));
    }catch (EmptyResultDataAccessException e) {
      e.printStackTrace();
    }
    return null;
  }
}
