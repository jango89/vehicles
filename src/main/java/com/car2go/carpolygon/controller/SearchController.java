package com.car2go.carpolygon.controller;

import com.car2go.carpolygon.json.SearchRequest;
import com.car2go.carpolygon.json.SearchResponse;
import com.car2go.carpolygon.usecase.SearchByPolygonAndVehicle;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SearchController {

  private final SearchByPolygonAndVehicle searchByPolygonAndVehicle;

  public SearchController(SearchByPolygonAndVehicle searchByPolygonAndVehicle) {
    this.searchByPolygonAndVehicle = searchByPolygonAndVehicle;
  }

  @GetMapping("/search/{location}")
  @ResponseStatus(HttpStatus.OK)
  public SearchResponse search(
      @ApiParam(value = "location", defaultValue = "Stuttgart") @PathVariable(value = "location") String location,
      @RequestParam(value = "vin", required = false) String vin,
      @RequestParam(value = "numberPlate", required = false) String numberPlate,
      @RequestParam(value = "model", required = false) String model,
      @ApiParam(value = "latitude", defaultValue = "48.7") @RequestParam(value = "latitude", required = false) Double latitude,
      @ApiParam(value = "longitude", defaultValue = "9.1") @RequestParam(value = "longitude", required = false) Double longitude,
      @ApiParam(value = "polygons", defaultValue = "9.11682,48.740797,9.182748200599177, 48.74465716670039,9.110011,48.740797,9.11682,48.740797")
      @RequestParam(value = "polygons", required = false) List<Double> polygons) {

    final SearchRequest searchRequest = new SearchRequest(location).setPolygons(polygons)
        .setModel(model).setVin(vin)
        .setNumberPlate(numberPlate).setLatitude(latitude).setLongitude(longitude);
    return searchByPolygonAndVehicle.execute(searchRequest);
  }
}
