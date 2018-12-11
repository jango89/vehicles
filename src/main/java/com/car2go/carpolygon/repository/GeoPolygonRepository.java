package com.car2go.carpolygon.repository;

import com.car2go.carpolygon.domain.GeoPolygon;
import java.util.List;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoPolygonRepository extends CrudRepository<GeoPolygon, String> {

  List<GeoPolygon> findByCoordsNear(Point coords, Distance distance);
}
