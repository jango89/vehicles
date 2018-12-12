package com.car2go.carpolygon.domain;

import static java.math.RoundingMode.HALF_UP;
import static java.util.stream.Collectors.toList;

import com.car2go.carpolygon.json.LocationResponse;
import com.car2go.carpolygon.json.PolygonSearchResponse;
import com.car2go.carpolygon.json.SearchRequest;
import com.car2go.carpolygon.json.VehicleResponse;
import com.car2go.carpolygon.json.VehicleSearchResponse;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

public class VehicleLocation {

  private final LocationResponse locationInfo;
  private final Map<Long, VehicleResponse> vehicles;
  private final Map<String, List<VehicleSearchResponse>> polygonIdAndVehiclesCache = new LinkedHashMap<>();
  final Set<VehicleResponse> vehiclesNeedsToBeSearched;

  public VehicleLocation(LocationResponse locationInfo, List<VehicleResponse> vehicles) {
    this.locationInfo = locationInfo;
    this.vehicles = vehicles.stream()
        .collect(Collectors.toMap(VehicleResponse::getId, Function.identity()));
    vehiclesNeedsToBeSearched = new HashSet<>(vehicles);
  }

  public Collection<VehicleResponse> getVehicles() {
    return vehicles.values();
  }

  public List<VehicleResponse> filterVehicles(SearchRequest searchRequest) {
    final Predicate<VehicleResponse> vehiclePredicate = vehicleVinPredicate(searchRequest.getVin())
        .and(vehicleModelPredicate(searchRequest.getModel()))
        .and(vehicleNumberPlatePredicate(searchRequest.getNumberPlate()))
        .and(vehicleLatitudePredicate(searchRequest.getLatitude()))
        .and(vehicleLongitudePredicate(searchRequest.getLongitude()));

    return vehicles.values().stream()
        .filter(vehiclePredicate)
        .collect(toList());
  }

  private Predicate<VehicleResponse> vehicleVinPredicate(String vin) {
    return vehicleResponse -> StringUtils.isEmpty(vin) ?
        !vehicleResponse.getVin().equals("") :
        vehicleResponse.getVin().equals(vin);
  }

  private Predicate<VehicleResponse> vehicleModelPredicate(String model) {
    return vehicleResponse -> StringUtils.isEmpty(model) ?
        !vehicleResponse.getModel().equals("") :
        vehicleResponse.getModel().equals(model);
  }

  private Predicate<VehicleResponse> vehicleNumberPlatePredicate(String numberPlate) {
    return vehicleResponse -> StringUtils.isEmpty(numberPlate) ?
        !vehicleResponse.getNumberPlate().equals("") :
        vehicleResponse.getNumberPlate().equals(numberPlate);
  }

  private Predicate<VehicleResponse> vehicleLatitudePredicate(Double latitude) {
    return vehicleResponse -> Objects.isNull(latitude) ?
        vehicleResponse.getPosition().getLatitude() > 0.0D :
        BigDecimal.valueOf(vehicleResponse.getPosition().getLatitude())
            .setScale(1, HALF_UP)
            .equals(BigDecimal.valueOf(latitude).setScale(1, HALF_UP));
  }

  private Predicate<VehicleResponse> vehicleLongitudePredicate(Double longitude) {
    return vehicleResponse -> Objects.isNull(longitude) ?
        vehicleResponse.getPosition().getLongitude() > 0.0D :
        BigDecimal.valueOf(vehicleResponse.getPosition().getLongitude())
            .setScale(1, HALF_UP)
            .equals(BigDecimal.valueOf(longitude).setScale(1, HALF_UP));
  }

  public VehicleSearchResponse updatePolygonIdCache(VehicleSearchResponse vehicleSearchResponse) {
    vehicleSearchResponse.getPolygonIds().forEach(polygonId -> {
      if (!polygonIdAndVehiclesCache.containsKey(polygonId)) {
        final LinkedList<VehicleSearchResponse> vehicles = new LinkedList<>();
        vehicles.add(vehicleSearchResponse);
        polygonIdAndVehiclesCache.put(polygonId, vehicles);
      } else {
        polygonIdAndVehiclesCache.get(polygonId).add(vehicleSearchResponse);
      }
    });
    vehiclesNeedsToBeSearched.remove(vehicles.get(vehicleSearchResponse.getId()));
    return vehicleSearchResponse;
  }

  public Set<VehicleResponse> getNonCachedVehicles() {
    return vehiclesNeedsToBeSearched;
  }

  public PolygonSearchResponse createPolygonSearchResponse(GeoPolygon item) {
    if (polygonDataAlreadyPresent(item)) {
      return toPolygonSearchResponse(item);
    } else {
      return new PolygonSearchResponse(item.getId(), item.getCoords());
    }
  }

  private PolygonSearchResponse toPolygonSearchResponse(GeoPolygon item) {
    final PolygonSearchResponse polygonSearchResponse = new PolygonSearchResponse(item.getId(),
        item.getCoords());
    final List<String> vehicleVins = polygonIdAndVehiclesCache.get(item.getId()).
        stream().map(VehicleSearchResponse::getVin).collect(toList());
    polygonSearchResponse.setVehicleVinNumbers(vehicleVins);
    return polygonSearchResponse;
  }

  private boolean polygonDataAlreadyPresent(GeoPolygon item) {
    return polygonIdAndVehiclesCache.containsKey(item.getId());
  }
}
