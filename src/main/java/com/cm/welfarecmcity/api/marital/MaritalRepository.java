package com.cm.welfarecmcity.api.marital;

import com.cm.welfarecmcity.dto.AffiliationDto;
import com.cm.welfarecmcity.dto.MaritalDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MaritalRepository extends JpaRepository<MaritalDto, Long>, JpaSpecificationExecutor<MaritalDto> {
}
