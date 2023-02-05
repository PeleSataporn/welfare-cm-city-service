package com.cm.welfarecmcity.exception.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeException extends RuntimeException {
  private final String notFound = "EMPLOYEE_NOT_FOUND_01";

  public EmployeeException(String message) {
    super(message);
  }
}
