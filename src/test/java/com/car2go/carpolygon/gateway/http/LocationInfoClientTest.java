package com.car2go.carpolygon.gateway.http;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.car2go.carpolygon.configuration.CustomGsonHttpMessageConverter;
import com.car2go.carpolygon.json.LocationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class LocationInfoClientTest {

  private LocationInfoClient locationInfoClient;

  private RestTemplate restTemplate = new RestTemplate();

  private ObjectMapper objectMapper = new ObjectMapper();

  private MockRestServiceServer mockServer;

  @Before
  public void init() {
    restTemplate.getMessageConverters().add(new CustomGsonHttpMessageConverter());
    locationInfoClient = new LocationInfoClient(restTemplate, objectMapper);
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  public void givenUrlShouldFetchPolygonDetails() throws JsonProcessingException {
    locationInfoClient.setUrl("test");
    mockServer.expect(requestTo("test"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(new ObjectMapper().writeValueAsBytes(locationResponse()),
            MediaType.TEXT_PLAIN));
    mockServer.expect(requestTo("test/1"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(new ObjectMapper().writeValueAsBytes(getLocationResponse()),
            MediaType.TEXT_PLAIN));
    final Optional<LocationResponse> locationResponse = locationInfoClient.fetch("Stuttgart");
    assertNotNull(locationResponse.get());
    assertTrue(locationResponse.get().getName().equalsIgnoreCase("stuttgart"));
    mockServer.verify();
  }

  private List locationResponse() {
    List list = new LinkedList();
    LocationResponse locationResponse = getLocationResponse();
    list.add(locationResponse);
    return list;
  }

  private LocationResponse getLocationResponse() {
    LocationResponse locationResponse = new LocationResponse();
    locationResponse.setId(1L);
    locationResponse.setName("Stuttgart");
    return locationResponse;
  }
}