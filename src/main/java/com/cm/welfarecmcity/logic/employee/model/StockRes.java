package com.cm.welfarecmcity.logic.employee.model;

import lombok.Data;

import java.util.List;

@Data
public class StockRes {
    private Long id;
    private List<StockDetailRes> stockDetails;
}
