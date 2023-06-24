package com.cm.welfarecmcity.api.loan;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.loandetail.LoanDetailLogicRepository;
import com.cm.welfarecmcity.api.loandetail.LoanDetailRepository;
import com.cm.welfarecmcity.constant.EmployeeStatusEnum;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.logic.document.DocumentRepository;
import com.cm.welfarecmcity.logic.document.model.EmployeeLoanNew;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        loanDto.setLoanBalance(Double.parseDouble(req.getLoanBalance()));
        loanDto.setLoanTime(req.getLoanTime());
        loanDto.setInterest(Integer.parseInt(req.getInterestLoan()));
        loanDto.setInterestPercent(Integer.parseInt(req.getInterestPercent()));
        loanDto.setNewLoan(true);
        if(req.getGuarantorOne() != null && req.getGuarantorTwo() != null){
            var result1 = documentRepository.getEmpCodeOfId(req.getGuarantorOne());
            loanDto.getGuarantorOne().setId(result1.getEmpId());
            var result2 = documentRepository.getEmpCodeOfId(req.getGuarantorTwo());
            loanDto.getGuarantorTwo().setId(result2.getEmpId());
        }
        val loan = loanRepository.save(loanDto);

        // inset loanDetail
        LoanDetailDto loanDetailDto = new LoanDetailDto();
        loanDetailDto.setInstallment(0);
        loanDetailDto.setInterest(Integer.parseInt(req.getInterestLoan()));
        loanDetailDto.setLoanMonth(req.getLoanMonth());
        loanDetailDto.setLoanOrdinary(0); //Integer.parseInt(req.getLoanOrdinary()
        loanDetailDto.getLoan().setId(loan.getId());
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
        val findEmployee = employeeRepository.findById(req.getEmpId());
        if (findEmployee.isEmpty()) {
            throw new EmployeeException("Employee id not found");
        }

        val employee = findEmployee.get();
        employee.getLoan().setId(loan.getId());

        return responseDataUtils.insertDataSuccess(loan.getId());
    }

    public String runningNumber(Long numberRun){
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        int currentDay = currentDate.getDayOfMonth();

        int runningNumber = (int) Long.parseLong(String.valueOf(numberRun));  // Start with 1
        String formattedRunningNumber = String.format("%04d", runningNumber);
        String runningNumberString = currentYear + "-" + String.format("%02d%02d", currentMonth, currentDay) + formattedRunningNumber;
        System.out.println(runningNumberString);

        return runningNumberString;
    }

}
