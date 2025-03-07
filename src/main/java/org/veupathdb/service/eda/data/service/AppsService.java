package org.veupathdb.service.eda.data.service;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ContainerRequest;
import org.gusdb.fgputil.functional.FunctionalInterfaces.SupplierWithException;
import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.lib.container.jaxrs.server.annotations.DisableJackson;
import org.veupathdb.service.eda.common.auth.StudyAccess;
import org.veupathdb.service.eda.common.client.NonEmptyResultStream.EmptyResultException;
import org.veupathdb.service.eda.data.core.AbstractPlugin;
import org.veupathdb.service.eda.data.metadata.AppsMetadata;
import org.veupathdb.service.eda.data.plugin.abundance.AbundanceBoxplotPlugin;
import org.veupathdb.service.eda.data.plugin.abundance.AbundanceScatterplotPlugin;
import org.veupathdb.service.eda.data.plugin.alphadiv.AlphaDivBoxplotPlugin;
import org.veupathdb.service.eda.data.plugin.alphadiv.AlphaDivScatterplotPlugin;
import org.veupathdb.service.eda.data.plugin.betadiv.BetaDivScatterplotPlugin;
import org.veupathdb.service.eda.data.plugin.correlation.CorrelationAssayAssayBipartitenetworkPlugin;
import org.veupathdb.service.eda.data.plugin.correlation.CorrelationAssayMetadataBipartitenetworkPlugin;
import org.veupathdb.service.eda.data.plugin.correlation.CorrelationBipartitenetworkPlugin;
import org.veupathdb.service.eda.data.plugin.differentialabundance.DifferentialAbundanceVolcanoplotPlugin;
import org.veupathdb.service.eda.data.plugin.differentialexpression.DifferentialExpressionVolcanoplotPlugin;
import org.veupathdb.service.eda.data.plugin.pass.*;
import org.veupathdb.service.eda.data.plugin.sample.*;
import org.veupathdb.service.eda.data.plugin.selfcorrelation.SelfCorrelationUnipartitenetworkPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.*;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.generated.resources.Apps;

import java.io.OutputStream;
import java.util.function.Consumer;

@Authenticated(allowGuests = true)
public class AppsService implements Apps {

  private static final Logger LOG = LogManager.getLogger(AppsService.class);

  @Context
  private ContainerRequest _request;

  @Override
  public GetAppsResponse getApps() {
    return GetAppsResponse.respond200WithApplicationJson(AppsMetadata.APPS);
  }

  @Override
  public PostAppsStandaloneMapVisualizationsMapMarkersResponse postAppsStandaloneMapVisualizationsMapMarkers(StandaloneMapMarkersPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapVisualizationsMapMarkersResponse.respond200WithApplicationJson(
      new StandaloneMapMarkersPostResponseStream(processRequest(new StandaloneMapMarkersPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapVisualizationsMapMarkersBubblesResponse postAppsStandaloneMapVisualizationsMapMarkersBubbles(StandaloneMapBubblesPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapVisualizationsMapMarkersBubblesResponse.respond200WithApplicationJson(
      new StandaloneMapBubblesPostResponseStream(processRequest(new BubbleMapMarkersPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapVisualizationsMapMarkersBubblesLegendResponse postAppsStandaloneMapVisualizationsMapMarkersBubblesLegend(StandaloneMapBubblesLegendPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapVisualizationsMapMarkersBubblesLegendResponse.respond200WithApplicationJson(
      new StandaloneMapBubblesLegendPostResponseStream(processRequest(new BubbleMapMarkersLegendPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapVisualizationsMapMarkersCollectionsResponse postAppsStandaloneMapVisualizationsMapMarkersCollections(StandaloneCollectionMapMarkerPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapVisualizationsMapMarkersCollectionsResponse.respond200WithApplicationJson(
      new StandaloneCollectionMapMarkerPostResponseStream(processRequest(new CollectionMapMarkersPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapContinuousCollectionsVisualizationsTimeseriesResponse postAppsStandaloneMapContinuousCollectionsVisualizationsTimeseries(CollectionFloatingLineplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapContinuousCollectionsVisualizationsTimeseriesResponse.respond200WithApplicationJson(
      new FloatingLineplotPostResponseStream(processRequest(new CollectionFloatingTimeSeriesPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapContinuousCollectionsVisualizationsLineplotResponse postAppsStandaloneMapContinuousCollectionsVisualizationsLineplot(CollectionFloatingLineplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapContinuousCollectionsVisualizationsLineplotResponse.respond200WithApplicationJson(
      new FloatingLineplotPostResponseStream(processRequest(new CollectionFloatingLineplotPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapContinuousCollectionsVisualizationsHistogramResponse postAppsStandaloneMapContinuousCollectionsVisualizationsHistogram(CollectionFloatingHistogramPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapContinuousCollectionsVisualizationsHistogramResponse.respond200WithApplicationJson(
      new FloatingHistogramPostResponseStream(processRequest(new CollectionFloatingHistogramPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapContinuousCollectionsVisualizationsBoxplotResponse postAppsStandaloneMapContinuousCollectionsVisualizationsBoxplot(CollectionFloatingBoxplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapContinuousCollectionsVisualizationsBoxplotResponse.respond200WithApplicationJson(
      new FloatingBoxplotPostResponseStream(processRequest(new CollectionFloatingBoxplotPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapCategoricalCollectionsVisualizationsBarplotResponse postAppsStandaloneMapCategoricalCollectionsVisualizationsBarplot(CollectionFloatingBarplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapCategoricalCollectionsVisualizationsBarplotResponse.respond200WithApplicationJson(
      new FloatingBarplotPostResponseStream(processRequest(new CollectionFloatingBarplotPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapCategoricalCollectionsVisualizationsLineplotResponse postAppsStandaloneMapCategoricalCollectionsVisualizationsLineplot(CollectionFloatingLineplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapCategoricalCollectionsVisualizationsLineplotResponse.respond200WithApplicationJson(
      new FloatingLineplotPostResponseStream(processRequest(new CollectionFloatingLineplotPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapCategoricalCollectionsVisualizationsConttableResponse postAppsStandaloneMapCategoricalCollectionsVisualizationsConttable(CollectionFloatingContTablePostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapCategoricalCollectionsVisualizationsConttableResponse.respond200WithApplicationJson(
      new FloatingContTablePostResponseStream(processRequest(new CollectionFloatingContTablePlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapXyrelationshipsVisualizationsTimeseriesResponse postAppsStandaloneMapXyrelationshipsVisualizationsTimeseries(FloatingLineplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapXyrelationshipsVisualizationsTimeseriesResponse.respond200WithApplicationJson(
      new FloatingLineplotPostResponseStream(processRequest(new FloatingTimeSeriesPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapXyrelationshipsVisualizationsScatterplotResponse postAppsStandaloneMapXyrelationshipsVisualizationsScatterplot(FloatingScatterplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapXyrelationshipsVisualizationsScatterplotResponse.respond200WithApplicationJson(
      new FloatingScatterplotPostResponseStream(processRequest(new FloatingScatterplotPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapXyrelationshipsVisualizationsLineplotResponse postAppsStandaloneMapXyrelationshipsVisualizationsLineplot(FloatingLineplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapXyrelationshipsVisualizationsLineplotResponse.respond200WithApplicationJson(
      new FloatingLineplotPostResponseStream(processRequest(new FloatingLineplotPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapDistributionsVisualizationsHistogramResponse postAppsStandaloneMapDistributionsVisualizationsHistogram(FloatingHistogramPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapDistributionsVisualizationsHistogramResponse.respond200WithApplicationJson(
      new FloatingHistogramPostResponseStream(processRequest(new FloatingHistogramPlugin(), entity))));
  }

  public PostAppsStandaloneMapDistributionsVisualizationsTimelineResponse postAppsStandaloneMapDistributionsVisualizationsTimeline(FloatingHistogramPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapDistributionsVisualizationsTimelineResponse.respond200WithApplicationJson(
      new FloatingHistogramPostResponseStream(processRequest(new FloatingDateHistogramPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapDistributionsVisualizationsBoxplotResponse postAppsStandaloneMapDistributionsVisualizationsBoxplot(FloatingBoxplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapDistributionsVisualizationsBoxplotResponse.respond200WithApplicationJson(
      new FloatingBoxplotPostResponseStream(processRequest(new FloatingBoxplotPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapCountsandproportionsVisualizationsBarplotResponse postAppsStandaloneMapCountsandproportionsVisualizationsBarplot(FloatingBarplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapCountsandproportionsVisualizationsBarplotResponse.respond200WithApplicationJson(
      new FloatingBarplotPostResponseStream(processRequest(new FloatingBarplotPlugin(), entity))));
  }

  @Override
  public PostAppsStandaloneMapCountsandproportionsVisualizationsConttableResponse postAppsStandaloneMapCountsandproportionsVisualizationsConttable(FloatingContTablePostRequest entity) {
    return wrapPlugin(() -> PostAppsStandaloneMapCountsandproportionsVisualizationsConttableResponse.respond200WithApplicationJson(
      new FloatingContTablePostResponseStream(processRequest(new FloatingContTablePlugin(), entity))));
  }

  static <T> T wrapPlugin(SupplierWithException<T> supplier) {
    try {
      return supplier.get();
    }
    catch (ValidationException e) {
      throw new BadRequestException(e.getMessage());
    }
    catch (EmptyResultException e) {
      LOG.info("Found empty body.", e);
      throw new WebApplicationException(e.getMessage(), 204);
    }
    catch (WebApplicationException e) {
      throw e;
    }
    catch (Exception e) {
      LOG.error("Could not execute app.", e);
      throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  private static <T extends DataPluginRequestBase> Consumer<OutputStream> processRequest(AbstractPlugin<T,?,?> plugin, T entity, ContainerRequest request) throws ValidationException {
    String appName = request.getUriInfo().getPathSegments().get(1).getPath();
    return processRequest(plugin, entity, appName, request);
  }

  static <T extends DataPluginRequestBase> Consumer<OutputStream> processRequest(AbstractPlugin<T,?,?> plugin, T entity, String appName, ContainerRequest request) throws ValidationException {
    var user = UserProvider.lookupUser(request).orElseThrow();
    StudyAccess.confirmPermission(user.getUserId(), entity.getStudyId(), StudyAccess::allowVisualizations);
    return plugin.processRequest(appName, entity);
  }

  private <T extends DataPluginRequestBase> Consumer<OutputStream> processRequest(AbstractPlugin<T,?,?> plugin, T entity) throws ValidationException {
    return processRequest(plugin, entity, _request);
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsMapMarkersResponse postAppsPassVisualizationsMapMarkers(MapPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsMapMarkersResponse.respond200WithApplicationJson(
      new MapPostResponseStream(processRequest(new MapPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsTableResponse postAppsPassVisualizationsTable(TablePostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsTableResponse.respond200WithApplicationJson(
      new TablePostResponseStream(processRequest(new TablePlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsScatterplotResponse postAppsPassVisualizationsScatterplot(ScatterplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsScatterplotResponse.respond200WithApplicationJson(
      new ScatterplotPostResponseStream(processRequest(new ScatterplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsDensityplotResponse postAppsPassVisualizationsDensityplot(DensityplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsDensityplotResponse.respond200WithApplicationJson(
      new DensityplotPostResponseStream(processRequest(new DensityplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsLineplotResponse postAppsPassVisualizationsLineplot(LineplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsLineplotResponse.respond200WithApplicationJson(
      new LineplotPostResponseStream(processRequest(new LineplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsTimeseriesResponse postAppsPassVisualizationsTimeseries(LineplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsTimeseriesResponse.respond200WithApplicationJson(
      new LineplotPostResponseStream(processRequest(new TimeSeriesPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsHistogramResponse postAppsPassVisualizationsHistogram(HistogramPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsHistogramResponse.respond200WithApplicationJson(
      new HistogramPostResponseStream(processRequest(new HistogramPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsBarplotResponse postAppsPassVisualizationsBarplot(BarplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsBarplotResponse.respond200WithApplicationJson(
      new BarplotPostResponseStream(processRequest(new BarplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsMapMarkersOverlayResponse postAppsPassVisualizationsMapMarkersOverlay(MapMarkersOverlayPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsMapMarkersOverlayResponse.respond200WithApplicationJson(
      new MapMarkersOverlayPostResponseStream(processRequest(new MapMarkersOverlayPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsBoxplotResponse postAppsPassVisualizationsBoxplot(BoxplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsBoxplotResponse.respond200WithApplicationJson(
      new BoxplotPostResponseStream(processRequest(new BoxplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsHeatmapResponse postAppsPassVisualizationsHeatmap(HeatmapPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsHeatmapResponse.respond200WithApplicationJson(
      new HeatmapPostResponseStream(processRequest(new HeatmapPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsTwobytwoResponse postAppsPassVisualizationsTwobytwo(TwoByTwoPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsTwobytwoResponse.respond200WithApplicationJson(
      new TwoByTwoPostResponseStream(processRequest(new TwoByTwoPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsPassVisualizationsConttableResponse postAppsPassVisualizationsConttable(MosaicPostRequest entity) {
    return wrapPlugin(() -> PostAppsPassVisualizationsConttableResponse.respond200WithApplicationJson(
      new ContTablePostResponseStream(processRequest(new ContTablePlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsSampleVisualizationsRecordCountResponse postAppsSampleVisualizationsRecordCount(RecordCountPostRequest entity) {
    return wrapPlugin(() -> PostAppsSampleVisualizationsRecordCountResponse.respond200WithApplicationJson(
      new RecordCountPostResponseStream(processRequest(new RecordCountPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsSampleVisualizationsMultiStreamResponse postAppsSampleVisualizationsMultiStream(MultiStreamPostRequest entity) {
    return wrapPlugin(() -> PostAppsSampleVisualizationsMultiStreamResponse.respond200WithTextPlain(
      new EntityTabularPostResponseStream(processRequest(new MultiStreamPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsSampleVisualizationsCollectionsTestResponse postAppsSampleVisualizationsCollectionsTest(TestCollectionsPostRequest entity) {
    return wrapPlugin(() -> PostAppsSampleVisualizationsCollectionsTestResponse.respond200WithTextPlain(
      new EntityTabularPostResponseStream(processRequest(new TestCollectionPlugin(), entity))));
  }

  @Override
  public PostAppsSampleVisualizationsCategoricalDistributionResponse postAppsSampleVisualizationsCategoricalDistribution(CategoricalDistributionPostRequest entity) {
    return wrapPlugin(() -> PostAppsSampleVisualizationsCategoricalDistributionResponse.respond200WithApplicationJson(
      new CategoricalDistributionPostResponseStream(processRequest(new CategoricalDistributionPlugin(), entity))));
  }

  @Override
  public PostAppsSamplewithcomputeVisualizationsVizWithComputeResponse postAppsSamplewithcomputeVisualizationsVizWithCompute(ExampleComputeVizPostRequest entity) {
    return wrapPlugin(() -> PostAppsSamplewithcomputeVisualizationsVizWithComputeResponse.respond200WithApplicationJson(
      new ExampleComputeVizPostResponseStream(processRequest(new ExampleComputeVizPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsAlphadivVisualizationsBoxplotResponse postAppsAlphadivVisualizationsBoxplot(AlphaDivBoxplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsAlphadivVisualizationsBoxplotResponse.respond200WithApplicationJson(
      new BoxplotPostResponseStream(processRequest(new AlphaDivBoxplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsAlphadivVisualizationsScatterplotResponse postAppsAlphadivVisualizationsScatterplot(AlphaDivScatterplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsAlphadivVisualizationsScatterplotResponse.respond200WithApplicationJson(
      new ScatterplotPostResponseStream(processRequest(new AlphaDivScatterplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsBetadivVisualizationsScatterplotResponse postAppsBetadivVisualizationsScatterplot(BetaDivScatterplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsBetadivVisualizationsScatterplotResponse.respond200WithApplicationJson(
      new ScatterplotPostResponseStream(processRequest(new BetaDivScatterplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsDifferentialabundanceVisualizationsVolcanoplotResponse postAppsDifferentialabundanceVisualizationsVolcanoplot(DifferentialAbundanceVolcanoplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsDifferentialabundanceVisualizationsVolcanoplotResponse.respond200WithApplicationJson(
      new DifferentialAbundanceStatsResponseStream(processRequest(new DifferentialAbundanceVolcanoplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsDifferentialexpressionVisualizationsVolcanoplotResponse postAppsDifferentialexpressionVisualizationsVolcanoplot(DifferentialExpressionVolcanoplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsDifferentialexpressionVisualizationsVolcanoplotResponse.respond200WithApplicationJson(
      new DifferentialExpressionStatsResponseStream(processRequest(new DifferentialExpressionVolcanoplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsCorrelationVisualizationsBipartitenetworkResponse postAppsCorrelationVisualizationsBipartitenetwork(CorrelationBipartitenetworkPostRequest entity) {
    return wrapPlugin(() -> PostAppsCorrelationVisualizationsBipartitenetworkResponse.respond200WithApplicationJson(
      new CorrelationBipartiteNetworkPostResponseStream(processRequest(new CorrelationBipartitenetworkPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsCorrelationassayassayVisualizationsBipartitenetworkResponse postAppsCorrelationassayassayVisualizationsBipartitenetwork(CorrelationAssayAssayBipartitenetworkPostRequest entity) {
    return wrapPlugin(() -> PostAppsCorrelationassayassayVisualizationsBipartitenetworkResponse.respond200WithApplicationJson(
      new CorrelationBipartiteNetworkPostResponseStream(processRequest(new CorrelationAssayAssayBipartitenetworkPlugin(), entity))));
  }
  @DisableJackson
  @Override
  public PostAppsSelfcorrelationVisualizationsUnipartitenetworkResponse postAppsSelfcorrelationVisualizationsUnipartitenetwork(SelfCorrelationUnipartitenetworkPostRequest entity) {
    return wrapPlugin(() -> PostAppsSelfcorrelationVisualizationsUnipartitenetworkResponse.respond200WithApplicationJson(
      new CorrelationNetworkPostResponseStream(processRequest(new SelfCorrelationUnipartitenetworkPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsCorrelationassaymetadataVisualizationsBipartitenetworkResponse postAppsCorrelationassaymetadataVisualizationsBipartitenetwork(CorrelationAssayMetadataBipartitenetworkPostRequest entity) {
    return wrapPlugin(() -> PostAppsCorrelationassaymetadataVisualizationsBipartitenetworkResponse.respond200WithApplicationJson(
      new CorrelationBipartiteNetworkPostResponseStream(processRequest(new CorrelationAssayMetadataBipartitenetworkPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsAbundanceVisualizationsBoxplotResponse postAppsAbundanceVisualizationsBoxplot(AbundanceBoxplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsAbundanceVisualizationsBoxplotResponse.respond200WithApplicationJson(
      new BoxplotPostResponseStream(processRequest(new AbundanceBoxplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsAbundanceVisualizationsScatterplotResponse postAppsAbundanceVisualizationsScatterplot(AbundanceScatterplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsAbundanceVisualizationsScatterplotResponse.respond200WithApplicationJson(
      new ScatterplotPostResponseStream(processRequest(new AbundanceScatterplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsDistributionsVisualizationsBoxplotResponse postAppsDistributionsVisualizationsBoxplot(BoxplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsDistributionsVisualizationsBoxplotResponse.respond200WithApplicationJson(
      new BoxplotPostResponseStream(processRequest(new BoxplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsDistributionsVisualizationsHistogramResponse postAppsDistributionsVisualizationsHistogram(HistogramPostRequest entity) {
    return wrapPlugin(() -> PostAppsDistributionsVisualizationsHistogramResponse.respond200WithApplicationJson(
      new HistogramPostResponseStream(processRequest(new HistogramPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsCountsandproportionsVisualizationsBarplotResponse postAppsCountsandproportionsVisualizationsBarplot(BarplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsCountsandproportionsVisualizationsBarplotResponse.respond200WithApplicationJson(
      new BarplotPostResponseStream(processRequest(new BarplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsCountsandproportionsVisualizationsTwobytwoResponse postAppsCountsandproportionsVisualizationsTwobytwo(TwoByTwoPostRequest entity) {
    return wrapPlugin(() -> PostAppsCountsandproportionsVisualizationsTwobytwoResponse.respond200WithApplicationJson(
      new TwoByTwoPostResponseStream(processRequest(new TwoByTwoPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsCountsandproportionsVisualizationsConttableResponse postAppsCountsandproportionsVisualizationsConttable(MosaicPostRequest entity) {
    return wrapPlugin(() -> PostAppsCountsandproportionsVisualizationsConttableResponse.respond200WithApplicationJson(
      new ContTablePostResponseStream(processRequest(new ContTablePlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsXyrelationshipsVisualizationsLineplotResponse postAppsXyrelationshipsVisualizationsLineplot(LineplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsXyrelationshipsVisualizationsLineplotResponse.respond200WithApplicationJson(
      new LineplotPostResponseStream(processRequest(new LineplotPlugin(), entity))));
  }

  @DisableJackson
  @Override
  public PostAppsXyrelationshipsVisualizationsScatterplotResponse postAppsXyrelationshipsVisualizationsScatterplot(ScatterplotPostRequest entity) {
    return wrapPlugin(() -> PostAppsXyrelationshipsVisualizationsScatterplotResponse.respond200WithApplicationJson(
      new ScatterplotPostResponseStream(processRequest(new ScatterplotPlugin(), entity))));
  }

}
