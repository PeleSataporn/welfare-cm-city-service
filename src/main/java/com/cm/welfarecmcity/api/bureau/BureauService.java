package com.cm.welfarecmcity.api.bureau;

import com.cm.welfarecmcity.dto.BureauDto;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BureauService {

  @Autowired private BureauRepository bureauRepository;

  @Transactional
  public List<BureauDto> searchBureau() {
    Sort sort = Sort.by("name");
    return bureauRepository.findAll(sort);
  }
}
