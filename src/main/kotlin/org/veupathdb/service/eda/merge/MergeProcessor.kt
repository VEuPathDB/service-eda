package org.veupathdb.service.eda.merge

import org.gusdb.fgputil.iterator.CloseableIterator
import org.veupathdb.service.eda.common.client.StreamingDataClient
import org.veupathdb.service.eda.common.client.spec.StreamSpec
import org.veupathdb.service.eda.compute.EDACompute
import org.veupathdb.service.eda.merge.core.stream.RootStreamingEntityNode
import org.veupathdb.service.eda.util.logger
import java.io.BufferedWriter
import java.io.InputStream
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.Optional

const val ComputedVarStreamName = "__COMPUTED_VAR_STREAM__"
const val Tab = "\t"

//
private typealias DataStream = CloseableIterator<Map<String, String>>

/**
 * Top-level tabular request processing class, responsible for (in execution
 * order):
 *
 * 1. initializing and collecting metadata
 * 2. building an entity stream processing tree which will merge all incoming
 *    data streams
 * 3. collecting stream specs for required streams
 * 4. requesting streams from subsetting and compute services
 * 5. determining whether a single required stream can be directly passed out as
 *    the response (with no merge processing)
 * 6. distributing the incoming data streams to the entity stream processing
 *    tree
 * 7. returning a consumer of the output stream which writes the merged streams
 */
internal class MergeProcessor(private val resources: MergeResources) {
  val log = logger()

  fun getMergeResult(): InputStream {
    val targetStream = newRootStreamingEntityNode()

    // get stream specs for streams needed by the node tree, which will be
    // merged into the result.
    //
    // Note: The list -> map -> list conversion here is undocumented in the
    // original implementation, but it may be needed to filter the list to only
    // distinct specs based on spec name?
    val requiredStreams = targetStream.requiredStreamSpecs
      .associateBy { it.streamName!! }
      .values
      .toList() as List<StreamSpec>

    // `streams` is a closeable resource that is closed later in the execution
    // of `StreamingDataClient.processIteratorStreams`.
    val streams = StreamingDataClient.buildIteratorStreams(requiredStreams) {
      when (it.streamName) {
        ComputedVarStreamName -> resources.newComputeStreamIterator()
        else -> resources.newSubsettingTabularStream(it)
      }
    }

    // Setup pipes so we can hand data off to the requester.
    //
    // TODO: this is kind of ugly, but it would require a major rethink about
    //       how some of this stuff is structured to clean it up.  It was very
    //       much oriented around HTTP requests.
    val readStream = PipedInputStream()
    val writeStream = PipedOutputStream(readStream)

    Thread {
      StreamingDataClient.processIteratorStreams(requiredStreams, streams) {
        writeMergedStream(writeStream, targetStream, it)
      }
    }.start()

    return readStream
  }

  private fun newRootStreamingEntityNode() =
    resources.metadata.getEntity(resources.targetEntityID).orElseThrow()
      .let { targetEntity -> RootStreamingEntityNode(
        targetEntity,
        resources.metadata.getTabularColumns(targetEntity, resources.outputVarSpecs),
        resources.subsetFilters,
        resources.metadata,
        resources.derivedVariableFactory,
        Optional.ofNullable(resources.computeInfo)
    ) }

  private fun MergeResources.newComputeStreamIterator(): DataStream {
    return computeInfo?.let {
      EDACompute.
      // FIXME: GET TABULAR DATA FROM COMPUTE JOB!!!
    } ?: throw IllegalStateException("Cannot get compute stream iterator if no compute is specified in request.")
  }

  private fun MergeResources.newSubsettingTabularStream(spec: StreamSpec): DataStream {
    val usableSpec = if (spec.filtersOverride.isPresent && subsetFilters.isNotEmpty()) {
      StreamSpec(spec.streamName, spec.entityId).apply {
        addAll(spec)
        setFiltersOverride(subsetFilters + spec.filtersOverride.get())
      }
    } else {
      spec
    }

    // FIXME: GET TABULAR DATA FROM SUBSETTING
  }

  private fun writeMergedStream(
    outputStream: OutputStream,
    targetEntityStream: RootStreamingEntityNode,
    dataStreams: Map<String, DataStream>,
  ) {
    log.info("All requested streams ({}) ready for consumption", dataStreams.size)

    // distribute the streams to their processors and make sure they all get
    // claimed.
    with (dataStreams.toMutableMap()) {
      // CAUTION: THIS METHOD MUTATES THE GIVEN MAP!
      targetEntityStream.acceptDataStreams(this)
      if (isNotEmpty())
        throw IllegalStateException(
          "Not all requested data streams were claimed by the processor tree.  " +
            "Remaining: " + keys.joinToString(", "))
    }

    outputStream.bufferedWriter().use { writer ->
      // Write the header row
      writer.writeRow(targetEntityStream.orderedOutputVars, Tab)

      while (targetEntityStream.hasNext())
        writer.writeRow(targetEntityStream.next().values, Tab)

      writer.flush()
    }
  }
}

/**
 * Writes a row of delimited values to the receiver instance followed by a
 * line break.
 *
 * @receiver Buffered writer the row will be written to.
 *
 * @param values Column values for the row to be written.
 *
 * @param separator Delimiter string that will be inserted between column
 * values.
 */
private fun BufferedWriter.writeRow(values: Iterable<String>, separator: String) {
  with(values.iterator()) {
    if (hasNext()) {
      write(next())

      while (hasNext()) {
        write(separator)
        write(next())
      }
    }

    newLine()
  }
}
