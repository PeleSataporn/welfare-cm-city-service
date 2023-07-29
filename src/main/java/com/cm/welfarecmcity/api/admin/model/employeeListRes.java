package com.cm.welfarecmcity.api.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class employeeListRes {
    private Long empId;
    private String employeeCode;
    private String fullName;
    private String departmentName;
    private String employeeTypeName;
    private String positionsName;
    private Boolean adminFlag;
}
