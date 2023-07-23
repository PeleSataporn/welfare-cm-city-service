package com.cm.welfarecmcity.api.admin.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
public class AdminConfigRes {
    private Long configId;
    private String name;
    private String value;
    private String description;
    private Blob image;
}
