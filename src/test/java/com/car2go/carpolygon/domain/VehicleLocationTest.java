package com.car2go.carpolygon.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.car2go.carpolygon.json.LocationResponse;
import com.car2go.carpolygon.json.PolygonSearchResponse;
import com.car2go.carpolygon.json.Position;
import com.car2go.carpolygon.json.SearchRequest;
import com.car2go.carpolygon.json.VehicleResponse;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class VehicleLocationTest {

  private VehicleLocation vehicleLocation;

  @Before
  public void init() {
    vehicleLocation = new VehicleLocation(locationResponse(), vehicles());
  }

  @Test
  public void givenSearchRequestWhenSearchingShouldFilterVehicles() {
    final List<VehicleResponse> vehicleResponses = vehicleLocation
        .filterVehicles(new SearchRequest("Stuttgart"));
    assertNotNull(vehicleResponses);
    assertEquals(vehicleResponses.size(), 1);
  }

  @Test
  public void givenSearchRequestWhenSearchingShouldFilterZeroVehicles() {
    final List<VehicleResponse> vehicleResponses = vehicleLocation
        .filterVehicles(new SearchRequest("Stuttgart")
            .setModel("dsasad"));
    assertNotNull(vehicleResponses);
    assertEquals(vehicleResponses.size(), 0);
  }

  @Test
  public void givenGeoPolygonItemShouldConvertToPolygonResponse() {
    final GeoPolygon geoPolygon = new GeoPolygon("id", null);
    final PolygonSearchResponse polygonSearchResponse = vehicleLocation
        .createPolygonSearchResponse(geoPolygon);
    assertNotNull(polygonSearchResponse);
    assertEquals(polygonSearchResponse.getId(), "id");
  }

  private LocationResponse locationResponse() {
    final LocationResponse locationResponse = new LocationResponse();
    final Position position = new Position();
    locationResponse.setCenter(position);
    return locationResponse;
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