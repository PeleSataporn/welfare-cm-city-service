package com.cm.welfarecmcity.logic.stock.model.search;

import com.cm.welfarecmcity.constant.OrderSortType;

public record StockByAdminOrderReqDto(
        OrderSortType id, OrderSortType createDate
) {
}
