package com.car2go.carpolygon.gateway.usecase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.car2go.carpolygon.gateway.http.LocationInfoClient;
import com.car2go.carpolygon.json.LocationResponse;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LocationInfoClientTest {

  @Autowired
  private LocationInfoClient locationInfoClient;

  @Test
  public void givenUrlShouldFetchPolygonDetails() {
    final Optional<LocationResponse> locationResponse = locationInfoClient.fetch("Stuttgart");
    assertNotNull(locationResponse.get());
    assertTrue(locationResponse.get().getName().equalsIgnoreCase("stuttgart"));
  }
}