package com.cm.welfarecmcity.api.admin;

import com.cm.welfarecmcity.api.admin.model.AdminConfigReq;
import com.cm.welfarecmcity.api.admin.model.AdminConfigRes;
import com.cm.welfarecmcity.logic.document.model.DocumentV1Res;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminConfigRepositoryLogic {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public StringBuilder buildQuerySqlConfigList() {
        val sql = new StringBuilder();
        sql.append(
                "  SELECT id as configId, description, name, value FROM admin_config "
        );
        return sql;
    }

    public List<AdminConfigRes> documentInfoConfigList() {
        val sql = buildQuerySqlConfigList();
        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(AdminConfigRes.class));
    }


}
