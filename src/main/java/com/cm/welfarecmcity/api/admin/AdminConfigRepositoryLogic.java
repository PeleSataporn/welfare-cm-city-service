package com.cm.welfarecmcity.api.admin;

import com.cm.welfarecmcity.api.admin.model.AdminConfigReq;
import com.cm.welfarecmcity.api.admin.model.AdminConfigRes;
import com.cm.welfarecmcity.api.admin.model.employeeListRes;
import com.cm.welfarecmcity.api.loandetail.model.LoanDetailRes;
import com.cm.welfarecmcity.logic.document.model.DocumentV1Res;
import com.cm.welfarecmcity.logic.document.model.EmployeeLoanNew;
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

    public StringBuilder buildQueryDetailOfEmp(String monthCurrent, String yearCurrent) {
        val sql = new StringBuilder();
        sql.append(
                "Select employee.id as empId,loan.id as loanId, employee.employee_code, CONCAT(employee.prefix, employee.first_name,' ', employee.last_name) AS fullName, " +
                        "stock.stock_accumulate AS stockAccumulate, loan.loan_value AS loanValue, loan.loan_balance AS loanBalance,loan.interest, " +
                        "loan_detail.installment, loan.loan_time AS loanTime, loan.interest_percent AS interestPercent, loan.guarantor_one_id AS guarantorOne" +
                        " , loan.guarantor_two_id AS guarantorTwo " +
                        "FROM employee LEFT JOIN stock ON employee.stock_id = stock.id LEFT JOIN stock_detail ON stock_detail.stock_id = stock.id " +
                        "LEFT JOIN loan ON employee.loan_id = loan.id LEFT JOIN loan_detail ON loan_detail.loan_id = loan.id " +
                        "where loan.deleted = FALSE "
        );
        if (monthCurrent != null) {
            sql.append(" AND loan_detail.loan_month = '").append(monthCurrent).append("'");
        }
        if (yearCurrent != null) {
            sql.append(" AND loan_detail.loan_year = '").append(yearCurrent).append("'");
        }
        sql.append(" GROUP BY loan.id ");

        return sql;
    }

    public List<EmployeeLoanNew> getLanDetailOfEmp(String monthCurrent, String yearCurrent) {
        val sql = buildQueryDetailOfEmp(monthCurrent,yearCurrent);
        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(EmployeeLoanNew.class));
    }


    public StringBuilder buildQuerySqlConfigEmployeeList(Boolean adminFlag) {
        val sql = new StringBuilder();
        sql.append(
                "  SELECT employee.id AS empId, employee.employee_code, CONCAT(employee.first_name,' ', employee.last_name) AS fullName, " +
                        " department.name as departmentName, " +
                        " employee_type.name as employeeTypeName, positions.name as positionsName, employee.admin_flag " +
                        " FROM employee LEFT JOIN department ON (employee.department_id = department.id ) " +
                        " LEFT JOIN employee_type ON (employee.employee_type_id = employee_type.id ) " +
                        " LEFT JOIN positions ON (employee.position_id = positions.id) " +
                        " WHERE employee.deleted = FALSE "
        );
        if(adminFlag){
            sql.append(" AND employee.admin_flag = true ");
        }else{
            sql.append(" AND employee.admin_flag = false or employee.admin_flag is null ");
        }
        sql.append(" ORDER BY fullName ");
        return sql;
    }

    public List<employeeListRes> documentInfoConfigEmployeeList(Boolean adminFlag) {
        val sql = buildQuerySqlConfigEmployeeList(adminFlag);
        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(employeeListRes.class));
    }

}
