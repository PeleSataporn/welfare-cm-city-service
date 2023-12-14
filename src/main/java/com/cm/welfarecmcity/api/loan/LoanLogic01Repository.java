package com.cm.welfarecmcity.api.loan;

import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.MaxNumber;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import com.cm.welfarecmcity.logic.document.model.EmployeeLoanNew;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoanLogic01Repository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public StringBuilder buildQuerySqlGetMaxNumber() {
        val sql = new StringBuilder();
        sql.append(
                " select COUNT(loan_no) as numberMax from loan WHERE loan_no is not null "
        );
        return sql;
    }

    public MaxNumber getNumberMaxLoan() {
        val sql = buildQuerySqlGetMaxNumber();
        return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(MaxNumber.class));
    }

    public StringBuilder buildQueryGetLoanDetailByLoanId(Long loanId) {
        val sql = new StringBuilder();
        sql.append(
                " select loan_id from loan_detail WHERE loan_id = " + loanId
        );
        return sql;
    }

    public DocumentReq getLoanDetailByLoanId(Long loanId) {
        val sql = buildQueryGetLoanDetailByLoanId(loanId);
        return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(DocumentReq.class));
    }

}
