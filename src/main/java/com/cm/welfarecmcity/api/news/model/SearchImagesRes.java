package com.cm.welfarecmcity.api.news.model;

import lombok.Builder;

@Builder
public record SearchImagesRes(
        Long id,
        String image
){
}
