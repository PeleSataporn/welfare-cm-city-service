package com.cm.welfarecmcity.api.employee.model;

import com.cm.welfarecmcity.dto.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.Date;
import java.util.List;

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

    @Comment("สถานภาพสมรส")
    private String marital;

    private Date birthday;

    @Comment("ตำแหน่ง")
    private PositionsDto position;

    @Comment("สังกัด")
    private AffiliationDto affiliation;

    @Comment("ประเภทพนักงาน")
    private EmployeeTypeDto employeeType;

    @Comment("ระดับ")
    private LevelDto level;

    @Column(columnDefinition = "double default 0.0")
    private double salary;

    @Comment("ค่าตอบแทน")
    private String compensation;

    @Comment("วันที่เริ่มทำงาน/วันที่เริมสัญญา (กรณีลูกจ้างรายวัน, ลูกจ้างโครงการเป็นต้น)")
    private String contractStartDate;

    @Comment("วันที่บรรจุเข้ารับราชการ")
    private String civilServiceDate;

    private int employeeStatus;

    @Comment("วันที่เริ่มเก็บเงิน")
    private Date billingStartDate;

    @Comment("เงินหุ้นรายเดือน (เป็นการแสดงผล)")
    @Column(columnDefinition = "double default 0.0")
    private Double monthlyStockMoney;

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

    private UserDto user;

    private List<GuarantorDto> guarantors;

    private List<BeneficiaryDto> beneficiaries;

    private StockDto stock;

    private LoanDto loan;

    private List<PetitionNotificationDto> PetitionNotifications;

    private Boolean approveFlag;
}
