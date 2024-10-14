package com.cm.welfarecmcity.api.department;

import com.cm.welfarecmcity.dto.DepartmentDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/department")
public class DepartmentController {

  @Autowired private DepartmentService departmentService;

  @PostMapping("search")
  public List<DepartmentDto> searchDepartment() {
    return departmentService.searchDepartment();
  }
}
