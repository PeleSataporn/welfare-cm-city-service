package com.cm.welfarecmcity.logic.register;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.constant.EmployeeStatusEnum;
import com.cm.welfarecmcity.dto.ContactDto;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.logic.email.EmailSenderService;
import com.cm.welfarecmcity.logic.register.model.req.ApproveRegisterReq;
import com.cm.welfarecmcity.logic.register.model.req.RegisterReq;
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
  private RegisterRepository registerRepository;

  @Autowired
  private EmailSenderService emailSendService;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Autowired
  private GenerateListener generateListener;

  public Long setModelEmployee(RegisterReq req) {
    val contact = new ContactDto();
    contact.setMobile(req.getTel());
    contact.setEmail(req.getEmail());

    val employee = new EmployeeDto();
    employee.setIdCard(req.getIdCard());
    employee.setFirstName(req.getFirstName());
    employee.setLastName(req.getLastName());
    employee.setEmployeeStatus(EmployeeStatusEnum.NEW_EMPLOYEE.getState());
    employee.setContact(contact);
    employee.setApproveFlag(false);
    employee.setAgency(req.getAgency());
    employee.setPosition(req.getPosition());

    return employeeRepository.save(employee).getId();
  }

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

    val emp = employeeRepository.save(employee);
    emailSendService.sendSimpleEmail(employee.getContact().getEmail());

    return responseDataUtils.insertDataSuccess(emp.getId());
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
  //  public ResponseModel<ResponseData> editStatusEmployeeResign(RegisterReq req) {
  //    String resultStatus = "";
  //    Long idEmp = null;
  //    EmployeeDto emp = employeeRepository.findById(req.getId()).get();
  //    emp.setEmployeeStatus("1");
  //    employeeRepository.save(emp);
  //    resultStatus = "normal";
  //    return responseDataUtils.DataResourceJson(resultStatus, idEmp);
  //  }
}
