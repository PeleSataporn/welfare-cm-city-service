package com.cm.welfarecmcity.logic.stock.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockRes {
    private Long id;
    private int stockValue;
    private int stockAccumulate;
    private String employeeCode;
    private String prefix;
    private String firstName;
    private String lastName;
    private int employeeStatus;
    private String idCard;
    private String status;
}
