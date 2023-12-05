package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.user.UserRepository;
import com.cm.welfarecmcity.dto.ForgetPasswordDto;
import com.cm.welfarecmcity.dto.UserDto;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.dto.base.ResponseTextStatus;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.exception.entity.UserException;
import com.cm.welfarecmcity.logic.document.DocumentRepository;
import com.cm.welfarecmcity.logic.login.model.LoginRes;
import com.cm.welfarecmcity.logic.login.model.ResetPasswordReq;
import com.cm.welfarecmcity.mapper.MapStructMapper;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private MapStructMapper mapStructMapper;

  @Transactional
  public ResponseModel<Object> login(UserDto dto) {
    UserDto user = new UserDto();
    val userCode = loginRepository.getEmpCodeOfId(dto.getUsername());
    String plainPassword = dto.getPassword();
    String hashedPassword = hashMD5(plainPassword);
    if (userCode.getPasswordFlag() == null || !userCode.getPasswordFlag()) {
      user = loginRepository.checkUserLogin(dto.getUsername(), dto.getPassword());
    } else {
      user = loginRepository.checkUserLogin(dto.getUsername(), hashedPassword);
    }
    String STORED_HASHED_PASSWORD = user.getPassword();

    if (user == null) {
      throw new UserException("User not found.");
    }

    if (checkstatusFlag(user, hashedPassword, STORED_HASHED_PASSWORD)) {
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
    } else {
      throw new UserException("User not found.");
    }
  }

  public boolean checkstatusFlag(UserDto dto, String hashedPassword, String STORED_HASHED_PASSWORD) {
    if (dto.getPasswordFlag() == null || !dto.getPasswordFlag()) {
      return true;
    } else {
      if (STORED_HASHED_PASSWORD.equals(hashedPassword)) {
        return true;
      } else {
        return false;
      }
    }
  }

  @Transactional
  public ResponseModel<ResponseData> changeForgetPassword(ForgetPasswordDto forgetPasswordDto) {
    // data response
    String resultStatus = "";
    Long idEmp = null;

    // check forget password
    val changeForgetPassword = loginRepository.checkChangeForgetPassword(
      forgetPasswordDto.getTel(),
      forgetPasswordDto.getIdCard(),
      forgetPasswordDto.getEmployeeCode()
    );

    if (changeForgetPassword != null && changeForgetPassword.getUserId() != null) {
      String hashedPassword = hashMD5(forgetPasswordDto.getNewPassword());
      UserDto emp = userRepository.findById(changeForgetPassword.getUserId()).get();
      emp.setPassword(hashedPassword);
      userRepository.save(emp);

      // update password_flag
      val findEmployee = employeeRepository.findById(changeForgetPassword.getId()).get();
      findEmployee.setPasswordFlag(true);
      employeeRepository.save(findEmployee);

      idEmp = changeForgetPassword.getId();
      resultStatus = "CHANGE_SUCCESS";
    } else {
      resultStatus = "CHANGE_ERROR";
    }
    return responseDataUtils.DataResourceJson(resultStatus, idEmp);
  }

  public static String hashMD5(String input) {
    return DigestUtils.md5Hex(input);
  }

  @Transactional
  public ResponseTextStatus resetPassword(ResetPasswordReq req) {
    val response = new ResponseTextStatus();

    val emp = employeeRepository.findById(req.getId()).get();
    emp.setPasswordFlag(true);

    val user = emp.getUser();
    String passwordOldEncrypt = hashMD5(req.getOldPassword());

    if (!user.getPassword().equals(passwordOldEncrypt) && user.getPassword() != null) {
      response.setStatusEmployee("password mismatch");
      return response;
    }

    String hashedPassword = hashMD5(req.getNewPassword());
    user.setPassword(hashedPassword);

    employeeRepository.save(emp);
    response.setStatusEmployee("success");

    return response;
  }

}
