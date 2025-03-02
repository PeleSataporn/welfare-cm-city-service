package com.cm.welfarecmcity.logic.stock;

import com.cm.welfarecmcity.dto.StockDetailDto;
import com.cm.welfarecmcity.dto.base.PageReq;
import com.cm.welfarecmcity.logic.stock.model.AddStockDetailAllReq;
import com.cm.welfarecmcity.logic.stock.model.StockDetailRes;
import com.cm.welfarecmcity.logic.stock.model.StockRes;
import com.cm.welfarecmcity.logic.stock.model.search.StockByAdminOrderReqDto;
import com.cm.welfarecmcity.logic.stock.model.search.StockByAdminReqDto;
import com.cm.welfarecmcity.utils.PaginationUtils;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StockLogicRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySql() {
    val sql = new StringBuilder();
    sql.append(
        " SELECT stock.id, stock_value, stock_accumulate, employee_code, first_name, last_name, employee_status, prefix, id_card, employee.id as employeeId FROM stock JOIN employee ON (employee.stock_id = stock.id AND employee.deleted = FALSE) WHERE stock.deleted = FALSE ");
    return sql;
  }

  public List<StockRes> searchStock() {
    val sql = buildQuerySql();
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(StockRes.class));
  }

  private StringBuilder searchStockByAdminQuerySql(
      boolean isCount, StockByAdminReqDto criteria, StockByAdminOrderReqDto order, PageReq page) {
    val sql = new StringBuilder();

    // select cause
    if (isCount) {
      sql.append(" SELECT COUNT(*) ");
    } else {
      sql.append(" SELECT s.id, s.stock_value, s.stock_accumulate, ");
      sql.append(
          " e.employee_code, e.first_name, e.last_name, e.employee_status, e.prefix, e.id_card, e.id as employeeId ");
    }

    // from cause
    sql.append(" FROM stock s ");
    sql.append(" JOIN employee e ON (e.stock_id = s.id AND e.deleted = FALSE) ");

    if (criteria != null) {
      sql.append(this.buildCriteriaSQL(criteria));
    }

    // order by cause
    if (!isCount && order != null) {
      sql.append(this.buildOrderSQL(order));
    }

    // pagination
    if (!isCount && page != null) {
      sql.append(PaginationUtils.pagination(page));
    }

    return sql;
  }

  public String buildCriteriaSQL(StockByAdminReqDto criteria) {
    val statements = new StringJoiner(" AND ");

    statements.add(" s.deleted = FALSE ");

    if (criteria.employeeCode() != null) {
      statements.add(" e.employee_code LIKE '%" + criteria.employeeCode() + "%' ");
    }

    if (criteria.firstName() != null) {
      statements.add(" e.first_name LIKE '%" + criteria.firstName() + "%' ");
    }

    if (criteria.lastName() != null) {
      statements.add(" e.last_name LIKE '%" + criteria.lastName() + "%' ");
    }

    if (criteria.idCard() != null) {
      statements.add(" e.id_card LIKE '%" + criteria.idCard() + "%' ");
    }

    return " WHERE " + statements;
  }

  private String buildOrderSQL(StockByAdminOrderReqDto order) {
    val statements = new StringJoiner(",");

    if (order.id() != null) {
      statements.add(" e.id " + order.id());
    }

    if (order.createDate() != null) {
      statements.add(" e.create_date " + order.createDate());
    }

    return " ORDER BY " + statements;
  }

  public List<StockRes> searchStockByAdmin(
      StockByAdminReqDto criteria, StockByAdminOrderReqDto order, PageReq page) {
    val sql = searchStockByAdminQuerySql(false, criteria, order, page);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(StockRes.class));
  }

  public Long count(StockByAdminReqDto criteria) {
    val sql = searchStockByAdminQuerySql(true, criteria, null, null);
    return jdbcTemplate.queryForObject(sql.toString(), Long.class);
  }

  public void addStockDetailAll(
      String month,
      String year,
      int installment,
      int stockValue,
      Long stockId,
      int stockAccumulate) {
    jdbcTemplate.update(
        "INSERT INTO `stock_detail`(`last_update`, `installment`,`stock_month`,`stock_value`,`stock_id`,`stock_year`, `stock_accumulate`) VALUES (?,?,?,?,?,?,?)",
        new Date(),
        installment,
        month,
        stockValue,
        stockId,
        year,
        stockAccumulate);
  }

  public StringBuilder getStockDetailByMonthSql(String oldMonth, String oldYear) {
    val sql = new StringBuilder();
    sql.append(
            " SELECT stock.id , stock_detail.stock_id, stock.stock_value, stock_detail.id as stockDetailId, stock_detail.installment, stock_detail.stock_month, "
                + " stock_detail.stock_year, stock_detail.stock_value as stockValueDetail, stock_detail.stock_accumulate "
                + " FROM stock JOIN stock_detail ON (stock_detail.stock_id = stock.id AND stock_detail.deleted = FALSE AND stock_detail.active = TRUE ) "
                + " JOIN employee ON (employee.stock_id = stock.id AND employee.deleted = FALSE AND employee.active = TRUE) "
                + " WHERE stock.deleted = FALSE AND stock.active = TRUE AND stock_detail.stock_month = '")
        .append(oldMonth)
        .append("' AND stock_detail.stock_year = '")
        .append(oldYear)
        .append("' AND stock_detail.deleted = FALSE AND stock_detail.active = TRUE ")
        .append(" AND employee.employee_status IN (2,5) AND employee.id != 0 ");
    return sql;
  }

  public List<StockDetailRes> getStockDetailByMonth(String oldMonth, String oldYear) {
    val sql = getStockDetailByMonthSql(oldMonth, oldYear);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(StockDetailRes.class));
  }

  public StringBuilder buildQueryDetailSql(AddStockDetailAllReq req) {
    val sql = new StringBuilder();
    sql.append(" SELECT * " + "FROM stock_detail " + "WHERE stock_detail.stock_month = '")
        .append(req.getNewMonth())
        .append("' AND stock_detail.stock_year = '")
        .append(req.getNewYear())
        .append("'");
    return sql;
  }

  public List<StockDetailDto> searchStockDetail(AddStockDetailAllReq req) {
    val sql = buildQueryDetailSql(req);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(StockDetailDto.class));
  }
}
