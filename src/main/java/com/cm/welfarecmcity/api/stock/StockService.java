package com.cm.welfarecmcity.api.stock;

import com.cm.welfarecmcity.dto.StockDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

  @Autowired
  private StockRepository stockRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  public ResponseModel<ResponseId> add(StockDto dto) {
    val stock = stockRepository.save(dto);
    return responseDataUtils.insertDataSuccess(stock.getId());
  }
}
