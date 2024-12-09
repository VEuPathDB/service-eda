package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = HistogramStatsImpl.class
)
public interface HistogramStats {
  @JsonProperty("subsetSize")
  Long getSubsetSize();

  @JsonProperty("subsetSize")
  void setSubsetSize(Long subsetSize);

  @JsonProperty("subsetMin")
  Object getSubsetMin();

  @JsonProperty("subsetMin")
  void setSubsetMin(Object subsetMin);

  @JsonProperty("subsetMax")
  Object getSubsetMax();

  @JsonProperty("subsetMax")
  void setSubsetMax(Object subsetMax);

  @JsonProperty("subsetMean")
  Object getSubsetMean();

  @JsonProperty("subsetMean")
  void setSubsetMean(Object subsetMean);

  @JsonProperty("numVarValues")
  Long getNumVarValues();

  @JsonProperty("numVarValues")
  void setNumVarValues(Long numVarValues);

  @JsonProperty("numDistinctValues")
  Long getNumDistinctValues();

  @JsonProperty("numDistinctValues")
  void setNumDistinctValues(Long numDistinctValues);

  @JsonProperty("numDistinctEntityRecords")
  Long getNumDistinctEntityRecords();

  @JsonProperty("numDistinctEntityRecords")
  void setNumDistinctEntityRecords(Long numDistinctEntityRecords);

  @JsonProperty("numMissingCases")
  Long getNumMissingCases();

  @JsonProperty("numMissingCases")
  void setNumMissingCases(Long numMissingCases);
}
