package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name = "Employee")
public class EmployeeDto extends BaseDto {

  private String employeeCode;
  private String prefix;
  private String firstName;
  private String lastName;
  private String idCard;
  private String gender;

  @Comment("สถานภาพสมรส")
  private int maritalStatus;

  private Date birthday;
  private int age;
  private String agency;
  private String position;
  private double salary;

  @Comment("ค่าตอบแทน")
  private String compensation;

  @Comment("วันที่เริ่มทำงาน/วันที่เริมสัญญา (กรณีลูกจ้างรายวัน, ลูกจ้างโครงการเป็นต้น)")
  private String contractStartDate;

  @Comment("วันที่บรรจุเข้ารับราชการ")
  private String civilServiceDate;

  private String employeeStatus;

  @Comment("วันที่เริ่มเก็บเงิน")
  private Date billingStartDate;

  @Comment("เงินหุ้นรายเดือน (เป็นการแสดงผล)")
  private Double monthlyStockMoney;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private AddressDto address;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private ContactDto contact;

  @Comment("วันที่เสียชีวิตของสมาชิก")
  private Date dateOfDeath;

  @Comment("วันที่ลาออก")
  private Date resignationDate;

  @Comment("วันที่อนุมัติให้ออก")
  private Date approvedResignationDate;

  @Comment("วันที่ครบอายุการเกษียณวันที่สิ้นสุดสัญญา (กรณีลูกจ้างรายวัน, ลูกจ้างโครงการ  เป็นต้น)")
  private Date retirementDate;

  @Comment("เลขที่บัญชีธนาคารเงินเดือน (1 คน 1 บัญชี)")
  private Date salaryBankAccountNumber;

  @Comment("เลขที่บัญชีธนาคารรับเงิน (1 คน 1 บัญชี)")
  private Date bankAccountReceivingNumber;

  private String reason;
  private String description;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private UserDto user;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "employee_id")
  private List<GuarantorDto> guarantors;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "employee_id")
  private List<BeneficiaryDto> beneficiaries;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private StockDto stock;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private LoanDto loan;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "employee_id")
  private List<PetitionNotificationDto> PetitionNotifications;
}
