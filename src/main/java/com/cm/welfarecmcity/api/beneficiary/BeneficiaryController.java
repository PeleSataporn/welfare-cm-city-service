package com.cm.welfarecmcity.api.beneficiary;

import com.cm.welfarecmcity.dto.BeneficiaryDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/beneficiary")
public class BeneficiaryController {

  @Autowired
  private BeneficiaryService beneficiaryService;

  @PostMapping
  public ResponseModel<ResponseId> add(@RequestBody BeneficiaryDto dto) {
    return beneficiaryService.add(dto);
  }

  @PatchMapping
  public ResponseModel<ResponseId> update(@RequestBody BeneficiaryDto dto) {
    return beneficiaryService.update(dto);
  }

  @GetMapping("{id}")
  public BeneficiaryDto getBeneficiary(@PathVariable Long id) {
    return beneficiaryService.getBeneficiary(id);
  }

  @DeleteMapping("{id}")
  public ResponseModel<ResponseId> delete(@PathVariable Long id) {
    return beneficiaryService.delete(id);
  }
}
