package com.cm.welfarecmcity.utils.orderby;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SortOrderBy {

  @Transactional
  public Sort.Direction orderBy(String value) {
    if (value.equalsIgnoreCase("desc")) {
      return Sort.Direction.DESC;
    } else {
      return Sort.Direction.ASC;
    }
  }
}
