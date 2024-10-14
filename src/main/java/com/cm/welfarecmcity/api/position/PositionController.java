package com.cm.welfarecmcity.api.position;

import com.cm.welfarecmcity.dto.PositionsDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/position")
public class PositionController {

  @Autowired private PositionService positionService;

  @PostMapping("search")
  public List<PositionsDto> searchPosition() {
    return positionService.searchPosition();
  }
}
