package com.cm.welfarecmcity.exception.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserException extends RuntimeException {
  private final String notFound = "USER_NOT_FOUND_01";

  public UserException(String message) {
    super(message);
  }
}
