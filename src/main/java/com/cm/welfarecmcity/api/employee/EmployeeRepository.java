package com.cm.welfarecmcity.api.employee;

import com.cm.welfarecmcity.dto.EmployeeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeRepository extends JpaRepository<EmployeeDto, Long>, JpaSpecificationExecutor<EmployeeDto> {
  public EmployeeDto getByUserId(long userId);

  public EmployeeDto getByStockId(long stockId);
}
