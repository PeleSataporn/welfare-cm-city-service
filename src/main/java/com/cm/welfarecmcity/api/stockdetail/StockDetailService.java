package com.cm.welfarecmcity.api.stockdetail;

import com.cm.welfarecmcity.dto.StockDetailDto;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import java.util.List;

import com.cm.welfarecmcity.utils.orderby.SortOrderBy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class StockDetailService {

  @Autowired
  private StockDetailRepository stockDetailRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Autowired
  private SortOrderBy sort;

  @Transactional
  public List<StockDetailDto> getStockDetail(Long stockId, String value) {
    return stockDetailRepository.findAllByStock_Id(stockId, Sort.by(sort.orderBy(value), "installment"));
  }
}
