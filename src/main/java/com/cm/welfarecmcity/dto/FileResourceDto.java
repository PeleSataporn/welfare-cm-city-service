package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.*;
import java.sql.Blob;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "FileResource")
public class FileResourceDto extends BaseDto {

  @Lob private Blob image;

  @Lob private Blob imageAddress;

  @Lob private Blob imageIdCard;
}
