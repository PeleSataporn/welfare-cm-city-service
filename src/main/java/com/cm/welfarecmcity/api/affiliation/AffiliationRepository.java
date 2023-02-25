package com.cm.welfarecmcity.api.affiliation;

import com.cm.welfarecmcity.dto.AffiliationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AffiliationRepository extends JpaRepository<AffiliationDto, Long>, JpaSpecificationExecutor<AffiliationDto> {}

