package com.cm.welfarecmcity.api.newsfiledetail;

import com.cm.welfarecmcity.dto.NewsFileDetailDto;
import com.cm.welfarecmcity.dto.embeddable.NewsFileDetailKey;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsFileDetailRepository
    extends JpaRepository<NewsFileDetailDto, NewsFileDetailKey> {

  Optional<List<NewsFileDetailDto>> findByNewsId(Long newsId);

  Optional<NewsFileDetailDto> findByFileResourceId(Long fileResourceId);
}
