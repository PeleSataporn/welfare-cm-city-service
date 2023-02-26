package com.cm.welfarecmcity.api.affiliation;

import com.cm.welfarecmcity.dto.AffiliationDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/affiliation")
public class AffiliationController {

  @Autowired
  private AffiliationService affiliationService;

  @PostMapping("search")
  public List<AffiliationDto> searchAffiliation() {
    return affiliationService.searchAffiliation();
  }

  @PostMapping("search-by-bureau/{bureauId}")
  public List<AffiliationDto> searchByBureau(@PathVariable Long bureauId) {
    return affiliationService.searchByBureau(bureauId);
  }
}
