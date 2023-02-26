package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name = "Beneficiary")
public class BeneficiaryDto extends BaseDto {

  private String prefix;
  private String firstName;
  private String lastName;
  private String gender;
  private Date birthday;

  @Comment("ความสัมพันธ์")
  private String relationship;

  @Comment("สถานภาพสมรส")
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private MaritalDto marital;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id")
  private EmployeeDto employee;
}
