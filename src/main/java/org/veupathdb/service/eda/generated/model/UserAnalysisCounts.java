package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = UserAnalysisCountsImpl.class
)
public interface UserAnalysisCounts {
  @JsonProperty("analysesCount")
  Integer getAnalysesCount();

  @JsonProperty("analysesCount")
  void setAnalysesCount(Integer analysesCount);

  @JsonProperty("importedAnalysesCount")
  Integer getImportedAnalysesCount();

  @JsonProperty("importedAnalysesCount")
  void setImportedAnalysesCount(Integer importedAnalysesCount);

  @JsonProperty("analysesPerStudy")
  List<StudyCount> getAnalysesPerStudy();

  @JsonProperty("analysesPerStudy")
  void setAnalysesPerStudy(List<StudyCount> analysesPerStudy);

  @JsonProperty("importedAnalysesPerStudy")
  List<StudyCount> getImportedAnalysesPerStudy();

  @JsonProperty("importedAnalysesPerStudy")
  void setImportedAnalysesPerStudy(List<StudyCount> importedAnalysesPerStudy);

  @JsonProperty("registeredUsersCount")
  Integer getRegisteredUsersCount();

  @JsonProperty("registeredUsersCount")
  void setRegisteredUsersCount(Integer registeredUsersCount);

  @JsonProperty("guestUsersCount")
  Integer getGuestUsersCount();

  @JsonProperty("guestUsersCount")
  void setGuestUsersCount(Integer guestUsersCount);

  @JsonProperty("registeredAnalysesCount")
  Integer getRegisteredAnalysesCount();

  @JsonProperty("registeredAnalysesCount")
  void setRegisteredAnalysesCount(Integer registeredAnalysesCount);

  @JsonProperty("guestAnalysesCount")
  Integer getGuestAnalysesCount();

  @JsonProperty("guestAnalysesCount")
  void setGuestAnalysesCount(Integer guestAnalysesCount);

  @JsonProperty("registeredFiltersCount")
  Integer getRegisteredFiltersCount();

  @JsonProperty("registeredFiltersCount")
  void setRegisteredFiltersCount(Integer registeredFiltersCount);

  @JsonProperty("guestFiltersCount")
  Integer getGuestFiltersCount();

  @JsonProperty("guestFiltersCount")
  void setGuestFiltersCount(Integer guestFiltersCount);

  @JsonProperty("registeredVisualizationsCount")
  Integer getRegisteredVisualizationsCount();

  @JsonProperty("registeredVisualizationsCount")
  void setRegisteredVisualizationsCount(Integer registeredVisualizationsCount);

  @JsonProperty("guestVisualizationsCount")
  Integer getGuestVisualizationsCount();

  @JsonProperty("guestVisualizationsCount")
  void setGuestVisualizationsCount(Integer guestVisualizationsCount);

  @JsonProperty("registeredUsersAnalysesCounts")
  List<UsersObjectsCount> getRegisteredUsersAnalysesCounts();

  @JsonProperty("registeredUsersAnalysesCounts")
  void setRegisteredUsersAnalysesCounts(List<UsersObjectsCount> registeredUsersAnalysesCounts);

  @JsonProperty("guestUsersAnalysesCounts")
  List<UsersObjectsCount> getGuestUsersAnalysesCounts();

  @JsonProperty("guestUsersAnalysesCounts")
  void setGuestUsersAnalysesCounts(List<UsersObjectsCount> guestUsersAnalysesCounts);

  @JsonProperty("registeredUsersFiltersCounts")
  List<UsersObjectsCount> getRegisteredUsersFiltersCounts();

  @JsonProperty("registeredUsersFiltersCounts")
  void setRegisteredUsersFiltersCounts(List<UsersObjectsCount> registeredUsersFiltersCounts);

  @JsonProperty("guestUsersFiltersCounts")
  List<UsersObjectsCount> getGuestUsersFiltersCounts();

  @JsonProperty("guestUsersFiltersCounts")
  void setGuestUsersFiltersCounts(List<UsersObjectsCount> guestUsersFiltersCounts);

  @JsonProperty("registeredUsersVisualizationsCounts")
  List<UsersObjectsCount> getRegisteredUsersVisualizationsCounts();

  @JsonProperty("registeredUsersVisualizationsCounts")
  void setRegisteredUsersVisualizationsCounts(
      List<UsersObjectsCount> registeredUsersVisualizationsCounts);

  @JsonProperty("guestUsersVisualizationsCounts")
  List<UsersObjectsCount> getGuestUsersVisualizationsCounts();

  @JsonProperty("guestUsersVisualizationsCounts")
  void setGuestUsersVisualizationsCounts(List<UsersObjectsCount> guestUsersVisualizationsCounts);
}
