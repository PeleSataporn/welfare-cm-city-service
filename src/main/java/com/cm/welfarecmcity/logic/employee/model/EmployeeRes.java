package com.cm.welfarecmcity.logic.employee.model;

import com.cm.welfarecmcity.dto.*;
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

  private PositionRes position;
  private String positionName;

  private AffiliationRes affiliation;
  private String affiliationName;

  private EmployeeTypeRes employeeType;
  private String employeeTypeName;

  private LevelRes level;
  private String levelName;

  private DepartmentRes department;
  private String departmentName;

  private String bureauName;
  private UserDto user;
  //  private LoanRes loan;
  //  private StockRes stock;

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
  private Boolean checkStockValueFlag;

  private ContactDto contact;
  private List<BeneficiariesRes> beneficiaries;

  @Comment("หุ้นสะสม")
  private int stockAccumulate;

  @Comment("เงินกู้คงเหลือ")
  private double loanBalance;
}
