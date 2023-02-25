package com.cm.welfarecmcity.api.position;

import com.cm.welfarecmcity.dto.PositionsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PositionRepository extends JpaRepository<PositionsDto, Long>, JpaSpecificationExecutor<PositionsDto> {}

