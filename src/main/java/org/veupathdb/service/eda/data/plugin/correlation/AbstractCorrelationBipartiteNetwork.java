package org.veupathdb.service.eda.data.plugin.correlation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.gusdb.fgputil.json.JsonUtil;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.data.core.AbstractPlugin;
import org.veupathdb.service.eda.generated.model.*;

public abstract class AbstractCorrelationBipartiteNetwork<T extends DataPluginRequestBase, R extends BaseCorrelationComputeConfig> extends AbstractPlugin<T, CorrelationNetworkSpec, R> {

  @Override
  public String getDisplayName() {
    return "Bipartite network";
  }

  @Override
  public String getDescription() {
    return "Visualize the correlation between two sets of data";
  }

  @Override
  public boolean computeGeneratesVars() {
    return false;
  }

  @Override
  protected void validateVisualizationSpec(CorrelationNetworkSpec pluginSpec) {
    // nothing to do here
  }

  @Override
  protected List<StreamSpec> getRequestedStreams(CorrelationNetworkSpec pluginSpec) {
    // this plugin only uses the stats result of the compute; no tabular data streams needed
    return Collections.emptyList();
  }

  @Override
  protected void writeResults(OutputStream out, Map<String, InputStream> dataStreams) throws IOException {

    CorrelationStatsResponse stats = getComputeResultStats(CorrelationStatsResponse.class);
    Number correlationCoefThreshold = getPluginSpec().getCorrelationCoefThreshold() != null ? getPluginSpec().getCorrelationCoefThreshold() : 0.2;
    Number pValueThreshold = getPluginSpec().getSignificanceThreshold() != null ? getPluginSpec().getSignificanceThreshold() : 0.05;
    boolean findNodeDegrees = getPluginSpec().getDegree() != null ? getPluginSpec().getDegree() : true;

    // The goal is to transform the response from the correlation app (CorrelationStatsResponse)
    // into a BipartiteNetwork that can be sent to the frontend. The BipartiteNetwork is composed
    // of nodes, links, column1NodeIDs, and column2NodeIDs.

    // Prep objects
    ArrayList<LinkData> links = new ArrayList<>();
    ArrayList<String> nodeIDs = new ArrayList<>();
    ArrayList<String> column1NodeIDs = new ArrayList<>();
    ArrayList<String> column2NodeIDs = new ArrayList<>();

    // All the information we need is contained in the stats object. Each row of stats corresponds to a link.
    // We want to grab the link, the nodes associated with the link, and make note of column assignments for
    // each node.
    stats.getStatistics().forEach((correlationRow) -> {

      // Skip rows that have no correlation coefficient, a NaN correlation coef, or a correlation coef that is too small. Filtering here prevents us
      // from showing nodes with no links.
      if (correlationRow.getCorrelationCoef() == null) return;
      if (Float.isNaN(Float.parseFloat(correlationRow.getCorrelationCoef()))) return;
      if (Math.abs(Float.parseFloat(correlationRow.getCorrelationCoef())) < correlationCoefThreshold.floatValue()) return;
      if (correlationRow.getPValue() != null) {
        if (Float.parseFloat(correlationRow.getPValue()) > pValueThreshold.floatValue()) return;
      }

      // First add the node ids (data1 and data2 from this row) to our growing list of node ids
      // We'll worry about duplicates later.
      nodeIDs.add(correlationRow.getData1());
      nodeIDs.add(correlationRow.getData2());

      // Add the data1 ids to column 1, and data2 ids to column 2. Again we'll address duplicates later.
      column1NodeIDs.add(correlationRow.getData1());
      column2NodeIDs.add(correlationRow.getData2());

      // Next create links
      // Create source and target objects.
      links.add(getLinkData(correlationRow));
    });

    // Get unique IDs
    List<String> uniqueColumn1IDs = column1NodeIDs.stream().distinct().toList();
    List<String> uniqueColumn2IDs = column2NodeIDs.stream().distinct().toList();
    List<String> uniqueNodeIDs = nodeIDs.stream().distinct().toList();

    // Turn the node ids into an array of Node objects, each with an id property
    ArrayList<NodeData> nodes = new ArrayList<>();
    uniqueNodeIDs.forEach((nodeID) -> {
      NodeData node = new NodeDataImpl();
      node.setId(nodeID);
      if (findNodeDegrees) {
        // this assumes a de-duplicated edge list
        int nodeDegree = Collections.frequency(nodeIDs, nodeID);
        node.setDegree(nodeDegree);
      }
      nodes.add(node);
    });

    // Finally create the bipartite network response and send it off!
    BipartiteNetworkData bipartiteNetworkData = new BipartiteNetworkDataImpl();
    bipartiteNetworkData.setLinks(links);
    bipartiteNetworkData.setNodes(nodes);
    NodeIdList partition1NodeIdList = new NodeIdListImpl();
    partition1NodeIdList.setNodeIds(uniqueColumn1IDs);
    NodeIdList partition2NodeIdList = new NodeIdListImpl();
    partition2NodeIdList.setNodeIds(uniqueColumn2IDs);
    List<NodeIdList> partitions = new ArrayList<>();
    partitions.add(partition1NodeIdList);
    partitions.add(partition2NodeIdList);
    bipartiteNetworkData.setPartitions(partitions);

    BipartiteNetworkConfig bipartiteNetworkConfig = new BipartiteNetworkConfigImpl();
    List<String> partitionsMetadata = new ArrayList<>();
    partitionsMetadata.add(stats.getData1Metadata());
    partitionsMetadata.add(stats.getData2Metadata());
    bipartiteNetworkConfig.setPartitionsMetadata(partitionsMetadata);

    BipartiteNetwork bipartiteNetwork = new BipartiteNetworkImpl();
    bipartiteNetwork.setData(bipartiteNetworkData);
    bipartiteNetwork.setConfig(bipartiteNetworkConfig);

    CorrelationBipartiteNetworkPostResponse response = new CorrelationBipartiteNetworkPostResponseImpl();
    response.setBipartitenetwork(bipartiteNetwork);
    response.setSignificanceThreshold(pValueThreshold);
    response.setCorrelationCoefThreshold(correlationCoefThreshold);

    JsonUtil.Jackson.writeValue(out, response);
    out.flush();

  }

  @NotNull
  private static LinkData getLinkData(CorrelationPoint correlationRow) {
    NodeData sourceNode = new NodeDataImpl();
    sourceNode.setId(correlationRow.getData1());
    NodeData targetNode = new NodeDataImpl();
    targetNode.setId(correlationRow.getData2());

    // Create link with the data from this row and add to links array.
    LinkData link = new LinkDataImpl();
    link.setSource(sourceNode);
    link.setTarget(targetNode);
    link.setWeight(String.valueOf(Math.abs(Float.parseFloat(correlationRow.getCorrelationCoef()))));
    // Link color is the sign of the correlation
    String color = Float.parseFloat(correlationRow.getCorrelationCoef()) < 0 ? "-1" : "1";
    link.setColor(color);
    return link;
  }
}
