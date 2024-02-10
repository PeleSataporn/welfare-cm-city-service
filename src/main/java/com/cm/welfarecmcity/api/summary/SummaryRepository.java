package com.cm.welfarecmcity.api.summary;

import com.cm.welfarecmcity.dto.SummaryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SummaryRepository extends JpaRepository<SummaryDto, Long>, JpaSpecificationExecutor<SummaryDto> {
  @Query("SELECT s FROM SummaryDto s WHERE s.sumMonth = :month AND s.sumYear = :year")
  SummaryDto findByMonthAndYear(String month, String year);
}
