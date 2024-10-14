package com.cm.welfarecmcity.api.notification;

import com.cm.welfarecmcity.api.notification.model.NotificationRes;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationLogicRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQueryNotificationByEmpId(Long empId) {
    val sql = new StringBuilder();
    sql.append(" select * from petition_notification WHERE employee_id = " + empId);
    return sql;
  }

  public List<NotificationRes> getNotificationByEmpId(Long empId) {
    val sql = buildQueryNotificationByEmpId(empId);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(NotificationRes.class));
  }
}
