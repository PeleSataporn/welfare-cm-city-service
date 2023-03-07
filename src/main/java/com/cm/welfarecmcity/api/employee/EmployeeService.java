package com.cm.welfarecmcity.api.employee;

import com.cm.welfarecmcity.api.employee.model.EmpEditReq;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.mapper.MapStructMapper;
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

  @Autowired
  private MapStructMapper mapStructMapper;

  @Transactional
  public EmployeeDto getEmployee(Long id) {
    val findEmployee = employeeRepository.findById(id);
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val employee = findEmployee.get();

    //    EmpResemployee
    //    for(val beneficiaries : employee.getBeneficiaries()){
    //      beneficiaries.getGender()
    //    }

    return findEmployee.get();
  }

  @Transactional
  public ResponseModel<ResponseId> updateEmp(EmpEditReq req) {
    val findEmployee = employeeRepository.findById(req.getId());
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val employeeMapper = mapStructMapper.reqToEmployee(req);
    employeeRepository.save(employeeMapper);

    return responseDataUtils.updateDataSuccess(req.getId());
  }
}
