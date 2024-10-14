package com.cm.welfarecmcity.api.bureau;

import com.cm.welfarecmcity.dto.BureauDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BureauRepository
    extends JpaRepository<BureauDto, Long>, JpaSpecificationExecutor<BureauDto> {}
