package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Document")
public class DocumentDto extends BaseDto {

  private String name;

  @Lob
  @Column(name = "pdf_file", columnDefinition = "LONGBLOB")
  private byte[] pdfFile;
}
