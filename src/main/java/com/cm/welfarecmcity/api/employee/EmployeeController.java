package com.cm.welfarecmcity.api.employee;

import com.cm.welfarecmcity.api.employee.model.EmpEditReq;
import com.cm.welfarecmcity.api.employee.model.UpdateAdminReq;
import com.cm.welfarecmcity.api.employee.model.UpdateResignReq;
import com.cm.welfarecmcity.api.employee.model.UpdateStockValueReq;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.logic.loan.model.BeneficiaryReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  @PatchMapping
  public ResponseModel<ResponseId> updateEmp(@RequestBody EmpEditReq req) {
    return employeeService.updateEmp(req);
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

  @GetMapping("test-host")
  public String testHost() {
    return "-- > Test Host <--";
  }

  @PatchMapping("/update-beneficiary-id")
  public ResponseModel<ResponseId> updateBeneficiaryId(@RequestBody List<BeneficiaryReq> req) throws JsonProcessingException {
    return employeeService.updateBeneficiaryId(req);
  }
}
