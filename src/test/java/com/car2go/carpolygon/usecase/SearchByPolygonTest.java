package com.car2go.carpolygon.usecase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.car2go.carpolygon.domain.GeoPolygon;
import com.car2go.carpolygon.domain.VehicleLocation;
import com.car2go.carpolygon.json.LocationResponse;
import com.car2go.carpolygon.json.PolygonSearchResponse;
import com.car2go.carpolygon.json.Position;
import com.car2go.carpolygon.json.SearchRequest;
import com.car2go.carpolygon.json.VehicleResponse;
import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RunWith(MockitoJUnitRunner.class)
public class SearchByPolygonTest {

  @InjectMocks
  private SearchByPolygon searchByPolygon;

  @Mock
  private MongoTemplate mongoTemplate;

  @Test
  public void givenSearchRequestWithNoPolygonsWhenSearchingByCoordsShouldGiveZeroPolygonSearchResponse() {
    final Collection<PolygonSearchResponse> polygonSearchResponses = searchByPolygon
        .execute(searchRequest(), vehicleInformation());
    assertNotNull(polygonSearchResponses);
    assertEquals(polygonSearchResponses.size(), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenSearchRequestWithPolygonsWhenSearchingByCoordsShouldThrowException() {
    List<Double[]> list = new LinkedList<>();
    list.add(new Double[]{1D, 2D});
    searchRequest().setPolygons(list);
  }

  @Test
  public void givenSearchRequestWithPolygonsWhenSearchingByCoordsShouldGivePolygonSearchResponse() {
    List<Double[]> list = new LinkedList<>();
    list.add(new Double[]{1D, 2D});
    list.add(new Double[]{1D, 2D});
    list.add(new Double[]{3D, 2D});
    list.add(new Double[]{1D, 2D});
    final SearchRequest searchRequest = searchRequest().setPolygons(list);
    when(mongoTemplate.find(polygonsCriteria(searchRequest), GeoPolygon.class))
        .thenReturn(geoPolygons());
    final Collection<PolygonSearchResponse> polygonSearchResponses = searchByPolygon
        .execute(searchRequest, vehicleInformation());
    assertNotNull(polygonSearchResponses);
    assertEquals(polygonSearchResponses.size(), 1);
  }

  private Query polygonsCriteria(SearchRequest searchRequest) {
    return new Query(new Criteria("coords")
        .intersects(searchRequest.toPolygon()));
  }

  private SearchRequest searchRequest() {
    return new SearchRequest("Stuttgart");
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

  private List<GeoPolygon> geoPolygons() {
    List<GeoPolygon> geoPolygons = new ArrayList<>();
    final GeoJsonPolygon geoJsonPolygon = new GeoJsonPolygon(new Point(1, 2), new Point(1, 2),
        new Point(1, 2), new Point(1, 2));
    GeoPolygon geoPolygon = new GeoPolygon("id", geoJsonPolygon);
    geoPolygons.add(geoPolygon);
    return geoPolygons;
  }

}