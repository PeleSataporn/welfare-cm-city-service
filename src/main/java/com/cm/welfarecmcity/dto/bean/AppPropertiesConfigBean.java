package com.cm.welfarecmcity.dto.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "server")
public class AppPropertiesConfigBean {

  private String pathFile;
}
