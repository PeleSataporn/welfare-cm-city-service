package com.cm.welfarecmcity.api.news;

import com.cm.welfarecmcity.api.news.model.SearchNewsMainRes;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NewsLogicRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  public StringBuilder searchNewsMainSql() {
    val sql = new StringBuilder();
    sql.append(
        " SELECT id, name, description, create_date, cover_img_id FROM news WHERE deleted = 0 ORDER BY create_date DESC LIMIT 4 ");
    return sql;
  }

  public List<SearchNewsMainRes> searchNewsMain() {
    val sql = searchNewsMainSql();

    return jdbcTemplate.query(
        sql.toString(),
        (rs, rowNum) -> {
          SearchNewsMainRes news = new SearchNewsMainRes();
          news.setId(rs.getLong("id"));
          news.setName(rs.getString("name"));
          news.setDescription(rs.getString("description"));
          news.setCreateDate(rs.getDate("create_date"));
          news.setCoverImgId(rs.getLong("cover_img_id"));
          return news;
        });
  }
}
