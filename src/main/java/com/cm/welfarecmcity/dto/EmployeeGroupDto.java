package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "EmployeeGroup")
public class EmployeeGroupDto extends BaseDto {

    private String nameTh;

    private String nameEn;

    private String description;
}
