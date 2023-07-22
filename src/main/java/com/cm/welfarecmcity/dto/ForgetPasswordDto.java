package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ForgetPasswordDto extends BaseDto {

  private String tel;
  private String newPassword;
  private String newPassword2;
  private String idCard;
  private String employeeCode;
  //    private Long id;
  private Long userId;
}
