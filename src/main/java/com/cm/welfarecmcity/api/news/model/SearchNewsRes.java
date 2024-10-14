package com.cm.welfarecmcity.api.news.model;

import java.util.Date;
import lombok.Data;

@Data
public class SearchNewsRes {

  private Long id;
  private String name;
  private String description;
  private Date createDate;
}
