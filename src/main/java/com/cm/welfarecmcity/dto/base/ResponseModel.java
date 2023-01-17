package com.cm.welfarecmcity.dto.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel<T>  {
    private final String result = "OK";
    private String message;
    private T data;
}
