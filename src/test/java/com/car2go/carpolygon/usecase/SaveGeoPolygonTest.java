package com.car2go.carpolygon.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.car2go.carpolygon.domain.GeoPolygon;
import com.car2go.carpolygon.gateway.http.GeoPolygonClient;
import com.car2go.carpolygon.json.GeoOptions;
import com.car2go.carpolygon.json.GeoPolygonResponse;
import com.car2go.carpolygon.json.GeometryJson;
import com.car2go.carpolygon.repository.GeoPolygonRepository;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SaveGeoPolygonTest {

  @InjectMocks
  private SaveGeoPolygon saveGeoPolygon;

  @Mock
  private GeoPolygonClient geoPolygonClient;

  @Mock
  private GeoPolygonRepository geoPolygonRepository;

  @Test
  public void givenUrlToFetchWhenNoDataFoundShouldSaveGeoPolygonsToDatabase() {
    when(geoPolygonClient.fetch()).thenReturn(geoPolygonResponses());
    when(geoPolygonRepository.saveAll(any())).thenReturn(geoPolygons());
    saveGeoPolygon.initialize();
    verify(geoPolygonRepository, times(1)).saveAll(any());
  }

  @Test
  public void givenUrlToFetchWhenDataAlreadyFoundShouldNotSaveGeoPolygonsToDatabase() {
    when(geoPolygonRepository.count()).thenReturn(1L);
    saveGeoPolygon.initialize();
    verify(geoPolygonRepository, times(0)).saveAll(any());
  }

  private Iterable<GeoPolygon> geoPolygons() {
    return Collections.EMPTY_LIST;
  }

  private List<GeoPolygonResponse> geoPolygonResponses() {
    List<GeoPolygonResponse> geoPolygonResponses = new LinkedList<>();
    GeoPolygonResponse geoPolygonResponse = new GeoPolygonResponse();
    final GeoOptions geoOptions = getGeoOptions();
    geoPolygonResponse.setOptions(geoOptions);
    geoPolygonResponse.setGeometry(geometry());
    return geoPolygonResponses;
  }

  private GeometryJson geometry() {
    final GeometryJson geometry = new GeometryJson();
    final LinkedList<LinkedList<Double[]>> objects = new LinkedList<>();
    final Double[] corods = new Double[2];
    corods[0] = 9.02;
    corods[1] = 48.02;
    final LinkedList<Double[]> values = new LinkedList<>();
    values.add(corods);
    objects.add(values);
    geometry.setCoordinates(objects);
    return geometry;
  }

  private GeoOptions getGeoOptions() {
    final GeoOptions geoOptions = new GeoOptions();
    geoOptions.setActive(true);
    geoOptions.setExcluded(false);
    return geoOptions;
  }
}