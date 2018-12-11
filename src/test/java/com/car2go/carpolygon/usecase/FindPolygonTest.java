package com.car2go.carpolygon.usecase;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.car2go.carpolygon.domain.GeoPolygon;
import com.car2go.carpolygon.repository.GeoPolygonRepository;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FindPolygonTest {

  @InjectMocks
  private FindPolygon findPolygon;

  @Mock
  private GeoPolygonRepository geoPolygonRepository;

  @Test
  public void givenIdWhenFindingPolygonShouldFindPolygon() {
    when(geoPolygonRepository.findById("id")).thenReturn(polygon());
    final GeoPolygon polygon = findPolygon.execute("id");
    assertNotNull(polygon);
    assertEquals(polygon.getId(), "id");
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenIdWhenFindingPolygonShouldThrowException() {
    findPolygon.execute("id");
  }

  private Optional<GeoPolygon> polygon() {
    return Optional.of(new GeoPolygon("id", null));
  }

}