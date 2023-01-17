package com.cm.welfarecmcity.api.addressbook;

import com.cm.welfarecmcity.dto.AddressDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/address-book")
public class AddressController {

  @Autowired
  private AddressService addressBookService;

  @PostMapping
  public ResponseModel<ResponseId> add(@RequestBody AddressDto dto) {
    return addressBookService.add(dto);
  }
}
