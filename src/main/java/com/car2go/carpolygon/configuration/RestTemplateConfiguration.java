package com.car2go.carpolygon.configuration;

import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

  @Bean
  public RestTemplate restTemplate() {
    final RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(new MyGsonHttpMessageConverter());
    return restTemplate;
  }

  class MyGsonHttpMessageConverter extends GsonHttpMessageConverter {

    public MyGsonHttpMessageConverter() {
      List<MediaType> types = Arrays.asList(
          new MediaType("text", "plain", DEFAULT_CHARSET)
      );
      super.setSupportedMediaTypes(types);
    }
  }
}
