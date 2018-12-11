package com.car2go.carpolygon.usecase;

import com.car2go.carpolygon.domain.GeoPolygon;
import com.car2go.carpolygon.repository.GeoPolygonRepository;
import org.springframework.stereotype.Service;

@Service
public class FindPolygon {

  private final GeoPolygonRepository geoPolygonRepository;

  public FindPolygon(GeoPolygonRepository geoPolygonRepository) {
    this.geoPolygonRepository = geoPolygonRepository;
  }

  public GeoPolygon execute(String id) {
    return geoPolygonRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Identifier is invalid"));
  }
}
