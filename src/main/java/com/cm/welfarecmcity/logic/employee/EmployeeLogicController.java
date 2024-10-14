package com.cm.welfarecmcity.logic.employee;

import com.cm.welfarecmcity.logic.employee.model.EmployeeOfMainRes;
import com.cm.welfarecmcity.logic.employee.model.EmployeeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/logic/v1/employee")
public class EmployeeLogicController {

  @Autowired private EmployeeLogicService employeeLogicService;

  @GetMapping("{id}")
  public ResponseEntity<EmployeeRes> getEmployee(@PathVariable Long id) {
    return new ResponseEntity<>(employeeLogicService.getEmployee(id), HttpStatus.OK);
  }

  @GetMapping("/of-main/{id}")
  public ResponseEntity<EmployeeOfMainRes> getEmployeeOfMain(@PathVariable Long id) {
    return new ResponseEntity<>(employeeLogicService.getEmployeeOfMain(id), HttpStatus.OK);
  }
}
