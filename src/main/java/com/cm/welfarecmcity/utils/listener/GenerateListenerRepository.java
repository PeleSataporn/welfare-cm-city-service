package com.cm.welfarecmcity.utils.listener;

import com.cm.welfarecmcity.utils.listener.model.GenerateRes;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GenerateListenerRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public List<GenerateRes> getRunningNumber() {
    val sql = " SELECT id, employee_code FROM employee WHERE employee.deleted = FALSE AND employee.employee_code is not null ";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(GenerateRes.class));
  }
}
