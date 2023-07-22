package com.cm.welfarecmcity.api.stock;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.stock.model.UpdateStockReq;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.StockDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

  @Autowired
  private StockRepository stockRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Transactional
  public ResponseModel<ResponseId> add(StockDto dto) {
    val stock = stockRepository.save(dto);
    return responseDataUtils.insertDataSuccess(stock.getId());
  }

  @Transactional
  public ResponseModel<ResponseId> update(UpdateStockReq req) {
    val stock = stockRepository.findById(req.getId()).get();
    val employee = employeeRepository.getByStockId(req.getId());

    if (req.getStockValue() != 0) {
      stock.setStockValue(req.getStockValue());

      employee.setMonthlyStockMoney(req.getStockValue());
      employeeRepository.save(employee);
    }

    return responseDataUtils.updateDataSuccess(stockRepository.save(stock).getId());
  }

  @Transactional
  public StockDto getStock(Long stockId) {
    return stockRepository.getById(stockId);
  }
}
