package com.car2go.carpolygon.gateway.redis;

import com.car2go.carpolygon.json.LocationResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "location", timeToLive = 3600)
public class LocationInfo {

  @Id
  private String locationName;
  private LocationResponse locationResponse;

  public LocationInfo() {
  }

  public LocationInfo(String locationName, LocationResponse locationResponse) {
    this.locationName = locationName;
    this.locationResponse = locationResponse;
  }

  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  public LocationResponse getLocationResponse() {
    return locationResponse;
  }

  public void setLocationResponse(LocationResponse locationResponse) {
    this.locationResponse = locationResponse;
  }
}
