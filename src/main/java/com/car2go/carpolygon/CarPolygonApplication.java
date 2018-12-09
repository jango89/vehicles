package com.car2go.carpolygon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.car2go.carpolygon")
@EnableSwagger2
public class CarPolygonApplication {

  public static void main(String[] args) {
    SpringApplication.run(CarPolygonApplication.class, args);
  }
}
