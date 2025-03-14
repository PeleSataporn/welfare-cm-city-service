package com.cm.welfarecmcity.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestModel<C, O> {
  C criteria;

  O order;

  PageReq pageReq = PageReq.getDefault();
}
