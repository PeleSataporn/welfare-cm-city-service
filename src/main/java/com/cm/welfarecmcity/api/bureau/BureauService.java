package com.cm.welfarecmcity.api.bureau;

import com.cm.welfarecmcity.dto.BureauDto;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BureauService {

  @Autowired
  private BureauRepository bureauRepository;

  @Transactional
  public List<BureauDto> searchBureau() {
    return bureauRepository.findAll();
  }
}
