package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Department")
public class DepartmentDto extends BaseDto {

  private String name;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "department_id")
  private List<EmployeeDto> employees;
}
