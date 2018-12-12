package com.car2go.carpolygon.usecase;

import com.car2go.carpolygon.domain.VehicleLocation;
import com.car2go.carpolygon.json.PolygonSearchResponse;
import com.car2go.carpolygon.json.SearchRequest;
import com.car2go.carpolygon.json.SearchResponse;
import com.car2go.carpolygon.json.VehicleSearchResponse;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SearchByPolygonAndVehicle {

  private final SearchByPolygon searchByPolygon;
  private final SearchByVehicle searchByVehicle;
  private final FetchVehicleInformationForLocation fetchVehicleInformationForLocation;

  public SearchByPolygonAndVehicle(SearchByPolygon searchByPolygon, SearchByVehicle searchByVehicle,
      FetchVehicleInformationForLocation fetchVehicleInformationForLocation) {
    this.searchByPolygon = searchByPolygon;
    this.searchByVehicle = searchByVehicle;
    this.fetchVehicleInformationForLocation = fetchVehicleInformationForLocation;
  }

  public SearchResponse execute(final SearchRequest searchRequest) {
    final VehicleLocation vehicleLocation = fetchVehicleInformationForLocation
        .execute(searchRequest.getLocation());
    final List<VehicleSearchResponse> vehicleSearchResponses = searchByVehicle
        .execute(searchRequest, vehicleLocation);
    final Collection<PolygonSearchResponse> polygonSearchResponses = searchByPolygon
        .execute(searchRequest, vehicleLocation);
    return new SearchResponse(polygonSearchResponses, vehicleSearchResponses);
  }
}
