package com.car2go.carpolygon.repository;

import com.car2go.carpolygon.gateway.redis.VehicleInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleInfoCacheRepository extends CrudRepository<VehicleInfo, Long> {

}
