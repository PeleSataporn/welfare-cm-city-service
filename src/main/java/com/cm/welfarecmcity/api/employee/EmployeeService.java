package com.cm.welfarecmcity.api.employee;

import com.cm.welfarecmcity.api.employee.model.EmpEditReq;
import com.cm.welfarecmcity.api.employee.model.UpdateAdminReq;
import com.cm.welfarecmcity.api.employee.model.UpdateResignReq;
import com.cm.welfarecmcity.api.employee.model.UpdateStockValueReq;
import com.cm.welfarecmcity.api.employeetype.EmployeeTypeRepository;
import com.cm.welfarecmcity.api.level.LevelRepository;
import com.cm.welfarecmcity.api.notification.NotificationRepository;
import com.cm.welfarecmcity.api.stock.StockRepository;
import com.cm.welfarecmcity.constant.EmployeeStatusEnum;
import com.cm.welfarecmcity.constant.NotificationStatusEnum;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.PetitionNotificationDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.mapper.MapStructMapper;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;
import java.util.Date;
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

  //  @Transactional
  //  public EmployeeDto getEmployee(Long id) {
  //    val findEmployee = employeeRepository.findById(id);
  //    if (findEmployee.isEmpty()) {
  //      throw new EmployeeException("Employee id not found");
  //    }
  //
  //    return findEmployee.get();
  //  }

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
    employee.setEmployeeStatus(req.getType());

    return responseDataUtils.updateDataSuccess(req.getId());
  }
}
