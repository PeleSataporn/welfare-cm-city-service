package com.cm.welfarecmcity.api.document.model;

import java.util.Date;
import lombok.Builder;

@Builder
public record DocumentRes(Long id, String name, Date createDate, Long size) {}
