package com.cm.welfarecmcity.api.admin.model;

import lombok.Data;

@Data
public class AdminConfigReq {
    private Long configId;
    private String name;
    private String value;
    private String description;
}
