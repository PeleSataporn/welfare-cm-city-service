package com.cm.welfarecmcity.dto.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ResponseModel<T>  {
    String result = "OK";
    String message = "";
    T data;
}
