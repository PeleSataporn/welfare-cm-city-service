package com.cm.welfarecmcity.api.admin;

import com.cm.welfarecmcity.dto.AdminConfigDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminConfigRepository extends JpaRepository<AdminConfigDto, Long>, JpaSpecificationExecutor<AdminConfigDto> {

}
