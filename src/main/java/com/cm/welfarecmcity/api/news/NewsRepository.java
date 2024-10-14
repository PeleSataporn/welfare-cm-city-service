package com.cm.welfarecmcity.api.news;

import com.cm.welfarecmcity.dto.NewsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NewsRepository
    extends JpaRepository<NewsDto, Long>, JpaSpecificationExecutor<NewsDto> {}
