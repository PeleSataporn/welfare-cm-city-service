package com.cm.welfarecmcity.api.loandetailhistory;

import com.cm.welfarecmcity.logic.document.DocumentService;
import com.cm.welfarecmcity.logic.document.model.*;
import com.cm.welfarecmcity.utils.DateUtils;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.val;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanDetailHistoryService {

  @Autowired private LoanDetailHistoryLogicRepository loanDetailHistoryLogicRepository;

  @Autowired private DocumentService documentService;

  public static int getMonthNumber(String thaiMonth) {
    return switch (thaiMonth) {
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
      case "ธันวาคม" -> 12;
      default -> 0;
    };
  }

  @Transactional
  public List<DocumentV1ResLoan> searchV1LoanHistory(String getMonthCurrent, String yearCurrent) {

    val result = new ArrayList<DocumentV1ResLoan>();
    var resLoan =
        loanDetailHistoryLogicRepository.searchV1LoanHistory(
            getMonthCurrent, yearCurrent, getMonthNumber(getMonthCurrent));

    List<List<String>> listMonthInterest;
    List<List<String>> listMonthPrinciple;
    List<List<String>> listLastMonthInterest;
    List<List<String>> listLastMonthPrinciple;
    List<List<String>> listTotalValueInterest;
    List<List<String>> listOutStandInterest;
    List<List<String>> listTotalValuePrinciple;
    List<List<String>> listOutStandPrinciple;
    if (getMonthCurrent.equals("ธันวาคม") && yearCurrent.equals("2567")) {
      listMonthInterest = new ArrayList<>();
      listMonthPrinciple = new ArrayList<>();
      listLastMonthInterest =
          readFileExcelForLoan(0, "excel-import-loan/import_loan_last_month_interest.xlsx");
      listLastMonthPrinciple =
          readFileExcelForLoan(0, "excel-import-loan/import_loan_last_month_principle.xlsx");
      listTotalValueInterest =
          readFileExcelForLoan(0, "excel-import-loan/import_loan_total_interest.xlsx");
      listOutStandInterest =
          readFileExcelForLoan(0, "excel-import-loan/import_loan_out_stand_interest.xlsx");
      listTotalValuePrinciple =
          readFileExcelForLoan(0, "excel-import-loan/import_loan_total_principle.xlsx");
      listOutStandPrinciple = new ArrayList<>();
    } else if (getMonthCurrent.equals("มกราคม") && yearCurrent.equals("2568")) {
      listMonthInterest =
          readFileExcelForLoan(0, "excel-import-loan/import_loan_month_interest.xlsx");
      listMonthPrinciple =
          readFileExcelForLoan(0, "excel-import-loan/import_loan_month_principle.xlsx");
      listLastMonthInterest =
          readFileExcelForLoan(1, "excel-import-loan/import_loan_last_month_interest.xlsx");
      listLastMonthPrinciple =
          readFileExcelForLoan(1, "excel-import-loan/import_loan_last_month_principle.xlsx");
      listTotalValueInterest =
          readFileExcelForLoan(1, "excel-import-loan/import_loan_total_interest.xlsx");
      listOutStandInterest =
          readFileExcelForLoan(1, "excel-import-loan/import_loan_out_stand_interest.xlsx");
      listTotalValuePrinciple =
          readFileExcelForLoan(1, "excel-import-loan/import_loan_total_principle.xlsx");
      listOutStandPrinciple = new ArrayList<>();
    } else if (getMonthCurrent.equals("กุมภาพันธ์") && yearCurrent.equals("2568")) {
      listMonthInterest =
          readFileExcelForLoan(1, "excel-import-loan/import_loan_month_interest.xlsx");
      listMonthPrinciple =
          readFileExcelForLoan(1, "excel-import-loan/import_loan_month_principle.xlsx");
      listLastMonthInterest =
          readFileExcelForLoan(2, "excel-import-loan/import_loan_last_month_interest.xlsx");
      listLastMonthPrinciple =
          readFileExcelForLoan(2, "excel-import-loan/import_loan_last_month_principle.xlsx");
      listTotalValueInterest =
          readFileExcelForLoan(2, "excel-import-loan/import_loan_total_interest.xlsx");
      listOutStandInterest =
          readFileExcelForLoan(2, "excel-import-loan/import_loan_out_stand_interest.xlsx");
      listTotalValuePrinciple =
          readFileExcelForLoan(2, "excel-import-loan/import_loan_total_principle.xlsx");
      listOutStandPrinciple =
          readFileExcelForLoan(0, "excel-import-loan/import_loan_out_stand_principle.xlsx");
    } else {
      listMonthInterest = new ArrayList<>();
      listMonthPrinciple = new ArrayList<>();
      listLastMonthInterest = new ArrayList<>();
      listLastMonthPrinciple = new ArrayList<>();
      listTotalValueInterest = new ArrayList<>();
      listOutStandInterest = new ArrayList<>();
      listTotalValuePrinciple = new ArrayList<>();
      listOutStandPrinciple = new ArrayList<>();
    }

    resLoan.forEach(
        res -> {
          //          List<String> employeeInfo = findEmployeeInfo(listMonthInterest,
          // res.getEmployeeCode());
          //          if (employeeInfo != null) {
          //              res.setMonthInterest(employeeInfo.get(5));
          //              res.setMonthPrinciple(employeeInfo.get(6));
          //              res.setTotalValueInterest(employeeInfo.get(17));
          //              res.setTotalValuePrinciple(employeeInfo.get(19));
          //              res.setLastMonthInterest(employeeInfo.get(13));
          //              res.setLastMonthPrinciple(employeeInfo.get(14));
          //              res.setOutStandInterest(employeeInfo.get(18));
          //              res.setOutStandPrinciple(employeeInfo.get(19));
          //          } else {
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

            if (!listMonthInterest.isEmpty()) {
              val resInfo = findEmployeeInfo(listMonthInterest, res.getEmployeeCode());
              if (resInfo != null) {
                res.setMonthInterest(resInfo.get(3));
              }
            }

            if (!listMonthPrinciple.isEmpty()) {
              val resInfo = findEmployeeInfo(listMonthPrinciple, res.getEmployeeCode());
              if (resInfo != null) {
                res.setMonthPrinciple(resInfo.get(3));
              }
            }

            if (!listLastMonthInterest.isEmpty()) {
              val resInfo = findEmployeeInfo(listLastMonthInterest, res.getEmployeeCode());
              if (resInfo != null) {
                res.setLastMonthInterest(resInfo.get(3));
              }
            }

            if (!listLastMonthPrinciple.isEmpty()) {
              val resInfo = findEmployeeInfo(listLastMonthPrinciple, res.getEmployeeCode());
              if (resInfo != null) {
                res.setLastMonthPrinciple(resInfo.get(3));
              }
            }

            if (!listTotalValueInterest.isEmpty()) {
              val resInfo = findEmployeeInfo(listTotalValueInterest, res.getEmployeeCode());
              if (resInfo != null) {
                res.setTotalValueInterest(resInfo.get(3));
              }
            }

            if (!listOutStandInterest.isEmpty()) {
              val resInfo = findEmployeeInfo(listOutStandInterest, res.getEmployeeCode());
              if (resInfo != null) {
                res.setOutStandInterest(resInfo.get(3));
              }
            }

            if (!listTotalValuePrinciple.isEmpty()) {
              val resInfo = findEmployeeInfo(listTotalValuePrinciple, res.getEmployeeCode());
              if (resInfo != null) {
                res.setTotalValuePrinciple(resInfo.get(3));
              }
            }

            if (!listOutStandPrinciple.isEmpty()) {
              val resInfo = findEmployeeInfo(listOutStandPrinciple, res.getEmployeeCode());
              if (resInfo != null) {
                res.setOutStandPrinciple(resInfo.get(3));
              }
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
          //          }
        });

    return result;
  }

  public static List<String> findEmployeeInfo(
      List<List<String>> employeeData, String employeeNumber) {
    for (List<String> employee : employeeData) {
      if (employee.size() > 1 && employee.get(1).equals(employeeNumber)) {
        return employee;
      }
    }
    return null; // Return null if not found
  }

  //  public boolean isLoanClosedInYear(Date closeLoanDate, String yearCurrent, String monthCurrent)
  // {
  //    if (closeLoanDate == null) {
  //      return true; // Handle null safely
  //    }
  //
  //    // Convert Date to LocalDate
  //    LocalDate localDate =
  // closeLoanDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  //    // Extract the year and check the condition
  //    if ((localDate.getYear() + 543) >= Integer.parseInt(yearCurrent)) {
  //      int monthFM = DateUtils.getThaiMonthIntOfValue(monthCurrent);
  //      if (localDate.getMonthValue() > monthFM) {
  //        return true;
  //      } else {
  //        return false;
  //      }
  //    } else {
  //      return false;
  //    }
  //  }

  public boolean isLoanClosedInYear(Date closeLoanDate, String yearCurrent, String monthCurrent) {
    if (closeLoanDate == null) {
      return true; // Treat null as "closed"
    }

    // Convert Date to LocalDate
    LocalDate localDate = closeLoanDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    int thaiYearClosed = localDate.getYear() + 543; // Convert to Thai Buddhist year
    int thaiYearCurrent = Integer.parseInt(yearCurrent);
    int monthCurrentInt =
        DateUtils.getThaiMonthIntOfValue(monthCurrent); // Convert month name to int

    // Check year condition
    if (thaiYearClosed > thaiYearCurrent) {
      return true;
    } else if (thaiYearClosed == thaiYearCurrent) {
      // Check month condition
      return localDate.getMonthValue() > monthCurrentInt;
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
        loanDetailHistoryLogicRepository.searchV1LoanHistory(
            getMonthCurrent, yearCurrent, getMonthNumber(getMonthCurrent));

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

  public List<List<String>> readFileExcelForLoan(Integer sheetIndex, String filePath) {
    InputStream fileStream = getClass().getClassLoader().getResourceAsStream(filePath);

    if (fileStream == null) {
      throw new RuntimeException("File not found in resources: " + filePath);
    }

    return readExcelColumnsFromResource(sheetIndex, fileStream);
  }

  public static List<List<String>> readExcelColumnsFromResource(
      Integer sheetIndex, InputStream fileStream) {
    List<List<String>> dataList = new ArrayList<>();

    try (Workbook workbook = WorkbookFactory.create(fileStream)) {
      Sheet sheet = workbook.getSheetAt(sheetIndex); // Read first sheet

      if (sheet == null || sheet.getRow(0) == null) {
        System.out.println("The sheet is empty.");
        return dataList;
      }

      // Read each row and store values in a list
      for (Row row : sheet) {
        List<String> rowData = new ArrayList<>();
        for (Cell cell : row) {
          String cellValue = getCellValue(cell);
          if (!cellValue.isEmpty()) {
            rowData.add(cellValue);
          }
        }
        if (!rowData.isEmpty()) {
          dataList.add(rowData);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    return dataList;
  }

  private static String getCellValue(Cell cell) {
    if (cell == null) return "";
    switch (cell.getCellType()) {
      case STRING:
        return cell.getStringCellValue().trim();
      case NUMERIC:
        // Check if it's a whole number (integer)
        double numericValue = cell.getNumericCellValue();
        if (numericValue == Math.floor(numericValue)) {
          return String.valueOf((long) numericValue); // Convert to long to remove .0
        } else {
          return String.valueOf(numericValue);
        }
      case BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      case FORMULA:
        return cell.getCellFormula();
      default:
        return "";
    }
  }
}
