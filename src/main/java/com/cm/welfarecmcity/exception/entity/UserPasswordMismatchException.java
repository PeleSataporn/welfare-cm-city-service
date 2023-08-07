package com.cm.welfarecmcity.exception.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordMismatchException extends RuntimeException {

  private final String notFound = "USER_PASSWORD_MISMATCH_02";

  public UserPasswordMismatchException(String message) {
    super(message);
  }
}
