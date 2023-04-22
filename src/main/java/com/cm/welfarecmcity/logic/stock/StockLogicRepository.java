package com.cm.welfarecmcity.logic.stock;

import com.cm.welfarecmcity.logic.stock.model.StockRes;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StockLogicRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySql() {
    val sql = new StringBuilder();
    sql.append(
      " SELECT stock.id, stock_value, stock_accumulate, employee_code, first_name, last_name FROM stock JOIN employee ON (employee.stock_id = stock.id AND employee.deleted = FALSE) WHERE stock.deleted = FALSE "
    );
    return sql;
  }

  public List<StockRes> searchStock() {
    val sql = buildQuerySql();

//    val ss = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(StockRes.class));

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(StockRes.class));
  }
}
