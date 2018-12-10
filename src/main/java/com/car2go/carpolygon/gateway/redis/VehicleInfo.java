package com.car2go.carpolygon.gateway.redis;

import com.car2go.carpolygon.json.VehicleResponse;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "vehicles", timeToLive = 60)
public class VehicleInfo {

  @Id
  private Long locationId;
  private List<VehicleResponse> vehicles;

  public VehicleInfo() {

  }

  public VehicleInfo(Long locationId, List<VehicleResponse> vehicles) {
    this.locationId = locationId;
    this.vehicles = vehicles;
  }

  public Long getLocationId() {
    return locationId;
  }

  public void setLocationId(Long locationId) {
    this.locationId = locationId;
  }

  public List<VehicleResponse> getVehicles() {
    return vehicles;
  }

  public void setVehicles(List<VehicleResponse> vehicles) {
    this.vehicles = vehicles;
  }
}
