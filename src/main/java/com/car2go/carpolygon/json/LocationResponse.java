package com.car2go.carpolygon.json;

public class LocationResponse {

  private Long id;
  private String name;
  private Position center;
  private Position upperLeft;
  private Position lowerRight;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Position getCenter() {
    return center;
  }

  public void setCenter(Position center) {
    this.center = center;
  }

  public Position getUpperLeft() {
    return upperLeft;
  }

  public void setUpperLeft(Position upperLeft) {
    this.upperLeft = upperLeft;
  }

  public Position getLowerRight() {
    return lowerRight;
  }

  public void setLowerRight(Position lowerRight) {
    this.lowerRight = lowerRight;
  }
}
