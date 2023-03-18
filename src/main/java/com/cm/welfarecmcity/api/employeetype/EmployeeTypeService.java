package com.cm.welfarecmcity.api.employeetype;

import com.cm.welfarecmcity.dto.EmployeeTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeTypeService {

  @Autowired
  private EmployeeTypeRepository employeeTypeRepository;

  public List<EmployeeTypeDto> searchEmployeeType() {
    return employeeTypeRepository.findAll();
  }
}
