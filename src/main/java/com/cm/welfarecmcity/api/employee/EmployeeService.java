package com.cm.welfarecmcity.api.employee;

import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ResponseDataUtils responseDataUtils;

    public ResponseModel<ResponseId> add(EmployeeDto dto) {
        val employee = employeeRepository.save(dto);
        return responseDataUtils.insertDataSuccess(employee.getId());
    }
}
