package com.cm.welfarecmcity.api.marital;

import com.cm.welfarecmcity.api.affiliation.AffiliationService;
import com.cm.welfarecmcity.dto.AffiliationDto;
import com.cm.welfarecmcity.dto.MaritalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/marital")
public class MaritalController {

    @Autowired
    private MaritalService maritalService;

    @PostMapping("search")
    public List<MaritalDto> searchMarital() {
        return maritalService.searchMarital();
    }
}
