package org.veupathdb.service.eda.data.plugin.standalonemap.conversion;

import org.veupathdb.service.eda.data.plugin.standalonemap.markers.MarkerData;
import org.veupathdb.service.eda.generated.model.BaseMarker;

public class ApiConverter {

  public static void populateBaseMarkerData(String geoAggValue, BaseMarker baseMarker, MarkerData<?> markerData) {
    baseMarker.setAvgLat(markerData.getLatLonAvg().getCurrentAverage().getLatitude());
    baseMarker.setAvgLon(markerData.getLatLonAvg().getCurrentAverage().getLongitude());
    baseMarker.setMaxLat(markerData.getMaxLat());
    baseMarker.setMaxLon(markerData.getMaxLon());
    baseMarker.setMinLat(markerData.getMinLat());
    baseMarker.setMinLon(markerData.getMinLon());
    baseMarker.setEntityCount(markerData.getCount());
    baseMarker.setGeoAggregateValue(geoAggValue);
  }

}
