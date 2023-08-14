package com.cm.welfarecmcity.mapper;

import com.cm.welfarecmcity.api.beneficiary.model.BeneficiaryRes;
import com.cm.welfarecmcity.api.employee.model.EmpByAdminRes;
import com.cm.welfarecmcity.api.employee.model.EmpEditReq;
import com.cm.welfarecmcity.api.notification.model.NotificationRes;
import com.cm.welfarecmcity.dto.BeneficiaryDto;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.PetitionNotificationDto;
import com.cm.welfarecmcity.logic.employee.model.EmployeeRes;
import com.cm.welfarecmcity.mapper.base.BaseMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = BaseMapperConfig.class)
public interface MapStructMapper {
  EmployeeDto reqToEmployee(EmpEditReq req);

  EmployeeRes employeeToRes(EmployeeDto dto);

  NotificationRes notificationToRes(PetitionNotificationDto dto);

  EmpByAdminRes employeeToByAdminRes(EmployeeDto dto);

  BeneficiaryRes beneficiaryToRes(BeneficiaryDto dto);
}
