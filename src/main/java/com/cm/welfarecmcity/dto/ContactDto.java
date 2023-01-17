package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Contact")
public class ContactDto extends BaseDto {

  private String mobile;
  private String officePhone;
  private String email;
  private String fax;
  private String lineId;
  private String facebook;
}
