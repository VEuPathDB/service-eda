package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sorting",
    "paging",
    "headerFormat",
    "trimTimeFromDateVars",
    "dataSource"
})
public class APITabularReportConfigImpl implements APITabularReportConfig {
  @JsonProperty("sorting")
  private List<SortSpecEntry> sorting;

  @JsonProperty("paging")
  private APIPagingConfig paging;

  @JsonProperty("headerFormat")
  private TabularHeaderFormat headerFormat;

  @JsonProperty("trimTimeFromDateVars")
  private Boolean trimTimeFromDateVars;

  @JsonProperty("dataSource")
  private DataSourceType dataSource;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("sorting")
  public List<SortSpecEntry> getSorting() {
    return this.sorting;
  }

  @JsonProperty("sorting")
  public void setSorting(List<SortSpecEntry> sorting) {
    this.sorting = sorting;
  }

  @JsonProperty("paging")
  public APIPagingConfig getPaging() {
    return this.paging;
  }

  @JsonProperty("paging")
  public void setPaging(APIPagingConfig paging) {
    this.paging = paging;
  }

  @JsonProperty("headerFormat")
  public TabularHeaderFormat getHeaderFormat() {
    return this.headerFormat;
  }

  @JsonProperty("headerFormat")
  public void setHeaderFormat(TabularHeaderFormat headerFormat) {
    this.headerFormat = headerFormat;
  }

  @JsonProperty("trimTimeFromDateVars")
  public Boolean getTrimTimeFromDateVars() {
    return this.trimTimeFromDateVars;
  }

  @JsonProperty("trimTimeFromDateVars")
  public void setTrimTimeFromDateVars(Boolean trimTimeFromDateVars) {
    this.trimTimeFromDateVars = trimTimeFromDateVars;
  }

  @JsonProperty("dataSource")
  public DataSourceType getDataSource() {
    return this.dataSource;
  }

  @JsonProperty("dataSource")
  public void setDataSource(DataSourceType dataSource) {
    this.dataSource = dataSource;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}
