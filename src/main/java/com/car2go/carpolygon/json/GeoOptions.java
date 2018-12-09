package com.car2go.carpolygon.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoOptions {

  private Boolean active;
  @JsonProperty("is_excluded")
  private Boolean isExcluded;

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Boolean getExcluded() {
    return isExcluded;
  }

  public void setExcluded(Boolean excluded) {
    isExcluded = excluded;
  }
}
