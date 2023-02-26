package com.cm.welfarecmcity.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ForgetPasswordDto {
    private String email;
    private String newPassword;
    private String newPassword2;
    private String idCard;
    private String employeeCode;
    private Long id;
    private Long userId;
}
