package com.car2go.carpolygon.usecase;

import com.car2go.carpolygon.domain.GeoPolygon;
import com.car2go.carpolygon.gateway.http.GeoPolygonClient;
import com.car2go.carpolygon.json.GeoPolygonResponse;
import com.car2go.carpolygon.repository.GeoPolygonRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaveGeoPolygon {

  private static final Logger LOGGER = LoggerFactory.getLogger(SaveGeoPolygon.class);
  private final GeoPolygonClient geoPolygonClient;
  private final GeoPolygonRepository geoPolygonRepository;

  public SaveGeoPolygon(GeoPolygonClient geoPolygonClient,
      GeoPolygonRepository geoPolygonRepository) {
    this.geoPolygonClient = geoPolygonClient;
    this.geoPolygonRepository = geoPolygonRepository;
  }

  @PostConstruct
  public void initialize() {
    if (hasAlreadyInserted()) {
      final List<GeoPolygonResponse> geoPolygonResponses = fetchFromUrl();
      final Set<GeoPolygon> geoPolygons = convertToDocument(geoPolygonResponses);
      saveToDatabase(geoPolygons);
    } else {
      LOGGER.warn("Geo polygons are already present");
    }
  }

  private void saveToDatabase(Set<GeoPolygon> geoPolygons) {
    geoPolygonRepository.saveAll(geoPolygons);
  }

  private boolean hasAlreadyInserted() {
    return geoPolygonRepository.count() < 1;
  }

  private Set<GeoPolygon> convertToDocument(List<GeoPolygonResponse> geoPolygonResponses) {
    return geoPolygonResponses.stream()
        .map(GeoPolygonResponse::toGeoPolygon)
        .collect(Collectors.toSet());
  }

  private List<GeoPolygonResponse> fetchFromUrl() {
    return geoPolygonClient.fetch();
  }

}
