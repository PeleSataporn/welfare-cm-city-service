package com.cm.welfarecmcity.api.admin.model;

import java.util.Date;
import lombok.Data;

@Data
public class AdminUpdateInfoReq {

  private Long id;
  private String prefix;
  private String firstName;
  private String lastName;
  private String gender;
  private String idCard;
  private Date birthday;
  // contact
  private String tel;
  private String lineId;
  private String facebook;
  private String email;
  private String address;
  // ระดับ
  private Long levelId;
  // ประเภทพนักงาน
  private Long employeeTypeId;
  // ตำแหน่ง
  private Long positionId;
  // หน่วยงาน
  private Long departmentId;
  // สังกัด
  private Long affiliationId;
  // สำนัก
  // private Long bureauId;
  private String marital;
  private double salary;
  private String compensation;
  private int monthlyStockMoney;
  private Date contractStartDate;
  private Date civilServiceDate;
  private Date billingStartDate;
  private String salaryBankAccountNumber;
  private String bankAccountReceivingNumber;
}
