package com.cm.welfarecmcity.logic.stock;

import com.cm.welfarecmcity.dto.StockDetailDto;
import com.cm.welfarecmcity.logic.stock.model.StockDetailRes;
import com.cm.welfarecmcity.logic.stock.model.StockRes;
import java.util.Date;
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
      " SELECT stock.id, stock_value, stock_accumulate, employee_code, first_name, last_name, employee_status FROM stock JOIN employee ON (employee.stock_id = stock.id AND employee.deleted = FALSE) WHERE stock.deleted = FALSE "
    );
    return sql;
  }

  public List<StockRes> searchStock() {
    val sql = buildQuerySql();
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(StockRes.class));
  }

  public void addStockDetailAll(String month, String year, int installment, int stockValue, Long stockId, int stockAccumulate) {
    jdbcTemplate.update(
      "INSERT INTO `stock_detail`(`last_update`, `installment`,`stock_month`,`stock_value`,`stock_id`,`stock_year`, `stock_accumulate`) VALUES (?,?,?,?,?,?,?)",
      new Date(),
      installment,
      month,
      stockValue,
      stockId,
      year,
      stockAccumulate
    );
  }

  public StringBuilder getStockDetailByMonthSql(String oldMonth, String oldYear) {
    val sql = new StringBuilder();
    sql
      .append(" SELECT * FROM stock_detail WHERE stock_month = '")
      .append(oldMonth)
      .append("' AND stock_year = '")
      .append(oldYear)
      .append("' AND deleted = FALSE ");
    return sql;
  }

  public List<StockDetailRes> getStockDetailByMonth(String oldMonth, String oldYear) {
    val sql = getStockDetailByMonthSql(oldMonth, oldYear);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(StockDetailRes.class));
  }
}
