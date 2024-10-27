package com.cm.welfarecmcity.api.loandetailhistory;

import com.cm.welfarecmcity.logic.document.DocumentService;
import com.cm.welfarecmcity.logic.document.model.CalculateInstallments;
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
  public List<DocumentV1ResLoan> searchV1LoanHistory(String getMonthCurrent, String yearCurrent) {

    var resLoan =
        loanDetailHistoryLogicRepository.searchV1LoanHistory(getMonthCurrent, yearCurrent);
    resLoan.forEach(
        res -> {
          if (res.getGuarantor1() != null) {
            val empCode1 = documentService.searchIdOfEmpCode(Long.valueOf(res.getGuarantor1()));
            res.setGuarantorCode1(empCode1.getEmpCode());
          }
          if (res.getGuarantor2() != null) {
            val empCode2 = documentService.searchIdOfEmpCode(Long.valueOf(res.getGuarantor2()));
            res.setGuarantorCode2(empCode2.getEmpCode());
          }
          //    function --> calculateLoanOld()
          if (res.getLoanValue() != null) {
            if (!res.getNewLoan()) {
              CalculateReq req = new CalculateReq();
              req.setPrincipal(Double.parseDouble(res.getLoanValue()));
              req.setInterestRate(Double.parseDouble(res.getInterestPercent()));
              req.setNumOfPayments(Integer.parseInt(res.getLoanTime()));
              req.setPaymentStartDate(res.getStartLoanDate());

              List<CalculateInstallments> resList =
                  null; // ** function --> calculateLoanNew() , calculateLoanOld
              try {
                resList = documentService.calculateLoanOld(req);
              } catch (ParseException e) {
                throw new RuntimeException(e);
              }
              int sumTotalValueInterest = 0;
              int setTotalValuePrinciple = 0;
              int sumTotalValueInterestOfInstallment = 0;
              int setTotalValuePrincipleOfInstallment = 0;
              for (CalculateInstallments calculateInstallments : resList) {
                sumTotalValueInterest += calculateInstallments.getInterest();
                setTotalValuePrinciple += calculateInstallments.getTotalDeduction();
                if (Integer.parseInt(res.getInstallment())
                    == calculateInstallments.getInstallment()) {
                  res.setMonthInterest(String.valueOf(calculateInstallments.getInterest()));
                  res.setMonthPrinciple(String.valueOf(calculateInstallments.getTotalDeduction()));
                  // Interest
                  sumTotalValueInterestOfInstallment = sumTotalValueInterest;
                  res.setTotalValueInterest(String.valueOf(sumTotalValueInterestOfInstallment));
                  // Principle
                  res.setTotalValuePrinciple(String.valueOf(setTotalValuePrinciple));
                  res.setOutStandPrinciple(
                      String.valueOf(calculateInstallments.getPrincipalBalance()));
                }
                if (Integer.parseInt(res.getLoanTime()) == calculateInstallments.getInstallment()) {
                  res.setLastMonthInterest(
                      String.valueOf(res.getInterestLastMonth())); // resList.get(i).getInterest())
                  res.setLastMonthPrinciple(
                      String.valueOf(calculateInstallments.getTotalDeduction()));
                }
                res.setOutStandInterest(
                    String.valueOf(sumTotalValueInterest - sumTotalValueInterestOfInstallment));
              }

            } else {
              CalculateReq req = new CalculateReq();
              req.setPrincipal(Double.parseDouble(res.getLoanValue()));
              req.setInterestRate(Double.parseDouble(res.getInterestPercent()));
              req.setNumOfPayments(Integer.parseInt(res.getLoanTime()));
              req.setPaymentStartDate(res.getStartLoanDate()); // "2024-01-01"

              List<CalculateInstallments> resList =
                  null; // ** function --> calculateLoanNew() , calculateLoanOld()
              try {
                resList = documentService.calculateLoanNew(req);
              } catch (ParseException e) {
                throw new RuntimeException(e);
              }
              int sumTotalValueInterest = 0;
              int setTotalValuePrinciple = 0;
              int sumTotalValueInterestOfInstallment = 0;
              int setTotalValuePrincipleOfInstallment = 0;
              int sumOutStandInterest = 0;
              int setOutStandPrinciple = 0;
              for (CalculateInstallments calculateInstallments : resList) {
                sumOutStandInterest += calculateInstallments.getInterest();
                setOutStandPrinciple += calculateInstallments.getPrincipal();
                if (Integer.parseInt(res.getInstallment())
                    == calculateInstallments.getInstallment()) {
                  res.setMonthInterest(String.valueOf(calculateInstallments.getInterest()));
                  res.setMonthPrinciple(String.valueOf(calculateInstallments.getTotalDeduction()));
                }
                int installmentCurrent = Integer.parseInt(res.getInstallment()) - 1;
                if (calculateInstallments.getInstallment() <= installmentCurrent) {
                  sumTotalValueInterest += calculateInstallments.getInterest();
                  setTotalValuePrinciple += calculateInstallments.getPrincipal();
                  sumTotalValueInterestOfInstallment = sumTotalValueInterest;
                  setTotalValuePrincipleOfInstallment = setTotalValuePrinciple;
                  res.setTotalValueInterest(String.valueOf(sumTotalValueInterest));
                  res.setTotalValuePrinciple(String.valueOf(setTotalValuePrinciple));
                }
                if (Integer.parseInt(res.getLoanTime()) == calculateInstallments.getInstallment()) {
                  res.setLastMonthInterest(String.valueOf(calculateInstallments.getInterest()));
                  res.setLastMonthPrinciple(
                      String.valueOf(calculateInstallments.getTotalDeduction()));
                }
              }
              res.setOutStandInterest(
                  String.valueOf(sumOutStandInterest - sumTotalValueInterestOfInstallment));
              res.setOutStandPrinciple(
                  String.valueOf(setOutStandPrinciple - setTotalValuePrincipleOfInstallment));
            }
          }
        });
    return resLoan;
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
