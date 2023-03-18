package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.user.UserRepository;
import com.cm.welfarecmcity.dto.ForgetPasswordDto;
import com.cm.welfarecmcity.dto.UserDto;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.UserException;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
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

  public ResponseModel<Object> login(UserDto dto) {
    val user = loginRepository.checkUserLogin(dto.getUsername(), dto.getPassword());

    if (user == null) {
      throw new UserException("User not found.");
    }

    val employee = employeeRepository.findById(user.getId());
    val response = new ResponseModel<>();
    response.setData(employee);

    return response;
  }

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
