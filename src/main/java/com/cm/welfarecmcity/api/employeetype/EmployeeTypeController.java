package com.cm.welfarecmcity.api.employeetype;

import com.cm.welfarecmcity.dto.EmployeeTypeDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/employee-type")
public class EmployeeTypeController {

  @Autowired private EmployeeTypeService employeeTypeService;

  @PostMapping("search")
  public List<EmployeeTypeDto> searchEmployeeType() {
    return employeeTypeService.searchEmployeeType();
  }
}
