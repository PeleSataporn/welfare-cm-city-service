package com.cm.welfarecmcity.logic.register;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.logic.register.model.RegisterReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

  @Autowired
  private EmployeeRepository employeeRepository;

  public void register(RegisterReq req) {
    // TODO: register
  }
}
