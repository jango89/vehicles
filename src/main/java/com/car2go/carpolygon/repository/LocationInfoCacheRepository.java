package com.car2go.carpolygon.repository;

import com.car2go.carpolygon.gateway.redis.LocationInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationInfoCacheRepository extends CrudRepository<LocationInfo, String> {

}
