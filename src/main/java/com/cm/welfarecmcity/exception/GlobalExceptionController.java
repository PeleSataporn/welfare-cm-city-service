package com.cm.welfarecmcity.exception;

import com.cm.welfarecmcity.dto.base.ErrorRes;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController {

  @ExceptionHandler
  public ResponseEntity<ErrorRes<Void>> handleEmployeeNotFoundException(EmployeeException ex) {
    val res = new ErrorRes<Void>(ex.getNotFound(), ex.getMessage(), null);
    return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
  }
}
