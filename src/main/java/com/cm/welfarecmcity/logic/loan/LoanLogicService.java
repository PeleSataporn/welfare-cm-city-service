package com.cm.welfarecmcity.logic.loan;

import com.cm.welfarecmcity.api.beneficiary.BeneficiaryRepository;
import com.cm.welfarecmcity.api.loan.LoanRepository;
import com.cm.welfarecmcity.api.loandetail.LoanDetailRepository;
import com.cm.welfarecmcity.dto.BeneficiaryDto;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.logic.document.DocumentRepository;
import com.cm.welfarecmcity.logic.document.DocumentService;
import com.cm.welfarecmcity.logic.document.model.CalculateInstallments;
import com.cm.welfarecmcity.logic.document.model.CalculateReq;
import com.cm.welfarecmcity.logic.document.model.DocumentV1Res;
import com.cm.welfarecmcity.logic.loan.model.*;
import com.cm.welfarecmcity.logic.stock.model.AddStockDetailAllReq;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanLogicService {

  @Autowired
  private LoanLogicRepository repository;

  @Autowired
  private LoanRepository loanRepository;

  @Autowired
  private LoanDetailRepository loanDetailRepository;

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private BeneficiaryRepository beneficiaryRepository;

  @Autowired
  private DocumentService documentService;

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
      repository.update(beneficiaryReq.getId(), beneficiaryReq.getActive());
    }
  }

  @Transactional
  public void add(AddLoanDetailAllReq req) {
    val listLoanDetail = repository.getLoanDetailByMonth(req.getOldMonth(), req.getOldYear());
    listLoanDetail.forEach(detail -> {
      val calculateReq = new CalculateReq();
      calculateReq.setPrincipal(detail.getLoanValue());
      calculateReq.setInterestRate(detail.getInterestPercent());
      calculateReq.setNumOfPayments(detail.getLoanTime());

      try {
        List<CalculateInstallments> calculate = new ArrayList<>();
        if (detail.isNewLoan()) {
          calculateReq.setPaymentStartDate(detail.getStartLoanDate());
          calculate = documentService.calculateLoanNew(calculateReq);
        } else {
          calculateReq.setPaymentStartDate("2023-01-31");
          calculate = documentService.calculateLoanOld(calculateReq);
        }

        val installment = detail.getInstallment() + 1;

        // set loan detail dto
        val loanDetailDto = new LoanDetailDto();
        loanDetailDto.setInstallment(installment);
        loanDetailDto.setLoanMonth(req.getNewMonth());
        loanDetailDto.setLoanYear(req.getNewYear());
        loanDetailDto.setLoanOrdinary(detail.getLoanOrdinary());
        loanDetailDto.setInterestPercent(detail.getInterestPercent());
        loanDetailDto.setInterestLastMonth(detail.getInterestLastMonth());

        // set loan update
        val loanDto = loanRepository.findById(detail.getLoanId()).get();
        loanDetailDto.setLoan(loanDto);
        calculate.forEach(calculateInstallments -> {
          if (calculateInstallments.getInstallment() == installment) {
            loanDetailDto.setInterest(calculateInstallments.getInterest());
            loanDto.setLoanBalance(calculateInstallments.getPrincipalBalance());
            loanDto.setInterest(calculateInstallments.getInterest());
          }
        });

        loanDetailRepository.save(loanDetailDto);
        loanRepository.save(loanDto);
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Transactional
  public void closeLoan(Long id) {
    val loan = loanRepository.findById(id).get();
    loan.setLoanBalance(0);
    loan.setActive(false);
    //    loan.setDeleted(true);
    loan.setInterest(0);
    loan.setGuarantorOne(null);
    loan.setGuarantorTwo(null);

    loanRepository.save(loan);
  }
}
