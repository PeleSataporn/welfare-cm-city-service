package com.cm.welfarecmcity.dto.base;

import lombok.Data;

@Data
public class ResponseModel<T> {
  String result = "OK";
  String message = "";
  T data;
}
