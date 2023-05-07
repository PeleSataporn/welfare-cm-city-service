package com.cm.welfarecmcity.api.employee.model;

import com.cm.welfarecmcity.dto.*;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
public class EmpEditReq {

  private Long id;
  private String employeeCode;
  private String prefix;
  private String firstName;
  private String lastName;
  private String idCard;
  private String gender;
  private int employeeStatus;

  @Comment("สถานภาพสมรส")
  private String marital;

  private Date birthday;

  @Comment("ตำแหน่ง")
  private PositionsDto position;

  @Comment("สังกัด")
  private AffiliationDto affiliation;

  @Comment("ประเภทพนักงาน")
  private EmployeeTypeDto employeeType;

  private Long employeeTypeId;

  @Comment("ระดับ")
  private LevelDto level;

  private Long levelId;

  @Column(columnDefinition = "double default 0.0")
  private double salary;

  @Comment("ค่าตอบแทน")
  private String compensation;

  @Comment("วันที่เริ่มทำงาน/วันที่เริมสัญญา (กรณีลูกจ้างรายวัน, ลูกจ้างโครงการเป็นต้น)")
  private Date contractStartDate;

  @Comment("วันที่บรรจุเข้ารับราชการ")
  private Date civilServiceDate;

  @Comment("วันที่เริ่มเก็บเงิน")
  private Date billingStartDate;

  @Comment("เงินหุ้นรายเดือน (เป็นการแสดงผล)")
  @Column(columnDefinition = "double default 0.0")
  private Double monthlyStockMoney;

  private ContactDto contact;

  @Comment("เลขที่บัญชีธนาคารเงินเดือน (1 คน 1 บัญชี)")
  private String salaryBankAccountNumber;

  @Comment("เลขที่บัญชีธนาคารรับเงิน (1 คน 1 บัญชี)")
  private String bankAccountReceivingNumber;

  private UserDto user;

  private LoanDto loan;

  private StockDto stock;

  private DepartmentDto department;

  private Boolean approveFlag;

  private Boolean passwordFlag;

  private Boolean profileFlag;
}
