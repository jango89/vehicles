package com.car2go.carpolygon.gateway.http;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.car2go.carpolygon.configuration.CustomGsonHttpMessageConverter;
import com.car2go.carpolygon.json.Position;
import com.car2go.carpolygon.json.VehicleResponse;
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

public class VehicleInfoClientTest {

  private VehicleInfoClient vehicleInfoClient;

  private RestTemplate restTemplate = new RestTemplate();

  private ObjectMapper objectMapper = new ObjectMapper();

  private MockRestServiceServer mockServer;

  @Before
  public void init() {
    restTemplate.getMessageConverters().add(new CustomGsonHttpMessageConverter());
    vehicleInfoClient = new VehicleInfoClient(restTemplate, objectMapper);
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  public void givenUrlShouldFetchVehicleDetails() throws JsonProcessingException {
    vehicleInfoClient.setUrl("test");
    mockServer.expect(requestTo("test/Stuttgart"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(new ObjectMapper().writeValueAsBytes(vehicles()),
            MediaType.TEXT_PLAIN));
    final List<VehicleResponse> vehicleResponses = vehicleInfoClient.fetch("Stuttgart");
    assertNotNull(vehicleResponses);
    mockServer.verify();
  }

  private List<VehicleResponse> vehicles() {
    VehicleResponse vehicleResponse = new VehicleResponse();
    vehicleResponse.setId(1L);
    vehicleResponse.setLocationId(2L);
    vehicleResponse.setModel("model");
    vehicleResponse.setNumberPlate("no");
    vehicleResponse.setVin("vin");
    final Position position = new Position();
    position.setLatitude(1.1D);
    position.setLongitude(1.1D);
    vehicleResponse.setPosition(position);
    final LinkedList<VehicleResponse> vehicles = new LinkedList<>();
    vehicles.add(vehicleResponse);
    return vehicles;
  }
}