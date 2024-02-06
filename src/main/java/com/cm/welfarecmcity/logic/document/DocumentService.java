package com.cm.welfarecmcity.logic.document;

import com.cm.welfarecmcity.api.loandetail.LoanDetailLogicRepository;
import com.cm.welfarecmcity.api.loandetail.LoanDetailRepository;
import com.cm.welfarecmcity.api.stockdetail.StockDetailLoginRepository;
import com.cm.welfarecmcity.api.stockdetail.StockDetailRepository;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.logic.document.model.*;
import jakarta.transaction.Transactional;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class DocumentService {

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private StockDetailRepository stockDetailRepository;

  @Autowired
  private StockDetailLoginRepository stockDetailLoginRepository;

  @Autowired
  private LoanDetailRepository loanDetailRepository;

  @Autowired
  private LoanDetailLogicRepository loanDetailLogicRepository;

  @Transactional
  public List<DocumentV1Res> searchDocumentV1(Long empId, String monthCurrent, String yearCurrent) {
    val res1 = documentRepository.documentInfoV1stock(empId, monthCurrent, yearCurrent);

    for (int i = 0; i < res1.size(); i++) {
      var res2 = new ArrayList<DocumentLoanV1Res>();
      if (monthCurrent != null) {
        res2 = (ArrayList<DocumentLoanV1Res>) documentRepository.documentInfoV1loan(res1.get(i).getEmpId(), monthCurrent, yearCurrent);

        if (res2.size() > 0) {
          res1.get(i).setLoanOrdinary(res2.get(0).getLoanOrdinary());
          res1.get(i).setLoanInstallment(res2.get(0).getLoanInstallment());
          res1.get(i).setInterest(res2.get(0).getInterest());
          res1.get(i).setLoanTime(res2.get(0).getLoanTime());
        }
      } else {
        res2 = (ArrayList<DocumentLoanV1Res>) documentRepository.documentInfoV1loan(empId, null, yearCurrent);

        if (res2.size() > 0) {
          res1.get(i).setLoanOrdinary(res2.get(i).getLoanOrdinary());
          res1.get(i).setLoanInstallment(res2.get(i).getLoanInstallment());
          res1.get(i).setInterest(res2.get(i).getInterest());
          res1.get(i).setLoanTime(res2.get(i).getLoanTime());
        }
      }

      // sum
      if (res1.get(i).getLoanOrdinary() != null && res1.get(i).getInterest() != null) {
        val stockValue = Integer.parseInt(res1.get(i).getStockValue());
        val loanOrdinary = Integer.parseInt(res1.get(i).getLoanOrdinary());
        val interest = Integer.parseInt(res1.get(i).getInterest());
        int sum = 0;
        if(res1.get(i).getLoanTime().equals(res1.get(i).getLoanInstallment())){
          sum = stockValue + (loanOrdinary + interest);
        }else{
          sum = stockValue + (loanOrdinary - interest) + interest;
        }
        res1.get(i).setSumMonth(String.valueOf(sum));
      } else if (res1.get(i).getLoanOrdinary() != null && res1.get(i).getInterest() == null) {
        val stockValue = Integer.parseInt(res1.get(i).getStockValue());
        val loanOrdinary = Integer.parseInt(res1.get(i).getLoanOrdinary());
        val sum = stockValue + loanOrdinary;

        res1.get(i).setSumMonth(String.valueOf(sum));
      } else if (res1.get(i).getLoanOrdinary() == null && res1.get(i).getInterest() != null) {
        val stockValue = Integer.parseInt(res1.get(i).getStockValue());
        val loanOrdinary = Integer.parseInt(res1.get(i).getLoanOrdinary());
        val sum = stockValue + loanOrdinary;

        res1.get(i).setSumMonth(String.valueOf(sum));
      } else {
        res1.get(i).setSumMonth(res1.get(i).getStockValue());
      }
    }

    return res1;
  }

  @Transactional
  public List<DocumentV2Res> searchDocumentV2(Long empId, String monthCurrent, String yearCurrent) {
    val documentV1 = searchDocumentV1(empId, monthCurrent, yearCurrent);

    if (monthCurrent != null) {
      val list = new ArrayList<DocumentV2Res>();

      for (DocumentV1Res documentV1Res : documentV1) {
        var loanDetailInterestTotal = 0;
        var loanDetailOrdinaryTotal = 0;
        var stockValueTotal = 0;
        var stockAccumulateTotal = 0;
        var totalMonth = 0;
        var loanInterest = 0;
        var loanOrdinary = 0;

        val documentV2 = new DocumentV2Res();
        documentV2.setDepartmentName(documentV1Res.getDepartmentName());

        if (documentV1Res.getInterest() != null) {
          loanInterest = Integer.parseInt(documentV1Res.getInterest());
        }

        if (documentV1Res.getLoanOrdinary() != null) {
          loanOrdinary = Integer.parseInt(documentV1Res.getLoanOrdinary());
        }

        loanDetailInterestTotal += loanInterest;
        loanDetailOrdinaryTotal += loanOrdinary;
        stockValueTotal += Integer.parseInt(documentV1Res.getStockValue() != null ? documentV1Res.getStockValue() : "0");
        stockAccumulateTotal += Integer.parseInt(documentV1Res.getStockAccumulate() != null ? documentV1Res.getStockAccumulate() : "0");
        totalMonth += Integer.parseInt(documentV1Res.getSumMonth() != null ? documentV1Res.getSumMonth() : "0");

        documentV2.setLoanDetailInterestTotal(String.valueOf(loanDetailInterestTotal));
        documentV2.setLoanDetailOrdinaryTotal(String.valueOf(loanDetailOrdinaryTotal));
        documentV2.setStockValueTotal(String.valueOf(stockValueTotal));
        documentV2.setStockAccumulateTotal(String.valueOf(stockAccumulateTotal));
        documentV2.setTotalMonth(String.valueOf(totalMonth));

        list.add(documentV2);
      }

      return list;
    } else {
      val list = new ArrayList<DocumentV2Res>();
      var loanDetailInterestTotal = 0;
      var loanDetailOrdinaryTotal = 0;
      var stockValueTotal = 0;
      var stockAccumulateTotal = 0;
      var totalMonth = 0;

      var loanInterest = 0;
      var loanOrdinary = 0;
      val documentV2 = new DocumentV2Res();

      for (DocumentV1Res documentV1Res : documentV1) {
        documentV2.setDepartmentName(documentV1Res.getDepartmentName());

        if (documentV1Res.getInterest() != null) {
          loanInterest = Integer.parseInt(documentV1Res.getInterest());
        }

        if (documentV1Res.getLoanOrdinary() != null) {
          loanOrdinary = Integer.parseInt(documentV1Res.getLoanOrdinary());
        }

        loanDetailInterestTotal += loanInterest;
        loanDetailOrdinaryTotal += loanOrdinary;
        stockValueTotal += Integer.parseInt(documentV1Res.getStockValue());
        stockAccumulateTotal += Integer.parseInt(documentV1Res.getStockAccumulate());
        totalMonth += Integer.parseInt(documentV1Res.getSumMonth());
      }

      documentV2.setLoanDetailInterestTotal(String.valueOf(loanDetailInterestTotal));
      documentV2.setLoanDetailOrdinaryTotal(String.valueOf(loanDetailOrdinaryTotal));
      documentV2.setStockValueTotal(String.valueOf(stockValueTotal));
      documentV2.setStockAccumulateTotal(String.valueOf(stockAccumulateTotal));
      documentV2.setTotalMonth(String.valueOf(totalMonth));

      list.add(documentV2);

      return list;
    }
  }

  @Transactional
  public EmployeeLoanNew searchEmployeeLoanNew(DocumentReq req) {
    try {
      val empFullData = documentRepository.getEmpFullData(req.getEmpCode());
      req.setStockId(empFullData.getStockId());
      req.setLoanId(empFullData.getLoanId());
      if (req.getLoanId() != null) {
        val loadDetail = documentRepository.searchEmployeeLoanOfNull(req);
        if (loadDetail.size() > 0) {
          req.setLoanId(empFullData.getLoanId());
        } else {
          req.setLoanId(null);
        }
      } else {
        req.setLoanId(null);
      }
      val employeeLoanNew = documentRepository.searchEmployeeLoanNew(req); //searchEmployeeLoanNewOfNull
      return employeeLoanNew;
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public List<GuaranteeRes> searchGuarantorUnique(String empCode) {
    try {
      var result = documentRepository.getEmpCodeOfId(empCode);
      return documentRepository.documentGuarantee(result.getEmpId());
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public DocumentReq searchEmpCodeOfId(String empCode) {
    try {
      return documentRepository.getEmpCodeOfId(empCode);
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public DocumentReq searchIdOfEmpCode(Long empCode) {
    try {
      return documentRepository.getIdOfEmpCode(empCode);
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public GrandTotalRes grandTotal(DocumentReq req) {
    //val res = documentRepository.grandTotal(req.getYearCurrent(), req.getMonthCurrent());
    GrandTotalRes res = new GrandTotalRes();
    val resEmp = documentRepository.documentInfoSumEmp();
    res.setSumEmp(resEmp.getSumEmp());
    val resLoan = documentRepository.documentInfoSumLoanEmp();
    res.setSumLoan(resLoan.getSumLoan());
    //    val resLoanBalance = documentRepository.getSumLoanBalance(req.getYearCurrent(), req.getMonthCurrent());
    //    res.setSumLoanBalance(resLoanBalance.getSumLoanBalance());
    val listLoanBalance = documentRepository.getSumLoanBalanceList(req.getYearCurrent(), req.getMonthCurrent());
    if (listLoanBalance.size() > 0) {
      calculateSumLoanBalance(listLoanBalance, res);
    } else {
      res.setSumLoanBalance(0L);
    }
    val resStockAccumulate = documentRepository.getSumStockAccumulate(req.getYearCurrent(), req.getMonthCurrent());
    res.setSumStockAccumulate(resStockAccumulate.getSumStockAccumulate() != null ? resStockAccumulate.getSumStockAccumulate() : 0);

    val resStockValue = documentRepository.getSumStockValue(req.getYearCurrent(), req.getMonthCurrent());
    res.setSumStockValue(resStockValue.getSumStockValue() != null ? resStockValue.getSumStockValue() : 0);
    val resLoanInterest = documentRepository.getSumLoanInterest(req.getYearCurrent(), req.getMonthCurrent());
    res.setSumLoanInterest(resLoanInterest.getSumLoanInterest() != null ? resLoanInterest.getSumLoanInterest() : 0);
    //    val resLoanOrdinary = documentRepository.getSumLoanOrdinary(req.getYearCurrent(), req.getMonthCurrent());
    //    res.setSumLoanOrdinary(resLoanOrdinary.getSumLoanOrdinary());
    val listLoanOrdinary = documentRepository.getSumLoanOrdinaryList(req.getYearCurrent(), req.getMonthCurrent());
    if (listLoanOrdinary.size() > 0) {
      calculateSumLoanOrdinary(listLoanOrdinary, res);
    } else {
      res.setSumLoanOrdinary(0L);
    }
    val sumTotal = (res.getSumStockValue() + res.getSumLoanInterest() + res.getSumLoanOrdinary());
    res.setSumTotal(sumTotal);

    return res;
  }

  public void calculateSumLoanBalance(List<LoanDetailDto> loanBalanceList, GrandTotalRes res) {
    if (loanBalanceList.size() > 0) {
      double sumLoanBalance = 0;
      for (LoanDetailDto list : loanBalanceList) {
        if (list.getInstallment() > 0) {
          if (list.getLoanBalance() > 0) {
            sumLoanBalance = sumLoanBalance + (list.getLoanBalance() + Math.round((list.getLoanOrdinary() - list.getInterest())));
          } else {
            sumLoanBalance = sumLoanBalance + list.getLoanOrdinary();
          }
        } else {
          sumLoanBalance = sumLoanBalance + 0;
        }
      }
      res.setSumLoanBalance((long) sumLoanBalance);
    }
  }

  public void calculateSumLoanOrdinary(List<LoanDetailDto> loanBalanceList, GrandTotalRes res) {
    if (loanBalanceList.size() > 0) {
      double sumLoanBalance = 0;
      //      double sumTotalLoanOrdinary = 0;
      for (LoanDetailDto list : loanBalanceList) {
        //        double sumLoanBalanceBox = 0;
        if (list.getInstallment() > 0) {
          if (list.getLoanBalance() > 0) {
            sumLoanBalance = sumLoanBalance + Math.round((list.getLoanOrdinary() - list.getInterest()));
          } else {
            sumLoanBalance = sumLoanBalance + list.getLoanOrdinary();
          }
        } else {
          sumLoanBalance = sumLoanBalance + 0;
        }
        //        sumLoanBalanceBox = list.getLoanOrdinary() - list.getInterest();
        //        sumTotalLoanOrdinary += sumLoanBalanceBox;
      }
      res.setSumLoanOrdinary((long) sumLoanBalance);
    }
  }

  @Transactional
  public List<DocumentInfoAllRes> documentInfoAll(DocumentReq req) {
    List<DocumentInfoAllRes> listInfoAll = documentRepository.documentInfoAll(req.getMonthCurrent(), req.getYearCurrent());

    // guarantee
    listInfoAll.forEach(infoAll -> {
      val guarantee = documentRepository.documentGuarantee(infoAll.getId());

      if (guarantee.size() > 1) {
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
      } else {
        guarantee
          .stream()
          .findFirst()
          .ifPresent(guaranteeOne -> {
            infoAll.setCodeGuaranteeOne(guaranteeOne.getCodeGuarantee());
            infoAll.setFullNameGuaranteeOne(guaranteeOne.getFullNameGuarantee());
          });
      }

      DocumentReq reqStock = new DocumentReq();
      reqStock.setStockId(infoAll.getStockId());
      reqStock.setMonthCurrent(req.getMonthCurrent());
      reqStock.setYearCurrent(req.getYearCurrent());
      stockDetailLoginRepository
        .documentInfoV3StockDetail(reqStock)
        .stream()
        .findFirst()
        .ifPresent(stockDetailDto -> infoAll.setInstallment(stockDetailDto.getInstallment()));

      // loan details
      if (infoAll.getLoanId() != null) {
        if (infoAll.getNewLoan() == false || infoAll.getNewLoan() == null) {
          val calculateReq = new CalculateReq();
          calculateReq.setPrincipal(infoAll.getLoanValue());
          calculateReq.setInterestRate(infoAll.getInterestPercent());
          calculateReq.setNumOfPayments(Integer.parseInt(infoAll.getLoanTime()));
          calculateReq.setPaymentStartDate("2023-01-31");

          val loan = loanDetailLogicRepository.loanDetail(infoAll.getLoanId(), req.getMonthCurrent(), req.getYearCurrent());
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
                    infoAll.setInterestMonthLast(
                      Integer.parseInt(infoAll.getInterestLastMonth() != null ? infoAll.getInterestLastMonth() : null)
                    );
                    infoAll.setEarlyMonthLast(calculation.getTotalDeduction());
                  }
                }
                infoAll.setOutStandInterest(sumTotalValueInterest - sumTotalValueInterestOfInstallment);
              } catch (ParseException e) {
                throw new RuntimeException(e);
              }
            });
        } else {
          val calculateReq = new CalculateReq();
          calculateReq.setPrincipal(infoAll.getLoanValue());
          calculateReq.setInterestRate(infoAll.getInterestPercent());
          calculateReq.setNumOfPayments(Integer.parseInt(infoAll.getLoanTime()));
          calculateReq.setPaymentStartDate(infoAll.getStartLoanDate());

          val loan = loanDetailLogicRepository.loanDetail(infoAll.getLoanId(), req.getMonthCurrent(), req.getYearCurrent());
          loan
            .stream()
            .reduce((first, second) -> second)
            .ifPresent(interest -> {
              try {
                val calculate = calculateLoanNew(calculateReq);

                int sumTotalValueInterest = 0;
                int setTotalValuePrinciple = 0;
                int sumTotalValueInterestOfInstallment = 0;
                int sumTotalValuePrincipleOfInstallment = 0;

                for (CalculateInstallments calculation : calculate) {
                  sumTotalValueInterest += calculation.getInterest();
                  setTotalValuePrinciple += calculation.getPrincipal(); //getTotalDeduction
                  if (calculation.getInstallment() == interest.getInstallment()) {
                    infoAll.setInterestMonth(calculation.getInterest());
                    infoAll.setEarlyMonth(calculation.getTotalDeduction());
                    infoAll.setInstallmentLoan(calculation.getInstallment());

                    sumTotalValueInterestOfInstallment = sumTotalValueInterest;
                    sumTotalValuePrincipleOfInstallment = setTotalValuePrinciple;
                    infoAll.setTotalValueInterest(sumTotalValueInterestOfInstallment);
                    infoAll.setTotalValuePrinciple(sumTotalValuePrincipleOfInstallment);
                    infoAll.setOutStandPrinciple(calculation.getPrincipalBalance()); // getBalanceLoan
                  }

                  if (calculation.getInstallment() == Integer.parseInt(infoAll.getLoanTime())) {
                    infoAll.setInterestMonthLast(calculation.getInterest());
                    infoAll.setEarlyMonthLast(calculation.getTotalDeduction());
                  }
                }
                infoAll.setOutStandInterest(sumTotalValueInterest); //sumTotalValueInterest - sumTotalValueInterestOfInstallment,
                //infoAll.setOutStandPrinciple(setTotalValuePrinciple);  //setTotalValuePrinciple - sumTotalValuePrincipleOfInstallment,
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
  public List<DocumentV1ResLoan> searchDocumentV1Loan(Long loanId, String getMonthCurrent, Boolean admin, Long empId, String yearCurrent) {
    if (!admin) {
      return null;
    } else {
      String testHistory = "";
      testHistory = String.valueOf(loanId);
      val loanHistory = loanDetailLogicRepository.loanHistory(empId);
      for (int i = 0; i < loanHistory.size(); i++) {
        testHistory = testHistory + ',' + loanHistory.get(i).getLoanId();
      }
      var resLoan = documentRepository.documentInfoV1Loan(loanId, getMonthCurrent, testHistory, yearCurrent);
      resLoan.forEach(res -> {
        if (res.getGuarantor1() != null) {
          val empCode1 = searchIdOfEmpCode(Long.valueOf(res.getGuarantor1()));
          res.setGuarantorCode1(empCode1.getEmpCode());
        }
        if (res.getGuarantor2() != null) {
          val empCode2 = searchIdOfEmpCode(Long.valueOf(res.getGuarantor2()));
          res.setGuarantorCode2(empCode2.getEmpCode());
        }
        //    function --> calculateLoanOld()
        if (res.getLoanValue() != null) {
          if (!res.getNewLoan() || res.getNewLoan() == null) {
            CalculateReq req = new CalculateReq();
            req.setPrincipal(Integer.parseInt(res.getLoanValue()));
            req.setInterestRate(Double.parseDouble(res.getInterestPercent()));
            req.setNumOfPayments(Integer.parseInt(res.getLoanTime()));
            req.setPaymentStartDate("2024-01-01");
            try {
              List<CalculateInstallments> resList = calculateLoanOld(req); // ** function --> calculateLoanNew() , calculateLoanOld
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
          } else {
            CalculateReq req = new CalculateReq();
            req.setPrincipal(Integer.parseInt(res.getLoanValue()));
            req.setInterestRate(Double.parseDouble(res.getInterestPercent()));
            req.setNumOfPayments(Integer.parseInt(res.getLoanTime()));
            req.setPaymentStartDate(res.getStartLoanDate());
            try {
              List<CalculateInstallments> resList = calculateLoanNew(req); // ** function --> calculateLoanNew() , calculateLoanOld()
              int sumTotalValueInterest = 0;
              int setTotalValuePrinciple = 0;
              int sumTotalValueInterestOfInstallment = 0;
              int setTotalValuePrincipleOfInstallment = 0;
              for (int i = 0; i < resList.size(); i++) {
                sumTotalValueInterest += resList.get(i).getInterest();
                setTotalValuePrinciple += resList.get(i).getPrincipal();
                if (Integer.parseInt(res.getInstallment()) == resList.get(i).getInstallment()) {
                  res.setMonthInterest(String.valueOf(resList.get(i).getInterest()));
                  res.setMonthPrinciple(String.valueOf(resList.get(i).getTotalDeduction()));
                  sumTotalValueInterestOfInstallment = sumTotalValueInterest;
                  setTotalValuePrincipleOfInstallment = setTotalValuePrinciple;
                  res.setTotalValueInterest(String.valueOf(sumTotalValueInterestOfInstallment));
                  res.setTotalValuePrinciple(String.valueOf(setTotalValuePrincipleOfInstallment));
                }
                if (Integer.parseInt(res.getLoanTime()) == resList.get(i).getInstallment()) {
                  res.setLastMonthInterest(String.valueOf(resList.get(i).getInterest()));
                  res.setLastMonthPrinciple(String.valueOf(resList.get(i).getTotalDeduction()));
                }
              }
              res.setOutStandInterest(String.valueOf(sumTotalValueInterest - sumTotalValueInterestOfInstallment));
              res.setOutStandPrinciple(String.valueOf(setTotalValuePrinciple - setTotalValuePrincipleOfInstallment));
            } catch (ParseException e) {
              e.printStackTrace();
            }
          }
        }
      });
      return resLoan;
    }
  }

  @Transactional
  public List<DocumentV2ResLoan> searchDocumentV2Loan(Long loanId, String getMonthCurrent, String yearCurrent) {
    return documentRepository.documentInfoV2Loan(loanId, getMonthCurrent, yearCurrent);
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
    List<CalculateInstallments> calculateInstallments = createAmortizationTableOld(principal, interestRate, numOfPayments);

    return calculateInstallments;
  }

  public List<CalculateInstallments> createAmortizationTableOld(double principal, double interestRates, int numOfPayments) {
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

  @Transactional
  public List<DocumentStockDevidend> calculateStockDividend(DocumentReq req) {
    var resDividend = documentRepository.documentInfoStockDividend(req.getEmpCode(), req.getYearCurrent(), null);
    if (resDividend != null) {
      resDividend.forEach(res -> {
        int totalDividend = 0;
        // stock dividend
        int sumYearOld = 0;
        int sumYearCurrent = 0;
        int stockDividend = 0;

        // Dividend of employeeCode ( yearCurrent )
        var resDividendYearCurrent = documentRepository.documentInfoStockDividendV1(res.getEmployeeCode(), req.getYearCurrent(), null);
        // Dividend of employeeCode ( yearOld )
        var resDividendYearOld = documentRepository.documentInfoStockDividendV1(res.getEmployeeCode(), null, req.getYearOld());
        if (Objects.requireNonNull(resDividendYearOld).size() > 0) {
          double stockDividendPercent = Double.parseDouble(req.getStockDividendPercent()) / 100;
          int stockValue = Integer.parseInt(resDividendYearOld.get(0).getStockValue());
          int stockAccumulate = Integer.parseInt(resDividendYearOld.get(0).getStockAccumulate());
          sumYearOld = (int) Math.round((stockValue + stockAccumulate) * stockDividendPercent); //* ((12-0) / 12)
        }
        for (DocumentStockDevidend documentStockDevidend : resDividendYearCurrent) {
          String stockMonth = documentStockDevidend.getStockMonth();
          int monthDifference = 12 - getMonthNumber(stockMonth);

          if (!stockMonth.equalsIgnoreCase("ธันวาคม")) {
            int sumMonth = 0;
            double stockDividendPercent = Double.parseDouble(req.getStockDividendPercent()) / 100;
            int stockValueMonth = Integer.parseInt(documentStockDevidend.getStockValue());
            sumMonth = (int) Math.round((stockValueMonth * stockDividendPercent) * monthDifference / 12);
            sumYearCurrent += sumMonth;
          }
        }
        stockDividend = Math.round(sumYearOld + sumYearCurrent);
        res.setStockDividend(String.valueOf(stockDividend));

        // Interest dividend
        int sumInterest = 0;
        int interestDividend = 0;
        var LoanDividendYearCurrent = documentRepository.documentInfoInterestDividend(res.getEmployeeCode(), req.getYearCurrent());
        for (DocumentStockDevidend loanDividend : LoanDividendYearCurrent) {
          sumInterest += Integer.parseInt(loanDividend.getInterest());
        }
        double interestDividendPercent = Double.parseDouble(req.getInterestDividendPercent()) / 100;
        interestDividend = (int) Math.round(sumInterest * interestDividendPercent);
        res.setCumulativeInterest(String.valueOf(sumInterest));
        res.setInterestDividend(String.valueOf(interestDividend));

        //  รวมปนผล = ( ปนผลหุน + ปนผลดอกเบี้ย )
        totalDividend = stockDividend + interestDividend;
        res.setTotalDividend(String.valueOf(totalDividend));
      });
    }

    return resDividend;
  }

  private int getMonthNumber(String month) {
    return switch (month) {
      case "มกราคม" -> 1;
      case "กุมภาพันธ์" -> 2;
      case "มีนาคม" -> 3;
      case "เมษายน" -> 4;
      case "พฤษภาคม" -> 5;
      case "มิถุนายน" -> 6;
      case "กรกฎาคม" -> 7;
      case "สิงหาคม" -> 8;
      case "กันยายน" -> 9;
      case "ตุลาคม" -> 10;
      case "พฤศจิกายน" -> 11;
      default -> 12; // For "ธันวาคม" month
    };
  }

  @Transactional
  public List<DocumentInfoAllLoanEmpRes> searchDocumentV1LoanById(
    Long loanId,
    String getMonthCurrent,
    Boolean admin,
    Long empId,
    String yearCurrent
  ) throws ParseException {
    if (!admin) {
      return null;
    } else {
      val empData = documentRepository.employeeById(empId);
      val loanData = documentRepository.loanById(loanId);

      for (DocumentInfoAllLoanEmpRes list : loanData) {
        int sumOrdinary = 0;
        list.setDepartmentName(empData.getDepartmentName());
        list.setEmployeeCode(empData.getEmployeeCode());
        list.setFullName(empData.getFullName());
        if (Integer.parseInt(list.getLoanYear()) >= 2567) {
          list.setLoanBalance(list.getLoanBalance() + (list.getLoanOrdinary() - list.getInterest()));
          val calculateReq = new CalculateReq();
          calculateReq.setPrincipal(list.getLoanValue());
          calculateReq.setInterestRate(list.getInterestPercent());
          calculateReq.setNumOfPayments(Integer.parseInt(list.getLoanTime()));
          calculateReq.setPaymentStartDate(list.getStartLoanDate());
          val calculate = calculateLoanNew(calculateReq);
          for (CalculateInstallments calculation : calculate) {
            if (calculation.getInstallment() <= list.getInstallment()) {
              sumOrdinary += calculation.getPrincipal();
            }
            if (calculation.getInstallment() == list.getInstallment()) {
              list.setPrincipal(calculation.getPrincipal());
            }
          }
          list.setSumOrdinary(sumOrdinary);
        } else {
          list.setSumOrdinary((int) (list.getLoanOrdinary() * list.getInstallment()));
          list.setPrincipal(0);
        }
      }
      return loanData;
    }
  }
}
