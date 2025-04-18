package org.veupathdb.service.eda.compute.controller;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.StreamingOutput;
import org.glassfish.jersey.server.ContainerRequest;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.service.eda.common.client.DatasetAccessClient;
import org.veupathdb.service.eda.common.client.EdaSubsettingClient;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.compute.EDACompute;
import org.veupathdb.service.eda.compute.jobs.ReservedFiles;
import org.veupathdb.service.eda.compute.plugins.PluginMeta;
import org.veupathdb.service.eda.compute.plugins.PluginProvider;
import org.veupathdb.service.eda.compute.plugins.PluginRegistry;
import org.veupathdb.service.eda.compute.plugins.alphadiv.AlphaDivPluginProvider;
import org.veupathdb.service.eda.compute.plugins.betadiv.BetaDivPluginProvider;
import org.veupathdb.service.eda.compute.plugins.correlation.CorrelationPluginProvider;
import org.veupathdb.service.eda.compute.plugins.differentialabundance.DifferentialAbundancePluginProvider;
import org.veupathdb.service.eda.compute.plugins.differentialexpression.DifferentialExpressionPluginProvider;
import org.veupathdb.service.eda.compute.plugins.example.ExamplePluginProvider;
import org.veupathdb.service.eda.compute.plugins.rankedabundance.RankedAbundancePluginProvider;
import org.veupathdb.service.eda.compute.plugins.selfcorrelation.SelfCorrelationPluginProvider;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.generated.resources.Computes;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;
import org.veupathdb.service.eda.merge.ServiceExternal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.veupathdb.service.eda.util.Exceptions.errToBadRequest;


/**
 * Compute Plugins Controller
 * <p>
 * This controller is the home location where specific compute plugin endpoints
 * are registered.
 * <p>
 * New plugin endpoints should be added in the Plugin Endpoints region in this
 * file, after the "Plugin Endpoints" doc block and before the "endregion Plugin
 * Endpoints" statement.
 * <p>
 * Plugin endpoints should follow the example set by the
 * {@link #postComputesExample(Boolean, ExamplePluginRequest)} method and call the
 * {@link #submitJob(PluginProvider, ComputeRequestBase, boolean)} method, passing in an
 * instance of the target {@link PluginProvider} for their plugin along with the
 * raw request body (entity).
 *
 * @author Elizabeth Paige Harper - <a href="https://github.com/foxcapades">https://github.com/foxcapades</a>
 * @since 1.0.0
 */
@Authenticated(allowGuests = true)
public class ComputeController implements Computes {

  private static final String TABULAR = "tabular";
  private static final String METADATA = "meta";
  private static final String STATISTICS = "statistics";

  @Context
  private ContainerRequest request;

  // region Plugin Endpoints
  // ╔════════════════════════════════════════════════════════════════════╗ //
  // ║  Plugin Endpoints                                                  ║ //
  // ║                                                                    ║ //
  // ║  Controller methods for handling requests to run specific          ║ //
  // ║  plugins.                                                          ║ //
  // ╚════════════════════════════════════════════════════════════════════╝ //


  @Override
  public PostComputesExampleResponse postComputesExample(Boolean autostart, ExamplePluginRequest entity) {
    return PostComputesExampleResponse.respond200WithApplicationJson(submitJob(new ExamplePluginProvider(), entity, autostart));
  }

  @Override
  public PostComputesExampleByFileResponse postComputesExampleByFile(String file, ExamplePluginRequest entity) {
    return resultFile(new ExamplePluginProvider(), file, entity, PostComputesExampleByFileResponse::respond200WithTextPlain);
  }

  @Override
  public PostComputesBetadivResponse postComputesBetadiv(Boolean autostart, BetaDivPluginRequest entity) {
    return PostComputesBetadivResponse.respond200WithApplicationJson(submitJob(new BetaDivPluginProvider(), entity, autostart));
  }

  @Override
  public PostComputesBetadivByFileResponse postComputesBetadivByFile(String file, BetaDivPluginRequest entity) {
    return resultFile(new BetaDivPluginProvider(), file, entity, PostComputesBetadivByFileResponse::respond200WithTextPlain);
  }

  @Override
  public PostComputesAlphadivResponse postComputesAlphadiv(Boolean autostart, AlphaDivPluginRequest entity) {
    return PostComputesAlphadivResponse.respond200WithApplicationJson(submitJob(new AlphaDivPluginProvider(), entity, autostart));
  }

  @Override
  public PostComputesAlphadivByFileResponse postComputesAlphadivByFile(String file, AlphaDivPluginRequest entity) {
    return resultFile(new AlphaDivPluginProvider(), file, entity, PostComputesAlphadivByFileResponse::respond200WithTextPlain);
  }

  @Override
  public PostComputesRankedabundanceResponse postComputesRankedabundance(Boolean autostart, RankedAbundancePluginRequest entity) {
    return PostComputesRankedabundanceResponse.respond200WithApplicationJson(submitJob(new RankedAbundancePluginProvider(), entity, autostart));
  }

  @Override
  public PostComputesRankedabundanceByFileResponse postComputesRankedabundanceByFile(String file, RankedAbundancePluginRequest entity) {
    return resultFile(new RankedAbundancePluginProvider(), file, entity, PostComputesRankedabundanceByFileResponse::respond200WithTextPlain);
  }

  @Override
  public PostComputesDifferentialabundanceResponse postComputesDifferentialabundance(Boolean autostart, DifferentialAbundancePluginRequest entity) {
    return PostComputesDifferentialabundanceResponse.respond200WithApplicationJson(submitJob(new DifferentialAbundancePluginProvider(), entity, autostart));
  }

  @Override
  public PostComputesDifferentialabundanceStatisticsResponse postComputesDifferentialabundanceStatistics(DifferentialAbundancePluginRequest entity) {
    return PostComputesDifferentialabundanceStatisticsResponse.respond200WithApplicationJson(new DifferentialAbundanceStatsResponseStream(out -> {
      try {
        getResultFileStreamer(new DifferentialAbundancePluginProvider(), STATISTICS, entity).write(out);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }));
  }

  @Override
  public PostComputesDifferentialexpressionResponse postComputesDifferentialexpression(Boolean autostart, DifferentialExpressionPluginRequest entity) {
    return PostComputesDifferentialexpressionResponse.respond200WithApplicationJson(submitJob(new DifferentialExpressionPluginProvider(), entity, autostart));
  }

  @Override
  public PostComputesDifferentialexpressionStatisticsResponse postComputesDifferentialexpressionStatistics(DifferentialExpressionPluginRequest entity) {
    return PostComputesDifferentialexpressionStatisticsResponse.respond200WithApplicationJson(new DifferentialExpressionStatsResponseStream(out -> {
      try {
        getResultFileStreamer(new DifferentialExpressionPluginProvider(), STATISTICS, entity).write(out);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }));
  }

  @Override
  public PostComputesCorrelationResponse postComputesCorrelation(Boolean autostart, CorrelationPluginRequest entity) {
    return PostComputesCorrelationResponse.respond200WithApplicationJson(submitJob(new CorrelationPluginProvider(), entity, autostart));
  }

  @Override
  public PostComputesCorrelationStatisticsResponse postComputesCorrelationStatistics(CorrelationPluginRequest entity) {
    return PostComputesCorrelationStatisticsResponse.respond200WithApplicationJson(new CorrelationStatsResponseStream(out -> {
      try {
        getResultFileStreamer(new CorrelationPluginProvider(), STATISTICS, entity).write(out);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }));
  }

  @Override
  public PostComputesSelfcorrelationResponse postComputesSelfcorrelation(Boolean autostart, SelfCorrelationPluginRequest entity) {
    return PostComputesSelfcorrelationResponse.respond200WithApplicationJson(submitJob(new SelfCorrelationPluginProvider(), entity, autostart));
  }

  @Override
  public PostComputesSelfcorrelationStatisticsResponse postComputesSelfcorrelationStatistics(SelfCorrelationPluginRequest entity) {
    return PostComputesSelfcorrelationStatisticsResponse.respond200WithApplicationJson(new CorrelationStatsResponseStream(out -> {
      try {
        getResultFileStreamer(new SelfCorrelationPluginProvider(), STATISTICS, entity).write(out);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }));
  }

  // endregion Plugin Endpoints

  // region Constant Endpoints
  // ╔════════════════════════════════════════════════════════════════════╗ //
  // ║  Constant Endpoints                                                ║ //
  // ║                                                                    ║ //
  // ║  Endpoints that must exist regardless of what plugins are added    ║ //
  // ║  or removed.                                                       ║ //
  // ╚════════════════════════════════════════════════════════════════════╝ //

  @Override
  public GetComputesResponse getComputes() {
    return GetComputesResponse.respond200WithApplicationJson(PluginRegistry.getPluginOverview());
  }

  // endregion Constant Endpoints

  // ╔════════════════════════════════════════════════════════════════════╗ //
  // ║  Helper Methods                                                    ║ //
  // ╚════════════════════════════════════════════════════════════════════╝ //

  /**
   * Submits a new plugin execution job to one of the internal job queues.
   *
   * @param plugin {@code PluginProvider} that will be used to validate and
   * submit the new job request.
   *
   * @param requestObject The raw request payload.
   *
   * @param autostart Whether to start a job if none already exists
   *
   * @return Basic information about the submitted job to be returned to the
   * caller.
   *
   * @param <R> Type of the raw request body that the target plugin accepts.
   *
   * @param <C> Type of the configuration wrapped by the raw request body that
   * the target plugin accepts.
   */
  private <R extends ComputeRequestBase, C> JobResponse submitJob(PluginProvider<R, C> plugin, R requestObject, boolean autostart) {
    var user = UserProvider.lookupUser(request).orElseThrow();

    requirePermissions(requestObject, user.getUserId());

    // Validate the request body
    Supplier<ReferenceMetadata> referenceMetadata = () -> {
      var studyId = requestObject.getStudyId();
      var meta = new ReferenceMetadata(EdaSubsettingClient.getStudy(studyId));
      var derivedVars = Optional.ofNullable(requestObject.getDerivedVariables()).orElse(Collections.emptyList());
      for (var derivedVar : errToBadRequest(() -> ServiceExternal.processDvMetadataRequest(studyId, derivedVars))) {
        meta.incorporateDerivedVariable(derivedVar);
      }
      return meta;
    };

    // make sure config property was submitted with non-null value
    try {
      Object config = requestObject.getClass().getMethod("getConfig").invoke(requestObject);
      if (config == null)
        throw new BadRequestException("The request object does not contain a 'config' property value.");
    }
    catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Request object type does not contain a callable 'getConfig()' method");
    }

    plugin.getValidator()
      .validate(requestObject, referenceMetadata);

    return EDACompute.getOrSubmitComputeJob(plugin, requestObject, autostart);
  }

  // generic wrapper around the streaming output producer below
  public <R extends ResponseDelegate, P extends ComputeRequestBase> R resultFile(
    PluginMeta<P> plugin,
    String file,
    ComputeRequestBase entity,
    Function<Object, R> responseFn
  ) {
    return responseFn.apply(getResultFileStreamer(plugin, file, entity));
  }

  /**
   * Returns a stream over the contents of the target {@code file} from the job
   * described by the given configuration {@code entity}.
   *
   * @param plugin Metadata about the plugin to which the target job belongs.
   *
   * @param file Name of the filetype target. One of "meta", "tabular", or "statistics".
   *
   * @param entity Job configuration request body.
   *
   * @return The controller response type generated by the given {@code responseFn} function.
   *
   * @param <P> Type of the job configuration request body.
   */
  private <P extends ComputeRequestBase> StreamingOutput getResultFileStreamer(
    PluginMeta<P> plugin,
    String file,
    ComputeRequestBase entity
  ) {
    // If there was no plugin with the given name, throw a 404
    if (plugin == null)
      throw new NotFoundException();

    requirePermissions(entity, UserProvider.lookupUser(request).orElseThrow().getUserId());

    var jobFiles = EDACompute.getComputeJobFiles(plugin, entity);

    var fileName = switch(file) {
      case METADATA   -> ReservedFiles.OutputMeta;
      case TABULAR    -> ReservedFiles.OutputTabular;
      case STATISTICS -> ReservedFiles.OutputStats;
      default         -> throw new NotFoundException();
    };

    var fileRef = jobFiles.stream()
      .filter(f -> f.getName().equals(fileName))
      .findFirst()
      .orElseThrow(NotFoundException::new);

    return output -> {
      try (var input = fileRef.open()) {
        input.transferTo(output);
      }
      catch (IOException e) {
        throw new RuntimeException("Unable to stream result file", e);
      }
    };
  }

  /**
   * Ensures that the requester has the required permission(s) to call a plugin
   * endpoint for a target study.
   * <p>
   * As of the time of this writing, the permission required for plugin
   * execution and lookup is "allowVisualizations".
   *
   * @param entity Raw request body containing the ID of the study the user must
   * have permissions on.
   *
   * @param userId ID of the user whose permissions should be tested.
   *
   * @throws ForbiddenException If the requester does not have the required
   * permission(s) on the target study.
   */
  private void requirePermissions(@NotNull ComputeRequestBase entity, long userId) {
    DatasetAccessClient.getStudyAccessByStudyId(entity.getStudyId(), userId)
      .orElseThrow(ForbiddenException::new);
  }
}
