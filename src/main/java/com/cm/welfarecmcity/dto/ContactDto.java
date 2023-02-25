package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name = "Contact")
public class ContactDto extends BaseDto {

  private String tel;
  private String officePhone;
  private String email;
  private String fax;
  private String lineId;
  private String facebook;

  @Comment("ที่อยู่ ต. อ. จ. และ รหัสไปรณี ให้เป็น text")
  private String address;
}
