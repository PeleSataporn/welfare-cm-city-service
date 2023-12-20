package com.cm.welfarecmcity.api.stockdetail;

import com.cm.welfarecmcity.dto.StockDetailDto;
import java.util.List;

import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StockDetailLoginRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySqlV1StockDetail(Long stockId, String value) {
    val sql = new StringBuilder();
    sql.append(" SELECT * FROM stock_detail ");

    sql.append(" WHERE stock_detail.stock_id = ").append(stockId).append(" AND stock_detail.stock_accumulate != 0 ");

    sql.append(" order by stock_detail.installment DESC, stock_detail.stock_year ");
    //sql.append(" order by id ").append(value);

    return sql;
  }

  public List<StockDetailDto> documentInfoV1StockDetail(Long stockId, String value) {
    val sql = buildQuerySqlV1StockDetail(stockId, value);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(StockDetailDto.class));
  }

  public StringBuilder buildQuerySqlV2StockDetail(StockDetailDto req) {
    val sql = new StringBuilder();
    sql.append(" SELECT * FROM stock_detail ");
    sql.append(" WHERE stock_month = '").append(req.getStockMonth()).append("' AND stock_year = '").append(req.getStockYear()).append("'");
    sql.append(" order by id ");
    //sql.append(" order by id ").append(value);

    return sql;
  }

  public List<StockDetailDto> documentInfoV2StockDetail(StockDetailDto req) {
    val sql = buildQuerySqlV2StockDetail(req);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(StockDetailDto.class));
  }

  public StringBuilder buildQuerySqlV3StockDetail(DocumentReq req) {
    val sql = new StringBuilder();
    sql.append(" SELECT * FROM stock_detail ");
    sql.append(" WHERE stock_id = ").append(req.getStockId());
    sql.append(" AND stock_month = '").append(req.getMonthCurrent()).append("' AND stock_year = '").append(req.getYearCurrent()).append("'");
    // sql.append(" order by id ");
    return sql;
  }

  public List<StockDetailDto> documentInfoV3StockDetail(DocumentReq req) {
    val sql = buildQuerySqlV3StockDetail(req);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(StockDetailDto.class));
  }

}
