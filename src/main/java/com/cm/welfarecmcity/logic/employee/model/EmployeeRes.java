package com.cm.welfarecmcity.logic.employee.model;

import com.cm.welfarecmcity.dto.AffiliationDto;
import com.cm.welfarecmcity.dto.BeneficiaryDto;
import com.cm.welfarecmcity.dto.ContactDto;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.Comment;

@Data
public class EmployeeRes {

  private String employeeCode;
  private String prefix;
  private String firstName;
  private String lastName;
  private String idCard;
  private String gender;
  private String marital;
  private Date birthday;

  private String positionName;
  private String affiliationName;
  private String employeeTypeName;
  private String levelName;
  private String departmentName;
  private String bureauName;

  private double salary;
  private String compensation;
  private Date contractStartDate;
  private Date civilServiceDate;
  private int employeeStatus;
  private Date billingStartDate;
  private int monthlyStockMoney;
  private Date retirementDate;
  private String salaryBankAccountNumber;
  private String bankAccountReceivingNumber;

  private Boolean approveFlag;
  private Boolean passwordFlag;
  private Boolean profileFlag;

  private ContactDto contact;
  private List<BeneficiariesRes> beneficiaries;
}
