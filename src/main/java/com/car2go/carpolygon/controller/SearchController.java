package com.car2go.carpolygon.controller;

import com.car2go.carpolygon.json.SearchRequest;
import com.car2go.carpolygon.json.SearchResponse;
import com.car2go.carpolygon.usecase.SearchByPolygonAndVehicle;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/polygon-car")
public class SearchController {

  private final SearchByPolygonAndVehicle searchByPolygonAndVehicle;

  public SearchController(SearchByPolygonAndVehicle searchByPolygonAndVehicle) {
    this.searchByPolygonAndVehicle = searchByPolygonAndVehicle;
  }

  @GetMapping("/search/{location}")
  @ResponseStatus(HttpStatus.OK)
  public SearchResponse search(@PathVariable("location") String location,
      @RequestParam("vin") String vin,
      @RequestParam("numberPlate") String numberPlate,
      @RequestParam("model") String model,
      @RequestParam("latitude") Double latitude,
      @RequestParam("longitude") Double longitude,
      @RequestParam("polygons") List<Double[]> polygons) {

    final SearchRequest searchRequest = new SearchRequest(location).setPolygons(polygons)
        .setModel(model).setVin(vin)
        .setNumberPlate(numberPlate).setLatitude(latitude).setLongitude(longitude);
    return searchByPolygonAndVehicle.execute(searchRequest);
  }
}
