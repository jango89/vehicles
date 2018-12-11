package com.car2go.carpolygon.json;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.Assert.isTrue;

import java.util.List;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.util.CollectionUtils;

public class SearchRequest {

  private String vin;
  private String numberPlate;
  private String model;
  private Double latitude;
  private Double longitude;
  private List<Double[]> polygons;
  private String location;

  public SearchRequest(String location) {
    this.location = location;
  }

  public String getVin() {
    return vin;
  }

  public SearchRequest setVin(String vin) {
    this.vin = vin;
    return this;
  }

  public String getNumberPlate() {
    return numberPlate;
  }

  public SearchRequest setNumberPlate(String numberPlate) {
    this.numberPlate = numberPlate;
    return this;
  }

  public String getModel() {
    return model;
  }

  public SearchRequest setModel(String model) {
    this.model = model;
    return this;
  }

  public Double getLatitude() {
    return latitude;
  }

  public SearchRequest setLatitude(Double latitude) {
    this.latitude = latitude;
    return this;
  }

  public Double getLongitude() {
    return longitude;
  }

  public SearchRequest setLongitude(Double longitude) {
    this.longitude = longitude;
    return this;
  }

  public List<Double[]> getPolygons() {
    return polygons;
  }

  public SearchRequest setPolygons(List<Double[]> polygons) {
    this.polygons = polygons;
    isTrue(polygons.size() < 4, "Should have at-least 4 sets of coordinates");
    isTrue(polygonsNotStartAndEndWithSameValue(polygons.get(0), lastValue(polygons)),
        "Start and End set of polygons should have same value");
    return this;
  }

  private boolean polygonsNotStartAndEndWithSameValue(Double[] firstArray, Double[] lastArray) {
    return !firstArray[0].equals(lastArray[0]) || !firstArray[0].equals(lastArray[1]);
  }

  private Double[] lastValue(List<Double[]> polygons) {
    return polygons.get(polygons.size() - 1);
  }

  public String getLocation() {
    return location;
  }

  public boolean isPolygonsPresent() {
    return !CollectionUtils.isEmpty(getPolygons());
  }

  public GeoJson toPolygon() {
    final List<Point> points = getPolygons().stream()
        .map(polygon -> new Point(polygon[0], polygon[1]))
        .collect(toList());
    return new GeoJsonPolygon(points);
  }
}
