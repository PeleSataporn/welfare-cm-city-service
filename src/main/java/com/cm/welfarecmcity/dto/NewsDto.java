package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "News")
public class NewsDto extends BaseDto {

  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @OneToOne(fetch = FetchType.LAZY)
  private FileResourceDto coverImg;

  @OneToMany(fetch = FetchType.LAZY)
  private List<FileResourceDto> listImg;
}
