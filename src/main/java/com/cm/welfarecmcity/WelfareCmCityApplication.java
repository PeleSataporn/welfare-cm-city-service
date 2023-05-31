package com.cm.welfarecmcity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@EnableScheduling
//extends SpringBootServletInitializer
@SpringBootApplication
public class WelfareCmCityApplication {

  public static void main(String[] args) {
    SpringApplication.run(WelfareCmCityApplication.class, args);
  }

  //  @Override
  //  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
  //    return application.sources(WelfareCmCityApplication.class);
  //  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://203.159.93.121:8080/");
      }
    };
  }
}
