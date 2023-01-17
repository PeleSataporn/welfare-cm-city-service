package com.cm.welfarecmcity.api.addressbook;

import com.cm.welfarecmcity.dto.AddressDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  public ResponseModel<ResponseId> add(AddressDto dto) {
    val address = addressRepository.save(dto);
    return responseDataUtils.insertDataSuccess(address.getId());
  }
}
