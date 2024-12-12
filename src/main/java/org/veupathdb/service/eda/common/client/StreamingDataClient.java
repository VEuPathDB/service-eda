package org.veupathdb.service.eda.common.client;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.vulpine.lib.jcfi.CheckedFunction;
import org.apache.logging.log4j.LogManager;
import org.gusdb.fgputil.AutoCloseableList;
import org.gusdb.fgputil.functional.FunctionalInterfaces;
import org.gusdb.fgputil.iterator.CloseableIterator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.client.spec.StreamSpecValidator;

import static org.gusdb.fgputil.functional.Functions.cSwallow;

public interface StreamingDataClient {
  StreamSpecValidator getStreamSpecValidator();

  static AutoCloseableList<CloseableIterator<Map<String, String>>> buildIteratorStreams(
    Collection<StreamSpec> requiredStreams,
    Function<StreamSpec, CloseableIterator<Map<String, String>>> streamGenerator
  ) {
    return new AutoCloseableList<>(requiredStreams.stream()
      .map(streamGenerator)
      .collect(Collectors.toList()));
  }

  static void buildAndProcessIteratorStreams(
    Collection<StreamSpec> requiredStreams,
    Function<StreamSpec, CloseableIterator<Map<String, String>>> streamGenerator,
    FunctionalInterfaces.ConsumerWithException<Map<String, CloseableIterator<Map<String, String>>>> streamProcessor
  ) {
    var streams = new LinkedHashMap<String, CloseableIterator<Map<String, String>>>(requiredStreams.size());

    try {
      requiredStreams.forEach(it -> streams.put(it.getStreamName(), streamGenerator.apply(it)));
      cSwallow(streamProcessor).accept(streams);
    } finally {
      for (var it : streams.entrySet()) {
        try {
          it.getValue().close();
        } catch (Exception e) {
          LogManager.getLogger(StreamingDataClient.class)
            .error("failed to close StreamSpec iterator {}", it.getKey(), e);
        }
      }
    }
  }

  // convert auto-closeable list into a named stream map for processing
  static Map<String, CloseableIterator<Map<String, String>>> iteratorStreamsToMap(
    List<StreamSpec> requiredStreams,
    AutoCloseableList<CloseableIterator<Map<String, String>>> streams
  ) {
    var streamMap = new LinkedHashMap<String, CloseableIterator<Map<String, String>>>();

    for (int i = 0; i < streams.size(); i++) {
      streamMap.put(requiredStreams.get(i).getStreamName(), streams.get(i));
    }

    return streamMap;
  }

  static void processIteratorStreams(
    List<StreamSpec> requiredStreams,
    AutoCloseableList<CloseableIterator<Map<String, String>>> streams,
    FunctionalInterfaces.ConsumerWithException<Map<String, CloseableIterator<Map<String, String>>>> streamProcessor
  ) {
    try (streams) {
      cSwallow(streamProcessor).accept(iteratorStreamsToMap(requiredStreams, streams));
    }
  }

  static void processDataStreams(
    List<StreamSpec> requiredStreams,
    AutoCloseableList<InputStream> dataStreams,
    FunctionalInterfaces.ConsumerWithException<Map<String, InputStream>> streamProcessor
  ) {
    try (dataStreams) {
      // convert auto-closeable list into a named stream map for processing
      Map<String, InputStream> streamMap = new LinkedHashMap<>();
      for (int i = 0; i < dataStreams.size(); i++) {
        streamMap.put(requiredStreams.get(i).getStreamName(), dataStreams.get(i));
      }
      cSwallow(streamProcessor).accept(streamMap);
    }
  }

  static AutoCloseableList<InputStream> buildDataStreams(
    List<StreamSpec> requiredStreams,
    CheckedFunction<StreamSpec, InputStream> streamGenerator
  ) {
    try {
      var dataStreams = new AutoCloseableList<InputStream>();
      dataStreams.ensureCapacity(requiredStreams.size());

      // parallelize the calls
      for (StreamSpec spec : requiredStreams) {
        dataStreams.add(new NonEmptyResultStream(spec.getStreamName(), streamGenerator.apply(spec)));
      }

      return dataStreams;
    // Explicitly catch EmptyResult so that it can be re-thrown and caught
    // downstream without being wrapped in generic catch.
    } catch (NonEmptyResultStream.EmptyResultException e) {
      throw e;
    } catch (Exception e) {
      // throw as a runtime exception
      throw new RuntimeException("Unable to fetch all required data", e);
    }
  }
}
