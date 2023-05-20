package com.cm.welfarecmcity.api.affiliation;

import com.cm.welfarecmcity.dto.AffiliationDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AffiliationService {

  @Autowired
  private AffiliationRepository affiliationRepository;

  @Transactional
  public List<AffiliationDto> searchAffiliation() {
    return affiliationRepository.findAll();
  }

  @Transactional
  public List<AffiliationDto> searchByBureau(Long bureauId) {
    return affiliationRepository.findAllByBureau_Id(bureauId);
  }
}
