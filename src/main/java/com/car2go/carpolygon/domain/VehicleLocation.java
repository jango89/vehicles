package com.car2go.carpolygon.domain;

import com.car2go.carpolygon.json.LocationResponse;
import com.car2go.carpolygon.json.VehicleResponse;
import java.util.List;

public class VehicleLocation {

  private LocationResponse locationInfo;
  private List<VehicleResponse> vehicles;

  public VehicleLocation(LocationResponse locationInfo,
      List<VehicleResponse> vehicles) {
    this.locationInfo = locationInfo;
    this.vehicles = vehicles;
  }

  public LocationResponse getLocationInfo() {
    return locationInfo;
  }

  public void setLocationInfo(LocationResponse locationInfo) {
    this.locationInfo = locationInfo;
  }

  public List<VehicleResponse> getVehicles() {
    return vehicles;
  }

  public void setVehicles(List<VehicleResponse> vehicles) {
    this.vehicles = vehicles;
  }
}
