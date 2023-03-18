package com.cm.welfarecmcity.dto.base;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.AccessType;

@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class BaseDto {

  @Id
  //  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @AccessType(AccessType.Type.PROPERTY)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date lastUpdate;

  @Deprecated(since = "")
  @Column(columnDefinition = "boolean default true")
  private Boolean active = true;

  @Column(columnDefinition = "boolean default false")
  private Boolean deleted = false;
}
