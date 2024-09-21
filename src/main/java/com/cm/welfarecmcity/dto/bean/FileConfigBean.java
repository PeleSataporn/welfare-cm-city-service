package com.cm.welfarecmcity.dto.bean;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileConfigBean {

    private String month;
    private String year;

    @Lob
    private byte[] fileData;
    private String fileName;

}
