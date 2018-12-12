package com.car2go.carpolygon.gateway.http;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.car2go.carpolygon.configuration.CustomGsonHttpMessageConverter;
import com.car2go.carpolygon.json.GeoOptions;
import com.car2go.carpolygon.json.GeoPolygonResponse;
import com.car2go.carpolygon.json.GeometryJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class GeoPolygonClientTest {

  private GeoPolygonClient geoPolygonClient;

  private RestTemplate restTemplate = new RestTemplate();

  private ObjectMapper objectMapper = new ObjectMapper();

  private MockRestServiceServer mockServer;

  @Before
  public void init() {
    restTemplate.getMessageConverters().add(new CustomGsonHttpMessageConverter());
    geoPolygonClient = new GeoPolygonClient(restTemplate, objectMapper);
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  public void givenUrlShouldFetchPolygonDetails() throws JsonProcessingException {
    geoPolygonClient.setUrl("test");
    mockServer.expect(requestTo("test"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(new ObjectMapper().writeValueAsBytes(geoPolygonResponses()),
            MediaType.TEXT_PLAIN));
    final List<GeoPolygonResponse> geometryJson = geoPolygonClient.fetch();
    assertNotNull(geometryJson);
    mockServer.verify();
  }

  private List<GeoPolygonResponse> geoPolygonResponses() {
    List<GeoPolygonResponse> geoPolygonResponses = new LinkedList<>();
    GeoPolygonResponse geoPolygonResponse = new GeoPolygonResponse();
    final GeoOptions geoOptions = getGeoOptions();
    geoPolygonResponse.setOptions(geoOptions);
    geoPolygonResponse.setGeometry(geometry());
    return geoPolygonResponses;
  }

  private GeometryJson geometry() {
    final GeometryJson geometry = new GeometryJson();
    final LinkedList<LinkedList<Double[]>> objects = new LinkedList<>();
    final Double[] corods = new Double[2];
    corods[0] = 9.02;
    corods[1] = 48.02;
    final LinkedList<Double[]> values = new LinkedList<>();
    values.add(corods);
    objects.add(values);
    geometry.setCoordinates(objects);
    return geometry;
  }

  private GeoOptions getGeoOptions() {
    final GeoOptions geoOptions = new GeoOptions();
    geoOptions.setActive(true);
    geoOptions.setExcluded(false);
    return geoOptions;
  }

}