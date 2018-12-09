package com.car2go.carpolygon.gateway;

import static org.junit.Assert.assertNotNull;

import com.car2go.carpolygon.json.GeoPolygonResponse;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GeoPolygonInterfaceTest {

  @Autowired
  private GeoPolygonInterface geoPolygonInterface;

  @Test
  public void givenUrlShouldFetchPolygonDetails() {
    geoPolygonInterface.setUrl(
        "https://gist.githubusercontent.com/codeofsumit/6540cdb245bd14c33b486b7981981b7b/raw/73ebda86c32923e203b2a8e61615da3e5f1695a2/polygons.json");
    final List<GeoPolygonResponse> geometryJson = geoPolygonInterface.fetch();
    assertNotNull(geometryJson);
  }
}