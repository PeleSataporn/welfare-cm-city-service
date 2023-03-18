package com.cm.welfarecmcity.logic.register;

import com.cm.welfarecmcity.api.affiliation.AffiliationRepository;
import com.cm.welfarecmcity.api.contact.ContactRepository;
import com.cm.welfarecmcity.api.department.DepartmentRepository;
import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.position.PositionRepository;
import com.cm.welfarecmcity.api.user.UserRepository;
import com.cm.welfarecmcity.constant.EmployeeStatusEnum;
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
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private AffiliationRepository affiliationRepository;

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private RegisterRepository registerRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailSenderService emailSendService;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Autowired
  private GenerateListener generateListener;

  @Autowired
  private DepartmentRepository departmentRepository;

  //  @Transactional
  public Long setModelEmployee(RegisterReq req) {
    val contact = new ContactDto();
    contact.setTel(req.getTel());
    contact.setEmail(req.getEmail());

    //    val contactcontactRepository.save(contact);

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
      case 4 -> {
        employee.setPrefix("ว่าที่ร้อยตรี (ชาย)");
        employee.setGender("ชาย");
      }
      case 5 -> {
        employee.setPrefix("ว่าที่ร้อยตรี (หญิง)");
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

    val department = departmentRepository.findById(req.getDapartmentId()).get();
    employee.setDepartment(department);

    return employeeRepository.save(employee).getId();
  }

  //  @Transactional
  public ResponseModel<ResponseData> addEmployee(RegisterReq req) {
    String resultStatus = "";
    Long idEmp = null;
    val result = registerRepository.checkEmployee(req.getIdCard());

    // check employee status เช็คสถานะสมาชิก มี 3 สถานะ [ ใช้งานปกติ ( ปัจจุบัน ),ลาออก, รอการอนุมัติ ] สมมติ
    if (result != null) {
      if (result.getEmployeeStatus() == EmployeeStatusEnum.NORMAL_EMPLOYEE.getState()) {
        // ใช้งานปกติ ( ปัจจุบัน )
        resultStatus = EmployeeStatusEnum.NORMAL_EMPLOYEE.name();
        idEmp = result.getId();
      } else if (result.getEmployeeStatus() == EmployeeStatusEnum.RESIGN_EMPLOYEE.getState()) {
        // ลาออก
        resultStatus = EmployeeStatusEnum.RESIGN_EMPLOYEE.name();
        idEmp = result.getId();

        val findEmployee = employeeRepository.findById(idEmp).get();
        findEmployee.setApproveFlag(false);
        employeeRepository.save(findEmployee);
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

  public ResponseModel<ResponseId> approveRegister(ApproveRegisterReq req) {
    val findEmployee = employeeRepository.findById(req.getId());
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee not found.");
    }
    val employee = findEmployee.get();
    employee.setEmployeeCode(generateListener.generateCustomerCode());
    employee.setEmployeeStatus(EmployeeStatusEnum.NORMAL_EMPLOYEE.ordinal());
    employee.setApproveFlag(req.getApproveFlag());

    val user = new UserDto();
    user.setUsername(employee.getEmployeeCode());
    user.setPassword(employee.getIdCard());
    val userTemp = userRepository.save(user);

    employee.setUser(userTemp);
    val emp = employeeRepository.save(employee);
    emailSendService.sendSimpleEmail(employee.getContact().getEmail(), employee.getEmployeeCode(), employee.getIdCard());

    return responseDataUtils.insertDataSuccess(req.getId());
  }

  public List<SearchNewRegisterRes> searchNewRegister() {
    val listNewRegister = registerRepository.searchNewRegister();
    if (listNewRegister.isEmpty()) {
      throw new EmployeeException("Employee not list new register.");
    }

    return listNewRegister;
  }

  public Integer countNewRegister() {
    return registerRepository.countNewRegister();
  }

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

  public ResponseModel<ResponseData> editStatusEmployeeResign(ResignRegisterReq req) {
    String resultStatus = "";
    Long idEmp = null;
    EmployeeDto emp = employeeRepository.findById(req.getId()).get();
    emp.setEmployeeStatus(EmployeeStatusEnum.NEW_EMPLOYEE.getState());
    employeeRepository.save(emp);
    resultStatus = "NEW_EMPLOYEE";
    return responseDataUtils.DataResourceJson(resultStatus, idEmp);
  }
}
