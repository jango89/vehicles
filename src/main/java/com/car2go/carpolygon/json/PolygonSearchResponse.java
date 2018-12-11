package com.car2go.carpolygon.json;

import java.util.List;
import java.util.Objects;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.util.CollectionUtils;

public class PolygonSearchResponse {

  private String id;
  private GeoJsonPolygon coords;
  private List<String> vehicleVinNumbers;

  public PolygonSearchResponse(String id, GeoJsonPolygon coords) {
    this.id = id;
    this.coords = coords;
  }

  public void setVehicleVinNumbers(List<String> vehicleVinNumbers) {
    this.vehicleVinNumbers = vehicleVinNumbers;
  }

  public String getId() {
    return id;
  }

  public GeoJsonPolygon getCoords() {
    return coords;
  }

  public List<String> getVehicleVinNumbers() {
    return vehicleVinNumbers;
  }

  public boolean needsEnrichmentOfVehicleInfo() {
    return !CollectionUtils.isEmpty(getVehicleVinNumbers());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PolygonSearchResponse that = (PolygonSearchResponse) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
