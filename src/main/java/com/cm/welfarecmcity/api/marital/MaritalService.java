package com.cm.welfarecmcity.api.marital;

import com.cm.welfarecmcity.api.affiliation.AffiliationRepository;
import com.cm.welfarecmcity.dto.AffiliationDto;
import com.cm.welfarecmcity.dto.MaritalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaritalService {

    @Autowired
    private MaritalRepository maritalRepository;

    public List<MaritalDto> searchMarital() {
        return maritalRepository.findAll();
    }
}
