package com.cm.welfarecmcity.api.level;

import com.cm.welfarecmcity.dto.LevelDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/level")
public class LevelController {

  @Autowired private LevelService levelService;

  @PostMapping("search")
  public List<LevelDto> searchLevel() {
    return levelService.searchLevel();
  }
}
