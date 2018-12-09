package com.car2go.carpolygon.json;

import java.util.LinkedList;
import java.util.List;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

public class GeometryJson {

  private LinkedList<LinkedList<Double[]>> coordinates;
  private String type;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public LinkedList<LinkedList<Double[]>> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(LinkedList<LinkedList<Double[]>> coordinates) {
    this.coordinates = coordinates;
  }

  public GeoJsonPolygon toGeoJsonPolygon() {
    final List<Point> points = new LinkedList<>();
    coordinates.forEach(item -> item.forEach(eachItem -> {
      points.add(new Point(eachItem[0], eachItem[1]));
    }));
    return new GeoJsonPolygon(points);
  }
}
