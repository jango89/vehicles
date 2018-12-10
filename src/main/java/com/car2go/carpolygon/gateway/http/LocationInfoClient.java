package com.car2go.carpolygon.gateway.http;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.car2go.carpolygon.json.LocationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

@Component
public class LocationInfoClient {

  @Value("${location.info.url}")
  public String url;

  private final static Logger LOGGER = LoggerFactory.getLogger(LocationInfoClient.class);

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  public LocationInfoClient(RestTemplate restTemplate,
      ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  public Optional<LocationResponse> fetch(String location) {
    URI uri = new UriTemplate(url).expand();
    LOGGER.info("Location data is being fetched from API");
    return restTemplate
        .exchange(uri, GET, new HttpEntity<>(headers()), List.class)
        .getBody().stream()
        .map(this::convert)
        .filter(locationResp -> validateName(locationResp, location))
        .map(this::fetchLocation)
        .findFirst();
  }

  private LocationResponse fetchLocation(Object locationResponse) {
    URI uri = new UriTemplate(url.concat("/{id}"))
        .expand(((LocationResponse) locationResponse).getId());
    return restTemplate.exchange(uri, GET, new HttpEntity<>(headers()), LocationResponse.class)
        .getBody();
  }

  private boolean validateName(Object locationResp, String location) {
    final LocationResponse locationResponse = (LocationResponse) locationResp;
    return !StringUtils.isEmpty(locationResponse.getName()) &&
        location.equalsIgnoreCase(locationResponse.getName());
  }

  private LocationResponse convert(Object item) {
    return objectMapper.convertValue(item, LocationResponse.class);
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
