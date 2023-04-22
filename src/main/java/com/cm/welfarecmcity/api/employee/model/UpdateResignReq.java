package com.cm.welfarecmcity.api.employee.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateResignReq {
    private Long id;
    private String reason;
}
