package com.cm.welfarecmcity.logic.register.model.req;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
public class RegisterReq {

  private int prefix;
  private String firstName;
  private String lastName;
  private String idCard;
  private String tel;
  private String email;
  // ตำแหน่ง
  private Long positionId;
  // สังกัด
  private Long affiliationId;
  // หน่วยงาน
  private Long dapartmentId;
  private Long levelId;
  private Long employeeTypeId;

  // Stock
  private int stockValue;
  private String stockMonth;
  private String stockYear;
}
