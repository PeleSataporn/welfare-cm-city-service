package com.cm.welfarecmcity.api.position;

import com.cm.welfarecmcity.dto.PositionsDto;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

  @Autowired
  private PositionRepository positionRepository;

  @Transactional
  public List<PositionsDto> searchPosition() {
    Sort sort = Sort.by("name");
    return positionRepository.findAll(sort);
  }
}
