package com.cm.welfarecmcity.logic.login;

import com.cm.welfarecmcity.dto.ForgetPasswordDto;
import com.cm.welfarecmcity.dto.UserDto;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/logic/v1/login")
public class LoginController {

  @Autowired
  private LoginService loginService;

  //  @PostMapping("/user-login")
  //  @POST
  //  @Path("/user-login")
  //  @Produces(MediaType.APPLICATION_JSON)
  //  @Consumes(MediaType.APPLICATION_JSON)

  //  @RequestMapping(value = "/user-login", produces = "application/json", method = RequestMethod.POST, consumes = "application/json")
  @PostMapping("/user-login")
  public ResponseEntity<ResponseModel<Object>> login(@RequestBody UserDto dto) {
    val response = loginService.login(dto);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/change/forget-password")
  public ResponseModel<ResponseData> login(@RequestBody ForgetPasswordDto dto) {
    return loginService.changeForgetPassword(dto);
  }
}
