package com.cm.welfarecmcity.utils.orderby;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SortOrderBy {

  public Sort.Direction orderBy(String value) {
    if (value.equalsIgnoreCase("desc")) {
      return Sort.Direction.DESC;
    } else {
      return Sort.Direction.ASC;
    }
  }
}
