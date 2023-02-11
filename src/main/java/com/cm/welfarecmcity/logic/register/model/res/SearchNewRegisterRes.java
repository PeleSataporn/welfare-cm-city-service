package com.cm.welfarecmcity.logic.register.model.res;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchNewRegisterRes {

  private Long id;
  private Date createDate;
  private String firstName;
  private String lastName;
  private String idCard;
  private String agency;
  private String position;
  private String tel;
  private String email;
}
