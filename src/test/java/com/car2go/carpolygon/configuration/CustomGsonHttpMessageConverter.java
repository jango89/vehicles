package com.car2go.carpolygon.configuration;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

public class CustomGsonHttpMessageConverter extends GsonHttpMessageConverter {

  public CustomGsonHttpMessageConverter() {
    List<MediaType> types = Arrays.asList(
        new MediaType("text", "plain", DEFAULT_CHARSET)
    );
    super.setSupportedMediaTypes(types);
  }
}
