package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.dto.UserDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  public ResponseModel<ResponseId> login(UserDto dto) {
    val user = loginRepository.checkUserLogin(dto.getUsername(), dto.getPassword());

    if (user == null) {
      //throw new Error("Password is incorrect...");
      return responseDataUtils.insertDataSuccess(null);
    }else{
      return responseDataUtils.insertDataSuccess(user.getId());
    }
  }

}
