package com.cm.welfarecmcity.logic.employee;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.logic.document.DocumentRepository;
import com.cm.welfarecmcity.logic.employee.model.EmployeeOfMainRes;
import com.cm.welfarecmcity.logic.employee.model.EmployeeRes;
import com.cm.welfarecmcity.mapper.MapStructMapper;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeLogicService {

  @Autowired
  private EmployeeLogicRepository employeeLogicRepository;

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private MapStructMapper mapStructMapper;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Transactional
  public EmployeeRes getEmployee(Long id) {
    val emp = employeeRepository.findById(id).get();

    val res = mapStructMapper.employeeToRes(emp);
    res.setPositionName(emp.getPosition().getName());
    res.setAffiliationName(emp.getAffiliation().getName());
    res.setEmployeeTypeName(emp.getEmployeeType().getName());
    res.setLevelName(emp.getLevel().getName());
    res.setDepartmentName(emp.getDepartment().getName());
    res.setBureauName(emp.getAffiliation().getBureau().getName());

    return res;
  }

  @Transactional
  public EmployeeOfMainRes getEmployeeOfMain(Long id) {
    val emp = employeeLogicRepository.getEmployeeOfMain(id);
    emp.setGuarantee(documentRepository.countGuarantee(id));

    return emp;
  }
}
