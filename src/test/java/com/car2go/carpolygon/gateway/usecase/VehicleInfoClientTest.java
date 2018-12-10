package com.car2go.carpolygon.gateway.usecase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.car2go.carpolygon.gateway.http.VehicleInfoClient;
import com.car2go.carpolygon.json.VehicleResponse;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class VehicleInfoClientTest {

  @Autowired
  private VehicleInfoClient vehicleInfoClient;

  @Test
  public void givenUrlShouldFetchVehicleDetails() {
    final List<VehicleResponse> vehicleResponses = vehicleInfoClient.fetch("Stuttgart");
    assertNotNull(vehicleResponses);
    assertTrue(vehicleResponses.size() > 0);
  }
}