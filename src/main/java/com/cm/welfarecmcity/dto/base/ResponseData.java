package com.cm.welfarecmcity.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ResponseData {
  private Long id;
  private String statusEmployee;
}
