package com.cm.welfarecmcity.logic.login.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRes {
    private Long id;
    private int employeeStatus;
    private Boolean passwordFlag;
}
