package com.cm.welfarecmcity.api.employeegroup;

import com.cm.welfarecmcity.dto.EmployeeGroupDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/employee-group")
public class EmployeeGroupController {

    @Autowired
    private EmployeeGroupService employeeGroupService;

    @PostMapping
    public ResponseModel<ResponseId> add(@RequestBody EmployeeGroupDto dto) {
        return employeeGroupService.add(dto);
    }
}
