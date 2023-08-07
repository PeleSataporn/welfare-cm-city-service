package com.cm.welfarecmcity.api.news.model;

import lombok.Data;

import java.util.Date;

@Data
public class SearchNewsRes {

  private Long id;
  private String name;
  private String description;
  private Date createDate;
}
