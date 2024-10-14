package com.cm.welfarecmcity.logic.register.model.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResignRegisterReq {

  private long id;
  //  private int prefix;
  //  private String firstName;
  //  private String lastName;
  //  private String idCard;
  //  private String tel;
  //  private String email;
  //  //   ตำแหน่ง
  //  private Long positionId;
  //  // สังกัด
  //  private Long affiliationId;

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
  private Long departmentId;
  private Long levelId;
  private Long employeeTypeId;

  // Stock
  private int stockValue;
  private String stockMonth;
  private String stockYear;
}
