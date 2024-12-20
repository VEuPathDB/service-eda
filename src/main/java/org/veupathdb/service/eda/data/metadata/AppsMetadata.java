package org.veupathdb.service.eda.data.metadata;

import java.util.Arrays;
import java.util.List;

import org.gusdb.fgputil.ListBuilder;
import org.veupathdb.service.eda.common.plugin.constraint.ConstraintSpec;
import org.veupathdb.service.eda.data.core.AbstractPlugin;
import org.veupathdb.service.eda.data.plugin.correlation.CorrelationBipartitenetworkPlugin;
import org.veupathdb.service.eda.data.plugin.differentialabundance.DifferentialAbundanceVolcanoplotPlugin;
import org.veupathdb.service.eda.data.plugin.differentialexpression.DifferentialExpressionVolcanoplotPlugin;
import org.veupathdb.service.eda.data.plugin.betadiv.BetaDivScatterplotPlugin;
import org.veupathdb.service.eda.data.plugin.alphadiv.AlphaDivBoxplotPlugin;
import org.veupathdb.service.eda.data.plugin.alphadiv.AlphaDivScatterplotPlugin;
import org.veupathdb.service.eda.data.plugin.abundance.AbundanceBoxplotPlugin;
import org.veupathdb.service.eda.data.plugin.abundance.AbundanceScatterplotPlugin;
import org.veupathdb.service.eda.data.plugin.pass.BarplotPlugin;
import org.veupathdb.service.eda.data.plugin.pass.BoxplotPlugin;
import org.veupathdb.service.eda.data.plugin.pass.ContTablePlugin;
import org.veupathdb.service.eda.data.plugin.pass.DensityplotPlugin;
import org.veupathdb.service.eda.data.plugin.pass.HeatmapPlugin;
import org.veupathdb.service.eda.data.plugin.pass.HistogramPlugin;
import org.veupathdb.service.eda.data.plugin.pass.LineplotPlugin;
import org.veupathdb.service.eda.data.plugin.pass.MapPlugin;
import org.veupathdb.service.eda.data.plugin.pass.MapMarkersOverlayPlugin;
import org.veupathdb.service.eda.data.plugin.pass.ScatterplotPlugin;
import org.veupathdb.service.eda.data.plugin.pass.TwoByTwoPlugin;
import org.veupathdb.service.eda.data.plugin.selfcorrelation.SelfCorrelationUnipartitenetworkPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.BubbleMapMarkersLegendPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.BubbleMapMarkersPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.CollectionFloatingBarplotPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.CollectionFloatingContTablePlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.CollectionFloatingHistogramPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.CollectionFloatingLineplotPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.FloatingBarplotPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.FloatingBoxplotPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.FloatingContTablePlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.FloatingHistogramPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.FloatingDateHistogramPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.FloatingLineplotPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.FloatingTimeSeriesPlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.StandaloneMapMarkersPlugin;
import org.veupathdb.service.eda.data.plugin.sample.*;
import org.veupathdb.service.eda.generated.model.*;

public class AppsMetadata {

  public static final String CLINEPI_PROJECT = "ClinEpiDB";
  public static final String ALLCLINEPI_PROJECT = "AllClinEpiDB";
  public static final String MICROBIOME_PROJECT = "MicrobiomeDB";
  public static final String VECTORBASE_PROJECT = "VectorBase";
  public static final List<String> NON_VB_GENOMICS_PROJECTS = List.of(
    "AmoebaDB", "CryptoDB", "FungiDB", "GiardiaDB", "HostDB",
    "MicrosporidiaDB", "PiroplasmaDB", "PlasmoDB", "ToxoDB",
    "TrichDB", "TriTrypDB", "EuPathDB");
  public static final List<String> MBIO_PLUS_GENOMICS_PROJECTS = new ListBuilder<String>()
    .add(MICROBIOME_PROJECT)
    .add(VECTORBASE_PROJECT)
    .addAll(NON_VB_GENOMICS_PROJECTS)
    .toList();

  // NOTE: these names must match the url segments defined in the api.raml
  // Pass vizs are now different based on mbio vs clinepi so we need to adjust the below array?
  public static final AppsGetResponse APPS = apps(
    app("standalone-map", "Standalone Map", null,
      "A collection of visualizations designed to support the unbiased exploration of relationships between spatiotemporal variables in a cartographic map.",
      List.of(VECTORBASE_PROJECT),
      viz("map-markers", new StandaloneMapMarkersPlugin()),
      viz("map-markers-bubbles", new BubbleMapMarkersPlugin()),
      viz("map-markers-bubbles-legend", new BubbleMapMarkersLegendPlugin())),
    app("standalone-map-xyrelationships", "X-Y Relationships", null,
      "Build plots to explore the relationship between two variables.",
      List.of(VECTORBASE_PROJECT),
      viz("lineplot", new FloatingLineplotPlugin()),
      viz("timeseries", new FloatingTimeSeriesPlugin())),
    app("standalone-map-distributions", "Distributions", null,
      "Plot simple distributions for any continuous variable, including metadata (e.g. age, height).",
      List.of(VECTORBASE_PROJECT),
      viz("histogram", new FloatingHistogramPlugin()),
      viz("timeline", new FloatingDateHistogramPlugin()),
      viz("boxplot", new FloatingBoxplotPlugin())),
    app("standalone-map-countsandproportions", "Counts and Proportions", null,
      "Use standard bar plots and contingency tables to examine and compare frequencies in the data.",
      List.of(VECTORBASE_PROJECT),
      viz("barplot", new FloatingBarplotPlugin()),
      viz("conttable", new FloatingContTablePlugin())),
    app("standalone-map-categorical-collections", "Plots for categorical grouped variables", null,
      "Use line plots, bar plots and contingency tables to explore data from categorical Grouped Variables.",
      List.of(VECTORBASE_PROJECT),
      viz("lineplot", new CollectionFloatingLineplotPlugin()),
      viz("barplot", new CollectionFloatingBarplotPlugin()),
      viz("conttable", new CollectionFloatingContTablePlugin())),
    app("standalone-map-continuous-collections", "Plots for continuous grouped variables", null,
      "Use line plots, box plots and histograms to explore data from continuous Grouped Variables.",
      List.of(VECTORBASE_PROJECT),
      viz("lineplot", new CollectionFloatingLineplotPlugin()),
      viz("barplot", new FloatingBarplotPlugin()),
      viz("histogram", new CollectionFloatingHistogramPlugin())),
    app("pass", "Pass-Through", null,
      "A collection of visualizations designed to support the unbiased exploration of relationships between variables",
      Arrays.asList(CLINEPI_PROJECT, ALLCLINEPI_PROJECT, VECTORBASE_PROJECT),
      viz("histogram", new HistogramPlugin()),
      viz("barplot", new BarplotPlugin()),
      viz("scatterplot", new ScatterplotPlugin()),
      viz("boxplot", new BoxplotPlugin()),
      viz("twobytwo", new TwoByTwoPlugin()),
      viz("conttable", new ContTablePlugin()),
      viz("lineplot", new LineplotPlugin()),
      viz("densityplot", new DensityplotPlugin()),
      viz("heatmap", new HeatmapPlugin()),
      viz("map-markers", new MapPlugin()),
      viz("map-markers-overlay", new MapMarkersOverlayPlugin())),
    app("alphadiv", "Alpha Diversity", "alphadiv",
      "Visualize within-sample (alpha) microbial diversity based on field-standard metrics, such as the Shannon Diversity Index, Simpson's Diversity Index, and Pielou's Evenness.",
      List.of(MICROBIOME_PROJECT),
      viz("boxplot", new AlphaDivBoxplotPlugin()),
      viz("scatterplot", new AlphaDivScatterplotPlugin())),
    app("abundance", "Ranked Abundance", "rankedabundance",
      "Plot the top n taxa, pathways, or genes from any study, ranked by either the median, maximum, variance, or third quartile of the relative abundance values.",
      List.of(MICROBIOME_PROJECT),
      viz("boxplot", new AbundanceBoxplotPlugin()),
      viz("scatterplot", new AbundanceScatterplotPlugin())),
    app("betadiv", "Beta Diversity", "betadiv",
      "Visualize between-sample (beta) comparisons in microbial diversity, using Bray-Curtis dissimilarity, Jensen-Shannon Divergence, or the Jaccard Index",
      List.of(MICROBIOME_PROJECT),
      viz("scatterplot", new BetaDivScatterplotPlugin())),
    app("differentialabundance", "Differential Abundance", "differentialabundance",
      "Find taxa or genes that are differentially abundant between two groups.",
      List.of(MICROBIOME_PROJECT),
      viz("volcanoplot", new DifferentialAbundanceVolcanoplotPlugin())),
    app("differentialexpression", "Differential Expression", "differentialexpression",
      "Find genes that are differentially expressed between two groups.",
      MBIO_PLUS_GENOMICS_PROJECTS,
      viz("volcanoplot", new DifferentialAbundanceVolcanoplotPlugin())),
    app("correlationassaymetadata", "Correlation (Taxa, Functional Data v. Metadata)", "correlation",
      "Discover relationships between metadata variables and taxonomic abundance.",
      List.of(MICROBIOME_PROJECT),
      viz("bipartitenetwork", new CorrelationBipartitenetworkPlugin())),
    app("correlationassayassay", "Correlation (Taxa v. Functional Data)", "correlation",
      "Uncover connections between taxonomic abundance and functional data such as genes or pathways.",
      List.of(MICROBIOME_PROJECT),
      viz("bipartitenetwork", new CorrelationBipartitenetworkPlugin())),
    app("correlation", "WGCNA Correlation (Eigengene v. Eigengene, Metadata)", "correlation",
      "Discover relationships between eigenengenes and metadata or other eigengenes.",
      NON_VB_GENOMICS_PROJECTS,
      viz("bipartitenetwork", new CorrelationBipartitenetworkPlugin())),
    app("selfcorrelation", "Correlation (Taxa v. Taxa)", "selfcorrelation",
      "Discover relationships between taxonomic data.",
      List.of(MICROBIOME_PROJECT),
      viz("unipartitenetwork", new SelfCorrelationUnipartitenetworkPlugin())),
    // app("selfcorrelation", "Correlation (Eigengene v. Eigengene)", "selfcorrelation",
    //   "Discover relationships between eigengenes.",
    //   NON_VB_GENOMICS_PROJECTS,
    //   viz("unipartitenetwork", new SelfCorrelationUnipartitenetworkPlugin())),
    app("distributions", "Distributions", null,
      "Plot simple distributions for any continuous variable, including metadata (e.g. age, height, etc.) or microbial assay results.",
      MBIO_PLUS_GENOMICS_PROJECTS,
      viz("histogram", new HistogramPlugin()),
      viz("boxplot", new BoxplotPlugin())),
    app("countsandproportions", "Counts and Proportions", null,
      "Use standard bar plots and 'row by column' (RxC) or 2x2 contingency tables to examine and compare frequencies in the data.",
      MBIO_PLUS_GENOMICS_PROJECTS,
      viz("barplot", new BarplotPlugin()),
      viz("twobytwo", new TwoByTwoPlugin()),
      viz("conttable", new ContTablePlugin())),
    app("xyrelationships", "X-Y Relationships", null,
      "Interested in creating your own X-Y visualizations of any study variables?  Look no further!  Click on one of the plot types on the right and get ready to be creative.",
      MBIO_PLUS_GENOMICS_PROJECTS,
      viz("scatterplot", new ScatterplotPlugin()),
      viz("lineplot", new LineplotPlugin())),
    app("maps", "Maps", null,
      "Map visualizations for any geographic data",
      List.of(MICROBIOME_PROJECT),
      // i think we could switch to StandaloneMapMarkersPlugin if we wanted to, need mockups frontend though
      // also if we did, id like to consider splitting bubbles into their own viz
      viz("map-markers", new MapPlugin()),
      viz("map-markers-overlay", new MapMarkersOverlayPlugin())),
    app("sample", "Sample", null,
      "Wrapper app for sample/test plugins",
      List.of(),
      viz("record-count", new RecordCountPlugin()),
      viz("multi-stream", new MultiStreamPlugin()),
      viz("collections-test", new TestCollectionPlugin()),
      viz("categorical-distribution", new CategoricalDistributionPlugin())),
    app("samplewithcompute", "Sample With Compute", "example",
      "Wrapper app for sample/test plugins that have associated computes",
      List.of(),
      viz("viz-with-compute", new ExampleComputeVizPlugin()))
  );

  //******************************************
  //***  Helper functions
  //******************************************

  private static AppsGetResponse apps(AppOverview... apps) {
    AppsGetResponse responseObj = new AppsGetResponseImpl();
    responseObj.setApps(Arrays.asList(apps));
    return responseObj;
  }

  private static AppOverview app(String name, String displayName, String computeName, String description, List<String> projects, VisualizationOverview... visualizations) {
    AppOverviewImpl app = new AppOverviewImpl();
    app.setName(name);
    app.setDisplayName(displayName);
    app.setDescription(description);
    app.setProjects(projects);
    app.setComputeName(computeName);
    app.setVisualizations(Arrays.asList(visualizations));
    return app;
  }

  private static <T extends DataPluginRequestBase, S, R> VisualizationOverview viz(String urlSegment, AbstractPlugin<T, S, R> visualizationPlugin) {
    ConstraintSpec constraints = visualizationPlugin.getConstraintSpec();
    VisualizationOverviewImpl viz = new VisualizationOverviewImpl();
    viz.setName(urlSegment);
    viz.setDisplayName(visualizationPlugin.getDisplayName());
    viz.setDescription(visualizationPlugin.getDescription());
    viz.setProjects(visualizationPlugin.getProjects());
    viz.setMaxPanels(visualizationPlugin.getMaxPanels());
    viz.setDataElementConstraints(constraints.isEmpty() ? null : constraints);
    viz.setDataElementDependencyOrder(constraints.getDependencyOrder());
    return viz;
  }
}
