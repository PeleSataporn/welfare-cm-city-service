package com.cm.welfarecmcity.mapper;

import com.cm.welfarecmcity.api.employee.model.EmpEditReq;
import com.cm.welfarecmcity.dto.BeneficiaryDto;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.mapper.base.BaseMapperConfig;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface MapStructMapper {
  EmployeeDto reqToEmployee(EmpEditReq req);

//  @Mapping(target = "id", ignore = true)
//  BeneficiaryDto reqToBeneficiary(BeneficiaryDto req);
}
