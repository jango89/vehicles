package com.car2go.carpolygon.usecase;

import com.car2go.carpolygon.domain.VehicleLocation;
import com.car2go.carpolygon.gateway.http.VehicleInfoClient;
import com.car2go.carpolygon.gateway.redis.LocationInfo;
import com.car2go.carpolygon.gateway.redis.VehicleInfo;
import com.car2go.carpolygon.json.LocationResponse;
import com.car2go.carpolygon.json.VehicleResponse;
import com.car2go.carpolygon.repository.VehicleInfoCacheRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FetchVehicleInformationForLocation {

  private final static Logger LOGGER = LoggerFactory.getLogger(FetchVehicleInformationForLocation.class);
  private final VehicleInfoClient vehicleInfoClient;
  private final FetchLocationInformation fetchLocationInformation;
  private final VehicleInfoCacheRepository vehicleInfoCacheRepository;

  public FetchVehicleInformationForLocation(VehicleInfoClient vehicleInfoClient,
      FetchLocationInformation fetchLocationInformation,
      VehicleInfoCacheRepository vehicleInfoCacheRepository) {
    this.vehicleInfoClient = vehicleInfoClient;
    this.fetchLocationInformation = fetchLocationInformation;
    this.vehicleInfoCacheRepository = vehicleInfoCacheRepository;
  }

  public VehicleLocation execute(String locationName) {
    final LocationInfo locationInfo = fetchLocationInformation.execute(locationName);
    final VehicleInfo vehicleInfo = findVehicleInfo(locationInfo);
    LOGGER.info("Vehicles found in {}", locationName);
    return new VehicleLocation(locationInfo.getLocationResponse(), vehicleInfo.getVehicles());
  }

  private VehicleInfo findVehicleInfo(LocationInfo locationInfo) {
    return vehicleInfoCacheRepository.findById(locationInfo.getLocationResponse().getId())
        .orElseGet(() -> getVehicleInfoFromApi(locationInfo));
  }

  private VehicleInfo getVehicleInfoFromApi(LocationInfo locationInfo) {
    final List<VehicleResponse> vehicles = vehicleInfoClient
        .fetch(locationInfo.getLocationResponse().getName());
    return persistInCache(locationInfo.getLocationResponse(), vehicles);
  }

  private VehicleInfo persistInCache(LocationResponse locationResponse,
      List<VehicleResponse> vehicles) {
    final VehicleInfo vehicleInfo = new VehicleInfo(locationResponse.getId(), vehicles);
    return vehicleInfoCacheRepository.save(vehicleInfo);
  }

}
