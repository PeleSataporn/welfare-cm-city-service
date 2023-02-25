package com.cm.welfarecmcity.logic.register;

import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.logic.register.model.req.ApproveRegisterReq;
import com.cm.welfarecmcity.logic.register.model.req.CancelRegisterReq;
import com.cm.welfarecmcity.logic.register.model.req.RegisterReq;
import com.cm.welfarecmcity.logic.register.model.res.SearchNewRegisterRes;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/logic/v1/register")
public class RegisterController {

  @Autowired
  private RegisterService registerService;

  @PostMapping("/add-employee")
  public ResponseModel<ResponseData> addEmployee(@RequestBody RegisterReq req) {
    return registerService.addEmployee(req);
  }

  @PatchMapping("/approve-register")
  public ResponseModel<ResponseId> approveRegister(@RequestBody ApproveRegisterReq req) {
    return registerService.approveRegister(req);
  }

  @PostMapping("/search-register")
  public List<SearchNewRegisterRes> searchNewRegister() {
    return registerService.searchNewRegister();
  }

  @PostMapping("/count-register")
  public Integer countNewRegister() {
    return registerService.countNewRegister();
  }

  @PatchMapping("/cancel-approve-register")
  public ResponseModel<ResponseId> cancelApproveRegister(@RequestBody CancelRegisterReq req) {
    return registerService.cancelApproveRegister(req);
  }
  //  @PutMapping("/editStatusEmployeeResign")
  //  public ResponseModel<ResponseData> editStatusEmployeeResign(@RequestBody RegisterReq req) {
  //    try {
  //      return registerService.editStatusEmployeeResign(req);
  //    } catch (Exception e) {
  //      return null;
  //    }
  //  }
}
