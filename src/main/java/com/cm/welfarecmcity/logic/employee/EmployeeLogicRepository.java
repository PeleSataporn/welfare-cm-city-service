package com.cm.welfarecmcity.logic.employee;

import com.cm.welfarecmcity.api.employee.model.EmpByAdminRes;
import com.cm.welfarecmcity.api.employee.model.search.EmployeeByAdminOrderReqDto;
import com.cm.welfarecmcity.api.employee.model.search.EmployeeByAdminReqDto;
import com.cm.welfarecmcity.dto.base.PageReq;
import com.cm.welfarecmcity.logic.employee.model.EmployeeOfMainRes;
import com.cm.welfarecmcity.utils.PaginationUtils;
import java.util.List;
import java.util.StringJoiner;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeLogicRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  public StringBuilder searchEmployeeQuerySql() {
    val sql = new StringBuilder();
    sql.append(
        " SELECT e.id , e.employee_code , e.prefix , e.first_name , e.last_name , e.id_card , e.gender , e.birthday , e.employee_status , l.name as levelName , "
            + " et.name as employeeTypeName , p.name as positionName , d.name as departmentName , a.name as affiliationName , b.name as bureauName, e.profile_img_id as imageId "
            + " FROM employee e "
            + " LEFT JOIN `level` l on l.id = e.employee_type_id "
            + " LEFT JOIN employee_type et on et.id = e.employee_type_id "
            + " LEFT JOIN positions p on p.id = e.position_id "
            + " LEFT JOIN department d on d.id = e.department_id "
            + " LEFT JOIN affiliation a on a.id = e.affiliation_id "
            + " LEFT JOIN bureau b on b.id  = a.bureau_id ");

    return sql;
  }

  public List<EmpByAdminRes> searchEmployee() {
    val sql = searchEmployeeQuerySql();

    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(EmpByAdminRes.class));
  }

  // Test Search
  //    public StringBuilder searchEmployeeByAdminQuerySql() {
  //        val sql = new StringBuilder();
  //        sql.append(
  //                " SELECT e.id , e.employee_code , e.prefix , e.first_name , e.last_name ,
  // e.id_card , e.gender , e.birthday , e.employee_status , l.name as levelName , " +
  //                        " et.name as employeeTypeName , p.name as positionName , d.name as
  // departmentName , a.name as affiliationName , b.name as bureauName, e.profile_img_id as imageId
  // " +
  //                        " FROM employee e " +
  //                        " LEFT JOIN `level` l on l.id = e.employee_type_id " +
  //                        " LEFT JOIN employee_type et on et.id = e.employee_type_id " +
  //                        " LEFT JOIN positions p on p.id = e.position_id " +
  //                        " LEFT JOIN department d on d.id = e.department_id " +
  //                        " LEFT JOIN affiliation a on a.id = e.affiliation_id " +
  //                        " LEFT JOIN bureau b on b.id  = a.bureau_id "
  //        );
  //
  //        return sql;
  //    }
  private StringBuilder searchEmployeeByAdminQuerySql(
      boolean isCount,
      EmployeeByAdminReqDto criteria,
      EmployeeByAdminOrderReqDto order,
      PageReq page) {
    val sql = new StringBuilder();

    // select cause
    if (isCount) {
      sql.append(" SELECT COUNT(*) ");
    } else {
      sql.append(
          " SELECT e.id , e.employee_code , e.prefix , e.first_name , e.last_name , e.id_card , e.gender , e.birthday , e.employee_status , l.name as levelName , ");
      sql.append(
          " et.name as employeeTypeName , p.name as positionName , d.name as departmentName , a.name as affiliationName , b.name as bureauName, e.profile_img_id as imageId ");
    }

    // from cause
    sql.append(" FROM employee e ");
    sql.append(" LEFT JOIN `level` l on l.id = e.employee_type_id ");
    sql.append(" LEFT JOIN employee_type et on et.id = e.employee_type_id ");
    sql.append(" LEFT JOIN positions p on p.id = e.position_id ");
    sql.append(" LEFT JOIN department d on d.id = e.department_id ");
    sql.append(" LEFT JOIN affiliation a on a.id = e.affiliation_id ");
    sql.append(" LEFT JOIN bureau b on b.id  = a.bureau_id ");

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

  public String buildCriteriaSQL(EmployeeByAdminReqDto criteria) {
    val statements = new StringJoiner(" AND ");

    statements.add(" e.active = true ");

    if (criteria.employeeCode() != null) {
      statements.add(" e.employee_code = '" + criteria.employeeCode() + "' ");
    }

    if (criteria.firstName() != null) {
      statements.add(" e.first_name LIKE '%" + criteria.firstName() + "%' ");
    }

    if (criteria.lastName() != null) {
      statements.add(" e.last_name LIKE '%" + criteria.lastName() + "%' ");
    }

    if (criteria.idCard() != null) {
      statements.add(" e.id_card = '" + criteria.idCard() + "' ");
    }

    return " WHERE " + statements;
  }

  private String buildOrderSQL(EmployeeByAdminOrderReqDto order) {
    val statements = new StringJoiner(",");

    if (order.id() != null) {
      statements.add(" e.id " + order.id());
    }

    if (order.createDate() != null) {
      statements.add(" e.create_date " + order.createDate());
    }

    return " ORDER BY " + statements;
  }

  public List<EmpByAdminRes> searchEmployeeByAdmin(
      EmployeeByAdminReqDto criteria, EmployeeByAdminOrderReqDto order, PageReq page) {
    val sql = searchEmployeeByAdminQuerySql(false, criteria, order, page);
    return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(EmpByAdminRes.class));
  }

  public Long count(EmployeeByAdminReqDto criteria) {
    val sql = searchEmployeeByAdminQuerySql(true, criteria, null, null);
    return jdbcTemplate.queryForObject(sql.toString(), Long.class);
  }

  public StringBuilder employeeOfMainSql(Long empId) {
    val sql = new StringBuilder();
    sql.append(
        " SELECT employee.id, employee.employee_code, employee.prefix,employee.first_name, employee.last_name, employee.gender, employee.salary, positions.name as positionName, "
            + " stock.stock_accumulate, loan.loan_value, loan.loan_balance, department.name as departmentName, employee.profile_img_id, employee.admin_flag, employee.password_flag, "
            + " employee.billing_start_date, loan.loan_no FROM employee "
            + " LEFT JOIN stock ON (employee.stock_id = stock.id AND stock.deleted = FALSE) LEFT JOIN loan ON (employee.loan_id = loan.id AND loan.deleted = FALSE) "
            + " LEFT JOIN positions ON (employee.position_id = positions.id AND positions.deleted = FALSE) LEFT JOIN department ON (employee.department_id = department.id AND department.deleted = FALSE) ");

    if (empId != null) {
      sql.append(" WHERE employee.id = ").append(empId);
    }

    return sql;
  }

  public EmployeeOfMainRes getEmployeeOfMain(Long empId) {
    val sql = employeeOfMainSql(empId);

    return jdbcTemplate.queryForObject(
        sql.toString(),
        (rs, rowNum) -> {
          EmployeeOfMainRes employee = new EmployeeOfMainRes();
          employee.setId(rs.getLong("id"));
          employee.setEmployeeCode(rs.getString("employee_code"));
          employee.setPrefix(rs.getString("prefix"));
          employee.setFirstName(rs.getString("first_name"));
          employee.setLastName(rs.getString("last_name"));
          employee.setGender(rs.getString("gender"));
          employee.setSalary(rs.getDouble("salary"));
          employee.setPositionName(rs.getString("positionName"));
          employee.setStockAccumulate(rs.getDouble("stock_accumulate"));
          employee.setLoanValue(rs.getDouble("loan_value"));
          employee.setLoanBalance(rs.getDouble("loan_balance"));
          employee.setDepartmentName(rs.getString("departmentName"));
          employee.setProfileImgId(rs.getLong("profile_img_id"));
          employee.setAdminFlag(rs.getBoolean("admin_flag"));
          employee.setPasswordFlag(rs.getBoolean("password_flag"));
          employee.setBillingStartDate(rs.getDate("billing_start_date"));
          employee.setLoanNo(rs.getString("loan_no"));

          return employee;
        });
  }
}
