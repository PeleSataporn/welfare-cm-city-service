package com.cm.welfarecmcity.api.user;

import com.cm.welfarecmcity.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<UserDto, Long>, JpaSpecificationExecutor<UserDto> {}
