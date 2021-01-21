package org.veupathdb.service.edass.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.edass.Resources;
import org.veupathdb.service.edass.generated.model.*;
import org.veupathdb.service.edass.model.*;
import org.veupathdb.service.edass.stubdb.StubDb;

import javax.sql.DataSource;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StudiesTest {

  private static TestModel _model;
  private static DataSource _dataSource;
  private static FiltersForTesting _filtersForTesting;
  private static Study _study;

  @BeforeAll
  public static void setUp() {
    _model = new TestModel();
    _dataSource = StubDb.getDataSource();
    _study = Study.loadStudy(_dataSource, "DS-2324");
    _filtersForTesting = new FiltersForTesting(_study);
  }

  @Test
  @DisplayName("Test entity tree to api tree")
  void testEntityTreeToAPITree() {

    APIEntity apiEntityTree = Studies.entityTreeToAPITree(_study.getEntityTree());
    assertEquals("GEMS_House", apiEntityTree.getId());

    assertEquals(2, apiEntityTree.getChildren().size());
  }

  @Test
  @DisplayName("Test get study details")
  void testGetStudyDetails() {
    APIStudyDetail studyDetail = Studies.getApiStudyDetail("DS-2324");
    assertNotNull(studyDetail);
  }

  @Test
  @DisplayName("Test valid construction of filters from API filters")
  void testConstructFilters() {
    
    List<APIFilter> afs = new ArrayList<>();
    
    APIStringSetFilter stringFilter = new APIStringSetFilterImpl();
    stringFilter.setEntityId(_model.participant.getId());
    stringFilter.setVariableId(_model.shoesize.getId());
    
    afs.add(stringFilter);
    
    APINumberRangeFilter numberFilter = new APINumberRangeFilterImpl();
    numberFilter.setEntityId(_model.observation.getId());
    numberFilter.setVariableId(_model.weight.getId());
    afs.add(numberFilter);
    
    Studies.constructFiltersFromAPIFilters(_model.study, afs);
    
    assertEquals(2, afs.size());
  }

  @Test
  @DisplayName("Test rejection of invalid API filters")
  void testIncorrectConstructFilters() {
    
    assertThrows(InternalServerErrorException.class, () -> {
      List<APIFilter> afs = new ArrayList<>();

      // a legit filter
      APIStringSetFilter stringFilter = new APIStringSetFilterImpl();
      stringFilter.setEntityId(_model.participant.getId());
      stringFilter.setVariableId(_model.shoesize.getId());

      afs.add(stringFilter);

      // illegit... can't be the superclass
      APIFilter nakedFilter = new APIFilterImpl();
      nakedFilter.setEntityId(_model.observation.getId());
      nakedFilter.setVariableId(_model.weight.getId());
      afs.add(nakedFilter);

      Studies.constructFiltersFromAPIFilters(_model.study, afs);
    });
  }
  @Test
  @DisplayName("Test variable distribution - no filters")
  void testVariableDistributionNoFilters() throws IOException {

    Study study = Study.loadStudy(Resources.getApplicationDataSource(), "DS-2324");

    String entityId = "GEMS_Part";
    Entity entity = study.getEntity(entityId).orElseThrow();

    String varId = "var_17";
    Variable var = entity.getVariable(varId).orElseThrow();

    List<Filter> filters = Collections.emptyList();

    int expectedVariableCount = 4;

    Map<String, Integer> expectedDistribution = new HashMap<>(){{
      put("blond", 2);
      put("brown", 1);
      put("silver", 1);
    }};

    testDistributionResponse(study, entity, var, filters, expectedVariableCount, expectedDistribution);
  }

  @Test
  @DisplayName("Test variable distribution - with filters")
  void testVariableDistribution() throws IOException {

    Study study = Study.loadStudy(Resources.getApplicationDataSource(), "DS-2324");

    String entityId = "GEMS_Part";
    Entity entity = study.getEntity(entityId).orElseThrow();

    String varId = "var_17";
    Variable var = entity.getVariable(varId).orElseThrow();

    List<Filter> filters = new ArrayList<>();
    filters.add(_filtersForTesting.houseCityFilter);
    filters.add(_filtersForTesting.houseObsWaterSupplyFilter);

    int expectedVariableCount = 2;

    Map<String, Integer> expectedDistribution = new HashMap<>(){{
      put("brown", 1);
      put("silver", 1);
    }};

    testDistributionResponse(study, entity, var, filters, expectedVariableCount, expectedDistribution);
  }

  private void testDistributionResponse(Study study, Entity entity, Variable var, List<Filter> filters, int expectedVariableCount, Map<String, Integer> expectedDistribution) throws IOException {

    // get distribution producer
    StreamingOutput responseProducer = Studies.getDistributionResponseStreamer(_dataSource, study, entity, filters, var);

    // produce response and write to local stream
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    responseProducer.write(outputStream);

    // parse response into API object
    VariableDistributionPostResponse response = new ObjectMapper()
        .readerFor(VariableDistributionPostResponse.class).readValue(outputStream.toString());

    // check variable count
    assertEquals(expectedVariableCount, response.getEntitiesCount());

    Map<String, Object> responseRows = response.getDistribution().getAdditionalProperties();

    // check number of distribution rows
    assertEquals(expectedDistribution.size(), responseRows.size());

    for (Map.Entry<String,Integer> expectedRow : expectedDistribution.entrySet()) {
      Integer count = (Integer)responseRows.get(expectedRow.getKey()); // will throw if not integer
      // check row exists for key
      assertNotNull(count);
      // check distribution size for key
      assertEquals(expectedRow.getValue(), count);
    }
  }


}
