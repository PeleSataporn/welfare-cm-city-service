package com.cm.welfarecmcity.logic.loan;

import com.cm.welfarecmcity.logic.loan.model.LoanRes;
import com.cm.welfarecmcity.logic.stock.StockLogicRepository;
import com.cm.welfarecmcity.logic.stock.model.StockRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanLogicService {

  @Autowired
  private LoanLogicRepository repository;

  //  @Autowired
  //  private ResponseDataUtils responseDataUtils;
  //
  //  @Autowired
  //  private EmployeeService employeeService;
  //
  //  @Autowired
  //  private MapStructMapper mapStructMapper;

  public List<LoanRes> searchLoan() {
//    val listStock = stockRepository.findAll();
//
//    List<StockRes> listStockRes = new ArrayList<>();
//
//    for (val stockDto : listStock) {
//      if (stockDto.getEmployee() != null) {
//        val stockRes = mapStructMapper.stockRes(stockDto);
//
//        val employee = employeeService.getEmployee(stockDto.getEmployee().getId());
//        val employeeRes = mapStructMapper.employeeRes(employee);
//
//        stockRes.setEmployee(employeeRes);
//
//        listStockRes.add(stockRes);
//      }
//    }
    return repository.searchLoan();
  }
}
