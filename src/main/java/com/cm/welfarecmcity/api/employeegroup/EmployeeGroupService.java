package com.cm.welfarecmcity.api.employeegroup;

import com.cm.welfarecmcity.dto.EmployeeGroupDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeGroupService {

    @Autowired
    private EmployeeGroupRepository employeeGroupRepository;

    @Autowired
    private ResponseDataUtils responseDataUtils;

    public ResponseModel<ResponseId> add(EmployeeGroupDto dto) {
        val employeeGroup = employeeGroupRepository.save(dto);
        return responseDataUtils.insertDataSuccess(employeeGroup.getId());
    }
}
