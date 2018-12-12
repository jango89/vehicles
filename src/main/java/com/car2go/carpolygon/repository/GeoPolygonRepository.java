package com.car2go.carpolygon.repository;

import com.car2go.carpolygon.domain.GeoPolygon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoPolygonRepository extends CrudRepository<GeoPolygon, String> {

}
