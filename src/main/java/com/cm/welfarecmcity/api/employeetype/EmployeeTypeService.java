package com.cm.welfarecmcity.api.employeetype;

import com.cm.welfarecmcity.dto.EmployeeTypeDto;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EmployeeTypeService {

  @Autowired private EmployeeTypeRepository employeeTypeRepository;

  @Transactional
  public List<EmployeeTypeDto> searchEmployeeType() {
    Sort sort = Sort.by("name");
    return employeeTypeRepository.findAll(sort);
  }
}
