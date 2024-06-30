package com.cm.welfarecmcity.api.employee.model;

import lombok.Data;

import java.util.Date;

@Data
public class EmpByAdminRes {
    private Long id;
    private String employeeCode;
    private String prefix;
    private String firstName;
    private String lastName;
    private String idCard;
    private String gender;
    private Date birthday;
    private int age;
    private int employeeStatus;
    private String levelName;
    private String employeeTypeName;
    private String positionName;
    private String departmentName;
    private String affiliationName;
    private String bureauName;
    private String image;
}
