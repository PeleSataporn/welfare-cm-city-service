package com.cm.welfarecmcity.api.beneficiary;

import com.cm.welfarecmcity.api.beneficiary.model.BeneficiaryRes;
import com.cm.welfarecmcity.dto.BeneficiaryDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.mapper.MapStructMapper;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryService {

  @Autowired private BeneficiaryRepository beneficiaryRepository;

  @Autowired private ResponseDataUtils responseDataUtils;

  @Autowired private MapStructMapper mapStructMapper;

  @Transactional
  public ResponseModel<ResponseId> add(BeneficiaryDto dto) {
    val beneficiary = beneficiaryRepository.save(dto);
    return responseDataUtils.insertDataSuccess(beneficiary.getId());
  }

  @Transactional
  public ResponseModel<ResponseId> update(BeneficiaryDto req) {
    if (req.getId() == 0L) {
      BeneficiaryDto beneficiaryDto = new BeneficiaryDto();

      beneficiaryDto.setCreateDate(req.getCreateDate());
      beneficiaryDto.setLastUpdate(req.getLastUpdate());
      beneficiaryDto.setDeleted(req.getDeleted());
      beneficiaryDto.setPrefix(req.getPrefix());
      beneficiaryDto.setFirstName(req.getFirstName());
      beneficiaryDto.setLastName(req.getLastName());
      beneficiaryDto.setGender(req.getGender());
      beneficiaryDto.setBirthday(req.getBirthday());
      beneficiaryDto.setRelationship(req.getRelationship());
      beneficiaryDto.setMarital(req.getMarital());
      beneficiaryDto.setLifeStatus(req.getLifeStatus());
      beneficiaryDto.setEmployee(req.getEmployee());

      return responseDataUtils.updateDataSuccess(
          beneficiaryRepository.save(beneficiaryDto).getId());
    } else {
      val findBeneficiary = beneficiaryRepository.findById(req.getId());
      if (findBeneficiary.isEmpty()) {
        throw new EmployeeException("Employee id not found");
      }

      return responseDataUtils.updateDataSuccess(beneficiaryRepository.save(req).getId());
    }
  }

  @Transactional
  public BeneficiaryRes getBeneficiary(Long id) {
    val findBeneficiary = beneficiaryRepository.findById(id);

    if (findBeneficiary.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    return mapStructMapper.beneficiaryToRes(findBeneficiary.get());
  }

  @Transactional
  public ResponseModel<ResponseId> delete(Long id) {
    val findBeneficiary = beneficiaryRepository.findById(id);
    if (findBeneficiary.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val beneficiary = findBeneficiary.get();
    beneficiaryRepository.delete(beneficiary);

    return responseDataUtils.deleteDataSuccess(id);
  }
}
