package com.cm.welfarecmcity.api.document.model;

import lombok.Builder;

import java.util.Date;

@Builder
public record DocumentRes (
        Long id,
        String name,
        Date createDate,
        Long size
) {
}
