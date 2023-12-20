package com.cm.welfarecmcity.api.loan;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.loanHistory.LoanHistoryRepository;
import com.cm.welfarecmcity.api.loandetail.LoanDetailRepository;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.LoanHistoryDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.logic.document.DocumentRepository;
import com.cm.welfarecmcity.logic.document.model.EmployeeLoanNew;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

  @Autowired
  private LoanRepository loanRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private LoanDetailRepository loanDetailRepository;

  @Autowired
  private LoanHistoryRepository loanHistoryRepository;

  @Autowired
  private LoanLogic01Repository loanLogicRepository;

  @Transactional
  public ResponseModel<ResponseId> add(LoanDto dto) {
    val loan = loanRepository.save(dto);
    return responseDataUtils.insertDataSuccess(loan.getId());
  }

  @Transactional
  public ResponseModel<ResponseId> addLoanNew(EmployeeLoanNew req) {
    // inset loan
    LoanDto loanDto = new LoanDto();
    //loanDto.setLoanNo("2566-0624001");
    loanDto.setLoanValue(Double.parseDouble(req.getLoanValue()));
    loanDto.setLoanBalance(Double.parseDouble(req.getLoanValue()));
    loanDto.setLoanTime(Long.valueOf(req.getLoanTime()).intValue());
    loanDto.setInterest(Integer.parseInt(req.getInterestLoan()));
    loanDto.setInterestPercent(Integer.parseInt(req.getInterestPercent()));
    loanDto.setNewLoan(true);
    loanDto.setStockFlag(req.getGuaranteeStockFlag());
    loanDto.setStartLoanDate(req.getStartDateLoan());
    if (req.getGuarantorOne() != null && req.getGuarantorTwo() != null) {
      var result1 = documentRepository.getEmpCodeOfId(req.getGuarantorOne());
      val emp1 = employeeRepository.findById(result1.getEmpId()).get();
      loanDto.setGuarantorOne(emp1);
      //loanDto.getGuarantorOne().setId(result1.getEmpId());
      var result2 = documentRepository.getEmpCodeOfId(req.getGuarantorTwo());
      val emp2 = employeeRepository.findById(result2.getEmpId()).get();
      loanDto.setGuarantorTwo(emp2);
      //loanDto.getGuarantorTwo().setId(result2.getEmpId());
    }
    val loan = loanRepository.save(loanDto);

    // inset loanDetail
    LoanDetailDto loanDetailDto = new LoanDetailDto();
    loanDetailDto.setInstallment(0);
    loanDetailDto.setInterest(Integer.parseInt(req.getInterestLoan()));
    loanDetailDto.setLoanMonth(req.getLoanMonth());
    loanDetailDto.setLoanOrdinary(0); //Integer.parseInt(req.getLoanOrdinary()
    val lone = loanRepository.findById(loan.getId()).get();
    loanDetailDto.setLoan(lone);
    loanDetailDto.setInterestPercent(Integer.parseInt(req.getInterestPercent()));
    loanDetailDto.setLoanYear(req.getLoanYear());
    //    loanDetailDto.setInterestLastMonth(0); //Integer.parseInt(req.getInterestLoanLastMonth()
    val loanDetail = loanDetailRepository.save(loanDetailDto);

    // insert to history loan
    LoanHistoryDto loanHistoryDto = new LoanHistoryDto();
    loanHistoryDto.setEmployeeId(req.getEmpId());
    loanHistoryDto.setLoanId(loan.getId());
    val loanHistory = loanHistoryRepository.save(loanHistoryDto);

    // update number running
    val numberMax = loanLogicRepository.getNumberMaxLoan();
    int numberCheckMax = numberMax.getNumberMax() != 0 ? numberMax.getNumberMax() + 1 : 1;
    String runningNumber = runningNumber(loan.getId(), numberCheckMax);
    val findLoan = loanRepository.findById(loan.getId());
    val loans = findLoan.get();
    loans.setLoanNo(runningNumber);

    // update loan to employee
    var result10 = documentRepository.getEmpCodeOfId(req.getEmployeeCode());
    val findEmployee = employeeRepository.findById(result10.getEmpId());
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val employee = findEmployee.get();
    val loanFind = loanRepository.findById(loan.getId()).get();
    employee.setLoan(loanFind);

    return responseDataUtils.insertDataSuccess(loan.getId());
  }

  public String runningNumber(Long numberRun, int numberMax) {
    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();
    int currentMonth = currentDate.getMonthValue();
    int currentDay = currentDate.getDayOfMonth();

    //int runningNumber = (int) Long.parseLong(String.valueOf(numberRun)); // Start with 1
    String formattedRunningNumber = String.format("%02d", numberMax);
    return (currentYear + 543) + "-" + String.format("%02d%02d", currentMonth, currentDay) + formattedRunningNumber;
    //System.out.println(runningNumberString);
  }

  @Transactional
  public void deleteLoanNew(EmployeeLoanNew req) {
    var empData = documentRepository.getEmpCodeOfId(req.getEmployeeCode());
    val employee = employeeRepository.findById(empData.getEmpId()).get();
    employee.setLoan(null);
    employeeRepository.save(employee);

    val lone = loanRepository.findById(req.getLoanId()).get();
    val result = loanLogicRepository.getLoanDetailByLoanId(lone.getId());

    // delete loanDetail
    val loneDetail = loanDetailRepository.findById(result.getLoanId()).get();
    loanDetailRepository.delete(loneDetail);

    // delete loan
    loanRepository.delete(lone);
  }
}
