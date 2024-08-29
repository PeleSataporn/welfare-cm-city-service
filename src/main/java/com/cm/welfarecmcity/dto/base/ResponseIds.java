package com.cm.welfarecmcity.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponseIds {
    List<Long> ids;
}
