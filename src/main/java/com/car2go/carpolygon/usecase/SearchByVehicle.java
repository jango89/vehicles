package com.car2go.carpolygon.usecase;

import static java.util.stream.Collectors.toList;

import com.car2go.carpolygon.domain.GeoPolygon;
import com.car2go.carpolygon.domain.VehicleLocation;
import com.car2go.carpolygon.json.SearchRequest;
import com.car2go.carpolygon.json.VehicleResponse;
import com.car2go.carpolygon.json.VehicleSearchResponse;
import com.car2go.carpolygon.repository.GeoPolygonRepository;
import java.util.List;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@Service
public class SearchByVehicle {

  static final int LENGTH_OF_CAR_IN_METERS = 5;
  private final GeoPolygonRepository geoPolygonRepository;

  public SearchByVehicle(GeoPolygonRepository geoPolygonRepository) {
    this.geoPolygonRepository = geoPolygonRepository;
  }

  public List<VehicleSearchResponse> execute(final SearchRequest searchRequest,
      final VehicleLocation vehicleLocation) {
    return vehicleLocation.filterVehicles(searchRequest)
        .stream()
        .map(this::createVehicleSearchResponse)
        .map(vehicleLocation::updatePoligonIdCache)
        .collect(toList());
  }

  private VehicleSearchResponse createVehicleSearchResponse(VehicleResponse vehicleResponse) {
    final Point point = new Point(vehicleResponse.getPosition().getLongitude(),
        vehicleResponse.getPosition().getLatitude());
    return new VehicleSearchResponse(vehicleResponse, fetchPolygonIds(point));
  }

  private List<String> fetchPolygonIds(Point point) {
    return geoPolygonRepository
        .findByCoordsNear(point, new Distance(LENGTH_OF_CAR_IN_METERS))
        .stream()
        .map(GeoPolygon::getId)
        .collect(toList());
  }
}
