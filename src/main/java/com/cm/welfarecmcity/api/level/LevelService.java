package com.cm.welfarecmcity.api.level;

import com.cm.welfarecmcity.dto.LevelDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LevelService {

  @Autowired private LevelRepository levelRepository;

  @Transactional
  public List<LevelDto> searchLevel() {
    Sort sort = Sort.by("name");
    return levelRepository.findAll(sort);
  }
}
