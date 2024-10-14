package com.cm.welfarecmcity.api.level;

import com.cm.welfarecmcity.dto.LevelDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LevelRepository
    extends JpaRepository<LevelDto, Long>, JpaSpecificationExecutor<LevelDto> {}
