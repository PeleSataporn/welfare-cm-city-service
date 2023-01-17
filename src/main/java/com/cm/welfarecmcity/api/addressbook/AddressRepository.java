package com.cm.welfarecmcity.api.addressbook;

import com.cm.welfarecmcity.dto.AddressDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AddressRepository extends JpaRepository<AddressDto, Long>, JpaSpecificationExecutor<AddressDto> {}
