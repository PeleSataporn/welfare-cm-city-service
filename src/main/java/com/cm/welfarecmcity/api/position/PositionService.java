package com.cm.welfarecmcity.api.position;

import com.cm.welfarecmcity.dto.PositionsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    public List<PositionsDto> searchPosition() {
        return positionRepository.findAll();
    }
}
