package com.cm.welfarecmcity.logic.register.model.req;

import lombok.Getter;
import lombok.Setter;

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
}
