package com.cm.welfarecmcity.api.employee;

import com.cm.welfarecmcity.api.admin.model.AdminUpdateInfoReq;
import com.cm.welfarecmcity.api.employee.model.*;
import com.cm.welfarecmcity.api.employeetype.EmployeeTypeRepository;
import com.cm.welfarecmcity.api.level.LevelRepository;
import com.cm.welfarecmcity.api.loan.LoanRepository;
import com.cm.welfarecmcity.api.loandetail.LoanDetailRepository;
import com.cm.welfarecmcity.api.notification.NotificationRepository;
import com.cm.welfarecmcity.api.stock.StockRepository;
import com.cm.welfarecmcity.constant.EmployeeStatusEnum;
import com.cm.welfarecmcity.constant.NotificationStatusEnum;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.PetitionNotificationDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.logic.loan.LoanLogicRepository;
import com.cm.welfarecmcity.logic.loan.model.BeneficiaryReq;
import com.cm.welfarecmcity.mapper.MapStructMapper;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private LevelRepository levelRepository;

  @Autowired
  private StockRepository stockRepository;

  @Autowired
  private EmployeeTypeRepository employeeTypeRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Autowired
  private MapStructMapper mapStructMapper;

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private LoanRepository loanRepository;

  @Autowired
  private LoanLogicRepository repository;

  @Autowired
  private LoanDetailRepository loanDetailRepository;

  @Transactional
  public List<EmpByAdminRes> searchEmployee() {
    val listRes = new ArrayList<EmpByAdminRes>();

    val list = employeeRepository.findAll();
    list.forEach(it -> {
      listRes.add(mapStructMapper.employeeToByAdminRes(it));
    });

    return listRes;
  }

  @Transactional
  public ResponseModel<ResponseId> updateEmp(EmpEditReq req) {
    val findEmployee = employeeRepository.findById(req.getId());
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val employeeMapper = mapStructMapper.reqToEmployee(req);
    employeeMapper.setBeneficiaries(findEmployee.get().getBeneficiaries());

    if (req.getLevelId() != 0) {
      employeeMapper.setLevel(levelRepository.findById(req.getLevelId()).get());
    }

    if (req.getEmployeeTypeId() != 0) {
      employeeMapper.setEmployeeType(employeeTypeRepository.findById(req.getEmployeeTypeId()).get());
    }

    // TODO
    employeeMapper.setLoan(findEmployee.get().getLoan());
    employeeMapper.setStock(findEmployee.get().getStock());
    employeeMapper.setDepartment(findEmployee.get().getDepartment());

    //    if (employeeMapper.getStock() != null) {
    //      val stock = employeeMapper.getStock();
    //      stock.setStockValue(req.getMonthlyStockMoney().intValue());
    //      val listStockDetails = stock.getStockDetails();
    //      listStockDetails.forEach(stockDetail -> stockDetail.setStock(stock));
    //    }
    //
    //    if (employeeMapper.getLoan() != null) {
    //      val loan = employeeMapper.getLoan();
    //      val listLoanDetails = loan.getLoanDetails();
    //      listLoanDetails.forEach(loanDetail -> loanDetail.setLoan(loan));
    //    }

    //    if (employeeMapper.getDepartment() != null) {
    //      val department = employeeMapper.getDepartment();
    //      val listEmployees = department.getEmployees();
    //      listEmployees.forEach(emp -> emp.setDepartment(department));
    //    }

    employeeMapper.setProfileFlag(true);

    employeeRepository.save(employeeMapper);

    return responseDataUtils.updateDataSuccess(req.getId());
  }

  @Transactional
  public ResponseModel<ResponseId> updateResign(UpdateResignReq req) {
    val findEmployee = employeeRepository.findById(req.getId());
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val employee = findEmployee.get();

    employee.setEmployeeStatus(EmployeeStatusEnum.PENDING_RESIGN_EMPLOYEE.getState());
    employee.setResignationDate(new Date());
    employee.setReason(req.getReason());

    employeeRepository.save(employee);

    // notify
    val notify = new PetitionNotificationDto();
    notify.setStatus(NotificationStatusEnum.RESIGN.getState());
    notify.setReason(req.getReason());
    notify.setEmployee(employee);

    notificationRepository.save(notify);

    return responseDataUtils.updateDataSuccess(req.getId());
  }

  @Transactional
  public ResponseModel<ResponseId> updateResignAdmin(UpdateAdminReq req) {
    val findEmployee = employeeRepository.findById(req.getId());
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val employee = findEmployee.get();

    if (req.getType() == 1) {
      employee.setEmployeeStatus(EmployeeStatusEnum.RESIGN_EMPLOYEE.getState());
      employee.setApprovedResignationDate(new Date());

      val stock = stockRepository.findById(employee.getStock().getId()).get();
      stock.setStockAccumulate(0);
      stock.setDeleted(true);
      stockRepository.save(stock);
    } else {
      employee.setMonthlyStockMoney(Integer.parseInt(req.getValue()));
      employee.getStock().setStockValue(Integer.parseInt(req.getValue()));
      employee.setCheckStockValueFlag(false);
    }

    // notify
    val notify = notificationRepository.findById(req.getNoId()).get();
    notificationRepository.delete(notify);

    employeeRepository.save(employee);

    return responseDataUtils.updateDataSuccess(req.getId());
  }

  @Transactional
  public ResponseModel<ResponseId> updateStockValue(UpdateStockValueReq req) {
    val findEmployee = employeeRepository.findById(req.getId());
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val employee = findEmployee.get();
    employee.setCheckStockValueFlag(true);

    // notify
    val notify = new PetitionNotificationDto();
    notify.setStatus(NotificationStatusEnum.STOCK_ACCUMULATE.getState());
    notify.setReason(Integer.toString(req.getStockValue()));
    notify.setDescription(Integer.toString(req.getStockOldValue()));
    notify.setEmployee(employee);

    notificationRepository.save(notify);

    return responseDataUtils.updateDataSuccess(req.getId());
  }

  @Transactional
  public ResponseModel<ResponseId> updateEmployeeStatus(UpdateAdminReq req) {
    val findEmployee = employeeRepository.findById(req.getId());
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val employee = findEmployee.get();
    if(req.getType() == EmployeeStatusEnum.ESCAPE_DEBT.getState()){
      employee.setEmployeeStatus(req.getType());
    }else{
      employee.setEmployeeStatus(req.getType());
      // set loan
      if(employee.getLoan().getId() != null){
        val loan = loanRepository.findById(employee.getLoan().getId()).get();
        loan.setLoanBalance(0);
        loan.setActive(false);
        //loan.setDeleted(true);
        loan.setInterest(0);
        loan.setGuarantorOne(null);
        loan.setGuarantorTwo(null);
        loanRepository.save(loan);

        // set loan_detail
        val detailLoan = repository.searchLoanOfLoanDetail(employee.getLoan().getId());
        if(!detailLoan.isEmpty()){
          for(LoanDetailDto list : detailLoan){
            val detail1 = loanDetailRepository.findById(list.getId()).get();
            detail1.setActive(false);
            loanDetailRepository.save(detail1);
          }
        }
      }

      // set stock
      val stock = stockRepository.findById(employee.getStock().getId()).get();
      stock.setStockAccumulate(0);
      stock.setActive(false);
      stock.setStockValue(0);
      //stock.setDeleted(true);
      stockRepository.save(stock);

      employee.setLoan(null);
      employee.setStock(null);
    }
    employeeRepository.save(employee);
    return responseDataUtils.updateDataSuccess(req.getId());
  }

  @Transactional
  public ResponseModel<ResponseId> updateBeneficiaryId(List<BeneficiaryReq> req) throws JsonProcessingException {
    val findEmployee = employeeRepository.findById(req.get(0).getEmpId());
    if (findEmployee.isEmpty()) {
      throw new EmployeeException("Employee id not found");
    }

    val employee = findEmployee.get();

    // list parse to json
    String jsonArrayString;
    ObjectMapper objectMapper = new ObjectMapper();
    jsonArrayString = objectMapper.writeValueAsString(req);

    // notify
    val notify = new PetitionNotificationDto();
    notify.setStatus(NotificationStatusEnum.BENEFICIARY.getState());

    notify.setReason("เปลี่ยนผู้รับผลประโยชน์");
    notify.setEmployee(employee);
    notify.setDescription(jsonArrayString);

    notificationRepository.save(notify);

    return responseDataUtils.updateDataSuccess(req.get(0).getEmpId());
  }

  @Transactional
  public ResponseModel<String> updateByUser(UpdateUserReq req) throws JsonProcessingException {
    val emp = employeeRepository.findById(req.getId()).get();
    // check notification

    if (
      (req.getFirstName() != null && !req.getFirstName().equals(emp.getFirstName())) ||
      (req.getLastName() != null && !req.getLastName().equals(emp.getLastName())) ||
      (req.getMarital() != null && !req.getMarital().equals(emp.getMarital()))
    ) {
      // list parse to json
      String jsonArrayString;
      ObjectMapper objectMapper = new ObjectMapper();
      jsonArrayString = objectMapper.writeValueAsString(req);

      val notify = new PetitionNotificationDto();
      notify.setStatus(NotificationStatusEnum.UPDATE_BY_USER.getState());
      notify.setReason("แก้ไขข้อมูลส่วนตัว");
      notify.setEmployee(emp);
      notify.setDescription(jsonArrayString);

      notificationRepository.save(notify);
    }

    emp.setBirthday(req.getBirthday());
    emp.setContractStartDate(req.getContractStartDate());
    emp.setCivilServiceDate(req.getCivilServiceDate());
    emp.setBillingStartDate(req.getBillingStartDate());
    emp.setSalaryBankAccountNumber(req.getSalaryBankAccountNumber());
    emp.setBankAccountReceivingNumber(req.getBankAccountReceivingNumber());

    // contact
    val contact = emp.getContact();
    contact.setTel(req.getTel());
    contact.setLineId(req.getLineId());
    contact.setFacebook(req.getFacebook());
    contact.setEmail(req.getEmail());
    contact.setAddress(req.getAddress());

    employeeRepository.save(emp);

    if (
      (req.getFirstName() != null && !req.getFirstName().equals(emp.getFirstName())) ||
      (req.getLastName() != null && !req.getLastName().equals(emp.getLastName())) ||
      (req.getMarital() != null && !req.getMarital().equals(emp.getMarital()))
    ) {
      return responseDataUtils.updateDataSuccessString("PENDING");
    } else {
      return responseDataUtils.updateDataSuccessString("UPDATE");
    }
  }

  @Transactional
  public void approveUpdateByUser(UpdateUserReq req) {
    val emp = employeeRepository.findById(req.getId()).get();
    emp.setFirstName(req.getFirstName());
    emp.setLastName(req.getLastName());
    emp.setMarital(req.getMarital());

    employeeRepository.save(emp);
  }
}
