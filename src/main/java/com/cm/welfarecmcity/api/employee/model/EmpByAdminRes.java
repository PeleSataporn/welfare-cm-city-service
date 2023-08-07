package com.cm.welfarecmcity.api.employee.model;

import lombok.Data;

@Data
public class EmpByAdminRes {
    private Long id;
    private String employeeCode;
    private String prefix;
    private String firstName;
    private String lastName;
    private String idCard;
}
