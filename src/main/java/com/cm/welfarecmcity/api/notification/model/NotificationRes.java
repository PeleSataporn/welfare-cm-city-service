package com.cm.welfarecmcity.api.notification.model;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationRes {
    private int status;
    private String reason;
    private String description;
    private Date createDate;
    private NotifyEmployeeRes employee;

}
