package com.cm.welfarecmcity.api.admin.model;

import java.sql.Blob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminConfigRes {
  private Long configId;
  private String name;
  private String value;
  private String description;
  private Blob image;
}
