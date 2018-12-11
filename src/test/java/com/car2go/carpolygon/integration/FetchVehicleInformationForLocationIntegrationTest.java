package com.car2go.carpolygon.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.car2go.carpolygon.domain.VehicleLocation;
import com.car2go.carpolygon.usecase.FetchVehicleInformationForLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FetchVehicleInformationForLocationIntegrationTest {

  @Autowired
  private FetchVehicleInformationForLocation fetchVehicleInformationForLocation;

  @Test
  public void givenLocationNameShouldFetchVehicle() {
    final VehicleLocation vehicleLocation = fetchVehicleInformationForLocation.execute("Stuttgart");
    assertNotNull(vehicleLocation);
    assertEquals(vehicleLocation.getLocationInfo().getName(), "Stuttgart");
  }
}
