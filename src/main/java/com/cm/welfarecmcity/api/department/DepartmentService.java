package com.cm.welfarecmcity.api.department;

import com.cm.welfarecmcity.dto.DepartmentDto;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

  @Autowired private DepartmentRepository departmentRepository;

  @Transactional
  public List<DepartmentDto> searchDepartment() {
    Sort sort = Sort.by("name");
    return departmentRepository.findAll(sort);
  }
}
