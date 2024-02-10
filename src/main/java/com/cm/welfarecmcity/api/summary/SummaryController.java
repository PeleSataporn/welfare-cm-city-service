package com.cm.welfarecmcity.api.summary;

import com.cm.welfarecmcity.api.summary.model.GetSummaryReq;
import com.cm.welfarecmcity.dto.SummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/summary")
public class SummaryController {

  @Autowired
  private SummaryService summaryService;

  @PostMapping("get-summary")
  public SummaryDto getSummary(@RequestBody GetSummaryReq req) {
    return summaryService.searchSummaryByMonthAndYear(req.getMonthCurrent(), req.getYearCurrent());
  }
}
