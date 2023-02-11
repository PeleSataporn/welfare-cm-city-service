package com.cm.welfarecmcity.dto.guarantor;

import com.cm.welfarecmcity.dto.GuarantorDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/guarantor")
public class GuarantorController {

    @Autowired
    private GuarantorService guarantorService;

    @PostMapping
    public ResponseModel<ResponseId> add(@RequestBody GuarantorDto dto) {
        return guarantorService.add(dto);
    }
}
