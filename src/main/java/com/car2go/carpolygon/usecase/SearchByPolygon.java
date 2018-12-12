package com.car2go.carpolygon.usecase;

import static com.car2go.carpolygon.usecase.SearchByVehicle.LENGTH_OF_CAR_IN_METERS;
import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toSet;

import com.car2go.carpolygon.domain.GeoPolygon;
import com.car2go.carpolygon.domain.VehicleLocation;
import com.car2go.carpolygon.json.PolygonSearchResponse;
import com.car2go.carpolygon.json.SearchRequest;
import com.car2go.carpolygon.json.VehicleResponse;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class SearchByPolygon {

  private final MongoTemplate mongoTemplate;

  public SearchByPolygon(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Collection<PolygonSearchResponse> execute(final SearchRequest searchRequest,
      final VehicleLocation vehicleLocation) {

    if (searchRequest.isPolygonsPresent()) {

      final List<ObjectId> polygonIdsNotFoundInCache = new LinkedList<>();

      final Set<PolygonSearchResponse> polygonSearchResponses = getPolygonResponses(searchRequest,
          vehicleLocation, polygonIdsNotFoundInCache);

      final Collection<PolygonSearchResponse> searchResponses = getNonCachedPolygonSearchResponses(
          vehicleLocation, polygonIdsNotFoundInCache);
      searchResponses.addAll(polygonSearchResponses);

      return searchResponses;
    }
    return EMPTY_LIST;
  }

  /**
   * All Polygons needs vehicle information enriched. Some Vehicle information not present in cache
   * needs to be populated. For each vehicle we trigger geoNear query, this is not costly operation
   * since we restrict using some geoPolygon Id's.
   */
  private Collection<PolygonSearchResponse> getNonCachedPolygonSearchResponses(
      VehicleLocation vehicleLocation, List<ObjectId> poligonIdsNotFoundInCache) {
    final Map<String, PolygonSearchResponse> nonCachedPolygonIdAndPolygonResponse = new LinkedHashMap<>();

    vehicleLocation.getNonCachedVehicles()
        .forEach(vehicleLoc -> mongoTemplate
            .find(nonCachedpolygonsCriteria(poligonIdsNotFoundInCache, vehicleLoc),
                GeoPolygon.class)
            .forEach(geoPolygon -> updatePolygonResponse(nonCachedPolygonIdAndPolygonResponse,
                vehicleLoc, geoPolygon)));

    return new LinkedList<>(nonCachedPolygonIdAndPolygonResponse.values());
  }

  private void updatePolygonResponse(Map<String, PolygonSearchResponse> poligonStore,
      VehicleResponse vehicleLoc, GeoPolygon geoPolygon) {

    final PolygonSearchResponse polygonSearchResponse = new PolygonSearchResponse(
        geoPolygon.getId(), geoPolygon.getCoords());
    polygonSearchResponse.setVehicleVinNumbers(new LinkedList<>());

    poligonStore.putIfAbsent(polygonSearchResponse.getId(), polygonSearchResponse);

    poligonStore.get(polygonSearchResponse.getId())
        .getVehicleVinNumbers().add(vehicleLoc.getVin());
  }

  /**
   * Helps to find all geo polygons matching given polygon data. Polygons are enriched with vehicle
   * information if that information is already present in cache inside Domain(VehicleInformation).
   */
  private Set<PolygonSearchResponse> getPolygonResponses(SearchRequest searchRequest,
      VehicleLocation vehicleLocation, List<ObjectId> idList) {
    return mongoTemplate.find(polygonsCriteria(searchRequest), GeoPolygon.class)
        .stream()
        .map(vehicleLocation::createPolygonSearchResponse)
        .peek(item -> updatePolygonIdsNotFound(idList, item))
        .collect(toSet());
  }

  private void updatePolygonIdsNotFound(List<ObjectId> idList, PolygonSearchResponse item) {
    if (item.needsEnrichmentOfVehicleInfo()) {
      idList.add(new ObjectId(item.getId()));
    }
  }

  private Query polygonsCriteria(SearchRequest searchRequest) {
    return new Query(new Criteria("coords")
        .intersects(searchRequest.toPolygon()));
  }

  private Query nonCachedpolygonsCriteria(
      List<ObjectId> poligonIdsNotFoundInCache, VehicleResponse vehicleResponse) {
    final GeoJsonPoint geoJsonPoint = new GeoJsonPoint(
        new Point(vehicleResponse.getPosition().getLongitude(),
            vehicleResponse.getPosition().getLatitude()));
    return new Query(new Criteria()
        .near(geoJsonPoint)
        .maxDistance(LENGTH_OF_CAR_IN_METERS)
        .andOperator(new Criteria("_id").in(poligonIdsNotFoundInCache)));
  }
}
