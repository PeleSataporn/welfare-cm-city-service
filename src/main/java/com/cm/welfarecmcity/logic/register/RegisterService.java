package com.cm.welfarecmcity.logic.register;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.dto.ContactDto;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
//import com.cm.welfarecmcity.logic.email.EmailSendService;
import com.cm.welfarecmcity.logic.email.EmailSenderService;
import com.cm.welfarecmcity.logic.register.model.RegisterReq;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
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

  public ResponseModel<ResponseData> addEmployee(RegisterReq req) {
    if (req.getEmployeeCode() != null) {
      val check = registerRepository.checkEmployeeCode(req);
      if (check == null) {
        throw new EmployeeException("invalid employee code or  idCard.");
      }

      val contact = new ContactDto();
      contact.setMobile(req.getTel());
      contact.setEmail(req.getEmail());

      val employee = new EmployeeDto();
      employee.setId(check.getId());
      employee.setEmployeeCode(req.getEmployeeCode());
      employee.setIdCard(req.getIdCard());
      employee.setPrefix(req.getPrefix());
      employee.setFirstName(req.getFirstName());
      employee.setLastName(req.getLastName());
      employee.setContact(contact);

      employeeRepository.save(employee);
      emailSendService.sendSimpleEmail(req.getEmail());

      return null;
    }else{
      String resultStatus = "";
      Long idEmp = null;
      val result = registerRepository.checkEmployeeCode2(req);
      // employeeStatus มาเช็ค สถานะสมาชิก มี 3 สถานะ [ ใช้งานปกติ ( ปัจจุบัน ),ลาออก, รอการอนุมัติ ] สมมติ
      if(result != null ){
        if(result.getEmployeeStatus().equals("1")){  // 1 = ใช้งานปกติ ( ปัจจุบัน )
          //  ไม่ต้องทำอะไร มีข้อมูลอยู่เเล้ว
          resultStatus = "normal";
          idEmp = result.getId();
        }else if(result.getEmployeeStatus().equals("2")){ // 2 = ลาออก
          //  ไม่ต้องทำอะไร มีข้อมูลอยู่เเล้ว
          resultStatus = "resign";
          idEmp = result.getId();
        }
      }else{  // 3 = รอการอนุมัติ
        val contact = new ContactDto();
        contact.setMobile(req.getTel());
        contact.setEmail(req.getEmail());

        val employee = new EmployeeDto();
        employee.setEmployeeCode(req.getEmployeeCode());
        employee.setIdCard(req.getIdCard());
        employee.setPrefix(req.getPrefix());
        employee.setFirstName(req.getFirstName());
        employee.setLastName(req.getLastName());
        employee.setContact(contact);
        employee.setEmployeeStatus("3");

        employeeRepository.save(employee);
        emailSendService.sendSimpleEmail(req.getEmail());
        resultStatus = "register";
        idEmp = null;
      }
      return responseDataUtils.DataResourceJson(resultStatus,idEmp);
    }
  }

  public ResponseModel<ResponseData> editStatusEmployeeResign(RegisterReq req) {
    String resultStatus = "";
    Long idEmp = null;
    EmployeeDto emp = employeeRepository.findById(req.getId()).get();
    emp.setEmployeeStatus("1");
    employeeRepository.save(emp);
    resultStatus = "normal";
    return responseDataUtils.DataResourceJson(resultStatus,idEmp);
  }

}
