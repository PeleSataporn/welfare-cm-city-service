package com.cm.welfarecmcity.utils;

import com.cm.welfarecmcity.dto.base.PageReq;

public class PaginationUtils {
    private PaginationUtils() {}

    public static String pagination(PageReq page) {
        int pageSize = page.getPageSize();
        int offset = (page.getPage() - 1) * pageSize;
        return String.format(" LIMIT %d OFFSET %d", pageSize, offset);
    }
}
