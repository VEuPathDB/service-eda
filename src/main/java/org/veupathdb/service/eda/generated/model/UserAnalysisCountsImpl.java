package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "analysesCount",
    "importedAnalysesCount",
    "analysesPerStudy",
    "importedAnalysesPerStudy",
    "registeredUsersCount",
    "guestUsersCount",
    "registeredAnalysesCount",
    "guestAnalysesCount",
    "registeredFiltersCount",
    "guestFiltersCount",
    "registeredVisualizationsCount",
    "guestVisualizationsCount",
    "registeredUsersAnalysesCounts",
    "guestUsersAnalysesCounts",
    "registeredUsersFiltersCounts",
    "guestUsersFiltersCounts",
    "registeredUsersVisualizationsCounts",
    "guestUsersVisualizationsCounts"
})
public class UserAnalysisCountsImpl implements UserAnalysisCounts {
  @JsonProperty("analysesCount")
  private Integer analysesCount;

  @JsonProperty("importedAnalysesCount")
  private Integer importedAnalysesCount;

  @JsonProperty("analysesPerStudy")
  private List<StudyCount> analysesPerStudy;

  @JsonProperty("importedAnalysesPerStudy")
  private List<StudyCount> importedAnalysesPerStudy;

  @JsonProperty("registeredUsersCount")
  private Integer registeredUsersCount;

  @JsonProperty("guestUsersCount")
  private Integer guestUsersCount;

  @JsonProperty("registeredAnalysesCount")
  private Integer registeredAnalysesCount;

  @JsonProperty("guestAnalysesCount")
  private Integer guestAnalysesCount;

  @JsonProperty("registeredFiltersCount")
  private Integer registeredFiltersCount;

  @JsonProperty("guestFiltersCount")
  private Integer guestFiltersCount;

  @JsonProperty("registeredVisualizationsCount")
  private Integer registeredVisualizationsCount;

  @JsonProperty("guestVisualizationsCount")
  private Integer guestVisualizationsCount;

  @JsonProperty("registeredUsersAnalysesCounts")
  private List<UsersObjectsCount> registeredUsersAnalysesCounts;

  @JsonProperty("guestUsersAnalysesCounts")
  private List<UsersObjectsCount> guestUsersAnalysesCounts;

  @JsonProperty("registeredUsersFiltersCounts")
  private List<UsersObjectsCount> registeredUsersFiltersCounts;

  @JsonProperty("guestUsersFiltersCounts")
  private List<UsersObjectsCount> guestUsersFiltersCounts;

  @JsonProperty("registeredUsersVisualizationsCounts")
  private List<UsersObjectsCount> registeredUsersVisualizationsCounts;

  @JsonProperty("guestUsersVisualizationsCounts")
  private List<UsersObjectsCount> guestUsersVisualizationsCounts;

  @JsonProperty("analysesCount")
  public Integer getAnalysesCount() {
    return this.analysesCount;
  }

  @JsonProperty("analysesCount")
  public void setAnalysesCount(Integer analysesCount) {
    this.analysesCount = analysesCount;
  }

  @JsonProperty("importedAnalysesCount")
  public Integer getImportedAnalysesCount() {
    return this.importedAnalysesCount;
  }

  @JsonProperty("importedAnalysesCount")
  public void setImportedAnalysesCount(Integer importedAnalysesCount) {
    this.importedAnalysesCount = importedAnalysesCount;
  }

  @JsonProperty("analysesPerStudy")
  public List<StudyCount> getAnalysesPerStudy() {
    return this.analysesPerStudy;
  }

  @JsonProperty("analysesPerStudy")
  public void setAnalysesPerStudy(List<StudyCount> analysesPerStudy) {
    this.analysesPerStudy = analysesPerStudy;
  }

  @JsonProperty("importedAnalysesPerStudy")
  public List<StudyCount> getImportedAnalysesPerStudy() {
    return this.importedAnalysesPerStudy;
  }

  @JsonProperty("importedAnalysesPerStudy")
  public void setImportedAnalysesPerStudy(List<StudyCount> importedAnalysesPerStudy) {
    this.importedAnalysesPerStudy = importedAnalysesPerStudy;
  }

  @JsonProperty("registeredUsersCount")
  public Integer getRegisteredUsersCount() {
    return this.registeredUsersCount;
  }

  @JsonProperty("registeredUsersCount")
  public void setRegisteredUsersCount(Integer registeredUsersCount) {
    this.registeredUsersCount = registeredUsersCount;
  }

  @JsonProperty("guestUsersCount")
  public Integer getGuestUsersCount() {
    return this.guestUsersCount;
  }

  @JsonProperty("guestUsersCount")
  public void setGuestUsersCount(Integer guestUsersCount) {
    this.guestUsersCount = guestUsersCount;
  }

  @JsonProperty("registeredAnalysesCount")
  public Integer getRegisteredAnalysesCount() {
    return this.registeredAnalysesCount;
  }

  @JsonProperty("registeredAnalysesCount")
  public void setRegisteredAnalysesCount(Integer registeredAnalysesCount) {
    this.registeredAnalysesCount = registeredAnalysesCount;
  }

  @JsonProperty("guestAnalysesCount")
  public Integer getGuestAnalysesCount() {
    return this.guestAnalysesCount;
  }

  @JsonProperty("guestAnalysesCount")
  public void setGuestAnalysesCount(Integer guestAnalysesCount) {
    this.guestAnalysesCount = guestAnalysesCount;
  }

  @JsonProperty("registeredFiltersCount")
  public Integer getRegisteredFiltersCount() {
    return this.registeredFiltersCount;
  }

  @JsonProperty("registeredFiltersCount")
  public void setRegisteredFiltersCount(Integer registeredFiltersCount) {
    this.registeredFiltersCount = registeredFiltersCount;
  }

  @JsonProperty("guestFiltersCount")
  public Integer getGuestFiltersCount() {
    return this.guestFiltersCount;
  }

  @JsonProperty("guestFiltersCount")
  public void setGuestFiltersCount(Integer guestFiltersCount) {
    this.guestFiltersCount = guestFiltersCount;
  }

  @JsonProperty("registeredVisualizationsCount")
  public Integer getRegisteredVisualizationsCount() {
    return this.registeredVisualizationsCount;
  }

  @JsonProperty("registeredVisualizationsCount")
  public void setRegisteredVisualizationsCount(Integer registeredVisualizationsCount) {
    this.registeredVisualizationsCount = registeredVisualizationsCount;
  }

  @JsonProperty("guestVisualizationsCount")
  public Integer getGuestVisualizationsCount() {
    return this.guestVisualizationsCount;
  }

  @JsonProperty("guestVisualizationsCount")
  public void setGuestVisualizationsCount(Integer guestVisualizationsCount) {
    this.guestVisualizationsCount = guestVisualizationsCount;
  }

  @JsonProperty("registeredUsersAnalysesCounts")
  public List<UsersObjectsCount> getRegisteredUsersAnalysesCounts() {
    return this.registeredUsersAnalysesCounts;
  }

  @JsonProperty("registeredUsersAnalysesCounts")
  public void setRegisteredUsersAnalysesCounts(
      List<UsersObjectsCount> registeredUsersAnalysesCounts) {
    this.registeredUsersAnalysesCounts = registeredUsersAnalysesCounts;
  }

  @JsonProperty("guestUsersAnalysesCounts")
  public List<UsersObjectsCount> getGuestUsersAnalysesCounts() {
    return this.guestUsersAnalysesCounts;
  }

  @JsonProperty("guestUsersAnalysesCounts")
  public void setGuestUsersAnalysesCounts(List<UsersObjectsCount> guestUsersAnalysesCounts) {
    this.guestUsersAnalysesCounts = guestUsersAnalysesCounts;
  }

  @JsonProperty("registeredUsersFiltersCounts")
  public List<UsersObjectsCount> getRegisteredUsersFiltersCounts() {
    return this.registeredUsersFiltersCounts;
  }

  @JsonProperty("registeredUsersFiltersCounts")
  public void setRegisteredUsersFiltersCounts(
      List<UsersObjectsCount> registeredUsersFiltersCounts) {
    this.registeredUsersFiltersCounts = registeredUsersFiltersCounts;
  }

  @JsonProperty("guestUsersFiltersCounts")
  public List<UsersObjectsCount> getGuestUsersFiltersCounts() {
    return this.guestUsersFiltersCounts;
  }

  @JsonProperty("guestUsersFiltersCounts")
  public void setGuestUsersFiltersCounts(List<UsersObjectsCount> guestUsersFiltersCounts) {
    this.guestUsersFiltersCounts = guestUsersFiltersCounts;
  }

  @JsonProperty("registeredUsersVisualizationsCounts")
  public List<UsersObjectsCount> getRegisteredUsersVisualizationsCounts() {
    return this.registeredUsersVisualizationsCounts;
  }

  @JsonProperty("registeredUsersVisualizationsCounts")
  public void setRegisteredUsersVisualizationsCounts(
      List<UsersObjectsCount> registeredUsersVisualizationsCounts) {
    this.registeredUsersVisualizationsCounts = registeredUsersVisualizationsCounts;
  }

  @JsonProperty("guestUsersVisualizationsCounts")
  public List<UsersObjectsCount> getGuestUsersVisualizationsCounts() {
    return this.guestUsersVisualizationsCounts;
  }

  @JsonProperty("guestUsersVisualizationsCounts")
  public void setGuestUsersVisualizationsCounts(
      List<UsersObjectsCount> guestUsersVisualizationsCounts) {
    this.guestUsersVisualizationsCounts = guestUsersVisualizationsCounts;
  }
}
