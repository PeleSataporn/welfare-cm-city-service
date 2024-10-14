package com.cm.welfarecmcity.api.department;

import com.cm.welfarecmcity.dto.DepartmentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DepartmentRepository
    extends JpaRepository<DepartmentDto, Long>, JpaSpecificationExecutor<DepartmentDto> {}
