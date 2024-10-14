package com.cm.welfarecmcity.api.document;

import com.cm.welfarecmcity.dto.DocumentDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<DocumentDto, Long> {
  List<DocumentDto> findAllByOrderByCreateDateDesc();
}
