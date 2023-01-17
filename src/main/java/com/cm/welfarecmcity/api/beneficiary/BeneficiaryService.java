package com.cm.welfarecmcity.api.beneficiary;

import com.cm.welfarecmcity.dto.BeneficiaryDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private ResponseDataUtils responseDataUtils;

    public ResponseModel<ResponseId> add(BeneficiaryDto dto) {
        val beneficiary = beneficiaryRepository.save(dto);
        return responseDataUtils.insertDataSuccess(beneficiary.getId());
    }
}
