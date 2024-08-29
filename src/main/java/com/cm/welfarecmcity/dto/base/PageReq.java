package com.cm.welfarecmcity.dto.base;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageReq {

    @Min(1)
    private Integer page = 1;

    @Min(1)
    @Max(100)
    private Integer pageSize = 50;

    public static PageReq getDefault() {
        return new PageReq(1, 50);
    }
}
