package com.cm.welfarecmcity.logic.loan;

import com.cm.welfarecmcity.api.beneficiary.BeneficiaryRepository;
import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.loan.LoanRepository;
import com.cm.welfarecmcity.api.loandetail.LoanDetailRepository;
import com.cm.welfarecmcity.dto.BeneficiaryDto;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.logic.document.DocumentRepository;
import com.cm.welfarecmcity.logic.document.DocumentService;
import com.cm.welfarecmcity.logic.document.model.*;
import com.cm.welfarecmcity.logic.loan.model.*;
import com.cm.welfarecmcity.logic.loan.model.GuaranteeRes;
import com.cm.welfarecmcity.logic.stock.model.AddStockDetailAllReq;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
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

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Transactional
  public List<LoanRes> searchLoan(AddLoanDetailAllReq req) {
    val result = repository.searchLoan(req);

    for(LoanRes list: result){
      Integer sum = 0;
      if(Integer.parseInt(list.getLoanYear()) >= 2567){
        if(list.getLoanBalance() > 0 && list.getInstallment() > 0){
          sum = ( list.getLoanBalanceDetail() + Math.round((list.getLoanOrdinary() - list.getInterestDetail())) );
        }else{
          sum = list.getLoanValue();
        }
        list.setLoanBalance(sum);
      }
    }

//    result.forEach(infoAll -> {
//
//      if(infoAll.getGuarantorOne() != null){
//        DocumentReq doc1 = documentRepository.getIdOfEmpCode(infoAll.getGuarantorOne());
//        infoAll.setGuarantorOneValue(doc1.getEmpCode());
//      }else{
//        infoAll.setGuarantorOneValue(null);
//      }
//
//      if(infoAll.getGuarantorTwo() != null){
//        DocumentReq doc2 = documentRepository.getIdOfEmpCode(infoAll.getGuarantorTwo());
//        infoAll.setGuarantorTwoValue(doc2.getEmpCode());
//      }else{
//        infoAll.setGuarantorTwoValue(null);
//      }
//
//    });
    return result;
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
        loanDetailDto.setInterestPercent(detail.getInterestPercent());
        loanDetailDto.setInterestLastMonth(detail.getInterestLastMonth());

        CalculateInstallments calLast = new CalculateInstallments();
        // set loan update
        val loanDto = loanRepository.findById(detail.getLoanId()).get();
        loanDetailDto.setLoan(loanDto);
        calculate.forEach(calculateInstallments -> {
          calLast.setInstallment(calculateInstallments.getInstallment());
          if (calculateInstallments.getInstallment() == installment) {
            loanDetailDto.setInterest(calculateInstallments.getInterest());
            loanDto.setLoanBalance(calculateInstallments.getPrincipalBalance());
            loanDto.setInterest(calculateInstallments.getInterest());
            loanDetailDto.setLoanBalance(calculateInstallments.getPrincipalBalance());
            loanDetailDto.setLoanOrdinary(calculateInstallments.getTotalDeduction());
          }
        });

        loanDetailRepository.save(loanDetailDto);
        loanRepository.save(loanDto);
        // close loan
        if(installment == (calLast.getInstallment() + 1)){
          closeLoan(detail.getLoanId());
        }
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

    val empLoan = repository.searchLoanOfEmployee(id);
    val emp1 = employeeRepository.findById(empLoan.getId()).get();
    emp1.setLoan(null);
    employeeRepository.save(emp1);

    val detailLoan = repository.searchLoanOfLoanDetail(id);
    for(LoanDetailDto list : detailLoan){
      val detail1 = loanDetailRepository.findById(list.getId()).get();
      detail1.setActive(false);
      loanDetailRepository.save(detail1);
    }

    loanRepository.save(loan);
  }

  @Transactional
  public ResponseModel<ResponseId> updateLoanEmpOfGuarantor(EmployeeLoanNew req){
    try {
      val findConfig = loanRepository.findById(req.getLoanId());
      if (findConfig.isEmpty()) {
        throw new EmployeeException("Loan not found");
      }
      val config = findConfig.get();
      if(req.getGuaranteeStockFlag()){
        config.setStockFlag(req.getGuaranteeStockFlag());
        config.setGuarantorOne(null);
        config.setGuarantorTwo(null);
      }else{
        config.setStockFlag(req.getGuaranteeStockFlag());
        if (req.getGuarantorOne() != null) {
          var result1 = documentRepository.getEmpCodeOfId(req.getGuarantorOne());
          val emp1 = employeeRepository.findById(result1.getEmpId()).get();
          config.setGuarantorOne(emp1);
          //loanDto.getGuarantorOne().setId(result1.getEmpId());
        }else{
          config.setGuarantorOne(null);
        }

        if(req.getGuarantorTwo() != null){
          var result2 = documentRepository.getEmpCodeOfId(req.getGuarantorTwo());
          val emp2 = employeeRepository.findById(result2.getEmpId()).get();
          config.setGuarantorTwo(emp2);
          //loanDto.getGuarantorTwo().setId(result2.getEmpId());
        }else{
          config.setGuarantorTwo(null);
        }
      }
      loanRepository.save(config);
      return responseDataUtils.insertDataSuccess(req.getLoanId());
    } catch (Exception e) {
      return null;
    }
  }

}
