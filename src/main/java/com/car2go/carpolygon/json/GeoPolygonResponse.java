package com.car2go.carpolygon.json;

import com.car2go.carpolygon.domain.GeoPolygon;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

public class GeoPolygonResponse {

  @JsonProperty("_id")
  private String id;
  private GeometryJson geometry;
  private GeoOptions options;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GeometryJson getGeometry() {
    return geometry;
  }

  public void setGeometry(GeometryJson geometry) {
    this.geometry = geometry;
  }

  public GeoOptions getOptions() {
    return options;
  }

  public void setOptions(GeoOptions options) {
    this.options = options;
  }

  public boolean isValid() {
    return options.getActive() && !options.getExcluded();
  }

  public GeoPolygon toGeoPolygon() {
    final GeoJsonPolygon geoJsonPolygon = getGeometry().toGeoJsonPolygon();
    return new GeoPolygon(getId(), geoJsonPolygon);
  }
}
