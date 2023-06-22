package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.user.UserRepository;
import com.cm.welfarecmcity.dto.ForgetPasswordDto;
import com.cm.welfarecmcity.dto.UserDto;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.exception.entity.UserException;
import com.cm.welfarecmcity.logic.login.model.LoginRes;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Transactional
  public ResponseModel<Object> login(UserDto dto) {
    val user = loginRepository.checkUserLogin(dto.getUsername(), dto.getPassword());

    if (user == null) {
      throw new UserException("User not found.");
    }

    val findEmployee = employeeRepository.findById(user.getId());

    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee not found.");
    }

    val employee = findEmployee.get();

    val res = new LoginRes();
    res.setId(employee.getId());
    res.setEmployeeStatus(employee.getEmployeeStatus());
    res.setPasswordFlag(employee.getPasswordFlag());
    if (employee.getStock() != null) {
      res.setStockId(employee.getStock().getId());
    } else {
      res.setStockId(null);
    }

    if (employee.getLoan() != null) {
      res.setLoanId(employee.getLoan().getId());
    } else {
      res.setLoanId(null);
    }

    val response = new ResponseModel<>();
    response.setData(res);

    return response;
  }

  @Transactional
  public ResponseModel<ResponseData> changeForgetPassword(ForgetPasswordDto forgetPasswordDto) {
    String resultStatus = "";
    Long idEmp = null;
    val changeForgetPassword = loginRepository.checkChangeForgetPassword(
      forgetPasswordDto.getEmail(),
      forgetPasswordDto.getIdCard(),
      forgetPasswordDto.getEmployeeCode()
    );
    if (changeForgetPassword != null && changeForgetPassword.getUserId() != null) {
      UserDto emp = userRepository.findById(changeForgetPassword.getUserId()).get();
      emp.setPassword(forgetPasswordDto.getNewPassword());
      userRepository.save(emp);

      // update password_flag
      val findEmployee = employeeRepository.findById(changeForgetPassword.getId()).get();
      findEmployee.setPasswordFlag(true);
      employeeRepository.save(findEmployee);

      idEmp = changeForgetPassword.getId();
      resultStatus = "CHANGE_SUCCESS";
    } else {
      idEmp = null;
      resultStatus = "CHANGE_ERROR";
    }
    return responseDataUtils.DataResourceJson(resultStatus, idEmp);
  }
}
