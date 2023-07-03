package com.cm.welfarecmcity.api.fileresource;

import com.cm.welfarecmcity.dto.FileResourceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FileResourceRepository extends JpaRepository<FileResourceDto, Long>, JpaSpecificationExecutor<FileResourceDto> {}
