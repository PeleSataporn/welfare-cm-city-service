package com.cm.welfarecmcity.exception.entity;

import com.cm.welfarecmcity.exception.ResourceNotFoundException;
import lombok.Getter;
import lombok.Setter;

public class DocumentException extends ResourceNotFoundException {
  public DocumentException(String message) {
    super(message);
  }
}

