package org.veupathdb.service.eda.ss.model;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gusdb.fgputil.functional.TreeNode;
import org.veupathdb.service.eda.ss.model.Variable.VariableDataShape;
import org.veupathdb.service.eda.ss.model.filter.DateRangeFilter;
import org.veupathdb.service.eda.ss.model.filter.DateSetFilter;
import org.veupathdb.service.eda.ss.model.filter.Filter;
import org.veupathdb.service.eda.ss.model.filter.NumberRangeFilter;
import org.veupathdb.service.eda.ss.model.filter.NumberSetFilter;
import org.veupathdb.service.eda.ss.model.filter.StringSetFilter;

/**
 * A class that holds a test model of a study and supporting objects
 * @author Steve
 *
 */
public class TestModel {

  // reusable study objects
  public Study study; 
  
  public Entity household;
  public Entity householdObs;
  public Entity participant;
  public Entity observation;
  public Entity sample;
  public Entity treatment;
  
  public VariableWithValues roof;
  public VariableWithValues shoesize;
  public VariableWithValues weight;
  public VariableWithValues favNumber;
  public VariableWithValues birthDate;
  public VariableWithValues favNewYears;
  public VariableWithValues mood;
  public VariableWithValues haircolor;
  public VariableWithValues networth;
  public VariableWithValues earsize;
  public VariableWithValues waterSupply;
  
  public Filter obsWeightFilter;
  public Filter houseRoofFilter;
  public Filter obsFavNumberFilter; // categorical numeric
  public Filter obsBirthDateFilter;  // continuous date
  public Filter obsFavNewYearsFilter;  // categorical numeric
  public Filter obsMoodFilter; // string 
  public Filter houseObsWaterSupplyFilter; // string 
  
  public TestModel() {
    createTestEntities();
    Study.StudyOverview overview = new Study.StudyOverview("GEMS", "datasetid_2222", "ds2324");
    study = new Study(overview, constructEntityTree(), createIdMap());
    constructVariables();
    createFilters();
  }
  
  private void createTestEntities() {
    household = new Entity("GEMS_House", "ds2324", "Household", "Households", "descrip", "Hshld");
    householdObs = new Entity("GEMS_HouseObs", "ds2324", "Household Observation", "Household Observations", "descrip", "HshldObsvtn");
    participant = new Entity("GEMS_Part", "ds2324", "Participant", "Participants", "descrip", "Prtcpnt");
    observation = new Entity("GEMS_PartObs", "ds2324", "Observation", "Observations", "descrip", "PrtcpntObsrvtn");
    sample = new Entity("GEMS_Sample", "ds2324", "Sample", "Samples", "descrip", "Smpl");
    treatment = new Entity("GEMS_Treat", "ds2324", "Treatment", "Treatments", "descrip", "Trtmnt");
  }
  
  private Map<String, Entity> createIdMap() {
    Map<String, Entity> idMap = new HashMap<>();
    idMap.put("GEMS_House", household);
    idMap.put("GEMS_HouseObs", householdObs);
    idMap.put("GEMS_Part", participant);
    idMap.put("GEMS_PartObs", observation);
    idMap.put("GEMS_Sample", sample);
    idMap.put("GEMS_Treat", treatment);
    return idMap;
   }

  /*
   * return a fresh entity tree.
   */
  public TreeNode<Entity> constructEntityTree() {
    TreeNode<Entity> householdNode = new TreeNode<>(household);

    TreeNode<Entity> houseObsNode = new TreeNode<>(householdObs);
    householdNode.addChildNode(houseObsNode);

    TreeNode<Entity> participantNode = new TreeNode<>(participant);
    householdNode.addChildNode(participantNode);

    TreeNode<Entity> observationNode = new TreeNode<>(observation);
    participantNode.addChildNode(observationNode);
    
    TreeNode<Entity> sampleNode = new TreeNode<>(sample);
    observationNode.addChildNode(sampleNode);
    
    TreeNode<Entity> treatmentNode = new TreeNode<>(treatment);
    observationNode.addChildNode(treatmentNode);
    
    return householdNode;
  }
  
  private void constructVariables() {
/*
	public StringVariable(String providerLabel, String id, Entity entity, 
			VariableDataShape dataShape, VariableDisplayType displayType, String displayName, Integer displayOrder, String parentId,
			String definition, List<String> vocabulary, Boolean isTemporal, Boolean isFeatured, Boolean isMergeKey,
			Number distinctValuesCount, Boolean isMultiValued) 
						
*/	  
    roof = new StringVariable("roof", "var_10", household, VariableDataShape.CATEGORICAL,
            Variable.VariableDisplayType.DEFAULT, "Roof", null, null,
            "Their roof", null, null, null, null, 12, false);
    household.addVariable(roof);
    
    // multi-valued string var
    haircolor = new StringVariable("haircolor", "var_17", participant, Variable.VariableDataShape.CATEGORICAL,
            Variable.VariableDisplayType.DEFAULT, "Hair color", null, null,
            "Their hair color", null, null, null, null, 21, true);
    participant.addVariable(haircolor);
    
    mood  = new StringVariable("mood", "var_16", observation, VariableDataShape.CATEGORICAL,
            Variable.VariableDisplayType.DEFAULT, "Mood", null, null,
            "Their mood", null, null, null, null, 96, false);
    observation.addVariable(mood);

    waterSupply  = new StringVariable("waterSupply", "var_19", householdObs, VariableDataShape.CATEGORICAL,
            Variable.VariableDisplayType.DEFAULT, "Waters supply", null, null,
            "Their water supply", null, null, null, null, 66, false);
    householdObs.addVariable(waterSupply);

    earsize = new StringVariable("earsize", "var_18", participant, VariableDataShape.CATEGORICAL,
			   Variable.VariableDisplayType.DEFAULT, "Roof", null, null,
			   "Their ear size", null, null, null, null, 87, false);
    participant.addVariable(earsize);
    
    /*
	public NumberVariable(String providerLabel, String id, Entity entity, boolean isLongitude,
			VariableDataShape dataShape, VariableDisplayType displayType, Integer displayOrder, String units, Integer precision,
			String displayName, String parentId, String definition, List<String> vocabulary, Number displayRangeMin,
			Number displayRangeMax, Number rangeMin, Number rangeMax, Number binWidthOverride,
			Number binWidth, Boolean isTemporal, Boolean isFeatured, Boolean isMergeKey,
			Number distinctValuesCount, Boolean isMultiValued) {			
*/	  

    // multi-valued number var (what can i say... left and right feet are different!)
    shoesize = new NumberVariable("shoesize", "var_11", participant, VariableType.NUMBER, VariableDataShape.CATEGORICAL,
            Variable.VariableDisplayType.DEFAULT, null, "", 1, "Shoe size", null,
            "their shoe size", null, null, null, null, null, null, null, null, null, null, 47, true);
    participant.addVariable(shoesize);

    networth = new NumberVariable("networth", "var_10", participant, VariableType.NUMBER, VariableDataShape.CONTINUOUS,
            Variable.VariableDisplayType.DEFAULT, null, "", 1, "Net worth", null,
            "Their net worth", null, null, null, null, null, null, null, null, null, null, 875, false);
    participant.addVariable(networth);

    weight = new NumberVariable("weight", "var_12", observation, VariableType.NUMBER, Variable.VariableDataShape.CONTINUOUS,
            Variable.VariableDisplayType.DEFAULT, null, "", 1, "Weight", null,
            "Their weight", null, null, null, null, null, null, null, null, null, null, 65, false);
    observation.addVariable(weight);

    favNumber = new NumberVariable("favNumber", "var_13", observation, VariableType.NUMBER, Variable.VariableDataShape.CATEGORICAL,
            Variable.VariableDisplayType.DEFAULT, null, "", 1, "Favorite number", null,
            "Their favorite number", null, null, null, null, null, null, null, null, null, null, 312, false);
    observation.addVariable(favNumber);
    
    /*
    public DateVariable(String providerLabel, String id, Entity entity,
			VariableDataShape dataShape, VariableDisplayType displayType,
			String displayName, Integer displayOrder, String parentId, String definition, List<String> vocabulary, String displayRangeMin,
			String displayRangeMax, String rangeMin, String rangeMax, String binWidthOverride,
			String binWidth, Boolean isTemporal, Boolean isFeatured, Boolean isMergeKey, 
			Number distinctValuesCount, Boolean isMultiValued)
    */
    birthDate = new DateVariable("birthDate", "var_14", observation, Variable.VariableDataShape.CONTINUOUS,
            Variable.VariableDisplayType.DEFAULT, "Birth date", null, null,
            "Their birth date", null, null, null, null, null, null, "week", 1, null, null, null, 13, false);
    observation.addVariable(birthDate);
    
    favNewYears = new DateVariable("favNewYears", "var_15", observation, Variable.VariableDataShape.CATEGORICAL,
            Variable.VariableDisplayType.DEFAULT, "Fav new years", null, null,
            "Their fav new years", null, null, null, null, null, null, "week", 1, null, null, null, 74, false);
    observation.addVariable(favNewYears);
    
  }
  
  private void createFilters() {
    
    // create observation weight filter
    obsWeightFilter = new NumberRangeFilter(observation, weight.getId(), 10, 20);

    List<Number> favNums = Arrays.asList(new Number[]{5,7,9});
    obsFavNumberFilter = new NumberSetFilter(observation, favNumber.getId(), favNums);

    obsBirthDateFilter = new DateRangeFilter(observation, birthDate.getId(),
        LocalDateTime.of(2019, Month.MARCH, 21, 0, 0),
        LocalDateTime.of(2019, Month.MARCH, 28, 0, 0));

    List<LocalDateTime> dates = new ArrayList<>();
    dates.add(LocalDateTime.of(2019, Month.MARCH, 21, 0, 0));
    dates.add(LocalDateTime.of(2019, Month.MARCH, 28, 0, 0));
    dates.add(LocalDateTime.of(2019, Month.JUNE, 12, 0, 0));
    obsFavNewYearsFilter = new DateSetFilter(observation, favNewYears.getId(), dates);

    List<String> moods = Arrays.asList("happy", "jolly", "giddy");
    obsMoodFilter = new StringSetFilter(observation, mood.getId(), moods);

    obsWeightFilter = new NumberRangeFilter(observation, weight.getId(), 10, 20);

    // create household roof filter
    List<String> roofs = Arrays.asList("metal", "tile");
    houseRoofFilter = new StringSetFilter(household, roof.getId(), roofs);

    // create household observation filter
    List<String> waterSupplies = Arrays.asList("piped", "well");
    houseObsWaterSupplyFilter = new StringSetFilter(householdObs, waterSupply.getId(), waterSupplies);
  }
}
