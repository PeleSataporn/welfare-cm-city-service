package com.cm.welfarecmcity.logic.register.model;

//import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterReq {

//  @NotBlank(message = "employee code is mandatory")
  private String employeeCode;

  private String prefix;
  private String firstName;
  private String lastName;

//  @NotBlank(message = "idCard is mandatory")
  private String idCard;

  private String tel;

//  @NotBlank(message = "email is mandatory")
  private String email;

// ประเภทพนักงาน
  private String employeeType;

  private Long id;
}
