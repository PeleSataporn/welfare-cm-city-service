package com.cm.welfarecmcity.logic.loan;

import com.cm.welfarecmcity.api.beneficiary.BeneficiaryRepository;
import com.cm.welfarecmcity.dto.BeneficiaryDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.logic.document.DocumentRepository;
import com.cm.welfarecmcity.logic.document.model.DocumentV1Res;
import com.cm.welfarecmcity.logic.loan.model.*;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanLogicService {

  @Autowired
  private LoanLogicRepository repository;

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private BeneficiaryRepository beneficiaryRepository;

  @Transactional
  public List<LoanRes> searchLoan() {
    return repository.searchLoan();
  }

  @Transactional
  public GuarantorRes guarantor(Long id) {
    return repository.guarantor(id);
  }

  @Transactional
  public GuaranteeRes guarantee(Long id) {
    val documentGuarantee = documentRepository.documentGuarantee(id);
    val res = new GuaranteeRes();

    for (int i = 0; i < documentGuarantee.size(); i++) {
      if (i == 0) {
        res.setGenderOne(documentGuarantee.get(i).getGender());
        res.setCodeGuaranteeOne(documentGuarantee.get(i).getCodeGuarantee());
        res.setFullNameGuaranteeOne(documentGuarantee.get(i).getFullNameGuarantee());
      } else {
        res.setGenderTwo(documentGuarantee.get(i).getGender());
        res.setCodeGuaranteeTwo(documentGuarantee.get(i).getCodeGuarantee());
        res.setFullNameGuaranteeTwo(documentGuarantee.get(i).getFullNameGuarantee());
      }
    }

    return res;
  }

  @Transactional
  public List<BeneficiaryRes> beneficiary(Long id) {
    return repository.beneficiary(id);
  }

  @Transactional
  public List<BeneficiaryRes> searchBeneficiary(Long id) {
    return repository.searchBeneficiary(id);
  }

  @Transactional
  public void update(List<BeneficiaryReq> req) {
    for (BeneficiaryReq beneficiaryReq : req) {
      repository.update(beneficiaryReq.getId(),beneficiaryReq.getActive());
    }
  }
}
