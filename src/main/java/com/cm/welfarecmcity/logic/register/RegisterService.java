package com.cm.welfarecmcity.logic.register;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.dto.ContactDto;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
//import com.cm.welfarecmcity.logic.email.EmailSendService;
import com.cm.welfarecmcity.logic.email.EmailSenderService;
import com.cm.welfarecmcity.logic.register.model.RegisterReq;
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

  public void register(RegisterReq req) {
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
    }
  }
}
