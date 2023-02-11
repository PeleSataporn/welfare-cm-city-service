package com.cm.welfarecmcity.logic.register.model.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterReq {

  private String firstName;
  private String lastName;
  private String idCard;
  private String tel;
  private String email;

  // หน่วยงาน
  private String agency;
  // ตำแหน่ง
  private String position;
}
