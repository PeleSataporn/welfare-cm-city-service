package com.cm.welfarecmcity.api.stock.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStockReq {
    private Long id;
    private int stockValue;
}
