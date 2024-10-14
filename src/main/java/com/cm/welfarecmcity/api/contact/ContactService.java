package com.cm.welfarecmcity.api.contact;

import com.cm.welfarecmcity.dto.ContactDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

  @Autowired private ContactRepository contactRepository;

  @Autowired private ResponseDataUtils responseDataUtils;

  @Transactional
  public ResponseModel<ResponseId> add(ContactDto dto) {
    val contact = contactRepository.save(dto);
    return responseDataUtils.insertDataSuccess(contact.getId());
  }
}
