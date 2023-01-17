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
@Table(name = "Address")
public class AddressDto extends BaseDto {

  @Comment("ที่อยู่ ต. อ. จ. และ รหัสไปรณี ให้เป็น text")
  private String stringAddress;

  private String code;

  // บ้านเลขที่
  private String no;
  // หมู่
  private String section;
  // อาคาร
  private String building;
  // เลขที่ห้อง
  private String room;
  // ชั้นที่
  private String floor;
  // หมู่บ้าน
  private String village;
  // ตรอก-ซอย
  private String alley;
  // ถนน
  private String road;
  // ตำบล-แขวง
  private String district;
  // อำเภอ-เขต
  private String amphur;
  // จังหวัด
  private String province;
  // รหัสไปรษณีย์
  private String postcode;
  // ประเทศ
  private String country;
  // ละติจูด
  private double latitude;
  // ลองจิจูด
  private double longitude;
}
