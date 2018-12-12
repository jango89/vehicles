package com.car2go.carpolygon.usecase;


import static java.util.Collections.EMPTY_LIST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.car2go.carpolygon.domain.VehicleLocation;
import com.car2go.carpolygon.json.LocationResponse;
import com.car2go.carpolygon.json.PolygonSearchResponse;
import com.car2go.carpolygon.json.Position;
import com.car2go.carpolygon.json.SearchRequest;
import com.car2go.carpolygon.json.SearchResponse;
import com.car2go.carpolygon.json.VehicleResponse;
import com.car2go.carpolygon.json.VehicleSearchResponse;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchByPolygonAndVehicleTest {

  @InjectMocks
  private SearchByPolygonAndVehicle searchByPolygonAndVehicle;

  @Mock
  private SearchByPolygon searchByPolygon;

  @Mock
  private SearchByVehicle searchByVehicle;

  @Mock
  private FetchVehicleInformationForLocation fetchVehicleInformationForLocation;

  @Test
  public void givenSearchRequestWhenFindingPolygonAndVehicleShouldRespondWithSearchResponse() {
    when(searchByPolygon.execute(any(), any())).thenReturn(searchPolygonResponse());

    when(searchByVehicle.execute(any(), any())).thenReturn(vehicleResponse());

    when(fetchVehicleInformationForLocation.execute("Stuttgart")).thenReturn(vehicleInformation());

    final SearchResponse searchResponse = searchByPolygonAndVehicle.execute(searchRequest());
    assertNotNull(searchResponse.getPolygonSearchResponse());
    assertNotNull(searchResponse.getVehicleSearchResponse());
    assertEquals(searchResponse.getPolygonSearchResponse().size(), 1);
    assertEquals(searchResponse.getVehicleSearchResponse().size(), 1);
  }

  private List<VehicleSearchResponse> vehicleResponse() {
    final VehicleResponse vehicleResponse = new VehicleResponse();
    vehicleResponse.setId(1L);
    VehicleSearchResponse vehicleSearchResponse = new VehicleSearchResponse(vehicleResponse,
        EMPTY_LIST);
    final LinkedList<VehicleSearchResponse> linkedList = new LinkedList<>();
    linkedList.add(vehicleSearchResponse);
    return linkedList;
  }

  private Collection<PolygonSearchResponse> searchPolygonResponse() {
    PolygonSearchResponse polygonSearchResponse = new PolygonSearchResponse("id", null);
    final LinkedList<PolygonSearchResponse> linkedList = new LinkedList<>();
    linkedList.add(polygonSearchResponse);
    return linkedList;
  }

  private VehicleLocation vehicleInformation() {
    return new VehicleLocation(locationResponse(), vehicles());
  }

  private List<VehicleResponse> vehicles() {
    return EMPTY_LIST;
  }

  private LocationResponse locationResponse() {
    final LocationResponse locationResponse = new LocationResponse();
    final Position position = new Position();
    locationResponse.setCenter(position);
    return locationResponse;
  }

  private SearchRequest searchRequest() {
    return new SearchRequest("Stuttgart");
  }
}