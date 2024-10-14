package com.cm.welfarecmcity.api.stock;

import com.cm.welfarecmcity.dto.StockDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockRepository
    extends JpaRepository<StockDto, Long>, JpaSpecificationExecutor<StockDto> {}
