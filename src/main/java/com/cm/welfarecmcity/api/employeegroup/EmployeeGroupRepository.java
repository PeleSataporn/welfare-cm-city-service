package com.cm.welfarecmcity.api.employeegroup;

import com.cm.welfarecmcity.dto.EmployeeGroupDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeGroupRepository extends JpaRepository<EmployeeGroupDto, Long>, JpaSpecificationExecutor<EmployeeGroupDto> {}
