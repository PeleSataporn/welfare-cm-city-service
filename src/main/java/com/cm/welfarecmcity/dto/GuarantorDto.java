package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Guarantor")
public class GuarantorDto extends BaseDto {

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private EmployeeDto employeeGuarantor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id")
  private EmployeeDto employee;
}
