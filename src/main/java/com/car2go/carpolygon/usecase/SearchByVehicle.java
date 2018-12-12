package com.car2go.carpolygon.usecase;

import static java.util.stream.Collectors.toList;

import com.car2go.carpolygon.domain.GeoPolygon;
import com.car2go.carpolygon.domain.VehicleLocation;
import com.car2go.carpolygon.json.SearchRequest;
import com.car2go.carpolygon.json.VehicleResponse;
import com.car2go.carpolygon.json.VehicleSearchResponse;
import java.util.List;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class SearchByVehicle {

  static final int LENGTH_OF_CAR_IN_METERS = 5;
  private final MongoTemplate mongoTemplate;

  public SearchByVehicle(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public List<VehicleSearchResponse> execute(final SearchRequest searchRequest,
      final VehicleLocation vehicleLocation) {
    return vehicleLocation.filterVehicles(searchRequest)
        .stream()
        .map(this::createVehicleSearchResponse)
        .map(vehicleLocation::updatePolygonIdCache)
        .collect(toList());
  }

  private VehicleSearchResponse createVehicleSearchResponse(VehicleResponse vehicleResponse) {
    final Point point = new Point(vehicleResponse.getPosition().getLongitude(),
        vehicleResponse.getPosition().getLatitude());
    return new VehicleSearchResponse(vehicleResponse, fetchPolygonIds(point));
  }

  private List<String> fetchPolygonIds(Point point) {
    return mongoTemplate.find(new Query(new Criteria("coords")
            .near(new GeoJsonPoint(point)).maxDistance(LENGTH_OF_CAR_IN_METERS)),
        GeoPolygon.class)
        .stream()
        .map(GeoPolygon::getId)
        .collect(toList());
  }
}
