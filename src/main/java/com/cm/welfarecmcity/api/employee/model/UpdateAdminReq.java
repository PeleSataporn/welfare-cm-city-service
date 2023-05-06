package com.cm.welfarecmcity.api.employee.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAdminReq {

  private Long id;
  private int type;
  private String value;

  private Long noId;
}
