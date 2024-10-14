package com.cm.welfarecmcity.api.contact;

import com.cm.welfarecmcity.dto.ContactDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/contact")
public class ContactController {

  @Autowired private ContactService contactService;

  @PostMapping
  public ResponseModel<ResponseId> add(@RequestBody ContactDto dto) {
    return contactService.add(dto);
  }
}
