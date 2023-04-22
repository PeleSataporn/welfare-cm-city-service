package com.cm.welfarecmcity.api.stockdetail;

import com.cm.welfarecmcity.dto.StockDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StockDetailRepository extends JpaRepository<StockDetailDto, Long>, JpaSpecificationExecutor<StockDetailDto> {

    List<StockDetailDto> findAllByStock_Id(Long stockId);
}
