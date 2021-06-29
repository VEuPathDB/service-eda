package org.veupathdb.service.eda.ss.service;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import org.gusdb.fgputil.Tuples.TwoTuple;
import org.gusdb.fgputil.functional.TreeNode;
import org.veupathdb.service.eda.generated.model.APIDateRangeFilter;
import org.veupathdb.service.eda.generated.model.APIDateSetFilter;
import org.veupathdb.service.eda.generated.model.APIDateVariable;
import org.veupathdb.service.eda.generated.model.APIDateVariableImpl;
import org.veupathdb.service.eda.generated.model.APIEntity;
import org.veupathdb.service.eda.generated.model.APIEntityImpl;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.APILongitudeRangeFilter;
import org.veupathdb.service.eda.generated.model.APILongitudeVariable;
import org.veupathdb.service.eda.generated.model.APILongitudeVariableImpl;
import org.veupathdb.service.eda.generated.model.APINumberRangeFilter;
import org.veupathdb.service.eda.generated.model.APINumberSetFilter;
import org.veupathdb.service.eda.generated.model.APINumberVariable;
import org.veupathdb.service.eda.generated.model.APINumberVariableImpl;
import org.veupathdb.service.eda.generated.model.APIStringSetFilter;
import org.veupathdb.service.eda.generated.model.APIStringVariable;
import org.veupathdb.service.eda.generated.model.APIStringVariableImpl;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;
import org.veupathdb.service.eda.generated.model.APIStudyDetailImpl;
import org.veupathdb.service.eda.generated.model.APIStudyOverview;
import org.veupathdb.service.eda.generated.model.APIStudyOverviewImpl;
import org.veupathdb.service.eda.generated.model.APITabularReportConfig;
import org.veupathdb.service.eda.generated.model.APIVariable;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableDisplayType;
import org.veupathdb.service.eda.generated.model.APIVariablesCategory;
import org.veupathdb.service.eda.generated.model.APIVariablesCategoryImpl;
import org.veupathdb.service.eda.generated.model.EntityCountPostRequest;
import org.veupathdb.service.eda.generated.model.EntityCountPostResponse;
import org.veupathdb.service.eda.generated.model.EntityCountPostResponseImpl;
import org.veupathdb.service.eda.generated.model.EntityIdGetResponse;
import org.veupathdb.service.eda.generated.model.EntityIdGetResponseImpl;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.EntityTabularPostResponseStream;
import org.veupathdb.service.eda.generated.model.StudiesGetResponseImpl;
import org.veupathdb.service.eda.generated.model.StudyIdGetResponse;
import org.veupathdb.service.eda.generated.model.StudyIdGetResponseImpl;
import org.veupathdb.service.eda.generated.model.VariableDistributionPostRequest;
import org.veupathdb.service.eda.generated.model.VariableDistributionPostResponseStream;
import org.veupathdb.service.eda.ss.Resources;
import org.veupathdb.service.eda.ss.model.filter.DateRangeFilter;
import org.veupathdb.service.eda.ss.model.filter.DateSetFilter;
import org.veupathdb.service.eda.ss.model.Entity;
import org.veupathdb.service.eda.ss.model.filter.Filter;
import org.veupathdb.service.eda.ss.model.filter.LongitudeRangeFilter;
import org.veupathdb.service.eda.ss.model.filter.NumberRangeFilter;
import org.veupathdb.service.eda.ss.model.filter.NumberSetFilter;
import org.veupathdb.service.eda.ss.model.filter.StringSetFilter;
import org.veupathdb.service.eda.ss.model.Study;
import org.veupathdb.service.eda.ss.model.StudySubsettingUtils;
import org.veupathdb.service.eda.ss.model.TabularReportConfig;
import org.veupathdb.service.eda.ss.model.Variable;
import org.veupathdb.service.eda.ss.model.Variable.VariableType;

import static org.gusdb.fgputil.functional.Functions.cSwallow;

public class Studies implements org.veupathdb.service.eda.generated.resources.Studies {

  static Map<String, APIStudyOverview> apiStudyOverviews;  // cache the overviews
  static Map<String, Study> studies = new ConcurrentHashMap<>(); // cache the studies

  private Study getStudy(String studyId){
    return studies.computeIfAbsent(studyId, id ->
        Study.loadStudy(Resources.getApplicationDataSource(), id));
  }

  @Override
  public GetStudiesResponse getStudies() {
    var out = new StudiesGetResponseImpl();
    out.setStudies(getStudyOverviews(Resources.getApplicationDataSource()));
    return GetStudiesResponse.respond200WithApplicationJson(out);
  }

  private List<APIStudyOverview> getStudyOverviews(DataSource datasource) {
    if (apiStudyOverviews == null) {
      List<Study.StudyOverview> overviews = Study.getStudyOverviews(datasource);
      Map<String, APIStudyOverview> tmp = new LinkedHashMap<>();
      for (Study.StudyOverview overview : overviews) {
        APIStudyOverview study = new APIStudyOverviewImpl();
        study.setId(overview.getId());
        study.setDatasetId(overview.getDatasetId());
        tmp.put(study.getId(), study);
      }
      apiStudyOverviews = tmp;
    }
    return new ArrayList<>( apiStudyOverviews.values() );
  }

  @Override
  public GetStudiesByStudyIdResponse getStudiesByStudyId(String studyId) {
    Study study = getStudy(studyId);
    APIStudyDetail apiStudyDetail = getApiStudyDetail(study);
    StudyIdGetResponse response = new StudyIdGetResponseImpl();
    response.setStudy(apiStudyDetail);
    
    return GetStudiesByStudyIdResponse.respond200WithApplicationJson(response);
  }

  @Override
  public GetStudiesEntitiesByStudyIdAndEntityIdResponse getStudiesEntitiesByStudyIdAndEntityId(String studyId, String entityId) {
    APIStudyDetail apiStudyDetail = getApiStudyDetail(getStudy(studyId));
    APIEntity entity = findEntityById(apiStudyDetail.getRootEntity(), entityId).orElseThrow(NotFoundException::new);
    EntityIdGetResponse response = new EntityIdGetResponseImpl();
    // copy properties of found entity, skipping children
    response.setId(entity.getId());
    response.setDescription(entity.getDescription());
    response.setDisplayName(entity.getDisplayName());
    response.setDisplayNamePlural(entity.getDisplayNamePlural());
    response.setVariables(entity.getVariables());
    for (Map.Entry<String,Object> prop : entity.getAdditionalProperties().entrySet()) {
      response.setAdditionalProperties(prop.getKey(), prop.getValue());
    }
    return GetStudiesEntitiesByStudyIdAndEntityIdResponse.respond200WithApplicationJson(response);
  }

  private  Optional<APIEntity> findEntityById(APIEntity entity, String entityId) {
    if (entity.getId().equals(entityId)) {
      return Optional.of(entity);
    }
    for (APIEntity child : entity.getChildren()){
      Optional<APIEntity> foundEntity = findEntityById(child, entityId);
      if (foundEntity.isPresent()) return foundEntity;
    }
    return Optional.empty();
  }

  public static APIStudyDetail getApiStudyDetail(Study study) {

    APIEntity apiEntityTree = entityTreeToAPITree(study.getEntityTree());

    APIStudyDetail apiStudyDetail = new APIStudyDetailImpl();
    apiStudyDetail.setId(study.getStudyId());
    apiStudyDetail.setDatasetId(study.getDatasetId());
    apiStudyDetail.setRootEntity(apiEntityTree);
    // TODO: lose or fill in study.setName() prop
    return apiStudyDetail;
  }
  
  public static APIEntity entityTreeToAPITree(TreeNode<Entity> root) {
    return root.mapStructure((entity, mappedChildren) -> {
      APIEntity apiEntity = new APIEntityImpl();
      apiEntity.setDescription(entity.getDescription());
      apiEntity.setDisplayName(entity.getDisplayName());
      apiEntity.setId(entity.getId());
      apiEntity.setIdColumnName(entity.getPKColName());
      apiEntity.setChildren(mappedChildren);
      
      List<APIVariable> apiVariables = new ArrayList<>();
      for (Variable var : entity.getVariables())
        apiVariables.add(variableToAPIVariable(var));
      apiEntity.setVariables(apiVariables);

      return apiEntity;
    });
  }
  
  private static APIVariable variableToAPIVariable(Variable var) {
    if (!var.getHasValues()) {
      APIVariablesCategory apiVar = new APIVariablesCategoryImpl();
      setApiVarProps(apiVar, var);
      return apiVar;
    }
    if (var.getType() == VariableType.DATE) {
      APIDateVariable apiVar = new APIDateVariableImpl();
      setApiVarProps(apiVar, var);
      apiVar.setDataShape(APIVariableDataShape.valueOf(var.getDataShape().toString()));
      apiVar.setDisplayType(APIVariableDisplayType.valueOf(var.getDisplayType().toString()));
      return apiVar;
    }
    else if (var.getType() == VariableType.NUMBER) {
      APINumberVariable apiVar = new APINumberVariableImpl();
      setApiVarProps(apiVar, var);
      apiVar.setUnits(var.getUnits());
      apiVar.setDataShape(APIVariableDataShape.valueOf(var.getDataShape().toString()));
      apiVar.setDisplayType(APIVariableDisplayType.valueOf(var.getDisplayType().toString()));
      return apiVar;
    }
    else if (var.getType() == VariableType.STRING) {
      APIStringVariable apiVar = new APIStringVariableImpl();
      apiVar.setDataShape(APIVariableDataShape.valueOf(var.getDataShape().toString()));
      apiVar.setDisplayType(APIVariableDisplayType.valueOf(var.getDisplayType().toString()));
      setApiVarProps(apiVar, var);
      return apiVar;
    }
    else if (var.getType() == VariableType.LONGITUDE) {
      APILongitudeVariable apiVar = new APILongitudeVariableImpl();
      apiVar.setDataShape(APIVariableDataShape.valueOf(var.getDataShape().toString()));
      apiVar.setDisplayType(APIVariableDisplayType.valueOf(var.getDisplayType().toString()));
      setApiVarProps(apiVar, var);
      return apiVar;
    }
    else {
      throw new RuntimeException("Invalid variable type " + var.getType());
    }
  }
  
  private static void setApiVarProps(APIVariable apiVar, Variable var) {
    apiVar.setId(var.getId());
    apiVar.setDisplayName(var.getDisplayName());
    apiVar.setProviderLabel(var.getProviderLabel());
    apiVar.setParentId(var.getParentId());
  }

  @Override
  public PostStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse 
  postStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableId(
      String studyId, String entityId, String variableId, VariableDistributionPostRequest request) {

  //  long start = System.currentTimeMillis();
    DataSource datasource = Resources.getApplicationDataSource();
   // System.out.println("---------------- 1: " + String.valueOf(System.currentTimeMillis() - start));
    
    // unpack data from API input to model objects
    List<String> vars = new ArrayList<>();
    vars.add(variableId);  // force into a list for the unpacker
    UnpackedRequest unpacked = unpack(datasource, studyId, entityId, request.getFilters(), vars, null);
   // System.out.println("---------------- 2: " + String.valueOf(System.currentTimeMillis() - start));

    Variable var = unpacked.getTargetEntity().getVariable(variableId)
        .orElseThrow(() -> new NotFoundException("Variable ID not found: " + variableId));
   // System.out.println("---------------- 3: " + String.valueOf(System.currentTimeMillis() - start));

    VariableDistributionPostResponseStream streamer = getDistributionResponseStreamer(
        datasource, unpacked.getStudy(), unpacked.getTargetEntity(), unpacked.getFilters(), var);
   // System.out.println("---------------- 4: " + String.valueOf(System.currentTimeMillis() - start));

    return PostStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse.
        respond200WithApplicationJson(streamer);
  }

  public static VariableDistributionPostResponseStream getDistributionResponseStreamer(
      DataSource dataSource,
      Study study,
      Entity targetEntity,
      List<Filter> filters,
      Variable variable
  ) {
    // get entity tree pruned to those entities of current interest
    TreeNode<Entity> prunedEntityTree = StudySubsettingUtils.pruneTree(
        study.getEntityTree(), filters, targetEntity);

    // get variable count (may do in parallel later)
    int variableCount = StudySubsettingUtils.getVariableCount(
        dataSource, prunedEntityTree, targetEntity, variable, filters);

    return new VariableDistributionPostResponseStream(outStream -> {
      BufferedOutputStream bufferedOutput = new BufferedOutputStream(outStream);
      try(
        // create a stream of distribution tuples converted from a database result
        Stream<TwoTuple<String, Integer>> distributionStream =
            StudySubsettingUtils.produceVariableDistribution(
              dataSource, prunedEntityTree, targetEntity, variable, filters);
        // create a JSON generator around the output stream
        JsonGenerator json = new JsonFactory().createGenerator(bufferedOutput, JsonEncoding.UTF8)
      ) {
        // write the output
        json.writeStartObject();
        json.writeNumberField("entitiesCount", variableCount);
        json.writeFieldName("distribution");
        json.writeStartObject();
        distributionStream.forEach(cSwallow(row -> json.writeNumberField(row.getKey(), row.getValue())));
        json.writeEndObject();
        json.writeEndObject();
        json.flush();
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  public PostStudiesEntitiesTabularByStudyIdAndEntityIdResponse postStudiesEntitiesTabularByStudyIdAndEntityId(String studyId,
      String entityId, EntityTabularPostRequest requestBody) {
    
    DataSource datasource = Resources.getApplicationDataSource();
    
    UnpackedRequest request = unpack(datasource, studyId, entityId, requestBody.getFilters(), 
    		requestBody.getOutputVariableIds(), requestBody.getReportConfig());
    
    EntityTabularPostResponseStream streamer = new EntityTabularPostResponseStream
        (outStream -> StudySubsettingUtils.produceTabularSubset(datasource, request.getStudy(),
            request.getTargetEntity(), request.getRequestedVariables(), request.getFilters(),
            request.getReportConfig(), outStream));

    return PostStudiesEntitiesTabularByStudyIdAndEntityIdResponse
        .respond200WithTextPlain(streamer);
  }

  @Override
  public PostStudiesEntitiesCountByStudyIdAndEntityIdResponse postStudiesEntitiesCountByStudyIdAndEntityId(
      String studyId, String entityId, EntityCountPostRequest rawRequest) {

    DataSource datasource = Resources.getApplicationDataSource();

    // unpack data from API input to model objects
    List<String> vars = new ArrayList<>();
    UnpackedRequest request = unpack(datasource, studyId, entityId, rawRequest.getFilters(), vars, null);

    TreeNode<Entity> prunedEntityTree = StudySubsettingUtils.pruneTree(
        request.getStudy().getEntityTree(), request.getFilters(), request.getTargetEntity());

    int count = StudySubsettingUtils.getEntityCount(
        datasource, prunedEntityTree, request.getTargetEntity(), request.getFilters());

    EntityCountPostResponse response = new EntityCountPostResponseImpl();
    response.setCount(count);

    return  PostStudiesEntitiesCountByStudyIdAndEntityIdResponse.respond200WithApplicationJson(response);
  }
  
  private UnpackedRequest unpack(DataSource datasource, String studyId, String entityId, List<APIFilter> apiFilters, 
		  List<String> variableIds, APITabularReportConfig apiReportConfig) {
    String studIdStr = "Study ID " + studyId;
    if (!validateStudyId(datasource, studyId))
      throw new NotFoundException(studIdStr + " is not found.");
   
    Study study = getStudy(studyId);
    Entity entity = study.getEntity(entityId).orElseThrow(() -> new NotFoundException("In " + studIdStr + " Entity ID not found: " + entityId));
    
    List<Variable> variables = getEntityVariables(entity, variableIds);

    List<Filter> filters = constructFiltersFromAPIFilters(study, apiFilters);
    
    TabularReportConfig reportConfig = constructTabularReportConfigFromAPIReportConfig(apiReportConfig);
  
    return new UnpackedRequest(study, entity, variables, filters, reportConfig);
  }
  /*
   * return true if valid study id
   */
  public boolean validateStudyId(DataSource datasource, String studyId) {
    return getStudyOverviews(datasource).stream()
            .anyMatch(study -> study.getId().equals(studyId));
  }

  /*
   * Given a study and a set of API filters, construct and return a set of filters, each being the appropriate
   * filter subclass
   */
  static List<Filter> constructFiltersFromAPIFilters(Study study, List<APIFilter> filters) {
    List<Filter> subsetFilters = new ArrayList<>();

    String errPrfx = "A filter references an unfound ";
    
    for (APIFilter apiFilter : filters) {
      
      // validate filter's entity id
      Supplier<BadRequestException> excep = 
          () -> new BadRequestException(errPrfx + "entity ID: " + apiFilter.getEntityId());
      Entity entity = study.getEntity(apiFilter.getEntityId()).orElseThrow(excep);
      
      // validate filter's variable id
      String varId = apiFilter.getVariableId();
      entity.getVariable(varId).orElseThrow(() -> new BadRequestException("Variable '" + varId + "' is not found"));

      Filter newFilter;
      if (apiFilter instanceof APIDateRangeFilter) {
        APIDateRangeFilter f = (APIDateRangeFilter)apiFilter;
        newFilter = new DateRangeFilter(entity, varId,
            convertDate(f.getMin()), convertDate(f.getMax()));
      } else if (apiFilter instanceof APIDateSetFilter) {
        APIDateSetFilter f = (APIDateSetFilter)apiFilter;
        List<LocalDateTime> dateSet = new ArrayList<>();
        for (String dateStr : f.getDateSet()) dateSet.add(convertDate(dateStr));
        newFilter = new DateSetFilter(entity, varId, dateSet);
      } else if (apiFilter instanceof APINumberRangeFilter) {
        APINumberRangeFilter f = (APINumberRangeFilter)apiFilter;
        newFilter = new NumberRangeFilter(entity, varId, f.getMin(), f.getMax());
      } else if (apiFilter instanceof APINumberSetFilter) {
        APINumberSetFilter f = (APINumberSetFilter)apiFilter;
        newFilter = new NumberSetFilter(entity, varId, f.getNumberSet());
      } else if (apiFilter instanceof APILongitudeRangeFilter) {
        APILongitudeRangeFilter f = (APILongitudeRangeFilter)apiFilter;
        newFilter = new LongitudeRangeFilter(entity, varId, f.getLeft(), f.getRight());
      } else if (apiFilter instanceof APIStringSetFilter) {
        APIStringSetFilter f = (APIStringSetFilter)apiFilter;
        newFilter = new StringSetFilter(entity, varId, f.getStringSet());
      } else
        throw new InternalServerErrorException("Input filter not an expected subclass of Filter");

      subsetFilters.add(newFilter);
    }
    return subsetFilters;
  }

  static List<Variable> getEntityVariables(Entity entity, List<String> variableIds) {

    List<Variable> variables = new ArrayList<>();
    
    for (String varId : variableIds) {
      String errMsg = "Variable '" + varId + "' is not found for entity with ID: '" + entity.getId() + "'";
      variables.add(entity.getVariable(varId).orElseThrow(() -> new BadRequestException(errMsg)));
    }
    return variables;
  }
  
  static LocalDateTime convertDate(String dateStr) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
      return LocalDateTime.parse(dateStr, formatter);
    }
    catch (DateTimeParseException e) {
      throw new BadRequestException("Can't parse date string: " + dateStr);
    }
  }
  
  static TabularReportConfig constructTabularReportConfigFromAPIReportConfig(APITabularReportConfig apiConfig) {
	  return new TabularReportConfig(apiConfig.getSortingVariableIds(), apiConfig.getPagingConfig().getNumRows(),
			  apiConfig.getPagingConfig().getOffset());
			  
  }

  static class UnpackedRequest {

    private final Study _study;
    private final List<Filter> _filters;
    private final Entity _targetEntity;
    private final List<Variable> _requestedVariables;
    private final TabularReportConfig _reportConfig;

    UnpackedRequest(Study study, Entity targetEntity, List<Variable> requestedVariables, 
    		List<Filter> filters, TabularReportConfig reportConfig) {
      _study = study;
      _targetEntity = targetEntity;
      _filters = filters;
      _requestedVariables = requestedVariables;
      _reportConfig = reportConfig;
    }

    public TabularReportConfig getReportConfig() {
		return _reportConfig;
	}

	public Study getStudy() {
      return _study;
    }

    public List<Filter> getFilters() {
      return _filters;
    }

    public Entity getTargetEntity() {
      return _targetEntity;
    }

    public List<Variable> getRequestedVariables() {
      return _requestedVariables;
    }
  }
}
