package com.cm.welfarecmcity.api.summary;

import com.cm.welfarecmcity.dto.SummaryDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

  @Autowired
  private SummaryRepository summaryRepository;

  @Transactional
  public SummaryDto searchSummaryByMonthAndYear(String month, String year) {
    return summaryRepository.findByMonthAndYear(month, year);
  }
}
