package com.cm.welfarecmcity.api.employee;

import com.cm.welfarecmcity.api.employee.model.EmpEditReq;
import com.cm.welfarecmcity.api.employeetype.EmployeeTypeRepository;
import com.cm.welfarecmcity.api.level.LevelRepository;
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
  private LevelRepository levelRepository;

  @Autowired
  private EmployeeTypeRepository employeeTypeRepository;

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

    return findEmployee.get();
  }

  @Transactional
  public ResponseModel<ResponseId> updateEmp(EmpEditReq req) {
    val findEmployee = employeeRepository.findById(req.getId());
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val employeeMapper = mapStructMapper.reqToEmployee(req);
    employeeMapper.setBeneficiaries(findEmployee.get().getBeneficiaries());

    if (req.getLevelId() != 0) {
      employeeMapper.setLevel(levelRepository.findById(req.getLevelId()).get());
    }

    if (req.getEmployeeTypeId() != 0) {
      employeeMapper.setEmployeeType(employeeTypeRepository.findById(req.getEmployeeTypeId()).get());
    }

    employeeMapper.setProfileFlag(true);

    employeeRepository.save(employeeMapper);

    return responseDataUtils.updateDataSuccess(req.getId());
  }
}
