package com.cm.welfarecmcity.logic.loan;

import com.cm.welfarecmcity.logic.loan.model.LoanRes;
import com.cm.welfarecmcity.logic.stock.model.StockRes;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoanLogicRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public StringBuilder buildQuerySql() {
    val sql = new StringBuilder();
    sql.append(
      " SELECT loan.id, loan_value, loan_balance, employee_code, first_name, last_name FROM loan JOIN employee ON (employee.loan_id = loan.id AND employee.deleted = FALSE) WHERE loan.deleted = FALSE "
    );
    return sql;
  }

  public List<LoanRes> searchLoan() {
    val sql = buildQuerySql();

//    val ss = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanRes.class));

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(LoanRes.class));
  }
}
