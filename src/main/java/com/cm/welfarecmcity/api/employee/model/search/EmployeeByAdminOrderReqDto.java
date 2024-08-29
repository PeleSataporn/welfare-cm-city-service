package com.cm.welfarecmcity.api.employee.model.search;

import com.cm.welfarecmcity.constant.OrderSortType;

public record EmployeeByAdminOrderReqDto(
        OrderSortType id, OrderSortType createDate
) {
}
