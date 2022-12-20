package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = APITabularReportConfigImpl.class
)
public interface APITabularReportConfig {
  @JsonProperty("sorting")
  List<SortSpecEntry> getSorting();

  @JsonProperty("sorting")
  void setSorting(List<SortSpecEntry> sorting);

  @JsonProperty("paging")
  APIPagingConfig getPaging();

  @JsonProperty("paging")
  void setPaging(APIPagingConfig paging);

  @JsonProperty("headerFormat")
  TabularHeaderFormat getHeaderFormat();

  @JsonProperty("headerFormat")
  void setHeaderFormat(TabularHeaderFormat headerFormat);

  @JsonProperty("trimTimeFromDateVars")
  Boolean getTrimTimeFromDateVars();

  @JsonProperty("trimTimeFromDateVars")
  void setTrimTimeFromDateVars(Boolean trimTimeFromDateVars);

  @JsonProperty("dataSource")
  DataSourceType getDataSource();

  @JsonProperty("dataSource")
  void setDataSource(DataSourceType dataSource);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
