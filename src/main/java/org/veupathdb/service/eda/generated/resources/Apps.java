package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.AbundanceBoxplotPostRequest;
import org.veupathdb.service.eda.generated.model.AbundanceScatterplotPostRequest;
import org.veupathdb.service.eda.generated.model.AlphaDivBoxplotPostRequest;
import org.veupathdb.service.eda.generated.model.AlphaDivScatterplotPostRequest;
import org.veupathdb.service.eda.generated.model.AppsGetResponse;
import org.veupathdb.service.eda.generated.model.BarplotPostRequest;
import org.veupathdb.service.eda.generated.model.BarplotPostResponse;
import org.veupathdb.service.eda.generated.model.BetaDivScatterplotPostRequest;
import org.veupathdb.service.eda.generated.model.BipartiteNetworkPostResponse;
import org.veupathdb.service.eda.generated.model.BoxplotPostRequest;
import org.veupathdb.service.eda.generated.model.BoxplotPostResponse;
import org.veupathdb.service.eda.generated.model.CategoricalDistributionPostRequest;
import org.veupathdb.service.eda.generated.model.CategoricalDistributionPostResponse;
import org.veupathdb.service.eda.generated.model.CollectionFloatingBarplotPostRequest;
import org.veupathdb.service.eda.generated.model.CollectionFloatingBoxplotPostRequest;
import org.veupathdb.service.eda.generated.model.CollectionFloatingContTablePostRequest;
import org.veupathdb.service.eda.generated.model.CollectionFloatingHistogramPostRequest;
import org.veupathdb.service.eda.generated.model.CollectionFloatingLineplotPostRequest;
import org.veupathdb.service.eda.generated.model.ContTablePostResponse;
import org.veupathdb.service.eda.generated.model.CorrelationAssayAssayBipartitenetworkPostRequest;
import org.veupathdb.service.eda.generated.model.CorrelationAssayMetadataBipartitenetworkPostRequest;
import org.veupathdb.service.eda.generated.model.CorrelationBipartiteNetworkPostResponse;
import org.veupathdb.service.eda.generated.model.CorrelationBipartitenetworkPostRequest;
import org.veupathdb.service.eda.generated.model.DensityplotPostRequest;
import org.veupathdb.service.eda.generated.model.DensityplotPostResponse;
import org.veupathdb.service.eda.generated.model.DifferentialAbundanceStatsResponse;
import org.veupathdb.service.eda.generated.model.DifferentialAbundanceVolcanoplotPostRequest;
import org.veupathdb.service.eda.generated.model.DifferentialExpressionStatsResponse;
import org.veupathdb.service.eda.generated.model.DifferentialExpressionVolcanoplotPostRequest;
import org.veupathdb.service.eda.generated.model.EntityTabularPostResponse;
import org.veupathdb.service.eda.generated.model.ExampleComputeVizPostRequest;
import org.veupathdb.service.eda.generated.model.ExampleComputeVizPostResponse;
import org.veupathdb.service.eda.generated.model.FloatingBarplotPostRequest;
import org.veupathdb.service.eda.generated.model.FloatingBarplotPostResponse;
import org.veupathdb.service.eda.generated.model.FloatingBoxplotPostRequest;
import org.veupathdb.service.eda.generated.model.FloatingBoxplotPostResponse;
import org.veupathdb.service.eda.generated.model.FloatingContTablePostRequest;
import org.veupathdb.service.eda.generated.model.FloatingContTablePostResponse;
import org.veupathdb.service.eda.generated.model.FloatingHistogramPostRequest;
import org.veupathdb.service.eda.generated.model.FloatingHistogramPostResponse;
import org.veupathdb.service.eda.generated.model.FloatingLineplotPostRequest;
import org.veupathdb.service.eda.generated.model.FloatingLineplotPostResponse;
import org.veupathdb.service.eda.generated.model.FloatingScatterplotPostRequest;
import org.veupathdb.service.eda.generated.model.FloatingScatterplotPostResponse;
import org.veupathdb.service.eda.generated.model.HeatmapPostRequest;
import org.veupathdb.service.eda.generated.model.HeatmapPostResponse;
import org.veupathdb.service.eda.generated.model.HistogramPostRequest;
import org.veupathdb.service.eda.generated.model.HistogramPostResponse;
import org.veupathdb.service.eda.generated.model.LineplotPostRequest;
import org.veupathdb.service.eda.generated.model.LineplotPostResponse;
import org.veupathdb.service.eda.generated.model.MapMarkersOverlayPostRequest;
import org.veupathdb.service.eda.generated.model.MapMarkersOverlayPostResponse;
import org.veupathdb.service.eda.generated.model.MapPostRequest;
import org.veupathdb.service.eda.generated.model.MapPostResponse;
import org.veupathdb.service.eda.generated.model.MosaicPostRequest;
import org.veupathdb.service.eda.generated.model.MultiStreamPostRequest;
import org.veupathdb.service.eda.generated.model.RecordCountPostRequest;
import org.veupathdb.service.eda.generated.model.RecordCountPostResponse;
import org.veupathdb.service.eda.generated.model.ScatterplotPostRequest;
import org.veupathdb.service.eda.generated.model.ScatterplotPostResponse;
import org.veupathdb.service.eda.generated.model.SelfCorrelationUnipartitenetworkPostRequest;
import org.veupathdb.service.eda.generated.model.StandaloneCollectionMapMarkerPostRequest;
import org.veupathdb.service.eda.generated.model.StandaloneCollectionMapMarkerPostResponse;
import org.veupathdb.service.eda.generated.model.StandaloneMapBubblesLegendPostRequest;
import org.veupathdb.service.eda.generated.model.StandaloneMapBubblesLegendPostResponse;
import org.veupathdb.service.eda.generated.model.StandaloneMapBubblesPostRequest;
import org.veupathdb.service.eda.generated.model.StandaloneMapBubblesPostResponse;
import org.veupathdb.service.eda.generated.model.StandaloneMapMarkersPostRequest;
import org.veupathdb.service.eda.generated.model.StandaloneMapMarkersPostResponse;
import org.veupathdb.service.eda.generated.model.TablePostRequest;
import org.veupathdb.service.eda.generated.model.TablePostResponse;
import org.veupathdb.service.eda.generated.model.TestCollectionsPostRequest;
import org.veupathdb.service.eda.generated.model.TwoByTwoPostRequest;
import org.veupathdb.service.eda.generated.model.TwoByTwoPostResponse;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/apps")
public interface Apps {
  @GET
  @Produces("application/json")
  GetAppsResponse getApps();

  @POST
  @Path("/standalone-map/visualizations/map-markers")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapVisualizationsMapMarkersResponse postAppsStandaloneMapVisualizationsMapMarkers(
      StandaloneMapMarkersPostRequest entity);

  @POST
  @Path("/standalone-map/visualizations/map-markers/bubbles")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapVisualizationsMapMarkersBubblesResponse postAppsStandaloneMapVisualizationsMapMarkersBubbles(
      StandaloneMapBubblesPostRequest entity);

  @POST
  @Path("/standalone-map/visualizations/map-markers/bubbles/legend")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapVisualizationsMapMarkersBubblesLegendResponse postAppsStandaloneMapVisualizationsMapMarkersBubblesLegend(
      StandaloneMapBubblesLegendPostRequest entity);

  @POST
  @Path("/standalone-map/visualizations/map-markers/collections")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapVisualizationsMapMarkersCollectionsResponse postAppsStandaloneMapVisualizationsMapMarkersCollections(
      StandaloneCollectionMapMarkerPostRequest entity);

  @POST
  @Path("/standalone-map-xyrelationships/visualizations/scatterplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapXyrelationshipsVisualizationsScatterplotResponse postAppsStandaloneMapXyrelationshipsVisualizationsScatterplot(
      FloatingScatterplotPostRequest entity);

  @POST
  @Path("/standalone-map-xyrelationships/visualizations/lineplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapXyrelationshipsVisualizationsLineplotResponse postAppsStandaloneMapXyrelationshipsVisualizationsLineplot(
      FloatingLineplotPostRequest entity);

  @POST
  @Path("/standalone-map-xyrelationships/visualizations/timeseries")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapXyrelationshipsVisualizationsTimeseriesResponse postAppsStandaloneMapXyrelationshipsVisualizationsTimeseries(
      FloatingLineplotPostRequest entity);

  @POST
  @Path("/standalone-map-distributions/visualizations/histogram")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapDistributionsVisualizationsHistogramResponse postAppsStandaloneMapDistributionsVisualizationsHistogram(
      FloatingHistogramPostRequest entity);

  @POST
  @Path("/standalone-map-distributions/visualizations/timeline")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapDistributionsVisualizationsTimelineResponse postAppsStandaloneMapDistributionsVisualizationsTimeline(
      FloatingHistogramPostRequest entity);

  @POST
  @Path("/standalone-map-distributions/visualizations/boxplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapDistributionsVisualizationsBoxplotResponse postAppsStandaloneMapDistributionsVisualizationsBoxplot(
      FloatingBoxplotPostRequest entity);

  @POST
  @Path("/standalone-map-countsandproportions/visualizations/barplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapCountsandproportionsVisualizationsBarplotResponse postAppsStandaloneMapCountsandproportionsVisualizationsBarplot(
      FloatingBarplotPostRequest entity);

  @POST
  @Path("/standalone-map-countsandproportions/visualizations/conttable")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapCountsandproportionsVisualizationsConttableResponse postAppsStandaloneMapCountsandproportionsVisualizationsConttable(
      FloatingContTablePostRequest entity);

  @POST
  @Path("/standalone-map-continuous-collections/visualizations/lineplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapContinuousCollectionsVisualizationsLineplotResponse postAppsStandaloneMapContinuousCollectionsVisualizationsLineplot(
      CollectionFloatingLineplotPostRequest entity);

  @POST
  @Path("/standalone-map-continuous-collections/visualizations/timeseries")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapContinuousCollectionsVisualizationsTimeseriesResponse postAppsStandaloneMapContinuousCollectionsVisualizationsTimeseries(
      CollectionFloatingLineplotPostRequest entity);

  @POST
  @Path("/standalone-map-continuous-collections/visualizations/boxplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapContinuousCollectionsVisualizationsBoxplotResponse postAppsStandaloneMapContinuousCollectionsVisualizationsBoxplot(
      CollectionFloatingBoxplotPostRequest entity);

  @POST
  @Path("/standalone-map-continuous-collections/visualizations/histogram")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapContinuousCollectionsVisualizationsHistogramResponse postAppsStandaloneMapContinuousCollectionsVisualizationsHistogram(
      CollectionFloatingHistogramPostRequest entity);

  @POST
  @Path("/standalone-map-categorical-collections/visualizations/lineplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapCategoricalCollectionsVisualizationsLineplotResponse postAppsStandaloneMapCategoricalCollectionsVisualizationsLineplot(
      CollectionFloatingLineplotPostRequest entity);

  @POST
  @Path("/standalone-map-categorical-collections/visualizations/barplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapCategoricalCollectionsVisualizationsBarplotResponse postAppsStandaloneMapCategoricalCollectionsVisualizationsBarplot(
      CollectionFloatingBarplotPostRequest entity);

  @POST
  @Path("/standalone-map-categorical-collections/visualizations/conttable")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsStandaloneMapCategoricalCollectionsVisualizationsConttableResponse postAppsStandaloneMapCategoricalCollectionsVisualizationsConttable(
      CollectionFloatingContTablePostRequest entity);

  @POST
  @Path("/pass/visualizations/map-markers")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsMapMarkersResponse postAppsPassVisualizationsMapMarkers(
      MapPostRequest entity);

  @POST
  @Path("/pass/visualizations/table")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsTableResponse postAppsPassVisualizationsTable(TablePostRequest entity);

  @POST
  @Path("/pass/visualizations/scatterplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsScatterplotResponse postAppsPassVisualizationsScatterplot(
      ScatterplotPostRequest entity);

  @POST
  @Path("/pass/visualizations/densityplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsDensityplotResponse postAppsPassVisualizationsDensityplot(
      DensityplotPostRequest entity);

  @POST
  @Path("/pass/visualizations/timeseries")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsTimeseriesResponse postAppsPassVisualizationsTimeseries(
      LineplotPostRequest entity);

  @POST
  @Path("/pass/visualizations/lineplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsLineplotResponse postAppsPassVisualizationsLineplot(
      LineplotPostRequest entity);

  @POST
  @Path("/pass/visualizations/histogram")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsHistogramResponse postAppsPassVisualizationsHistogram(
      HistogramPostRequest entity);

  @POST
  @Path("/pass/visualizations/boxplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsBoxplotResponse postAppsPassVisualizationsBoxplot(
      BoxplotPostRequest entity);

  @POST
  @Path("/pass/visualizations/heatmap")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsHeatmapResponse postAppsPassVisualizationsHeatmap(
      HeatmapPostRequest entity);

  @POST
  @Path("/pass/visualizations/barplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsBarplotResponse postAppsPassVisualizationsBarplot(
      BarplotPostRequest entity);

  @POST
  @Path("/pass/visualizations/map-markers-overlay")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsMapMarkersOverlayResponse postAppsPassVisualizationsMapMarkersOverlay(
      MapMarkersOverlayPostRequest entity);

  @POST
  @Path("/pass/visualizations/twobytwo")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsTwobytwoResponse postAppsPassVisualizationsTwobytwo(
      TwoByTwoPostRequest entity);

  @POST
  @Path("/pass/visualizations/conttable")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsPassVisualizationsConttableResponse postAppsPassVisualizationsConttable(
      MosaicPostRequest entity);

  @POST
  @Path("/alphadiv/visualizations/boxplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsAlphadivVisualizationsBoxplotResponse postAppsAlphadivVisualizationsBoxplot(
      AlphaDivBoxplotPostRequest entity);

  @POST
  @Path("/alphadiv/visualizations/scatterplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsAlphadivVisualizationsScatterplotResponse postAppsAlphadivVisualizationsScatterplot(
      AlphaDivScatterplotPostRequest entity);

  @POST
  @Path("/abundance/visualizations/boxplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsAbundanceVisualizationsBoxplotResponse postAppsAbundanceVisualizationsBoxplot(
      AbundanceBoxplotPostRequest entity);

  @POST
  @Path("/abundance/visualizations/scatterplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsAbundanceVisualizationsScatterplotResponse postAppsAbundanceVisualizationsScatterplot(
      AbundanceScatterplotPostRequest entity);

  @POST
  @Path("/betadiv/visualizations/scatterplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsBetadivVisualizationsScatterplotResponse postAppsBetadivVisualizationsScatterplot(
      BetaDivScatterplotPostRequest entity);

  @POST
  @Path("/differentialabundance/visualizations/volcanoplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsDifferentialabundanceVisualizationsVolcanoplotResponse postAppsDifferentialabundanceVisualizationsVolcanoplot(
      DifferentialAbundanceVolcanoplotPostRequest entity);

  @POST
  @Path("/differentialexpression/visualizations/volcanoplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsDifferentialexpressionVisualizationsVolcanoplotResponse postAppsDifferentialexpressionVisualizationsVolcanoplot(
      DifferentialExpressionVolcanoplotPostRequest entity);

  @POST
  @Path("/correlation/visualizations/bipartitenetwork")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsCorrelationVisualizationsBipartitenetworkResponse postAppsCorrelationVisualizationsBipartitenetwork(
      CorrelationBipartitenetworkPostRequest entity);

  @POST
  @Path("/correlationassayassay/visualizations/bipartitenetwork")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsCorrelationassayassayVisualizationsBipartitenetworkResponse postAppsCorrelationassayassayVisualizationsBipartitenetwork(
      CorrelationAssayAssayBipartitenetworkPostRequest entity);

  @POST
  @Path("/selfcorrelation/visualizations/unipartitenetwork")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsSelfcorrelationVisualizationsUnipartitenetworkResponse postAppsSelfcorrelationVisualizationsUnipartitenetwork(
      SelfCorrelationUnipartitenetworkPostRequest entity);

  @POST
  @Path("/correlationassaymetadata/visualizations/bipartitenetwork")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsCorrelationassaymetadataVisualizationsBipartitenetworkResponse postAppsCorrelationassaymetadataVisualizationsBipartitenetwork(
      CorrelationAssayMetadataBipartitenetworkPostRequest entity);

  @POST
  @Path("/distributions/visualizations/histogram")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsDistributionsVisualizationsHistogramResponse postAppsDistributionsVisualizationsHistogram(
      HistogramPostRequest entity);

  @POST
  @Path("/distributions/visualizations/boxplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsDistributionsVisualizationsBoxplotResponse postAppsDistributionsVisualizationsBoxplot(
      BoxplotPostRequest entity);

  @POST
  @Path("/countsandproportions/visualizations/barplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsCountsandproportionsVisualizationsBarplotResponse postAppsCountsandproportionsVisualizationsBarplot(
      BarplotPostRequest entity);

  @POST
  @Path("/countsandproportions/visualizations/twobytwo")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsCountsandproportionsVisualizationsTwobytwoResponse postAppsCountsandproportionsVisualizationsTwobytwo(
      TwoByTwoPostRequest entity);

  @POST
  @Path("/countsandproportions/visualizations/conttable")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsCountsandproportionsVisualizationsConttableResponse postAppsCountsandproportionsVisualizationsConttable(
      MosaicPostRequest entity);

  @POST
  @Path("/xyrelationships/visualizations/scatterplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsXyrelationshipsVisualizationsScatterplotResponse postAppsXyrelationshipsVisualizationsScatterplot(
      ScatterplotPostRequest entity);

  @POST
  @Path("/xyrelationships/visualizations/lineplot")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsXyrelationshipsVisualizationsLineplotResponse postAppsXyrelationshipsVisualizationsLineplot(
      LineplotPostRequest entity);

  @POST
  @Path("/sample/visualizations/record-count")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsSampleVisualizationsRecordCountResponse postAppsSampleVisualizationsRecordCount(
      RecordCountPostRequest entity);

  @POST
  @Path("/sample/visualizations/multi-stream")
  @Produces("text/plain")
  @Consumes("application/json")
  PostAppsSampleVisualizationsMultiStreamResponse postAppsSampleVisualizationsMultiStream(
      MultiStreamPostRequest entity);

  @POST
  @Path("/sample/visualizations/collections-test")
  @Produces("text/plain")
  @Consumes("application/json")
  PostAppsSampleVisualizationsCollectionsTestResponse postAppsSampleVisualizationsCollectionsTest(
      TestCollectionsPostRequest entity);

  @POST
  @Path("/sample/visualizations/categorical-distribution")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsSampleVisualizationsCategoricalDistributionResponse postAppsSampleVisualizationsCategoricalDistribution(
      CategoricalDistributionPostRequest entity);

  @POST
  @Path("/samplewithcompute/visualizations/viz-with-compute")
  @Produces("application/json")
  @Consumes("application/json")
  PostAppsSamplewithcomputeVisualizationsVizWithComputeResponse postAppsSamplewithcomputeVisualizationsVizWithCompute(
      ExampleComputeVizPostRequest entity);

  class GetAppsResponse extends ResponseDelegate {
    private GetAppsResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetAppsResponse(Response response) {
      super(response);
    }

    public static GetAppsResponse respond200WithApplicationJson(AppsGetResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetAppsResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapVisualizationsMapMarkersResponse extends ResponseDelegate {
    private PostAppsStandaloneMapVisualizationsMapMarkersResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapVisualizationsMapMarkersResponse(Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapVisualizationsMapMarkersResponse respond200WithApplicationJson(
        StandaloneMapMarkersPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapVisualizationsMapMarkersResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapVisualizationsMapMarkersBubblesResponse extends ResponseDelegate {
    private PostAppsStandaloneMapVisualizationsMapMarkersBubblesResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapVisualizationsMapMarkersBubblesResponse(Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapVisualizationsMapMarkersBubblesResponse respond200WithApplicationJson(
        StandaloneMapBubblesPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapVisualizationsMapMarkersBubblesResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapVisualizationsMapMarkersBubblesLegendResponse extends ResponseDelegate {
    private PostAppsStandaloneMapVisualizationsMapMarkersBubblesLegendResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapVisualizationsMapMarkersBubblesLegendResponse(Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapVisualizationsMapMarkersBubblesLegendResponse respond200WithApplicationJson(
        StandaloneMapBubblesLegendPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapVisualizationsMapMarkersBubblesLegendResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapVisualizationsMapMarkersCollectionsResponse extends ResponseDelegate {
    private PostAppsStandaloneMapVisualizationsMapMarkersCollectionsResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapVisualizationsMapMarkersCollectionsResponse(Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapVisualizationsMapMarkersCollectionsResponse respond200WithApplicationJson(
        StandaloneCollectionMapMarkerPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapVisualizationsMapMarkersCollectionsResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapXyrelationshipsVisualizationsScatterplotResponse extends ResponseDelegate {
    private PostAppsStandaloneMapXyrelationshipsVisualizationsScatterplotResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapXyrelationshipsVisualizationsScatterplotResponse(
        Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapXyrelationshipsVisualizationsScatterplotResponse respond200WithApplicationJson(
        FloatingScatterplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapXyrelationshipsVisualizationsScatterplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapXyrelationshipsVisualizationsLineplotResponse extends ResponseDelegate {
    private PostAppsStandaloneMapXyrelationshipsVisualizationsLineplotResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapXyrelationshipsVisualizationsLineplotResponse(Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapXyrelationshipsVisualizationsLineplotResponse respond200WithApplicationJson(
        FloatingLineplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapXyrelationshipsVisualizationsLineplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapXyrelationshipsVisualizationsTimeseriesResponse extends ResponseDelegate {
    private PostAppsStandaloneMapXyrelationshipsVisualizationsTimeseriesResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapXyrelationshipsVisualizationsTimeseriesResponse(
        Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapXyrelationshipsVisualizationsTimeseriesResponse respond200WithApplicationJson(
        FloatingLineplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapXyrelationshipsVisualizationsTimeseriesResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapDistributionsVisualizationsHistogramResponse extends ResponseDelegate {
    private PostAppsStandaloneMapDistributionsVisualizationsHistogramResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapDistributionsVisualizationsHistogramResponse(Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapDistributionsVisualizationsHistogramResponse respond200WithApplicationJson(
        FloatingHistogramPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapDistributionsVisualizationsHistogramResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapDistributionsVisualizationsTimelineResponse extends ResponseDelegate {
    private PostAppsStandaloneMapDistributionsVisualizationsTimelineResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapDistributionsVisualizationsTimelineResponse(Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapDistributionsVisualizationsTimelineResponse respond200WithApplicationJson(
        FloatingHistogramPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapDistributionsVisualizationsTimelineResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapDistributionsVisualizationsBoxplotResponse extends ResponseDelegate {
    private PostAppsStandaloneMapDistributionsVisualizationsBoxplotResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapDistributionsVisualizationsBoxplotResponse(Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapDistributionsVisualizationsBoxplotResponse respond200WithApplicationJson(
        FloatingBoxplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapDistributionsVisualizationsBoxplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapCountsandproportionsVisualizationsBarplotResponse extends ResponseDelegate {
    private PostAppsStandaloneMapCountsandproportionsVisualizationsBarplotResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapCountsandproportionsVisualizationsBarplotResponse(
        Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapCountsandproportionsVisualizationsBarplotResponse respond200WithApplicationJson(
        FloatingBarplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapCountsandproportionsVisualizationsBarplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapCountsandproportionsVisualizationsConttableResponse extends ResponseDelegate {
    private PostAppsStandaloneMapCountsandproportionsVisualizationsConttableResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapCountsandproportionsVisualizationsConttableResponse(
        Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapCountsandproportionsVisualizationsConttableResponse respond200WithApplicationJson(
        FloatingContTablePostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapCountsandproportionsVisualizationsConttableResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapContinuousCollectionsVisualizationsLineplotResponse extends ResponseDelegate {
    private PostAppsStandaloneMapContinuousCollectionsVisualizationsLineplotResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapContinuousCollectionsVisualizationsLineplotResponse(
        Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapContinuousCollectionsVisualizationsLineplotResponse respond200WithApplicationJson(
        FloatingLineplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapContinuousCollectionsVisualizationsLineplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapContinuousCollectionsVisualizationsTimeseriesResponse extends ResponseDelegate {
    private PostAppsStandaloneMapContinuousCollectionsVisualizationsTimeseriesResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapContinuousCollectionsVisualizationsTimeseriesResponse(
        Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapContinuousCollectionsVisualizationsTimeseriesResponse respond200WithApplicationJson(
        FloatingLineplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapContinuousCollectionsVisualizationsTimeseriesResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapContinuousCollectionsVisualizationsBoxplotResponse extends ResponseDelegate {
    private PostAppsStandaloneMapContinuousCollectionsVisualizationsBoxplotResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapContinuousCollectionsVisualizationsBoxplotResponse(
        Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapContinuousCollectionsVisualizationsBoxplotResponse respond200WithApplicationJson(
        FloatingBoxplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapContinuousCollectionsVisualizationsBoxplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapContinuousCollectionsVisualizationsHistogramResponse extends ResponseDelegate {
    private PostAppsStandaloneMapContinuousCollectionsVisualizationsHistogramResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapContinuousCollectionsVisualizationsHistogramResponse(
        Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapContinuousCollectionsVisualizationsHistogramResponse respond200WithApplicationJson(
        FloatingHistogramPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapContinuousCollectionsVisualizationsHistogramResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapCategoricalCollectionsVisualizationsLineplotResponse extends ResponseDelegate {
    private PostAppsStandaloneMapCategoricalCollectionsVisualizationsLineplotResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapCategoricalCollectionsVisualizationsLineplotResponse(
        Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapCategoricalCollectionsVisualizationsLineplotResponse respond200WithApplicationJson(
        FloatingLineplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapCategoricalCollectionsVisualizationsLineplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapCategoricalCollectionsVisualizationsBarplotResponse extends ResponseDelegate {
    private PostAppsStandaloneMapCategoricalCollectionsVisualizationsBarplotResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapCategoricalCollectionsVisualizationsBarplotResponse(
        Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapCategoricalCollectionsVisualizationsBarplotResponse respond200WithApplicationJson(
        FloatingBarplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapCategoricalCollectionsVisualizationsBarplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsStandaloneMapCategoricalCollectionsVisualizationsConttableResponse extends ResponseDelegate {
    private PostAppsStandaloneMapCategoricalCollectionsVisualizationsConttableResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsStandaloneMapCategoricalCollectionsVisualizationsConttableResponse(
        Response response) {
      super(response);
    }

    public static PostAppsStandaloneMapCategoricalCollectionsVisualizationsConttableResponse respond200WithApplicationJson(
        FloatingContTablePostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsStandaloneMapCategoricalCollectionsVisualizationsConttableResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsMapMarkersResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsMapMarkersResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsMapMarkersResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsMapMarkersResponse respond200WithApplicationJson(
        MapPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsMapMarkersResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsTableResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsTableResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsTableResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsTableResponse respond200WithApplicationJson(
        TablePostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsTableResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsScatterplotResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsScatterplotResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsScatterplotResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsScatterplotResponse respond200WithApplicationJson(
        ScatterplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsScatterplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsDensityplotResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsDensityplotResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsDensityplotResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsDensityplotResponse respond200WithApplicationJson(
        DensityplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsDensityplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsTimeseriesResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsTimeseriesResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsTimeseriesResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsTimeseriesResponse respond200WithApplicationJson(
        LineplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsTimeseriesResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsLineplotResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsLineplotResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsLineplotResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsLineplotResponse respond200WithApplicationJson(
        LineplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsLineplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsHistogramResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsHistogramResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsHistogramResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsHistogramResponse respond200WithApplicationJson(
        HistogramPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsHistogramResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsBoxplotResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsBoxplotResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsBoxplotResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsBoxplotResponse respond200WithApplicationJson(
        BoxplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsBoxplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsHeatmapResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsHeatmapResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsHeatmapResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsHeatmapResponse respond200WithApplicationJson(
        HeatmapPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsHeatmapResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsBarplotResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsBarplotResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsBarplotResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsBarplotResponse respond200WithApplicationJson(
        BarplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsBarplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsMapMarkersOverlayResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsMapMarkersOverlayResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsMapMarkersOverlayResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsMapMarkersOverlayResponse respond200WithApplicationJson(
        MapMarkersOverlayPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsMapMarkersOverlayResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsTwobytwoResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsTwobytwoResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsTwobytwoResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsTwobytwoResponse respond200WithApplicationJson(
        TwoByTwoPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsTwobytwoResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsPassVisualizationsConttableResponse extends ResponseDelegate {
    private PostAppsPassVisualizationsConttableResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsPassVisualizationsConttableResponse(Response response) {
      super(response);
    }

    public static PostAppsPassVisualizationsConttableResponse respond200WithApplicationJson(
        ContTablePostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsPassVisualizationsConttableResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsAlphadivVisualizationsBoxplotResponse extends ResponseDelegate {
    private PostAppsAlphadivVisualizationsBoxplotResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsAlphadivVisualizationsBoxplotResponse(Response response) {
      super(response);
    }

    public static PostAppsAlphadivVisualizationsBoxplotResponse respond200WithApplicationJson(
        BoxplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsAlphadivVisualizationsBoxplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsAlphadivVisualizationsScatterplotResponse extends ResponseDelegate {
    private PostAppsAlphadivVisualizationsScatterplotResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsAlphadivVisualizationsScatterplotResponse(Response response) {
      super(response);
    }

    public static PostAppsAlphadivVisualizationsScatterplotResponse respond200WithApplicationJson(
        ScatterplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsAlphadivVisualizationsScatterplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsAbundanceVisualizationsBoxplotResponse extends ResponseDelegate {
    private PostAppsAbundanceVisualizationsBoxplotResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsAbundanceVisualizationsBoxplotResponse(Response response) {
      super(response);
    }

    public static PostAppsAbundanceVisualizationsBoxplotResponse respond200WithApplicationJson(
        BoxplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsAbundanceVisualizationsBoxplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsAbundanceVisualizationsScatterplotResponse extends ResponseDelegate {
    private PostAppsAbundanceVisualizationsScatterplotResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsAbundanceVisualizationsScatterplotResponse(Response response) {
      super(response);
    }

    public static PostAppsAbundanceVisualizationsScatterplotResponse respond200WithApplicationJson(
        ScatterplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsAbundanceVisualizationsScatterplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsBetadivVisualizationsScatterplotResponse extends ResponseDelegate {
    private PostAppsBetadivVisualizationsScatterplotResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsBetadivVisualizationsScatterplotResponse(Response response) {
      super(response);
    }

    public static PostAppsBetadivVisualizationsScatterplotResponse respond200WithApplicationJson(
        ScatterplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsBetadivVisualizationsScatterplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsDifferentialabundanceVisualizationsVolcanoplotResponse extends ResponseDelegate {
    private PostAppsDifferentialabundanceVisualizationsVolcanoplotResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsDifferentialabundanceVisualizationsVolcanoplotResponse(Response response) {
      super(response);
    }

    public static PostAppsDifferentialabundanceVisualizationsVolcanoplotResponse respond200WithApplicationJson(
        DifferentialAbundanceStatsResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsDifferentialabundanceVisualizationsVolcanoplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsDifferentialexpressionVisualizationsVolcanoplotResponse extends ResponseDelegate {
    private PostAppsDifferentialexpressionVisualizationsVolcanoplotResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsDifferentialexpressionVisualizationsVolcanoplotResponse(Response response) {
      super(response);
    }

    public static PostAppsDifferentialexpressionVisualizationsVolcanoplotResponse respond200WithApplicationJson(
        DifferentialExpressionStatsResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsDifferentialexpressionVisualizationsVolcanoplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsCorrelationVisualizationsBipartitenetworkResponse extends ResponseDelegate {
    private PostAppsCorrelationVisualizationsBipartitenetworkResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsCorrelationVisualizationsBipartitenetworkResponse(Response response) {
      super(response);
    }

    public static PostAppsCorrelationVisualizationsBipartitenetworkResponse respond200WithApplicationJson(
        CorrelationBipartiteNetworkPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsCorrelationVisualizationsBipartitenetworkResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsCorrelationassayassayVisualizationsBipartitenetworkResponse extends ResponseDelegate {
    private PostAppsCorrelationassayassayVisualizationsBipartitenetworkResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsCorrelationassayassayVisualizationsBipartitenetworkResponse(Response response) {
      super(response);
    }

    public static PostAppsCorrelationassayassayVisualizationsBipartitenetworkResponse respond200WithApplicationJson(
        CorrelationBipartiteNetworkPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsCorrelationassayassayVisualizationsBipartitenetworkResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsSelfcorrelationVisualizationsUnipartitenetworkResponse extends ResponseDelegate {
    private PostAppsSelfcorrelationVisualizationsUnipartitenetworkResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsSelfcorrelationVisualizationsUnipartitenetworkResponse(Response response) {
      super(response);
    }

    public static PostAppsSelfcorrelationVisualizationsUnipartitenetworkResponse respond200WithApplicationJson(
        Object entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsSelfcorrelationVisualizationsUnipartitenetworkResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsCorrelationassaymetadataVisualizationsBipartitenetworkResponse extends ResponseDelegate {
    private PostAppsCorrelationassaymetadataVisualizationsBipartitenetworkResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsCorrelationassaymetadataVisualizationsBipartitenetworkResponse(
        Response response) {
      super(response);
    }

    public static PostAppsCorrelationassaymetadataVisualizationsBipartitenetworkResponse respond200WithApplicationJson(
        BipartiteNetworkPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsCorrelationassaymetadataVisualizationsBipartitenetworkResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsDistributionsVisualizationsHistogramResponse extends ResponseDelegate {
    private PostAppsDistributionsVisualizationsHistogramResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsDistributionsVisualizationsHistogramResponse(Response response) {
      super(response);
    }

    public static PostAppsDistributionsVisualizationsHistogramResponse respond200WithApplicationJson(
        HistogramPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsDistributionsVisualizationsHistogramResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsDistributionsVisualizationsBoxplotResponse extends ResponseDelegate {
    private PostAppsDistributionsVisualizationsBoxplotResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsDistributionsVisualizationsBoxplotResponse(Response response) {
      super(response);
    }

    public static PostAppsDistributionsVisualizationsBoxplotResponse respond200WithApplicationJson(
        BoxplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsDistributionsVisualizationsBoxplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsCountsandproportionsVisualizationsBarplotResponse extends ResponseDelegate {
    private PostAppsCountsandproportionsVisualizationsBarplotResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsCountsandproportionsVisualizationsBarplotResponse(Response response) {
      super(response);
    }

    public static PostAppsCountsandproportionsVisualizationsBarplotResponse respond200WithApplicationJson(
        BarplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsCountsandproportionsVisualizationsBarplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsCountsandproportionsVisualizationsTwobytwoResponse extends ResponseDelegate {
    private PostAppsCountsandproportionsVisualizationsTwobytwoResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsCountsandproportionsVisualizationsTwobytwoResponse(Response response) {
      super(response);
    }

    public static PostAppsCountsandproportionsVisualizationsTwobytwoResponse respond200WithApplicationJson(
        TwoByTwoPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsCountsandproportionsVisualizationsTwobytwoResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsCountsandproportionsVisualizationsConttableResponse extends ResponseDelegate {
    private PostAppsCountsandproportionsVisualizationsConttableResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsCountsandproportionsVisualizationsConttableResponse(Response response) {
      super(response);
    }

    public static PostAppsCountsandproportionsVisualizationsConttableResponse respond200WithApplicationJson(
        ContTablePostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsCountsandproportionsVisualizationsConttableResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsXyrelationshipsVisualizationsScatterplotResponse extends ResponseDelegate {
    private PostAppsXyrelationshipsVisualizationsScatterplotResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsXyrelationshipsVisualizationsScatterplotResponse(Response response) {
      super(response);
    }

    public static PostAppsXyrelationshipsVisualizationsScatterplotResponse respond200WithApplicationJson(
        ScatterplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsXyrelationshipsVisualizationsScatterplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsXyrelationshipsVisualizationsLineplotResponse extends ResponseDelegate {
    private PostAppsXyrelationshipsVisualizationsLineplotResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsXyrelationshipsVisualizationsLineplotResponse(Response response) {
      super(response);
    }

    public static PostAppsXyrelationshipsVisualizationsLineplotResponse respond200WithApplicationJson(
        LineplotPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsXyrelationshipsVisualizationsLineplotResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsSampleVisualizationsRecordCountResponse extends ResponseDelegate {
    private PostAppsSampleVisualizationsRecordCountResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsSampleVisualizationsRecordCountResponse(Response response) {
      super(response);
    }

    public static PostAppsSampleVisualizationsRecordCountResponse respond200WithApplicationJson(
        RecordCountPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsSampleVisualizationsRecordCountResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsSampleVisualizationsMultiStreamResponse extends ResponseDelegate {
    private PostAppsSampleVisualizationsMultiStreamResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsSampleVisualizationsMultiStreamResponse(Response response) {
      super(response);
    }

    public static PostAppsSampleVisualizationsMultiStreamResponse respond200WithTextPlain(
        EntityTabularPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/plain");
      responseBuilder.entity(entity);
      return new PostAppsSampleVisualizationsMultiStreamResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsSampleVisualizationsCollectionsTestResponse extends ResponseDelegate {
    private PostAppsSampleVisualizationsCollectionsTestResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostAppsSampleVisualizationsCollectionsTestResponse(Response response) {
      super(response);
    }

    public static PostAppsSampleVisualizationsCollectionsTestResponse respond200WithTextPlain(
        EntityTabularPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/plain");
      responseBuilder.entity(entity);
      return new PostAppsSampleVisualizationsCollectionsTestResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsSampleVisualizationsCategoricalDistributionResponse extends ResponseDelegate {
    private PostAppsSampleVisualizationsCategoricalDistributionResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsSampleVisualizationsCategoricalDistributionResponse(Response response) {
      super(response);
    }

    public static PostAppsSampleVisualizationsCategoricalDistributionResponse respond200WithApplicationJson(
        CategoricalDistributionPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsSampleVisualizationsCategoricalDistributionResponse(responseBuilder.build(), entity);
    }
  }

  class PostAppsSamplewithcomputeVisualizationsVizWithComputeResponse extends ResponseDelegate {
    private PostAppsSamplewithcomputeVisualizationsVizWithComputeResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostAppsSamplewithcomputeVisualizationsVizWithComputeResponse(Response response) {
      super(response);
    }

    public static PostAppsSamplewithcomputeVisualizationsVizWithComputeResponse respond200WithApplicationJson(
        ExampleComputeVizPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostAppsSamplewithcomputeVisualizationsVizWithComputeResponse(responseBuilder.build(), entity);
    }
  }
}
