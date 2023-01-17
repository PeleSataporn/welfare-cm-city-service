package com.cm.welfarecmcity.api.contact;

import com.cm.welfarecmcity.dto.ContactDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContactRepository extends JpaRepository<ContactDto, Long>, JpaSpecificationExecutor<ContactDto> {}
