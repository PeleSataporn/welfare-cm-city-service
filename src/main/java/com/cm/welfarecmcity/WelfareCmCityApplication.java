package com.cm.welfarecmcity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WelfareCmCityApplication {

  public static void main(String[] args) {
    SpringApplication.run(WelfareCmCityApplication.class, args);
  }

}
