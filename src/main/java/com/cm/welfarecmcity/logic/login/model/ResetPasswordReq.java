package com.cm.welfarecmcity.logic.login.model;

import lombok.Data;

@Data
public class ResetPasswordReq {

  private Long id;
  private String oldPassword;
  private String newPassword;
  private String confirmPassword;
}
