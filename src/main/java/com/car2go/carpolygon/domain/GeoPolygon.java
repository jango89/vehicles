package com.car2go.carpolygon.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "geo_polygon")
public class GeoPolygon {

  @Id
  private String id;
  @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
  private GeoJsonPolygon coords;

  @PersistenceConstructor
  public GeoPolygon(String id, GeoJsonPolygon coordinates) {
    this.id = id;
    this.coords = coordinates;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GeoJsonPolygon getCoords() {
    return coords;
  }

  public void setCoords(GeoJsonPolygon coords) {
    this.coords = coords;
  }
}
