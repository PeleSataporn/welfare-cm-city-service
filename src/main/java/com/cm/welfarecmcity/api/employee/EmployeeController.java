package com.cm.welfarecmcity.api.employee;

import com.cm.welfarecmcity.api.employee.model.*;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.logic.loan.model.BeneficiaryReq;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  @PatchMapping
  public ResponseModel<ResponseId> updateEmp(@RequestBody EmpEditReq req) {
    return employeeService.updateEmp(req);
  }

  @PostMapping("/search")
  public List<EmpByAdminRes> searchEmployee() throws SQLException {
    return employeeService.searchEmployee();
  }

  @PatchMapping("/update-resign")
  public ResponseModel<ResponseId> updateResign(@RequestBody UpdateResignReq req) {
    return employeeService.updateResign(req);
  }

  @PatchMapping("/update-resign-admin")
  public ResponseModel<ResponseId> updateResignAdmin(@RequestBody UpdateAdminReq req) {
    return employeeService.updateResignAdmin(req);
  }

  @PatchMapping("/update-stock-value")
  public ResponseModel<ResponseId> updateStockValue(@RequestBody UpdateStockValueReq req) {
    return employeeService.updateStockValue(req);
  }

  @PatchMapping("/update-emp-status")
  public ResponseModel<ResponseId> updateEmployeeStatus(@RequestBody UpdateAdminReq req) {
    return employeeService.updateEmployeeStatus(req);
  }

  @PatchMapping("/update-beneficiary-id")
  public ResponseModel<ResponseId> updateBeneficiaryId(@RequestBody List<BeneficiaryReq> req) throws JsonProcessingException {
    return employeeService.updateBeneficiaryId(req);
  }

  @PatchMapping("/update-by-user")
  public ResponseModel<String> updateByUser(@RequestBody UpdateUserReq req) throws JsonProcessingException {
    return employeeService.updateByUser(req);
  }

  @PatchMapping("/approve-update-by-user")
  public void approveUpdateByUser(@RequestBody UpdateUserReq req) {
    employeeService.approveUpdateByUser(req);
  }
}
