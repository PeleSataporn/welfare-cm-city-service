package com.cm.welfarecmcity.api.news.model;

import lombok.Data;

@Data
public class CreateNewsReq {
  private Long newsId;
  private String name;
  private String description;
  //  private Long coverImgId;
  //    private List<Long> listImg;
}
