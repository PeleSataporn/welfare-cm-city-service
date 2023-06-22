package com.cm.welfarecmcity.logic.employee.model;

import com.cm.welfarecmcity.dto.EmployeeDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.List;
import lombok.Data;

@Data
public class DepartmentRes {

  private Long id;
  private String name;
  //  private List<EmpRes> employees;
}
