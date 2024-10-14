package com.cm.welfarecmcity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SizeImageEnum {
  XS("xs", null, 40),
  S("s", null, 100),
  M("m", null, 400),
  L("l", null, 600),
  XL("xl", null, 800),
  XXL("xxl", null, 1280);

  private final String size;

  private final Integer height;

  private final Integer width;
}
