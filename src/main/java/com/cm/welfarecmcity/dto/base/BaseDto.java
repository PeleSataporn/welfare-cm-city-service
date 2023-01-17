package com.cm.welfarecmcity.dto.base;

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
public class BaseDto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @AccessType(AccessType.Type.PROPERTY)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date lastUpdate;

  @Deprecated(since = "")
  private Boolean active = true;

  private Boolean deleted = false;
}
