package com.cm.welfarecmcity.api.beneficiary.model;

import java.util.Date;
import lombok.Data;
import org.hibernate.annotations.Comment;

@Data
public class BeneficiaryRes {

  private Long id;
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
}
