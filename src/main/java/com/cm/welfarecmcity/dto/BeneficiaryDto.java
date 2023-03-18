package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

//  private Long id;
  private String prefix;
  private String firstName;
  private String lastName;
  private String gender;
  private Date birthday;

  @Comment("ความสัมพันธ์")
  private String relationship;

  @Comment("สถานภาพสมรส")
  private String marital;

  @Comment("สถานภาพ")
  private String lifeStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  private EmployeeDto employee;
}
