package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.sql.Blob;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "AdminConfig")
public class AdminConfigDto extends BaseDto {

  private String name;
  private String value;
  private String description;
  @Lob private Blob image;
}
