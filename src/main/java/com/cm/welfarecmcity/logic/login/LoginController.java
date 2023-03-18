package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.dto.ForgetPasswordDto;
import com.cm.welfarecmcity.dto.UserDto;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/logic/v1/login")
public class LoginController {

  @Autowired
  private LoginService loginService;

  @PostMapping("/user-login")
  public ResponseModel<Object> login(@RequestBody UserDto dto) {
    return loginService.login(dto);
  }

  @PostMapping("/change/forget-password")
  public ResponseModel<ResponseData> login(@RequestBody ForgetPasswordDto dto) {
    return loginService.changeForgetPassword(dto);
  }
}
