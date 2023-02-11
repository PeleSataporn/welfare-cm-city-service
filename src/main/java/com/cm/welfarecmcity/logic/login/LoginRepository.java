package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.dto.UserDto;
import com.cm.welfarecmcity.logic.register.model.CheckEmployeeCodeRes;
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
      .append(" SELECT id, username, password FROM user WHERE username = ")
      .append(username)
      .append(" AND ")
      .append("password = ")
      .append(password);
      //.append(" AND user.deleted = false");

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
}
