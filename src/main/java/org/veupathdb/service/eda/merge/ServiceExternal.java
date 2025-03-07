package org.veupathdb.service.eda.merge;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ContainerRequest;
import org.gusdb.fgputil.validation.ValidationException;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.lib.container.jaxrs.server.annotations.DisableJackson;
import org.veupathdb.lib.container.jaxrs.utils.RequestKeys;
import org.veupathdb.service.eda.common.auth.StudyAccess;
import org.veupathdb.service.eda.common.client.NonEmptyResultStream;
import org.veupathdb.service.eda.common.model.Units;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.generated.resources.Merging;
import org.veupathdb.service.eda.merge.core.MergeRequestProcessor;
import org.veupathdb.service.eda.merge.core.request.MergedTabularRequestResources;
import org.veupathdb.service.eda.merge.core.request.RequestResources;

import java.util.Arrays;
import java.util.List;

import static org.veupathdb.service.eda.util.Exceptions.errToBadRequest;

/**
 * Implementation of endpoints at or below the /merging top-level endpoint.  These endpoints are intended to be
 * accessible via the external docker network; therefore authentication/authorization is required.
 */
@Authenticated(allowGuests = true)
public class ServiceExternal implements Merging {

  private static final Logger LOG = LogManager.getLogger(ServiceExternal.class);

  private static final String MISSING_AUTH_MSG =
    "Request must include authentication information in the form of a " +
      RequestKeys.BEARER_TOKEN_HEADER + " header or query param";

  @Context
  ContainerRequest _request;

  /**
   * Returns 204 with no action taken.  The purpose of this endpoint is to get the derived variable specs into the API docs
   *
   * @param entity entity containing a config for each type of derived variable
   * @return success response with empty body
   */
  @Override
  public PostMergingDerivedVariablesInputSpecsResponse postMergingDerivedVariablesInputSpecs(DerivedVariableDocumentationRequest entity) {
    return PostMergingDerivedVariablesInputSpecsResponse.respond204();
  }

  /**
   * Returns metadata about supported units and unit conversions
   *
   * @return units metadata response
   */
  @Override
  public GetMergingDerivedVariablesMetadataUnitsResponse getMergingDerivedVariablesMetadataUnits() {
    // no need to verify user or user perms; anyone can see units metadata
    return GetMergingDerivedVariablesMetadataUnitsResponse.respond200WithApplicationJson(createUnitsApiResponse());
  }

  private UnitConversionMetadataResponse createUnitsApiResponse() {
    UnitConversionMetadataResponse response = new UnitConversionMetadataResponseImpl();
    response.setTypes(Arrays.stream(Units.UnitType.values()).map(type -> {
      UnitType apiType = new UnitTypeImpl();
      apiType.setDisplayName(type.name().toLowerCase().replace('_',' '));
      apiType.setUnits(Arrays.stream(Units.Unit.values())
        .filter(unit -> unit.getType() == type)
        .map(unit -> {
          Unit apiUnit = new UnitImpl();
          apiUnit.setDisplayName(unit.name().toLowerCase().replace('_',' '));
          apiUnit.setValues(unit.getValues());
          return apiUnit;
        })
        .toList());
      return apiType;
    }).toList());
    return response;
  }

  @Override
  public PostMergingDerivedVariablesMetadataVariablesResponse postMergingDerivedVariablesMetadataVariables(DerivedVariableBulkMetadataRequest requestBody) {
    // check access to full tabular results since this endpoint is intended to be exposed through traefik
    return PostMergingDerivedVariablesMetadataVariablesResponse.respond200WithApplicationJson(processDvMetadataRequest(requestBody));
  }

  @DisableJackson
  @Override
  public PostMergingQueryResponse postMergingQuery(MergedEntityTabularPostRequest requestBody) {
    var user = UserProvider.lookupUser(_request).orElseThrow();

    // check access to full tabular results since this endpoint is intended to be exposed through traefik
    StudyAccess.confirmPermission(user.getUserId(), requestBody.getStudyId(), StudyAccess::allowResultsAll);

    return PostMergingQueryResponse.respond200WithTextTabSeparatedValues(processMergedTabularRequest(requestBody));
  }

  @NotNull
  static List<DerivedVariableMetadata> processDvMetadataRequest(DerivedVariableBulkMetadataRequest requestBody) {
    return errToBadRequest(() -> processDvMetadataRequest(requestBody.getStudyId(), requestBody.getDerivedVariables()));
  }

  @NotNull
  public static List<DerivedVariableMetadata> processDvMetadataRequest(
    @NotNull String studyId,
    @NotNull List<DerivedVariableSpec> derivedVariables
  ) throws ValidationException {
    var requestDvColumns = VariableDef.toDotNotationSet(derivedVariables);

    return new RequestResources(studyId, derivedVariables)
      .getDerivedVariableFactory()
      .getAllDerivedVars()
      .stream()
      // only return metadata for requested vars; others may have been generated by DV plugins
      .filter(dv -> requestDvColumns.contains(VariableDef.toDotNotation(dv)))
      // cast to superclass for return value
      .map(o -> (DerivedVariableMetadata)o)
      .toList();
  }

  static EntityTabularPostResponseStream processMergedTabularRequest(MergedEntityTabularPostRequest requestBody) {
    try {
      return new EntityTabularPostResponseStream(
        new MergeRequestProcessor(new MergedTabularRequestResources(requestBody))
          .createMergedResponseSupplier());
    }
    catch (ValidationException e) {
      LOG.error("Invalid request", e);
      throw new BadRequestException(e.getMessage());
    } catch (NonEmptyResultStream.EmptyResultException e) {
      // This will be caught and handled by data service.
      return new EntityTabularPostResponseStream(out -> LOG.info("Returning empty response."));
    }
  }
}
