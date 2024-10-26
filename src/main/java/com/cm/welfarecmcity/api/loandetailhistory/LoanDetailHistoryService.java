package com.cm.welfarecmcity.api.loandetailhistory;

import com.cm.welfarecmcity.logic.document.DocumentService;
import com.cm.welfarecmcity.logic.document.model.CalculateReq;
import com.cm.welfarecmcity.logic.document.model.DocumentV1ResLoan;
import com.cm.welfarecmcity.logic.document.model.DocumentV2ResLoan;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanDetailHistoryService {

  @Autowired private LoanDetailHistoryLogicRepository loanDetailHistoryLogicRepository;

  @Autowired private DocumentService documentService;

  @Transactional
  public List<DocumentV1ResLoan> searchV1LoanHistory(String getMonthCurrent, String yearCurrent)
      throws ParseException {

    var loanHistories =
        loanDetailHistoryLogicRepository.searchV1LoanHistory(getMonthCurrent, yearCurrent);

    for (val loanHistory : loanHistories) {
      if (loanHistory.getGuarantor1() != null) {
        val empCode1 = documentService.searchIdOfEmpCode(Long.valueOf(loanHistory.getGuarantor1()));
        loanHistory.setGuarantorCode1(empCode1.getEmpCode());
      }

      if (loanHistory.getGuarantor2() != null) {
        val empCode2 = documentService.searchIdOfEmpCode(Long.valueOf(loanHistory.getGuarantor2()));
        loanHistory.setGuarantorCode2(empCode2.getEmpCode());
      }

      if (loanHistory.getLoanValue() != null) {
        val calculate = setCalculateReq(loanHistory);
        val calculateInstallments = documentService.calculateLoanOld(calculate);
        int sumTotalValueInterest = 0;
        int setTotalValuePrinciple = 0;
        int sumTotalValueInterestOfInstallment = 0;
        if (!loanHistory.getNewLoan()) {

          for (val calculateInstallment : calculateInstallments) {
            sumTotalValueInterest += calculateInstallment.getInterest();
            setTotalValuePrinciple += calculateInstallment.getTotalDeduction();

            if (Integer.parseInt(loanHistory.getInstallment())
                == calculateInstallment.getInstallment()) {
              loanHistory.setMonthInterest(String.valueOf(calculateInstallment.getInterest()));
              loanHistory.setMonthPrinciple(
                  String.valueOf(calculateInstallment.getTotalDeduction()));

              // Interest
              sumTotalValueInterestOfInstallment = sumTotalValueInterest;
              loanHistory.setTotalValueInterest(String.valueOf(sumTotalValueInterestOfInstallment));

              // Principle
              loanHistory.setTotalValuePrinciple(String.valueOf(setTotalValuePrinciple));
              loanHistory.setOutStandPrinciple(
                  String.valueOf(calculateInstallment.getPrincipalBalance()));
            }
          }
        } else {

          int setTotalValuePrincipleOfInstallment = 0;
          int sumOutStandInterest = 0;
          int setOutStandPrinciple = 0;

          for (val calculateInstallment : calculateInstallments) {
            sumOutStandInterest += calculateInstallment.getInterest();
            setOutStandPrinciple += calculateInstallment.getPrincipal();

            if (Integer.parseInt(loanHistory.getInstallment())
                == calculateInstallment.getInstallment()) {
              loanHistory.setMonthInterest(String.valueOf(calculateInstallment.getInterest()));
              loanHistory.setMonthPrinciple(
                  String.valueOf(calculateInstallment.getTotalDeduction()));
            }

            int installmentCurrent = Integer.parseInt(loanHistory.getInstallment()) - 1;

            if (calculateInstallment.getInstallment() <= installmentCurrent) {
              sumTotalValueInterest += calculateInstallment.getInterest();
              setTotalValuePrinciple += calculateInstallment.getPrincipal();
              sumTotalValueInterestOfInstallment = sumTotalValueInterest;
              setTotalValuePrincipleOfInstallment = setTotalValuePrinciple;
              loanHistory.setTotalValueInterest(String.valueOf(sumTotalValueInterest));
              loanHistory.setTotalValuePrinciple(String.valueOf(setTotalValuePrinciple));
            }

            if (Integer.parseInt(loanHistory.getLoanTime())
                == calculateInstallment.getInstallment()) {
              loanHistory.setLastMonthInterest(String.valueOf(calculateInstallment.getInterest()));
              loanHistory.setLastMonthPrinciple(
                  String.valueOf(calculateInstallment.getTotalDeduction()));
            }
          }
          loanHistory.setOutStandInterest(
              String.valueOf(sumOutStandInterest - sumTotalValueInterestOfInstallment));
          loanHistory.setOutStandPrinciple(
              String.valueOf(setOutStandPrinciple - setTotalValuePrincipleOfInstallment));
        }
      }
    }
    return loanHistories;
  }

  @Transactional
  public CalculateReq setCalculateReq(DocumentV1ResLoan loanHistory) {
    val calculate = new CalculateReq();
    calculate.setPrincipal(Double.parseDouble(loanHistory.getLoanValue()));
    calculate.setInterestRate(Double.parseDouble(loanHistory.getInterestPercent()));
    calculate.setNumOfPayments(Integer.parseInt(loanHistory.getLoanTime()));
    calculate.setPaymentStartDate(loanHistory.getStartLoanDate());

    return calculate;
  }

  @Transactional
  public List<DocumentV2ResLoan> searchV2LoanHistory(String getMonthCurrent, String yearCurrent) {
    return loanDetailHistoryLogicRepository.searchV2LoanHistory(getMonthCurrent, yearCurrent);
  }
}
