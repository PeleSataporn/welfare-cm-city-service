package com.cm.welfarecmcity.logic.register;

import com.cm.welfarecmcity.logic.register.model.res.CheckEmployeeCodeRes;
import com.cm.welfarecmcity.logic.register.model.res.SearchNewRegisterRes;
import java.util.List;
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

  public StringBuilder buildQuerySql(String idCard) {
    val sql = new StringBuilder();
    sql
      .append(" SELECT id, employee_code, id_card, employee_status FROM employee WHERE ")
      .append(" id_card = '")
      .append(idCard)
      .append("'");
    return sql;
  }

  public CheckEmployeeCodeRes checkEmployee(String idCard) {
    val sql = buildQuerySql(idCard);
    try {
      return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(CheckEmployeeCodeRes.class));
    } catch (EmptyResultDataAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public StringBuilder buildQuerySqlSearchNewRegister(Boolean count) {
    val sql = new StringBuilder();

    if (count) {
      sql.append(" SELECT COUNT(employee.id) ");
    } else {
      sql.append(
        " SELECT employee.id, employee.create_date, employee.first_name, employee.last_update, " +
        " employee.id_card, employee.agency, employee.position, contact.mobile AS tel, contact.email "
      );
    }

    sql.append(" FROM employee JOIN contact ON employee.contact_id = contact.id WHERE employee.approve_flag = FALSE ");
    return sql;
  }

  public List<SearchNewRegisterRes> searchNewRegister() {
    val sql = buildQuerySqlSearchNewRegister(null);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(SearchNewRegisterRes.class));
  }

  public Integer countNewRegister() {
    val sql = buildQuerySqlSearchNewRegister(false);
    return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
  }
}
