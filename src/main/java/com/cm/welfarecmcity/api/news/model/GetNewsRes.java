package com.cm.welfarecmcity.api.news.model;

import lombok.Data;

@Data
public class GetNewsRes {

  private Long id;
  private String name;
  private String description;
  private Long coverImgId;
}
