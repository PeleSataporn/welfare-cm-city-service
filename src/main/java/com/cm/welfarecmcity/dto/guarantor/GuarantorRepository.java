package com.cm.welfarecmcity.dto.guarantor;

import com.cm.welfarecmcity.dto.GuarantorDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GuarantorRepository extends JpaRepository<GuarantorDto, Long>, JpaSpecificationExecutor<GuarantorDto> {}
