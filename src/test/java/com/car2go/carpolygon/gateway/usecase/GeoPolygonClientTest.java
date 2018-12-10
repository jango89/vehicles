package com.car2go.carpolygon.gateway.usecase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.car2go.carpolygon.gateway.http.GeoPolygonClient;
import com.car2go.carpolygon.json.GeoPolygonResponse;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GeoPolygonClientTest {

  @Autowired
  private GeoPolygonClient geoPolygonClient;

  @Test
  public void givenUrlShouldFetchPolygonDetails() {
    final List<GeoPolygonResponse> geometryJson = geoPolygonClient.fetch();
    assertNotNull(geometryJson);
    assertTrue(geometryJson.size() > 0);
  }
}