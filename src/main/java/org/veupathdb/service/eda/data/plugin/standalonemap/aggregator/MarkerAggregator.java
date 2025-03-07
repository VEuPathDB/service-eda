package org.veupathdb.service.eda.data.plugin.standalonemap.aggregator;

public interface MarkerAggregator<T> {

  /**
   * Add a variable value to incorporate into aggregated result.
   */
  void addValue(String[] rec);

  /**
   * Returns true if
   */
  boolean appliesTo(String[] rec);

  /**
   * @return result of aggregated variable values.
   */
  T finish();
}
