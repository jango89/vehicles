package com.car2go.carpolygon.gateway;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.car2go.carpolygon.json.GeoPolygonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

@Component
public class GeoPolygonInterface {

  @Value("${geo.polygon.json.url}")
  public String url;

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  public GeoPolygonInterface(RestTemplate restTemplate,
      ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  public List<GeoPolygonResponse> fetch() {
    URI uri = new UriTemplate(url).expand();
    return (List<GeoPolygonResponse>) restTemplate
        .exchange(uri, GET, new HttpEntity<>(headers()), List.class)
        .getBody()
        .stream()
        .map(this::getReadValue)
        .filter(isPoligonActive())
        .collect(toList());
  }

  private Predicate isPoligonActive() {
    return resp -> ((GeoPolygonResponse) resp).isValid();
  }

  private GeoPolygonResponse getReadValue(Object item) {
    return objectMapper.convertValue(item, GeoPolygonResponse.class);
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
