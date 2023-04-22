package com.cm.welfarecmcity.mapper;

import com.cm.welfarecmcity.api.employee.model.EmpEditReq;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.mapper.base.BaseMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface MapStructMapper {
  EmployeeDto reqToEmployee(EmpEditReq req);
  //  StockRes stockRes(StockDto dto);
  //
  //  EmployeeRes employeeRes(EmployeeDto dto);

  //  @Mapping(target = "id", ignore = true)
  //  BeneficiaryDto reqToBeneficiary(BeneficiaryDto req);
}
