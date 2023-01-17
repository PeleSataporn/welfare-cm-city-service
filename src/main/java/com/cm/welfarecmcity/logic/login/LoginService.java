package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.dto.UserDto;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired
  private LoginRepository loginRepository;

  public void login(UserDto dto) {
    val user = loginRepository.checkUserLogin(dto.getUsername(), dto.getPassword());

    if (user == null) {
      throw new Error("Password is incorrect.");
    }
  }
}
