package com.cm.welfarecmcity.api.employee.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Setter
@Getter
public class UpdateUserReq {

  private Long id;
  private String firstName;
  private String lastName;
  private Date birthday;
  private String marital;
  private Date contractStartDate;
  private Date civilServiceDate;
  private Date billingStartDate;
  private String salaryBankAccountNumber;
  private String bankAccountReceivingNumber;

  private String tel;
  private String email;
  private String lineId;
  private String facebook;
  private String address;
}
