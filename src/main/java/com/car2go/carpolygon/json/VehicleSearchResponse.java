package com.car2go.carpolygon.json;

import java.util.List;
import java.util.Objects;

public class VehicleSearchResponse {

  private Long id;
  private String vin;
  private Long locationId;
  private String numberPlate;
  private String model;
  private Position position;
  private List<String> polygonIds;

  public VehicleSearchResponse(VehicleResponse vehicleResponse, List<String> polygonIds) {
    this.id = vehicleResponse.getId();
    this.vin = vehicleResponse.getVin();
    this.locationId = vehicleResponse.getLocationId();
    this.model = vehicleResponse.getModel();
    this.position = vehicleResponse.getPosition();
    this.numberPlate = vehicleResponse.getNumberPlate();
    this.polygonIds = polygonIds;
  }

  public Long getId() {
    return id;
  }

  public String getVin() {
    return vin;
  }

  public Long getLocationId() {
    return locationId;
  }

  public String getNumberPlate() {
    return numberPlate;
  }

  public String getModel() {
    return model;
  }

  public Position getPosition() {
    return position;
  }

  public List<String> getPolygonIds() {
    return polygonIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VehicleSearchResponse that = (VehicleSearchResponse) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
