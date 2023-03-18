package com.cm.welfarecmcity.api.level;

import com.cm.welfarecmcity.dto.LevelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelService {

  @Autowired
  private LevelRepository levelRepository;

  public List<LevelDto> searchLevel() {
    return levelRepository.findAll();
  }
}
