package com.cm.welfarecmcity.api.beneficiary;

import com.cm.welfarecmcity.dto.BeneficiaryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BeneficiaryRepository extends JpaRepository<BeneficiaryDto, Long>, JpaSpecificationExecutor<BeneficiaryDto> {}
