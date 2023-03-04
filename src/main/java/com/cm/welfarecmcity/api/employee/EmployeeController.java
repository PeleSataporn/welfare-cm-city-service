package com.cm.welfarecmcity.api.employee;

import com.cm.welfarecmcity.api.employee.model.EmpEditReq;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  @GetMapping("{id}")
  public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
    return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
  }

  @PatchMapping
  //@RequestMapping(path = "{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseModel<ResponseId> updateEmp(@RequestBody EmpEditReq req) {
    return employeeService.updateEmp(req);
  }
  //  @GetMapping("{id}")
  //  public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
  //    EmployeeDto
  //  }
}
