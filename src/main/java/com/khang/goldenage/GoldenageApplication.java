package com.khang.goldenage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GoldenageApplication {

  public static void main(String[] args) {
    SpringApplication.run(GoldenageApplication.class, args);
  }

}
