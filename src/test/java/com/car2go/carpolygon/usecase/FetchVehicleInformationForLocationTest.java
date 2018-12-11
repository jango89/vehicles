package com.car2go.carpolygon.usecase;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.car2go.carpolygon.domain.VehicleLocation;
import com.car2go.carpolygon.gateway.http.VehicleInfoClient;
import com.car2go.carpolygon.gateway.redis.LocationInfo;
import com.car2go.carpolygon.gateway.redis.VehicleInfo;
import com.car2go.carpolygon.json.LocationResponse;
import com.car2go.carpolygon.json.VehicleResponse;
import com.car2go.carpolygon.repository.VehicleInfoCacheRepository;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FetchVehicleInformationForLocationTest {

  @InjectMocks
  private FetchVehicleInformationForLocation fetchVehicleInformationForLocation;

  @Mock
  private VehicleInfoClient vehicleInfoClient;
  @Mock
  private FetchLocationInformation fetchLocationInformation;
  @Mock
  private VehicleInfoCacheRepository vehicleInfoCacheRepository;

  @Test
  public void givenLocationNameShouldFetchVehicleInformationFromCache() {
    when(vehicleInfoCacheRepository.findById(any())).thenReturn(vehicleInfo());
    when(fetchLocationInformation.execute(any())).thenReturn(locationInfo().get());
    final VehicleLocation vehicleLocation = fetchVehicleInformationForLocation.execute("Stuttgart");
    assertNotNull(vehicleLocation);
    assertNotNull(vehicleLocation.getVehicles());
    verify(vehicleInfoClient, times(0)).fetch(any());
  }

  @Test
  public void givenLocationNameShouldFetchVehicleInformationFromRepository() {
    when(vehicleInfoCacheRepository.findById(any())).thenReturn(Optional.empty());
    when(fetchLocationInformation.execute(any())).thenReturn(locationInfo().get());
    when(vehicleInfoClient.fetch(any())).thenReturn(vehicleResponse());
    when(vehicleInfoCacheRepository.save(any())).thenReturn(vehicleInfo().get());
    final VehicleLocation vehicleLocation = fetchVehicleInformationForLocation.execute("Stuttgart");
    assertNotNull(vehicleLocation);
    assertNotNull(vehicleLocation.getVehicles());
    verify(vehicleInfoClient, times(1)).fetch(any());
  }

  private List<VehicleResponse> vehicleResponse() {
    List<VehicleResponse> vehicleResponses = new LinkedList<>();
    VehicleResponse vehicleResponse = new VehicleResponse();
    vehicleResponse.setId(2L);
    vehicleResponses.add(vehicleResponse);
    return vehicleResponses;
  }

  private Optional<LocationInfo> locationInfo() {
    LocationInfo locationInfo = new LocationInfo("stuttgart", new LocationResponse());
    return Optional.of(locationInfo);
  }

  private Optional<VehicleInfo> vehicleInfo() {
    VehicleInfo vehicleInfo = new VehicleInfo(1L, new LinkedList<>());
    return Optional.of(vehicleInfo);
  }
}