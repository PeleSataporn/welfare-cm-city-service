package com.cm.welfarecmcity.api.employee;

import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
}
