package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Affiliation")
//@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class AffiliationDto extends BaseDto {

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bureau_id")
  @JsonIgnore
  //  @JsonManagedReference(value = "bureau_id")
  private BureauDto bureau;
}
