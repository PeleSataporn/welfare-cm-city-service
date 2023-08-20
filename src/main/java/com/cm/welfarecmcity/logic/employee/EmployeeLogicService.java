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
    res.setPositionName(emp.getPosition() != null ? emp.getPosition().getName() : null);
    res.setAffiliationName(emp.getAffiliation() != null ? emp.getAffiliation().getName() : null);
    res.setEmployeeTypeName(emp.getEmployeeType() != null ? emp.getEmployeeType().getName() : null);
    res.setLevelName(emp.getLevel() != null ? emp.getLevel().getName() : null);
    res.setDepartmentName(emp.getDepartment() != null ? emp.getDepartment().getName() : null);
    res.setBureauName(emp.getAffiliation() != null ? emp.getAffiliation().getBureau().getName() : null);

    return res;
  }

  public EmployeeOfMainRes getEmployeeOfMain(Long id) {
    val emp = employeeLogicRepository.getEmployeeOfMain(id);
    emp.setGuarantee(documentRepository.countGuarantee(id));

    return emp;
  }
}
