package com.cm.welfarecmcity.logic.employee.model;

import java.util.Date;
import lombok.Data;

@Data
public class BeneficiariesRes {

  private Long id;
  private String prefix;
  private String firstName;
  private String lastName;
  private String gender;
  private Date birthday;
  private String relationship;
  private String marital;
  private String lifeStatus;
}
