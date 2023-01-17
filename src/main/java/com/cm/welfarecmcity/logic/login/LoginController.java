package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.dto.UserDto;
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

  @PostMapping
  public void login(@RequestBody UserDto dto) {
    loginService.login(dto);
  }
}
