package com.car2go.carpolygon;

import com.github.caryyu.spring.embedded.redisserver.RedisServerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbededRedisTestConfiguration {

  @Bean
  public RedisServerConfiguration redisServerConfiguration() {
    return new RedisServerConfiguration();
  }

}