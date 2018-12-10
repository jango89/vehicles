package com.car2go.carpolygon.gateway.http;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.car2go.carpolygon.json.VehicleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

@Component
public class VehicleInfoClient {

  @Value("${vehicle.info.url}")
  public String url;
  private final static Logger LOGGER = LoggerFactory.getLogger(VehicleInfoClient.class);

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  public VehicleInfoClient(RestTemplate restTemplate,
      ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  public List<VehicleResponse> fetch(String location) {
    URI uri = new UriTemplate(url.concat("/{location}"))
        .expand(location);
    LOGGER.info("Vehicles are retrieved for {} ", location);
    return (List<VehicleResponse>) restTemplate
        .exchange(uri, GET, new HttpEntity<>(headers()), List.class)
        .getBody()
        .stream()
        .map(this::convert)
        .collect(toList());
  }

  private VehicleResponse convert(Object item) {
    return objectMapper.convertValue(item, VehicleResponse.class);
  }

  private MultiValueMap<String, String> headers() {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("content-type", APPLICATION_JSON_VALUE);
    return httpHeaders;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

}
