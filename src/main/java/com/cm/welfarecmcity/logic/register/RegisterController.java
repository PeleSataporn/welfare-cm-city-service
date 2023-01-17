package com.cm.welfarecmcity.logic.register;

import com.cm.welfarecmcity.logic.register.model.RegisterReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/logic/v1/register")
public class RegisterController {

  @Autowired
  private RegisterService registerService;

  @PostMapping
  public void register(@RequestBody RegisterReq req) {
    registerService.register(req);
  }
}
