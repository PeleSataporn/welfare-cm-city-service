package com.cm.welfarecmcity.api.loandetailhistory;

import com.cm.welfarecmcity.logic.document.DocumentService;
import com.cm.welfarecmcity.logic.document.model.*;
import com.cm.welfarecmcity.utils.DateUtils;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanDetailHistoryService {

  @Autowired private LoanDetailHistoryLogicRepository loanDetailHistoryLogicRepository;

  @Autowired private DocumentService documentService;

  @Transactional
  public List<DocumentV1ResLoan> searchV1LoanHistory(String getMonthCurrent, String yearCurrent) {

    val result = new ArrayList<DocumentV1ResLoan>();
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

          //          if (res.getLoanActive()) {
          //            result.add(res);
          //          } else if (!res.getLoanActive()
          //              && Objects.equals(res.getInstallment(), res.getLoanTime())) {
          //            result.add(res);
          //          }

          if (isLoanClosedInYear(res.getCloseLoanDate(), yearCurrent, getMonthCurrent)) {
            result.add(res);
          }
        });

    return result;
  }

  public boolean isLoanClosedInYear(Date closeLoanDate, String yearCurrent, String monthCurrent) {
    if (closeLoanDate == null) {
      return true; // Handle null safely
    }

    // Convert Date to LocalDate
    LocalDate localDate = closeLoanDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    // Extract the year and check the condition
    if ((localDate.getYear() + 543) >= Integer.parseInt(yearCurrent)) {
      int monthFM = DateUtils.getThaiMonthIntOfValue(monthCurrent);
      if (localDate.getMonthValue() > monthFM) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
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
  public List<DocumentV2ResLoanV2> searchV2LoanHistory(String getMonthCurrent, String yearCurrent) {
    // Step 1: Fetch loan data
    val result = new ArrayList<DocumentV1ResLoan>();
//    var resLoan =
//        loanDetailHistoryLogicRepository.searchV2LoanHistory(getMonthCurrent, yearCurrent);

    var resLoan =
            loanDetailHistoryLogicRepository.searchV1LoanHistory(getMonthCurrent, yearCurrent);

    // Step 2: Filter and collect the valid results
    resLoan.forEach(
        res -> {
          //          if (res.getLoanActive()
          //              || (!res.getLoanActive()
          //                  && Objects.equals(res.getInstallment(), res.getLoanTime()))) {
          //            result.add(res);
          //          }

          if (isLoanClosedInYear(res.getCloseLoanDate(), yearCurrent, getMonthCurrent)) {
            result.add(res);
          }
        });

    // Step 3: Group by departmentName and sum loanValueTotal
    Map<String, Double> groupedData =
        result.stream()
            .collect(
                Collectors.groupingBy(
                        DocumentV1ResLoan::getDepartmentName,
                    Collectors.summingDouble(
                        res ->
                            Double.parseDouble(
                                res.getLoanValue() != null
                                    ? String.valueOf(res.getLoanValue())
                                    : "0"))));

    // Step 4: Convert grouped data to a list of DocumentV2ResLoanV2
    return groupedData.entrySet().stream()
        .map(
            entry -> {
              DocumentV2ResLoanV2 dto = new DocumentV2ResLoanV2();
              dto.setDepartmentName(entry.getKey());
              dto.setLoanValueTotal(String.valueOf(entry.getValue()));
              return dto;
            })
        .collect(Collectors.toList());
  }
}
