package com.cm.welfarecmcity.api.fileresource.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddImageReq {

  private Long empId;
  private MultipartFile image;
}
