package com.cm.welfarecmcity.api.employee.model.search;

public record EmployeeByAdminReqDto(
    String employeeCode, String firstName, String lastName, String idCard) {}
