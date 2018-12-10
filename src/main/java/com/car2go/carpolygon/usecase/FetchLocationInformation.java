package com.car2go.carpolygon.usecase;

import com.car2go.carpolygon.gateway.http.LocationInfoClient;
import com.car2go.carpolygon.gateway.redis.LocationInfo;
import com.car2go.carpolygon.json.LocationResponse;
import com.car2go.carpolygon.repository.LocationInfoCacheRepository;
import org.springframework.stereotype.Service;

@Service
class FetchLocationInformation {

  private final LocationInfoClient locationInfoClient;
  private final LocationInfoCacheRepository locationInfoCacheRepository;

  public FetchLocationInformation(
      LocationInfoClient locationInfoClient,
      LocationInfoCacheRepository locationInfoCacheRepository) {
    this.locationInfoClient = locationInfoClient;
    this.locationInfoCacheRepository = locationInfoCacheRepository;
  }

  public LocationInfo execute(String locationName) {
    return locationInfoCacheRepository.findById(locationName.toLowerCase().trim())
        .orElseGet(() -> getLocationFromApi(locationName));
  }

  private LocationInfo getLocationFromApi(String locationName) {
    return locationInfoClient.fetch(locationName)
        .map(this::persistInCache)
        .orElseThrow(() -> new IllegalArgumentException("Invalid Location"));
  }

  private LocationInfo persistInCache(LocationResponse locationResponse) {
    final LocationInfo locationInfo = new LocationInfo(locationResponse.getName(),
        locationResponse);
    return locationInfoCacheRepository.save(locationInfo);
  }
}
