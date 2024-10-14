package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "User")
public class UserDto extends BaseDto {

  private String username;
  private String password;
  private Boolean passwordFlag;
}
