package com.cm.welfarecmcity.api.affiliation;

import com.cm.welfarecmcity.dto.AffiliationDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AffiliationService {

  @Autowired
  private AffiliationRepository affiliationRepository;

  public List<AffiliationDto> searchAffiliation() {
    return affiliationRepository.findAll();
  }
}
