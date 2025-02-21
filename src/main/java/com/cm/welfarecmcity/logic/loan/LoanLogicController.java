package com.cm.welfarecmcity.logic.loan;

import com.cm.welfarecmcity.dto.base.RequestModel;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.dto.base.SearchDataResponse;
import com.cm.welfarecmcity.logic.document.model.EmployeeLoanNew;
import com.cm.welfarecmcity.logic.loan.model.*;
import com.cm.welfarecmcity.logic.loan.model.search.LoanByAdminOrderReqDto;
import com.cm.welfarecmcity.logic.loan.model.search.LoanByAdminReqDto;
import com.cm.welfarecmcity.utils.response.ResponseDataUtils;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logic/v1/loan")
public class LoanLogicController {

  @Autowired private LoanLogicService service;

  @PostMapping("search")
  public List<LoanRes> searchLoan(@RequestBody AddLoanDetailAllReq req) {
    return service.searchLoan(req);
  }

  @PostMapping("v2/search")
  public ResponseModel<SearchDataResponse<LoanRes>> searchLoanByAdmin(
      @RequestBody RequestModel<LoanByAdminReqDto, LoanByAdminOrderReqDto> req) {
    val res = service.searchLoanByAdmin(req);
    return ResponseDataUtils.fetchDataSuccess(res);
  }

  @GetMapping("guarantor/{id}")
  public GuarantorRes guarantor(@PathVariable Long id) {
    return service.guarantor(id);
  }

  @GetMapping("guarantee/{id}")
  public GuaranteeRes guarantee(@PathVariable Long id) {
    return service.guarantee(id);
  }

  @GetMapping("beneficiary/{id}")
  public List<BeneficiaryRes> beneficiary(@PathVariable Long id) {
    return service.beneficiary(id);
  }

  @GetMapping("beneficiary/search/{id}")
  public List<BeneficiaryRes> searchBeneficiary(@PathVariable Long id) {
    return service.searchBeneficiary(id);
  }

  @PatchMapping("beneficiary/update")
  public void update(@RequestBody List<BeneficiaryReq> req) {
    service.update(req);
  }

  @PostMapping("add-all")
  public void addAll(@RequestBody AddLoanDetailAllReq req) {
    service.add(req);
  }

  @GetMapping("close/{id}")
  public void closeLoan(@PathVariable Long id) {
    service.closeLoan(id);
  }

  @PostMapping("update-loan-emp-guarantor")
  public ResponseModel<ResponseId> updateLoanEmpOfGuarantor(@RequestBody EmployeeLoanNew req) {
    return service.updateLoanEmpOfGuarantor(req);
  }
}
