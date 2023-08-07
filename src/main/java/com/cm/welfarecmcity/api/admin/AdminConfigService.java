package com.cm.welfarecmcity.api.admin;

import com.cm.welfarecmcity.api.admin.model.AdminConfigReq;
import com.cm.welfarecmcity.api.admin.model.AdminConfigRes;
import com.cm.welfarecmcity.api.admin.model.AdminUpdateInfoReq;
import com.cm.welfarecmcity.api.admin.model.employeeListRes;
import com.cm.welfarecmcity.api.affiliation.AffiliationRepository;
import com.cm.welfarecmcity.api.bureau.BureauRepository;
import com.cm.welfarecmcity.api.department.DepartmentRepository;
import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.employeetype.EmployeeTypeRepository;
import com.cm.welfarecmcity.api.level.LevelRepository;
import com.cm.welfarecmcity.api.loan.LoanRepository;
import com.cm.welfarecmcity.api.loandetail.LoanDetailRepository;
import com.cm.welfarecmcity.api.position.PositionRepository;
import com.cm.welfarecmcity.dto.AdminConfigDto;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.logic.document.DocumentRepository;
import com.cm.welfarecmcity.logic.document.model.CalculateInstallments;
import com.cm.welfarecmcity.logic.document.model.CalculateReq;
import com.cm.welfarecmcity.logic.document.model.EmployeeLoanNew;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
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
public class AdminConfigService {

  @Autowired
  private AdminConfigRepositoryLogic adminConfigRepositoryLogic;

  @Autowired
  private AdminConfigRepository adminConfigRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Autowired
  private LoanRepository loanRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private LoanDetailRepository loanDetailRepository;

  @Autowired
  private LevelRepository levelRepository;

  @Autowired
  private EmployeeTypeRepository employeeTypeRepository;

  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private AffiliationRepository affiliationRepository;

  @Autowired
  private BureauRepository bureauRepository;

  @Transactional
  public List<AdminConfigRes> getConfigByList() {
    try {
      return adminConfigRepositoryLogic.documentInfoConfigList();
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public List<employeeListRes> getEmployeeByList(AdminConfigReq req) {
    try {
      return adminConfigRepositoryLogic.documentInfoConfigEmployeeList(req.getAdminFlag());
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public Boolean updateRoleEmp(AdminConfigReq req) {
    try {
      val findEmployee = employeeRepository.findById(req.getEmpId());
      if (findEmployee.isEmpty()) {
        throw new EmployeeException("Employee id not found");
      }
      val employee = findEmployee.get();
      employee.setAdminFlag(req.getAdminFlag());
      return true;
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public ResponseModel<ResponseId> editConfig(AdminConfigReq req) {
    try {
      val findConfig = adminConfigRepository.findById(req.getConfigId());
      if (findConfig.isEmpty()) {
        throw new EmployeeException("Config Admin not found");
      }
      val config = findConfig.get();
      config.setValue(req.getValue());

      if (req.getConfigId() == 1) {
        var result = adminConfigRepositoryLogic.getLanDetailOfEmp(req.getMonthCurrent(), req.getYearCurrent());
        for (EmployeeLoanNew empNew : result) {
          if (Integer.parseInt(empNew.getLoanBalance()) > 0) {
            // set Req calculateLoanNew
            CalculateReq calNew = new CalculateReq();
            calNew.setInterestRate(Double.parseDouble(req.getValue()));
            calNew.setNumOfPayments(empNew.getLoanTime());
            calNew.setPrincipal(Double.parseDouble(empNew.getLoanBalance()));
            calNew.setPaymentStartDate(req.getPaymentStartDate());
            // get list loan new
            List<CalculateInstallments> listLoanNew = calculateLoanNew(calNew);
            // close loan
            closeLoan(empNew.getLoanId());
            // set addLoanNew
            empNew.setInterestPercent(req.getValue());
            empNew.setLoanValue(empNew.getLoanBalance());
            empNew.setInterestLoan(String.valueOf(0));
            empNew.setStartDateLoan(req.getPaymentStartDate());
            empNew.setLoanMonth(req.getMonthCurrent());
            empNew.setLoanYear(req.getYearCurrent());
            empNew.setLoanOrdinary(String.valueOf(listLoanNew.get(0).getTotalDeduction()));
            addLoanNew(empNew);
          }
        }
      }
      return responseDataUtils.insertDataSuccess(req.getConfigId());
    } catch (Exception e) {
      return null;
    }
  }

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

  public void addLoanNew(EmployeeLoanNew req) {
    // inset loan
    LoanDto loanDto = new LoanDto();
    //loanDto.setLoanNo("2566-0624001");
    loanDto.setLoanValue(Double.parseDouble(req.getLoanValue()));
    loanDto.setLoanBalance(Double.parseDouble(req.getLoanValue()));
    loanDto.setLoanTime(req.getLoanTime());
    loanDto.setInterest(Integer.parseInt(req.getInterestLoan()));
    loanDto.setInterestPercent(Integer.parseInt(req.getInterestPercent()));
    loanDto.setNewLoan(true);
    loanDto.setStartLoanDate(req.getStartDateLoan());
    if (req.getGuarantorOne() != null && req.getGuarantorTwo() != null) {
      //            var result1 = documentRepository.getEmpCodeOfId(req.getGuarantorOne());
      val emp1 = employeeRepository.findById(Long.valueOf(req.getGuarantorOne())).get();
      loanDto.setGuarantorOne(emp1);
      //loanDto.getGuarantorOne().setId(result1.getEmpId());
      //          var result2 = documentRepository.getEmpCodeOfId(req.getGuarantorTwo());
      val emp2 = employeeRepository.findById(Long.valueOf(req.getGuarantorTwo())).get();
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
    loanDetailDto.setInterestLastMonth(0); //Integer.parseInt(req.getInterestLoanLastMonth()
    val loanDetail = loanDetailRepository.save(loanDetailDto);

    // update number running
    String runningNumber = runningNumber(loan.getId());
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
  }

  public String runningNumber(Long numberRun) {
    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();
    int currentMonth = currentDate.getMonthValue();
    int currentDay = currentDate.getDayOfMonth();

    int runningNumber = (int) Long.parseLong(String.valueOf(numberRun)); // Start with 1
    String formattedRunningNumber = String.format("%04d", runningNumber);
    String runningNumberString = currentYear + "-" + String.format("%02d%02d", currentMonth, currentDay) + formattedRunningNumber;
    System.out.println(runningNumberString);

    return runningNumberString;
  }

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

  public AdminConfigDto configById(long id) {
    return adminConfigRepository.findById(id).get();
  }

  @Transactional
  public void addConfigImg(AdminConfigDto image, Long id) {
    val config = adminConfigRepository.findById(id).get();
    config.setImage(image.getImage());
    adminConfigRepository.save(config);
  }

  @Transactional
  public void updateInfo(AdminUpdateInfoReq req) {
    val emp = employeeRepository.findById(req.getId()).get();
    emp.setPrefix(req.getPrefix());
    emp.setFirstName(req.getFirstName());
    emp.setLastName(req.getLastName());
    emp.setGender(req.getGender());
    emp.setIdCard(req.getIdCard());
    emp.setBirthday(req.getBirthday());
    // contact
    val contact = emp.getContact();
    contact.setTel(req.getTel());
    contact.setLineId(req.getLineId());
    contact.setFacebook(req.getFacebook());
    contact.setEmail(req.getEmail());
    contact.setAddress(req.getAddress());
    // level
    emp.setLevel(levelRepository.findById(req.getLevelId()).get());
    // employeeType
    emp.setEmployeeType(employeeTypeRepository.findById(req.getEmployeeTypeId()).get());
    // position
    emp.setPosition(positionRepository.findById(req.getPositionId()).get());
    // department
    emp.setDepartment(departmentRepository.findById(req.getDepartmentId()).get());
    // affiliation
    emp.setAffiliation(affiliationRepository.findById(req.getAffiliationId()).get());
    emp.setMarital(req.getMarital());
    emp.setSalary(req.getSalary());
    emp.setCompensation(req.getCompensation());
    emp.setMonthlyStockMoney(req.getMonthlyStockMoney());
    emp.setContractStartDate(req.getContractStartDate());
    emp.setCivilServiceDate(req.getCivilServiceDate());
    emp.setBillingStartDate(req.getBillingStartDate());
    emp.setSalaryBankAccountNumber(req.getSalaryBankAccountNumber());
    emp.setBankAccountReceivingNumber(req.getBankAccountReceivingNumber());

    employeeRepository.save(emp);
  }
}
