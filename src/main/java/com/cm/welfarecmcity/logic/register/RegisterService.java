package com.cm.welfarecmcity.logic.register;

import com.cm.welfarecmcity.api.affiliation.AffiliationRepository;
import com.cm.welfarecmcity.api.contact.ContactRepository;
import com.cm.welfarecmcity.api.department.DepartmentRepository;
import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.employeetype.EmployeeTypeRepository;
import com.cm.welfarecmcity.api.level.LevelRepository;
import com.cm.welfarecmcity.api.notification.NotificationRepository;
import com.cm.welfarecmcity.api.position.PositionRepository;
import com.cm.welfarecmcity.api.stock.StockRepository;
import com.cm.welfarecmcity.api.user.UserRepository;
import com.cm.welfarecmcity.constant.EmployeeStatusEnum;
import com.cm.welfarecmcity.constant.NotificationStatusEnum;
import com.cm.welfarecmcity.dto.*;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.logic.email.EmailSenderService;
import com.cm.welfarecmcity.logic.register.model.req.ApproveRegisterReq;
import com.cm.welfarecmcity.logic.register.model.req.CancelRegisterReq;
import com.cm.welfarecmcity.logic.register.model.req.RegisterReq;
import com.cm.welfarecmcity.logic.register.model.req.ResignRegisterReq;
import com.cm.welfarecmcity.logic.register.model.res.SearchNewRegisterRes;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import com.cm.welfarecmcity.utils.listener.GenerateListener;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private PositionRepository positionRepository;

  @Autowired private AffiliationRepository affiliationRepository;

  @Autowired private ContactRepository contactRepository;

  @Autowired private RegisterRepository registerRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private EmailSenderService emailSendService;

  @Autowired private ResponseDataUtils responseDataUtils;

  @Autowired private GenerateListener generateListener;

  @Autowired private DepartmentRepository departmentRepository;

  @Autowired private LevelRepository levelRepository;

  @Autowired private EmployeeTypeRepository employeeTypeRepository;

  @Autowired private NotificationRepository notificationRepository;

  @Autowired private StockRepository stockRepository;

  @Transactional
  public Long setModelEmployee(RegisterReq req) {
    val contact = new ContactDto();
    contact.setTel(req.getTel());
    if (req.getEmail() != null) {
      contact.setEmail(req.getEmail());
    }

    val employee = new EmployeeDto();
    employee.setIdCard(req.getIdCard());
    employee.setFirstName(req.getFirstName());
    employee.setLastName(req.getLastName());
    employee.setEmployeeStatus(EmployeeStatusEnum.NEW_EMPLOYEE.getState());
    employee.setContact(contact);
    employee.setApproveFlag(false);

    switch (req.getPrefix()) {
      case 1 -> {
        employee.setPrefix("นาย");
        employee.setGender("ชาย");
      }
      case 2 -> {
        employee.setPrefix("นางสาว");
        employee.setGender("หญิง");
      }
      case 3 -> {
        employee.setPrefix("นาง");
        employee.setGender("หญิง");
      }
      case 6 -> {
        employee.setPrefix("ว่าที่ร้อยตรี");
        employee.setGender("ชาย");
      }
      case 7 -> {
        employee.setPrefix("ว่าที่ร้อยตรีหญิง");
        employee.setGender("หญิง");
      }
      case 8 -> {
        employee.setPrefix("ว่าที่ร้อยโท");
        employee.setGender("ชาย");
      }
      case 9 -> {
        employee.setPrefix("ว่าที่ร้อยโทหญิง");
        employee.setGender("หญิง");
      }
      case 10 -> {
        employee.setPrefix("ว่าที่ร้อยเอก");
        employee.setGender("ชาย");
      }
      case 11 -> {
        employee.setPrefix("ว่าที่ร้อยเอกหญิง");
        employee.setGender("หญิง");
      }
      case 12 -> {
        employee.setPrefix("สิบตรี");
        employee.setGender("ชาย");
      }
      case 13 -> {
        employee.setPrefix("สิบตรีหญิง");
        employee.setGender("หญิง");
      }
      case 14 -> {
        employee.setPrefix("สิบโท");
        employee.setGender("ชาย");
      }
      case 15 -> {
        employee.setPrefix("สิบโทหญิง");
        employee.setGender("หญิง");
      }
      case 16 -> {
        employee.setPrefix("สิบเอก");
        employee.setGender("ชาย");
      }
      case 17 -> {
        employee.setPrefix("สิบเอกหญิง");
        employee.setGender("หญิง");
      }
      case 18 -> {
        employee.setPrefix("จ่าสิบตรี");
        employee.setGender("ชาย");
      }
      case 19 -> {
        employee.setPrefix("จ่าสิบตรีหญิง");
        employee.setGender("หญิง");
      }
      case 20 -> {
        employee.setPrefix("จ่าสิบโท");
        employee.setGender("ชาย");
      }
      case 21 -> {
        employee.setPrefix("จ่าสิบโทหญิง");
        employee.setGender("หญิง");
      }
      case 22 -> {
        employee.setPrefix("จ่าสิบเอก");
        employee.setGender("ชาย");
      }
      case 23 -> {
        employee.setPrefix("จ่าสิบเอกหญิง");
        employee.setGender("หญิง");
      }
      default -> {
        employee.setPrefix(null);
        employee.setGender(null);
      }
    }

    val position = positionRepository.findById(req.getPositionId()).get();
    employee.setPosition(position);

    val affiliation = affiliationRepository.findById(req.getAffiliationId()).get();
    employee.setAffiliation(affiliation);

    val department = departmentRepository.findById(req.getDepartmentId()).get();
    employee.setDepartment(department);

    if (req.getLevelId() != 0) {
      val level = levelRepository.findById(req.getLevelId()).get();
      employee.setLevel(level);
    }

    val employeeType = employeeTypeRepository.findById(req.getEmployeeTypeId()).get();
    employee.setEmployeeType(employeeType);
    employee.setMonthlyStockMoney(req.getStockValue());

    // stock
    val stock = new StockDto();
    stock.setStockValue(req.getStockValue());

    if (req.getInstallment() == 1) {
      stock.setStockAccumulate(req.getStockValue());
    } else {
      stock.setStockAccumulate(0);
    }

    val stockDetail = new StockDetailDto();
    stockDetail.setStockValue(req.getStockValue());
    stockDetail.setStockMonth(req.getStockMonth());
    stockDetail.setStockYear(req.getStockYear());

    if (req.getInstallment() == 1) {
      stockDetail.setInstallment(1);
      stockDetail.setStockAccumulate(req.getStockValue());
    } else {
      stockDetail.setInstallment(0);
      stockDetail.setStockAccumulate(0);
    }

    // stockDetail
    List<StockDetailDto> stockDetailList = new ArrayList<>();
    stockDetailList.add(stockDetail);
    stock.setStockDetails(stockDetailList);
    employee.setStock(stock);

    val empTemp = employeeRepository.save(employee);

    // notify
    val notify = new PetitionNotificationDto();
    notify.setStatus(NotificationStatusEnum.REGISTER.getState());
    notify.setReason("สมัครเข้าใช้งานระบบ");
    notify.setEmployee(empTemp);

    notificationRepository.save(notify);

    return empTemp.getId();
  }

  @Transactional
  public ResponseModel<ResponseData> addEmployee(RegisterReq req) {
    String resultStatus = "";
    Long idEmp = null;
    val result = registerRepository.checkEmployee(req.getIdCard());

    // check employee status เช็คสถานะสมาชิก มี 3 สถานะ [ ใช้งานปกติ ( ปัจจุบัน ),ลาออก,
    // รอการอนุมัติ ] สมมติ
    if (result != null) {
      if (result.getEmployeeStatus() == EmployeeStatusEnum.NORMAL_EMPLOYEE.getState()) {
        // ใช้งานปกติ ( ปัจจุบัน )
        resultStatus = EmployeeStatusEnum.NORMAL_EMPLOYEE.name();
        idEmp = result.getId();
      } else if (result.getEmployeeStatus() == EmployeeStatusEnum.RESIGN_EMPLOYEE.getState()) {
        // ลาออก
        val findEmployee = employeeRepository.findById(result.getId()).get();
        val stock = findEmployee.getStock();
        stock.setStockAccumulate(0);
        stock.setStockValue(0);
        employeeRepository.save(findEmployee);

        idEmp = setModelEmployee(req);
        resultStatus = EmployeeStatusEnum.NEW_EMPLOYEE.name();
      } else if (result.getEmployeeStatus() == EmployeeStatusEnum.NEW_EMPLOYEE.getState()) {
        // ลาออก
        //        throw new EmployeeException("ID Card has already been used.");
        resultStatus = EmployeeStatusEnum.ERROR_EMPLOYEE.name();
        idEmp = result.getId();
      }
    } else {
      // 3 = รอการอนุมัติ (สมัครเข้าใช้งานใหม่)
      idEmp = setModelEmployee(req);
      resultStatus = EmployeeStatusEnum.NEW_EMPLOYEE.name();
    }
    return responseDataUtils.DataResourceJson(resultStatus, idEmp);
  }

  @Transactional
  public ResponseModel<ResponseId> approveRegister(ApproveRegisterReq req) {
    val findEmployee = employeeRepository.findById(req.getId());

    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee not found.");
    }

    val exists = employeeRepository.existsByEmployeeCode(req.getEmployeeCode());

    if (exists) {
      throw new EmployeeException("Employee employee code not found.");
    }

    val employee = findEmployee.get();
    employee.setEmployeeCode(req.getEmployeeCode());
    employee.setEmployeeStatus(EmployeeStatusEnum.NORMAL_EMPLOYEE.getState());
    employee.setApproveFlag(req.getApproveFlag());

    val user = new UserDto();
    user.setUsername(employee.getEmployeeCode());
    user.setPassword(employee.getIdCard());
    val userTemp = userRepository.save(user);

    employee.setUser(userTemp);
    employeeRepository.save(employee);

    // send email
    val contact = contactRepository.findById(employee.getContact().getId()).get();
    if (contact.getEmail() != null) {
      emailSendService.sendSimpleEmail(
          contact.getEmail(), employee.getEmployeeCode(), employee.getIdCard());
    }

    // notify
    val notify = notificationRepository.findById(req.getNoId()).get();
    notificationRepository.delete(notify);

    return responseDataUtils.insertDataSuccess(req.getId());
  }

  @Transactional
  public List<SearchNewRegisterRes> searchNewRegister() {
    val listNewRegister = registerRepository.searchNewRegister();
    if (listNewRegister.isEmpty()) {
      throw new EmployeeException("Employee not list new register.");
    }

    return listNewRegister;
  }

  @Transactional
  public Integer countNewRegister() {
    return registerRepository.countNewRegister();
  }

  @Transactional
  public ResponseModel<ResponseId> cancelApproveRegister(CancelRegisterReq req) {
    val findEmployee = employeeRepository.findById(req.getId());
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not null.");
    }
    val employee = findEmployee.get();
    employee.setDeleted(true);
    employeeRepository.save(employee);

    emailSendService.sendSimpleEmailCancel(employee.getContact().getEmail(), req.getRemark());
    return responseDataUtils.deleteDataSuccess(req.getId());
  }

  @Transactional
  public ResponseModel<ResponseData> editStatusEmployeeResign(ResignRegisterReq req) {
    // return ResponseModel
    String resultStatus = "";
    Long idEmp = null;

    // find employee
    EmployeeDto emp = employeeRepository.findById(req.getId()).get();
    emp.setEmployeeStatus(EmployeeStatusEnum.NEW_EMPLOYEE.getState());
    emp.setFirstName(req.getFirstName());
    emp.setLastName(req.getLastName());

    val position = positionRepository.findById(req.getPositionId()).get();
    emp.setPosition(position);

    val affiliation = affiliationRepository.findById(req.getAffiliationId()).get();
    emp.setAffiliation(affiliation);

    val department = departmentRepository.findById(req.getDepartmentId()).get();
    emp.setDepartment(department);

    val level = levelRepository.findById(req.getLevelId()).get();
    emp.setLevel(level);

    val employeeType = employeeTypeRepository.findById(req.getEmployeeTypeId()).get();
    emp.setEmployeeType(employeeType);
    emp.setMonthlyStockMoney(req.getStockValue());

    switch (req.getPrefix()) {
      case 1 -> {
        emp.setPrefix("นาย");
        emp.setGender("ชาย");
      }
      case 2 -> {
        emp.setPrefix("นางสาว");
        emp.setGender("หญิง");
      }
      case 3 -> {
        emp.setPrefix("นาง");
        emp.setGender("หญิง");
      }
      case 6 -> {
        emp.setPrefix("ว่าที่ร้อยตรี");
        emp.setGender("ชาย");
      }
      case 7 -> {
        emp.setPrefix("ว่าที่ร้อยตรีหญิง");
        emp.setGender("หญิง");
      }
      case 8 -> {
        emp.setPrefix("ว่าที่ร้อยโท");
        emp.setGender("ชาย");
      }
      case 9 -> {
        emp.setPrefix("ว่าที่ร้อยโทหญิง");
        emp.setGender("หญิง");
      }
      case 10 -> {
        emp.setPrefix("ว่าที่ร้อยเอก");
        emp.setGender("ชาย");
      }
      case 11 -> {
        emp.setPrefix("ว่าที่ร้อยเอกหญิง");
        emp.setGender("หญิง");
      }
      case 12 -> {
        emp.setPrefix("สิบตรี");
        emp.setGender("ชาย");
      }
      case 13 -> {
        emp.setPrefix("สิบตรีหญิง");
        emp.setGender("หญิง");
      }
      case 14 -> {
        emp.setPrefix("สิบโท");
        emp.setGender("ชาย");
      }
      case 15 -> {
        emp.setPrefix("สิบโทหญิง");
        emp.setGender("หญิง");
      }
      case 16 -> {
        emp.setPrefix("สิบเอก");
        emp.setGender("ชาย");
      }
      case 17 -> {
        emp.setPrefix("สิบเอกหญิง");
        emp.setGender("หญิง");
      }
      case 18 -> {
        emp.setPrefix("จ่าสิบตรี");
        emp.setGender("ชาย");
      }
      case 19 -> {
        emp.setPrefix("จ่าสิบตรีหญิง");
        emp.setGender("หญิง");
      }
      case 20 -> {
        emp.setPrefix("จ่าสิบโท");
        emp.setGender("ชาย");
      }
      case 21 -> {
        emp.setPrefix("จ่าสิบโทหญิง");
        emp.setGender("หญิง");
      }
      case 22 -> {
        emp.setPrefix("จ่าสิบเอก");
        emp.setGender("ชาย");
      }
      case 23 -> {
        emp.setPrefix("จ่าสิบเอกหญิง");
        emp.setGender("หญิง");
      }
      default -> {
        emp.setPrefix(null);
        emp.setGender(null);
      }
    }

    val contact = contactRepository.findById(emp.getContact().getId()).get();
    contact.setEmail(req.getEmail());
    contact.setTel(req.getTel());

    employeeRepository.save(emp);
    resultStatus = "NEW_EMPLOYEE";
    return responseDataUtils.DataResourceJson(resultStatus, idEmp);
  }
}
