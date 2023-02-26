package com.cm.welfarecmcity.api.bureau;

import com.cm.welfarecmcity.dto.BureauDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/bureau")
public class BureauController {

    @Autowired
    private BureauService bureauService;

    @PostMapping("search")
    public List<BureauDto> searchBureau() {
        return bureauService.searchBureau();
    }
}
