package com.cm.welfarecmcity.api.employeetype;

import com.cm.welfarecmcity.dto.EmployeeTypeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeTypeRepository
    extends JpaRepository<EmployeeTypeDto, Long>, JpaSpecificationExecutor<EmployeeTypeDto> {}
