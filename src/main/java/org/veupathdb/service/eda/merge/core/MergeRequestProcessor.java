package org.veupathdb.service.eda.merge.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.AutoCloseableList;
import org.gusdb.fgputil.functional.FunctionalInterfaces.ConsumerWithException;
import org.gusdb.fgputil.functional.Functions;
import org.gusdb.fgputil.iterator.CloseableIterator;
import org.gusdb.fgputil.iterator.IteratorUtil;
import org.gusdb.fgputil.validation.ValidationException;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.StreamingDataClient;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.merge.core.request.ComputeInfo;
import org.veupathdb.service.eda.merge.core.request.MergedTabularRequestResources;
import org.veupathdb.service.eda.merge.core.stream.RootStreamingEntityNode;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.gusdb.fgputil.FormatUtil.TAB;
import static org.veupathdb.service.eda.merge.core.stream.RootStreamingEntityNode.COMPUTED_VAR_STREAM_NAME;

/**
 * Top-level tabular request processing class, responsible for (in execution
 * order):
 * <p>
 * <ol>
 *   <li>initializing and collecting metadata</li>
 *   <li>building an entity stream processing tree which will merge all incoming
 *       data streams</li>
 *   <li>collecting stream specs for required streams</li>
 *   <li>requesting streams from subsetting and compute services</li>
 *   <li>determining whether a single required stream can be directly passed out
 *       as the response (with no merge processing)</li>
 *   <li>distributing the incoming data streams to the entity stream processing
 *       tree</li>
 *   <li>returning a consumer of the output stream which writes the merged
 *       streams</li>
 * </ol>
 */
public class MergeRequestProcessor {

  private static final Logger LOG = LogManager.getLogger(MergeRequestProcessor.class);

  private final MergedTabularRequestResources _resources;

  public MergeRequestProcessor(MergedTabularRequestResources resources) {
    _resources = resources;
  }

  public Consumer<OutputStream> createMergedResponseSupplier() throws ValidationException {
    // gather request resources
    RootStreamingEntityNode targetStream = getRootStreamingEntityNode();

    // get stream specs for streams needed by the node tree, which will be merged into this request's response
    Map<String, StreamSpec> requiredStreams = Functions.getMapFromValues(targetStream.getRequiredStreamSpecs(), StreamSpec::getStreamName);

    // create stream generator
    Function<StreamSpec, CloseableIterator<Map<String, String>>> streamGenerator = spec ->
      COMPUTED_VAR_STREAM_NAME.equals(spec.getStreamName())
        // need to get compute stream from compute service
        ? _resources.getComputeStreamIterator()
        // all other streams come from subsetting service
        : _resources.getSubsettingTabularStream(spec);

    final List<StreamSpec> requiredStreamSpecs  = new ArrayList<>(requiredStreams.values());

    @SuppressWarnings("resource") // closed by StreamingDataClient.processIteratorStreams
    AutoCloseableList<CloseableIterator<Map<String, String>>> closeableDataStreams = StreamingDataClient.buildIteratorStreams(requiredStreamSpecs, streamGenerator);

    return out -> {

      // create stream processor
      ConsumerWithException<Map<String, CloseableIterator<Map<String, String>>>> streamProcessor =
        dataStreams -> writeMergedStream(targetStream, dataStreams, out);

      // build and process streams
      StreamingDataClient.processIteratorStreams(requiredStreamSpecs, closeableDataStreams, streamProcessor);
    };
  }

  @NotNull
  private RootStreamingEntityNode getRootStreamingEntityNode() throws ValidationException {
    String targetEntityId = _resources.getTargetEntityId();
    List<VariableSpec> outputVarSpecs = _resources.getOutputVariableSpecs();
    ReferenceMetadata metadata = _resources.getMetadata();
    Optional<ComputeInfo> computeInfo = _resources.getComputeInfo();

    // request validated; convert requested entity and vars to defs
    EntityDef targetEntity = metadata.getEntity(targetEntityId).orElseThrow();
    List<VariableDef> outputVarDefs = metadata.getTabularColumns(targetEntity, outputVarSpecs);

    // build entity node tree to aggregate the data into a streaming response
    return new RootStreamingEntityNode(targetEntity, outputVarDefs,
      _resources.getSubsetFilters(), metadata, _resources.getDerivedVariableFactory(), computeInfo);
  }

  private static void writeMergedStream(
    RootStreamingEntityNode targetEntityStream,
    Map<String, CloseableIterator<Map<String, String>>> dataStreams,
    OutputStream out
  ) {
    LOG.info("All requested streams ({}) ready for consumption", dataStreams.size());

    // distribute the streams to their processors and make sure they all get claimed
    Map<String, CloseableIterator<Map<String, String>>> distributionMap = new HashMap<>(dataStreams); // make a copy which will get cleared out
    targetEntityStream.acceptDataStreams(distributionMap);
    if (!distributionMap.isEmpty())
      throw new IllegalStateException("Not all requested data streams were claimed by the processor tree.  " +
        "Remaining: " + String.join(", ", distributionMap.keySet()));

    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {

      // write the header row
      String headerRow = String.join(TAB, targetEntityStream.getOrderedOutputVars());
      writer.write(headerRow);
      writer.newLine();

      // write the entity rows
      for (Map<String,String> row : IteratorUtil.toIterable(targetEntityStream)) {
        writer.write(String.join(TAB, row.values()));
        writer.newLine();
      }

      // flush any remaining chars
      writer.flush();
    }
    catch (IOException e) {
      throw new RuntimeException("Unable to write output stream", e);
    }
  }
}
