package com.car2go.carpolygon.usecase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.car2go.carpolygon.domain.GeoPolygon;
import com.car2go.carpolygon.domain.VehicleLocation;
import com.car2go.carpolygon.json.LocationResponse;
import com.car2go.carpolygon.json.Position;
import com.car2go.carpolygon.json.SearchRequest;
import com.car2go.carpolygon.json.VehicleResponse;
import com.car2go.carpolygon.json.VehicleSearchResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

@RunWith(MockitoJUnitRunner.class)
public class SearchByVehicleTest {

  @InjectMocks
  private SearchByVehicle searchByVehicle;

  @Mock
  private MongoTemplate mongoTemplate;

  @Test
  public void givenSearchRequestWhenFindingVehicleShouldRespondWithSearchResults() {
    when(mongoTemplate.find(any(), any())).thenReturn(geoPolygons());
    final List<VehicleSearchResponse> vehicleSearchResponses = searchByVehicle
        .execute(searchRequest(), vehicleInformation());
    assertNotNull(vehicleSearchResponses);
    assertEquals(vehicleSearchResponses.size(), 1);
  }

  @Test
  public void givenSearchRequestWithValidModelWhenFindingVehicleShouldRespondWithSearchResults() {
    when(mongoTemplate.find(any(), any())).thenReturn(geoPolygons());
    final SearchRequest searchRequest = searchRequest();
    searchRequest.setModel("model");
    final List<VehicleSearchResponse> vehicleSearchResponses = searchByVehicle
        .execute(searchRequest, vehicleInformation());
    assertNotNull(vehicleSearchResponses);
    assertEquals(vehicleSearchResponses.size(), 1);
  }

  @Test
  public void givenSearchRequestWithInvalidModelWhenFindingVehicleShouldRespondWithZeroSearchResults() {
    final SearchRequest searchRequest = searchRequest();
    searchRequest.setModel("blah");
    final List<VehicleSearchResponse> vehicleSearchResponses = searchByVehicle
        .execute(searchRequest, vehicleInformation());
    assertNotNull(vehicleSearchResponses);
    assertEquals(vehicleSearchResponses.size(), 0);
  }

  @Test
  public void givenSearchRequestWithInvalidNumberPlateWhenFindingVehicleShouldRespondWithZeroSearchResults() {
    final SearchRequest searchRequest = searchRequest();
    searchRequest.setNumberPlate("blah");
    final List<VehicleSearchResponse> vehicleSearchResponses = searchByVehicle
        .execute(searchRequest, vehicleInformation());
    assertNotNull(vehicleSearchResponses);
    assertEquals(vehicleSearchResponses.size(), 0);
  }

  @Test
  public void givenSearchRequestWithInvalidVinWhenFindingVehicleShouldRespondWithZeroSearchResults() {
    final SearchRequest searchRequest = searchRequest();
    searchRequest.setVin("blah");
    final List<VehicleSearchResponse> vehicleSearchResponses = searchByVehicle
        .execute(searchRequest, vehicleInformation());
    assertNotNull(vehicleSearchResponses);
    assertEquals(vehicleSearchResponses.size(), 0);
  }

  private VehicleLocation vehicleInformation() {
    return new VehicleLocation(locationResponse(), vehicles());
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

  private LocationResponse locationResponse() {
    final LocationResponse locationResponse = new LocationResponse();
    final Position position = new Position();
    locationResponse.setCenter(position);
    return locationResponse;
  }

  private List<Object> geoPolygons() {
    List<Object> geoPolygons = new ArrayList<>();
    final GeoJsonPolygon geoJsonPolygon = new GeoJsonPolygon(new Point(1, 2), new Point(1, 2),
        new Point(1, 2), new Point(1, 2));
    GeoPolygon geoPolygon = new GeoPolygon("id", geoJsonPolygon);
    geoPolygons.add(geoPolygon);
    return geoPolygons;
  }

  private SearchRequest searchRequest() {
    return new SearchRequest("Stuttgart");
  }
}