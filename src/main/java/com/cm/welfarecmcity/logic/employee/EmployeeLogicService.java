package com.cm.welfarecmcity.logic.employee;

import com.cm.welfarecmcity.logic.document.DocumentRepository;
import com.cm.welfarecmcity.logic.employee.model.EmployeeOfMainRes;
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

  @Transactional
  public EmployeeOfMainRes getEmployeeOfMain(Long id) {
    val emp = employeeLogicRepository.getEmployeeOfMain(id);
    emp.setGuarantee(documentRepository.countGuarantee(id));

    return emp;
  }
}
