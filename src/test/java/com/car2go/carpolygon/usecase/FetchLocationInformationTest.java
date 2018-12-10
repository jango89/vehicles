package com.car2go.carpolygon.usecase;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.car2go.carpolygon.gateway.http.LocationInfoClient;
import com.car2go.carpolygon.gateway.redis.LocationInfo;
import com.car2go.carpolygon.json.LocationResponse;
import com.car2go.carpolygon.repository.LocationInfoCacheRepository;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FetchLocationInformationTest {

  @InjectMocks
  private FetchLocationInformation fetchLocationInformation;

  @Mock
  private LocationInfoClient locationInfoClient;
  @Mock
  private LocationInfoCacheRepository locationInfoCacheRepository;

  @Test
  public void givenLocationNameShouldFetchLocationInfoFromRepository() {
    when(locationInfoCacheRepository.findById("stuttgart")).thenReturn(locationInfo());
    final LocationInfo locationInfo = fetchLocationInformation.execute("Stuttgart");
    assertNotNull(locationInfo);
    verify(locationInfoClient, times(0)).fetch("stuttgart");
  }

  @Test
  public void givenLocationNameShouldFetchLocationInfoFromUrl() {
    when(locationInfoCacheRepository.findById("stuttgart")).thenReturn(Optional.empty());
    when(locationInfoClient.fetch(anyString())).thenReturn(locationResponse());
    when(locationInfoCacheRepository.save(any())).thenReturn(locationInfo().get());
    final LocationInfo locationInfo = fetchLocationInformation.execute("Stuttgart");
    assertNotNull(locationInfo);
    verify(locationInfoClient, times(1)).fetch("Stuttgart");
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenLocationNameShouldThrowNowLocationInformation() {
    fetchLocationInformation.execute("Stuttgart");
  }

  private Optional<LocationInfo> locationInfo() {
    LocationInfo locationInfo = new LocationInfo("stuttgart", new LocationResponse());
    return Optional.of(locationInfo);
  }

  private Optional<LocationResponse> locationResponse() {
    return Optional.of(new LocationResponse());
  }
}