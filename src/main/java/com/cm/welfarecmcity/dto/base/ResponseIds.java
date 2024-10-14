package com.cm.welfarecmcity.dto.base;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseIds {
  List<Long> ids;
}
