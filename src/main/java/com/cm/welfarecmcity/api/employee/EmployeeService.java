package com.cm.welfarecmcity.api.employee;

import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Transactional
  public EmployeeDto getEmployee(Long id) {
    val findEmployee = employeeRepository.findById(id);
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    return findEmployee.get();
  }

  @Transactional
  public ResponseModel<ResponseData> getEmployeeTest(Long id) {
    val findEmployee = employeeRepository.findById(id);
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val s = findEmployee.get();
    return responseDataUtils.test1(s.getId());

  }
}
