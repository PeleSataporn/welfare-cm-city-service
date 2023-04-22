package com.cm.welfarecmcity.logic.stock;

import com.cm.welfarecmcity.logic.stock.model.StockRes;

import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockLogicService {

  @Autowired
  private StockLogicRepository stockRepository;

  //  @Autowired
  //  private ResponseDataUtils responseDataUtils;
  //
  //  @Autowired
  //  private EmployeeService employeeService;
  //
  //  @Autowired
  //  private MapStructMapper mapStructMapper;

  public List<StockRes> searchStock() {
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
    return stockRepository.searchStock();
  }
}
