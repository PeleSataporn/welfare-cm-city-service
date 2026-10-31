package com.cm.welfarecmcity.Scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class BackUpTableRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void backupAllTables(String backupDate) {

        backupTable("employee", "employee_bk_" + backupDate);

        backupTable("loan", "loan_bk_" + backupDate);

        backupTable("loan_detail", "loan_detail_bk_" + backupDate);

        backupTable("loan_detail_history", "loan_detail_history_bk_" + backupDate);

        backupTable("stock", "stock_bk_" + backupDate);

        backupTable("stock_detail", "stock_detail_bk_" + backupDate);
    }

    private void backupTable(String sourceTable, String backupTable) {

        String sql = "CREATE TABLE " + backupTable
                + " AS SELECT * FROM " + sourceTable;

        jdbcTemplate.execute(sql);

        log.info("Backup completed: {}", backupTable);
    }
}
