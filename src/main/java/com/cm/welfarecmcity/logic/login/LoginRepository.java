package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.dto.UserDto;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySql(String username, String password) {
    val sql = new StringBuilder();

    sql
      .append(" SELECT 1 FROM user WHERE username = ")
      .append(username)
      .append(" AND ")
      .append("password = ")
      .append(password)
      .append(" AND user.deleted = false");

    return sql;
  }

  public UserDto checkUserLogin(String username, String password) {
    val sql = buildQuerySql(username, password);
    return jdbcTemplate.queryForObject(sql.toString(), UserDto.class);
  }
}
