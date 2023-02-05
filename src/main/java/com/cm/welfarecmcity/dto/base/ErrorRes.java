package com.cm.welfarecmcity.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorRes<T> {

  private String code;
  private String message;
  private T data;
}
