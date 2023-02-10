package com.cm.welfarecmcity.logic.register;

import com.cm.welfarecmcity.logic.register.model.CheckEmployeeCodeRes;
import com.cm.welfarecmcity.logic.register.model.RegisterReq;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RegisterRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySql(RegisterReq req) {
    val sql = new StringBuilder();
    sql
      .append(" SELECT id, employee_code, id_card FROM employee WHERE employee_code = '")
      .append(req.getEmployeeCode())
      .append("' AND id_card = '")
      .append(req.getIdCard())
      .append("'");
    return sql;
  }

  public CheckEmployeeCodeRes checkEmployeeCode(RegisterReq req) {
    val sql = buildQuerySql(req);
    try {
      return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(CheckEmployeeCodeRes.class));
    } catch (EmptyResultDataAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  // test
  public StringBuilder buildQuerySql2(RegisterReq req) {
    val sql = new StringBuilder();
    sql.append(" SELECT id, employee_code, id_card, employee_status FROM employee WHERE ")
            .append(" id_card = '")
            .append(req.getIdCard())
            .append("'");
    return sql;
  }

  public CheckEmployeeCodeRes checkEmployeeCode2(RegisterReq req) {
    val sql = buildQuerySql2(req);
    try {
      return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(CheckEmployeeCodeRes.class));
    } catch (EmptyResultDataAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

}
