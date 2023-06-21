package com.cm.welfarecmcity.logic.document;

import com.cm.welfarecmcity.api.loandetail.LoanDetailLogicRepository;
import com.cm.welfarecmcity.api.loandetail.LoanDetailRepository;
import com.cm.welfarecmcity.api.stockdetail.StockDetailRepository;
import com.cm.welfarecmcity.logic.document.model.*;
import jakarta.transaction.Transactional;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private StockDetailRepository stockDetailRepository;

  @Autowired
  private LoanDetailRepository loanDetailRepository;

  @Autowired
  private LoanDetailLogicRepository loanDetailLogicRepository;

  @Transactional
  public List<DocumentV1Res> searchDocumentV1(Long stockId, String monthCurrent) {
    return documentRepository.documentInfoV1(stockId, monthCurrent);
  }

  @Transactional
  public List<DocumentV2Res> searchDocumentV2(Long stockId) {
    return documentRepository.documentInfoV2(stockId);
  }

  @Transactional
  public EmployeeLoanNew searchEmployeeLoanNew(Long empId) {
    try {
      return documentRepository.searchEmployeeLoanNew(empId);
    }catch (Exception e){
      return null;
    }

  }

  @Transactional
  public List<GuaranteeRes> searchGuarantorUnique(String empCode) {
    try {
      var result = documentRepository.getEmpCodeOfId(empCode);
      return documentRepository.documentGuarantee(result.getEmpId());
    }catch (Exception e){
      return null;
    }
  }

  @Transactional
  public GrandTotalRes grandTotal() {
    val res = documentRepository.grandTotal();

    val sumTotal = (res.getSumStockValue() + res.getSumLoanInterest() + res.getSumLoanOrdinary());
    res.setSumTotal(sumTotal);

    return res;
  }

  @Transactional
  public List<DocumentInfoAllRes> documentInfoAll() {
    List<DocumentInfoAllRes> listInfoAll = documentRepository.documentInfoAll();

    // guarantee
    listInfoAll.forEach(infoAll -> {
      val guarantee = documentRepository.documentGuarantee(infoAll.getId());

      guarantee
        .stream()
        .findFirst()
        .ifPresent(guaranteeOne -> {
          infoAll.setCodeGuaranteeOne(guaranteeOne.getCodeGuarantee());
          infoAll.setFullNameGuaranteeOne(guaranteeOne.getFullNameGuarantee());
        });

      guarantee
        .stream()
        .reduce((first, second) -> second)
        .ifPresent(guaranteeTwo -> {
          infoAll.setCodeGuaranteeTwo(guaranteeTwo.getCodeGuarantee());
          infoAll.setFullNameGuaranteeTwo(guaranteeTwo.getFullNameGuarantee());
        });

      stockDetailRepository
        .findAllByStock_Id(infoAll.getStockId(), null)
        .stream()
        .findFirst()
        .ifPresent(stockDetailDto -> infoAll.setInstallment(stockDetailDto.getInstallment()));

      // loan details
      if (infoAll.getLoanId() != null) {

        if(infoAll.getNewLoan() == false || infoAll.getNewLoan() == null){
          val calculateReq = new CalculateReq();
          calculateReq.setPrincipal(infoAll.getLoanValue());
          calculateReq.setInterestRate(infoAll.getInterestPercent());
          calculateReq.setNumOfPayments(Integer.parseInt(infoAll.getLoanTime()));
          calculateReq.setPaymentStartDate("2023-01-31");

          val loan = loanDetailLogicRepository.loanDetail(infoAll.getLoanId());
          loan
                  .stream()
                  .reduce((first, second) -> second)
                  .ifPresent(interest -> {
                    try {
                      val calculate = calculateLoanOld(calculateReq);

                      int sumTotalValueInterest = 0;
                      int setTotalValuePrinciple = 0;
                      int sumTotalValueInterestOfInstallment = 0;
                      int sumTotalValuePrincipleOfInstallment = 0;

                      for (CalculateInstallments calculation : calculate) {
                        sumTotalValueInterest += calculation.getInterest();
                        setTotalValuePrinciple += calculation.getTotalDeduction();
                        if (calculation.getInstallment() == interest.getInstallment()) {
                          infoAll.setInterestMonth(calculation.getInterest());
                          infoAll.setEarlyMonth(calculation.getTotalDeduction());
                          infoAll.setInstallmentLoan(calculation.getInstallment());

                          // Interest
                          sumTotalValueInterestOfInstallment = sumTotalValueInterest;
                          infoAll.setTotalValueInterest(sumTotalValueInterestOfInstallment);
                          // Principle
                          infoAll.setTotalValuePrinciple(setTotalValuePrinciple);
                          infoAll.setOutStandPrinciple(calculation.getPrincipalBalance());
                        }

                        if (calculation.getInstallment() == Integer.parseInt(infoAll.getLoanTime())) {
                          infoAll.setInterestMonthLast(Integer.parseInt(infoAll.getInterestLastMonth() != null ? infoAll.getInterestLastMonth() : null));
                          infoAll.setEarlyMonthLast(calculation.getTotalDeduction());
                        }
                      }
                      infoAll.setOutStandInterest(sumTotalValueInterest - sumTotalValueInterestOfInstallment);
                    } catch (ParseException e) {
                      throw new RuntimeException(e);
                    }

                  });
        }else{
          val calculateReq = new CalculateReq();
          calculateReq.setPrincipal(infoAll.getLoanValue());
          calculateReq.setInterestRate(infoAll.getInterestPercent());
          calculateReq.setNumOfPayments(Integer.parseInt(infoAll.getLoanTime()));
          calculateReq.setPaymentStartDate("2023-01-31");

          val loan = loanDetailLogicRepository.loanDetail(infoAll.getLoanId());
          loan
            .stream()
            .reduce((first, second) -> second)
            .ifPresent(interest -> {
              //            try {
              //              int sumTotalValueInterest = 0;
              //              int setTotalValuePrinciple = 0;
              //              int sumTotalValueInterestOfInstallment = 0;
              //              int setTotalValuePrincipleOfInstallment = 0;
              //
              //              val calculate = calculateLoan(calculateReq);
              //              calculate.forEach(it -> {
              //                if (it.getInstallment() == interest.getInstallment()) {
              //                  sumTotalValueInterest += it.getInterest();
              //                  setTotalValuePrinciple += it.getPrincipal();
              //
              //                  infoAll.setInterestMonth(it.getInterest());
              //                  infoAll.setEarlyMonth(it.getPrincipal());
              //                  infoAll.setInstallmentLoan(it.getInstallment());
              //
              //                  sumTotalValueInterestOfInstallment = sumTotalValueInterest;
              //                  setTotalValuePrincipleOfInstallment = setTotalValuePrinciple;
              //                  res.setTotalValueInterest(String.valueOf(sumTotalValueInterestOfInstallment));
              //                  res.setTotalValuePrinciple(String.valueOf(setTotalValuePrincipleOfInstallment));
              //                }
              //
              //                if (it.getInstallment() == Integer.parseInt(infoAll.getLoanTime())) {
              //                  infoAll.setInterestMonthLast(it.getInterest());
              //                  infoAll.setEarlyMonthLast(it.getPrincipal());
              //                }
              //              });
              //            } catch (ParseException e) {
              //              throw new RuntimeException(e);
              //            }

              try {
                val calculate = calculateLoanNew(calculateReq);

                int sumTotalValueInterest = 0;
                int setTotalValuePrinciple = 0;
                int sumTotalValueInterestOfInstallment = 0;
                int sumTotalValuePrincipleOfInstallment = 0;

                for (CalculateInstallments calculation : calculate) {
                  sumTotalValueInterest += calculation.getInterest();
                  setTotalValuePrinciple += calculation.getTotalDeduction();
                  if (calculation.getInstallment() == interest.getInstallment()) {
                    infoAll.setInterestMonth(calculation.getInterest());
                    infoAll.setEarlyMonth(calculation.getTotalDeduction());
                    infoAll.setInstallmentLoan(calculation.getInstallment());

                    sumTotalValueInterestOfInstallment = sumTotalValueInterest;
                    sumTotalValuePrincipleOfInstallment = setTotalValuePrinciple;
                    infoAll.setTotalValueInterest(sumTotalValueInterestOfInstallment);
                    infoAll.setTotalValuePrinciple(sumTotalValuePrincipleOfInstallment);
                  }

                  if (calculation.getInstallment() == Integer.parseInt(infoAll.getLoanTime())) {
                    infoAll.setInterestMonthLast(calculation.getInterest());
                    infoAll.setEarlyMonthLast(calculation.getTotalDeduction());
                  }
                }
                infoAll.setOutStandInterest(sumTotalValueInterest - sumTotalValueInterestOfInstallment);
                infoAll.setOutStandPrinciple(setTotalValuePrinciple - sumTotalValuePrincipleOfInstallment);
              } catch (ParseException e) {
                throw new RuntimeException(e);
              }

            });
        }
      }
    });

    return listInfoAll;
  }

  // loan
  @Transactional
  public List<DocumentV1ResLoan> searchDocumentV1Loan(Long loanId, String getMonthCurrent) {
    var resLoan = documentRepository.documentInfoV1Loan(loanId, getMonthCurrent);
    resLoan.forEach(res -> {
//    function --> calculateLoanOld()
      if (res.getLoanValue() != null) {
        if(res.getNewLoan() == false || res.getNewLoan() == null){
        CalculateReq req = new CalculateReq();
        req.setPrincipal(Integer.parseInt(res.getLoanValue()));
        req.setInterestRate(Double.parseDouble(res.getInterestPercent()));
        req.setNumOfPayments(Integer.parseInt(res.getLoanTime()));
        req.setPaymentStartDate("2023-01-31");
        try {
          List<CalculateInstallments> resList = calculateLoanOld(req);  // ** function --> calculateLoanNew() , calculateLoanOld
          int sumTotalValueInterest = 0;
          int setTotalValuePrinciple = 0;
          int sumTotalValueInterestOfInstallment = 0;
          int setTotalValuePrincipleOfInstallment = 0;
          for (int i = 0; i < resList.size(); i++) {
            sumTotalValueInterest += resList.get(i).getInterest();
            setTotalValuePrinciple += resList.get(i).getTotalDeduction();
            if (Integer.parseInt(res.getInstallment()) == resList.get(i).getInstallment()) {
              res.setMonthInterest(String.valueOf(resList.get(i).getInterest()));
              res.setMonthPrinciple(String.valueOf(resList.get(i).getTotalDeduction()));
              // Interest
              sumTotalValueInterestOfInstallment = sumTotalValueInterest;
              res.setTotalValueInterest(String.valueOf(sumTotalValueInterestOfInstallment));
              // Principle
              res.setTotalValuePrinciple(String.valueOf(setTotalValuePrinciple));
              res.setOutStandPrinciple(String.valueOf(resList.get(i).getPrincipalBalance()));
            }
            if (Integer.parseInt(res.getLoanTime()) == resList.get(i).getInstallment()) {
              res.setLastMonthInterest(String.valueOf(res.getInterestLastMonth())); //resList.get(i).getInterest())
              res.setLastMonthPrinciple(String.valueOf(resList.get(i).getTotalDeduction()));
            }
            res.setOutStandInterest(String.valueOf(sumTotalValueInterest - sumTotalValueInterestOfInstallment));
          }

        } catch (ParseException e) {
          e.printStackTrace();
        }
      }else{
//      function --> calculateLoanNew()
//        CalculateReq req = new CalculateReq();
//        req.setPrincipal(Integer.parseInt(res.getLoanValue()));
//        req.setInterestRate(Double.parseDouble(res.getInterestPercent()));
//        req.setNumOfPayments(Integer.parseInt(res.getLoanTime()));
//        req.setPaymentStartDate("2023-01-31");
//        try {
//          List<CalculateInstallments> resList = calculateLoanNew(req);  // ** function --> calculateLoanNew() , calculateLoanOld()
//          int sumTotalValueInterest = 0;
//          int setTotalValuePrinciple = 0;
//          int sumTotalValueInterestOfInstallment = 0;
//          int setTotalValuePrincipleOfInstallment = 0;
//          for (int i = 0; i < resList.size(); i++) {
//            sumTotalValueInterest += resList.get(i).getInterest();
//            setTotalValuePrinciple += resList.get(i).getPrincipal();
//            if (Integer.parseInt(res.getInstallment()) == resList.get(i).getInstallment()) {
//              res.setMonthInterest(String.valueOf(resList.get(i).getInterest()));
//              res.setMonthPrinciple(String.valueOf(resList.get(i).getTotalDeduction()));
//              sumTotalValueInterestOfInstallment = sumTotalValueInterest;
//              setTotalValuePrincipleOfInstallment = setTotalValuePrinciple;
//              res.setTotalValueInterest(String.valueOf(sumTotalValueInterestOfInstallment));
//              res.setTotalValuePrinciple(String.valueOf(setTotalValuePrincipleOfInstallment));
//            }
//            if (Integer.parseInt(res.getLoanTime()) == resList.get(i).getInstallment()) {
//              res.setLastMonthInterest(String.valueOf(resList.get(i).getInterest()));
//              res.setLastMonthPrinciple(String.valueOf(resList.get(i).getTotalDeduction()));
//            }
//          }
//          res.setOutStandInterest(String.valueOf(sumTotalValueInterest - sumTotalValueInterestOfInstallment));
//          res.setOutStandPrinciple(String.valueOf(setTotalValuePrinciple - setTotalValuePrincipleOfInstallment));
//        } catch (ParseException e) {
//          e.printStackTrace();
//        }
        }
      }
    });

    return resLoan;
  }

  @Transactional
  public List<DocumentV2ResLoan> searchDocumentV2Loan(Long loanId, String getMonthCurrent) {
    return documentRepository.documentInfoV2Loan(loanId, getMonthCurrent);
  }

  // calculate Loan

  // loan New
  @Transactional
  public List<CalculateInstallments> calculateLoanNew(CalculateReq req) throws ParseException {
    double principal = req.getPrincipal();
    double interestRate = req.getInterestRate();
    int numOfPayments = req.getNumOfPayments();

    String dateSt = req.getPaymentStartDate();
    LocalDate date = LocalDate.parse(dateSt);

    // calculate loan
    LocalDate paymentStartDate = LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
    double installment = calculateLoanInstallment(principal, interestRate, numOfPayments);
    List<CalculateInstallments> calculateInstallments = createAmortizationTableNew(
      principal,
      interestRate,
      numOfPayments,
      installment,
      paymentStartDate
    );

    return calculateInstallments;
  }

  public double calculateLoanInstallment(double principal, double interestRate, int numOfPayments) {
    double monthlyInterestRate = (interestRate / 100) / 12;
    double installment = (principal * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -numOfPayments));
    return installment;
  }

  public List<CalculateInstallments> createAmortizationTableNew(
    double principal,
    double interestRate,
    int numOfPayments,
    double installment,
    LocalDate paymentStartDate
  ) {
    List<CalculateInstallments> result = new ArrayList<>();
    DecimalFormat decimalFormat = new DecimalFormat("#");

    double remainingBalance = principal;
    double toralDeduction = principal;
    LocalDate paymentDate = paymentStartDate;
    LocalDate paymentDateDeduction = paymentStartDate;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter dateFormatterDay = DateTimeFormatter.ofPattern("dd");

    for (int i = 1; i <= numOfPayments; i++) {
      CalculateInstallments cal = new CalculateInstallments();
      LocalDate paymentDateShow = paymentStartDate.plusMonths(i - 1);

      // Balance
      YearMonth currentPaymentMonth = YearMonth.from(paymentDate);
      int daysInMonth = currentPaymentMonth.lengthOfMonth();
      double interest = Math.round((remainingBalance * (interestRate / 100) / 365) * daysInMonth);
      double principalPaid = Math.round(installment - interest);

      if (i == numOfPayments) {
        double principalPaidLast = remainingBalance;
        double installmentSumLastMonth = principalPaidLast - interest;
        //remainingBalance -= principalPaid;
        // paymentDate.format(dateFormatter)

        cal.setInstallment(i);
        cal.setBalanceLoan(Integer.parseInt(decimalFormat.format(principalPaidLast)));
        cal.setDeductionDate(paymentDateShow.format(dateFormatter));
        cal.setAmountDay(paymentDateShow.format(dateFormatterDay));
        cal.setInterest((int) interest);
        cal.setPrincipal(Integer.parseInt(decimalFormat.format(principalPaidLast)));
        cal.setPrincipalBalance(0);
        cal.setTotalDeduction(Integer.parseInt(decimalFormat.format(principalPaidLast)));
        result.add(cal);
      } else {
        remainingBalance -= principalPaid;
        if (i == 1) {
          cal.setInstallment(i);
          cal.setBalanceLoan(Integer.parseInt(decimalFormat.format(principal)));
          cal.setDeductionDate(paymentDateShow.format(dateFormatter));
          cal.setAmountDay(paymentDateShow.format(dateFormatterDay));
          cal.setInterest(Integer.parseInt(decimalFormat.format(interest)));
          cal.setPrincipal(Integer.parseInt(decimalFormat.format(principalPaid)));
          cal.setPrincipalBalance(Integer.parseInt(decimalFormat.format(remainingBalance)));
          cal.setTotalDeduction(Integer.parseInt(decimalFormat.format(installment)));
          result.add(cal);
        } else {
          // toralDeduction
          YearMonth currentPaymentMonthDeduction = YearMonth.from(paymentDateDeduction);
          int daysInMonthDeduction = currentPaymentMonthDeduction.lengthOfMonth();
          double interestDeduction = Math.round((toralDeduction * (interestRate / 100) / 365) * daysInMonthDeduction);
          double principalPaidDeduction = Math.round(installment - interestDeduction);
          toralDeduction -= principalPaidDeduction;
          paymentDateDeduction = paymentDateDeduction.plusMonths(1);

          cal.setInstallment(i);
          cal.setBalanceLoan(Integer.parseInt(decimalFormat.format(toralDeduction)));
          cal.setDeductionDate(paymentDateShow.format(dateFormatter));
          cal.setAmountDay(paymentDateShow.format(dateFormatterDay));
          cal.setInterest(Integer.parseInt(decimalFormat.format(interest)));
          cal.setPrincipal(Integer.parseInt(decimalFormat.format(principalPaid)));
          cal.setPrincipalBalance(Integer.parseInt(decimalFormat.format(remainingBalance)));
          cal.setTotalDeduction(Integer.parseInt(decimalFormat.format(installment)));
          result.add(cal);
        }
      }
      paymentDate = paymentDate.plusMonths(1);
    }

    return result;
  }

  // lan old

  @Transactional
  public List<CalculateInstallments> calculateLoanOld(CalculateReq req) throws ParseException {
    double principal = req.getPrincipal();
    double interestRate = req.getInterestRate();
    int numOfPayments = req.getNumOfPayments();

    String dateSt = req.getPaymentStartDate();
    LocalDate date = LocalDate.parse(dateSt);

    // calculate loan
    LocalDate paymentStartDate = LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());

    double installment = calculateLoanInstallment(principal, interestRate, numOfPayments);
    List<CalculateInstallments> calculateInstallments = createAmortizationTableOld(
            principal,
            interestRate,
            numOfPayments
    );

    return calculateInstallments;
  }

  public List<CalculateInstallments> createAmortizationTableOld(
          double principal,
          double interestRates,
          int numOfPayments
  ) {
    List<CalculateInstallments> result = new ArrayList<>();
    DecimalFormat decimalFormat = new DecimalFormat("#");

    double loanAmount = principal; // Replace with the actual loan amount
    double interestRate = interestRates; // Replace with the actual interest rate
    int loanTermInMonths = numOfPayments; // Replace with the actual loan term in months

    double monthlyInterestRate = interestRate / 12;
    double monthlyPayment = loanAmount / loanTermInMonths;

    for (int i = 1; i <= loanTermInMonths; i++) {
      CalculateInstallments cal = new CalculateInstallments();
      double interestPayment = Math.round((loanAmount * (interestRate / 100)) / 12);
      double principalPayment = Math.round(monthlyPayment - interestPayment);

      if (i == loanTermInMonths) {
        double principalPaidLast = loanAmount;
        cal.setInstallment(i);
        cal.setBalanceLoan(0);
        cal.setDeductionDate("");
        cal.setAmountDay("");
        cal.setInterest((int) interestPayment);
        cal.setPrincipal(Integer.parseInt(decimalFormat.format(principalPaidLast)));
        cal.setPrincipalBalance(0);
        cal.setTotalDeduction(Integer.parseInt(decimalFormat.format(principalPaidLast)));
        result.add(cal);
      } else {
        loanAmount = Math.round(loanAmount - monthlyPayment);
        cal.setInstallment(i);
        cal.setBalanceLoan(0);
        cal.setDeductionDate("");
        cal.setAmountDay("");
        cal.setInterest(Integer.parseInt(decimalFormat.format(interestPayment)));
        cal.setPrincipal(Integer.parseInt(decimalFormat.format(principalPayment)));
        cal.setPrincipalBalance(Integer.parseInt(decimalFormat.format(loanAmount)));
        cal.setTotalDeduction(Integer.parseInt(decimalFormat.format(monthlyPayment)));
        result.add(cal);
      }
    }

    return result;
  }

}
