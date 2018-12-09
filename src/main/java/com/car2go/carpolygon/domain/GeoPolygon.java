package com.car2go.carpolygon.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "geo_polygon")
public class GeoPolygon {

  @Id
  private String id;
  private GeoJsonPolygon coordinates;

  @PersistenceConstructor
  public GeoPolygon(String id, GeoJsonPolygon coordinates) {
    this.id = id;
    this.coordinates = coordinates;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GeoJsonPolygon getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(GeoJsonPolygon coordinates) {
    this.coordinates = coordinates;
  }
}
