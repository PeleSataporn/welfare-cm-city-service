package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Affiliation")
public class AffiliationDto extends BaseDto {

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bureau_id")
  @JsonIgnore
  private BureauDto bureau;
}
