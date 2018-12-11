package com.car2go.carpolygon.json;

import java.util.Collection;
import java.util.List;

public class SearchResponse {

  private Collection<PolygonSearchResponse> polygonSearchResponse;
  private Collection<VehicleSearchResponse> vehicleSearchResponse;

  public SearchResponse(Collection<PolygonSearchResponse> polygonSearchResponse,
      Collection<VehicleSearchResponse> vehicleSearchResponse) {
    this.polygonSearchResponse = polygonSearchResponse;
    this.vehicleSearchResponse = vehicleSearchResponse;
  }

  public Collection<PolygonSearchResponse> getPolygonSearchResponse() {
    return polygonSearchResponse;
  }

  public Collection<VehicleSearchResponse> getVehicleSearchResponse() {
    return vehicleSearchResponse;
  }
}

