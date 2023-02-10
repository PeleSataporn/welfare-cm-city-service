package com.cm.welfarecmcity.logic.register;

import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.logic.register.model.RegisterReq;

import com.sun.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/logic/v1/register")
public class RegisterController {

  @Autowired
  private RegisterService registerService;

  @PostMapping("/addEmployee")
  public ResponseModel<ResponseData> addEmployee(@RequestBody RegisterReq req) {
    try{
      return registerService.addEmployee(req);
    }catch (Exception e){
     return null;
    }
  }

  @PutMapping("/editStatusEmployeeResign")
  public ResponseModel<ResponseData> editStatusEmployeeResign(@RequestBody RegisterReq req) {
    try{
      return registerService.editStatusEmployeeResign(req);
    }catch (Exception e){
      return null;
    }
  }

}
