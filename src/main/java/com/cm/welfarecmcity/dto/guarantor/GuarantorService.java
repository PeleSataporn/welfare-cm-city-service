package com.cm.welfarecmcity.dto.guarantor;

import com.cm.welfarecmcity.dto.GuarantorDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuarantorService {

  @Autowired private GuarantorRepository guarantorRepository;

  @Autowired private ResponseDataUtils responseDataUtils;

  public ResponseModel<ResponseId> add(GuarantorDto dto) {
    val guarantor = guarantorRepository.save(dto);
    return responseDataUtils.insertDataSuccess(guarantor.getId());
  }
}
