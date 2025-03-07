package org.veupathdb.service.eda.data.plugin.standalonemap.aggregator;

import org.veupathdb.service.eda.data.plugin.standalonemap.markers.OverlayRecoder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Overlay aggregator for categorical variables on the standalone map marker.
 */
public class QualitativeOverlayAggregator implements MarkerAggregator<Map<String, QualitativeOverlayAggregator.CategoricalOverlayData>> {
  private final OverlayRecoder overlayRecoder;
  private final Map<String, Integer> count = new HashMap<>();
  private final int index;
  private int n = 0;

  public QualitativeOverlayAggregator(OverlayRecoder overlayRecoder, int index) {
    this.overlayRecoder = overlayRecoder;
    this.index = index;
  }

  @Override
  public void addValue(String[] arr) {
    if (arr[index] == null || arr[index].isEmpty()) {
      return;
    }
    // Recode the variable from its raw value. This might be quantizing a continuous or a pass-through function for categoricals.
    final String overlayValue = overlayRecoder.recode(arr[index]);
    int newCount = count.getOrDefault(overlayValue, 0);
    // Keep track of counts for each overlay var as well as total entity count.
    count.put(overlayValue, newCount + 1);
    n++;
  }

  @Override
  public boolean appliesTo(String[] rec) {
    return rec[index] != null && !rec[index].isEmpty();
  }

  @Override
  public Map<String, CategoricalOverlayData> finish() {
    return count.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> new CategoricalOverlayData(e.getValue(), (double) e.getValue() / n)));
  }

  public record CategoricalOverlayData(int count, double proportion) {}
}
