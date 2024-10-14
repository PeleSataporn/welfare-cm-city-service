package com.cm.welfarecmcity.api.stockdetail;

import com.cm.welfarecmcity.dto.StockDetailDto;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import com.cm.welfarecmcity.utils.orderby.SortOrderBy;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockDetailService {

  @Autowired private StockDetailLoginRepository stockDetailLoginRepository;

  @Autowired private ResponseDataUtils responseDataUtils;

  @Autowired private SortOrderBy sort;

  @Transactional
  public List<StockDetailDto> searchStockDetail(Long stockId, String value) {
    return stockDetailLoginRepository.documentInfoV1StockDetail(stockId, value);
  }

  @Transactional
  public StockDetailDto getStockDetail(StockDetailDto req) {
    val stockDetailList = stockDetailLoginRepository.documentInfoV2StockDetail(req);
    if (stockDetailList.isEmpty()) {
      return null;
    } else {
      return stockDetailList.get(stockDetailList.size() - 1);
    }
  }
}
