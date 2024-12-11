package org.veupathdb.service.eda.common.client;

import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.ProcessingException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.gusdb.fgputil.AutoCloseableList;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.functional.FunctionalInterfaces;
import org.gusdb.fgputil.iterator.CloseableIterator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.client.spec.StreamSpecValidator;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import static org.gusdb.fgputil.functional.Functions.cSwallow;

public abstract class StreamingDataClient extends ServiceClient {
  private static final Logger LOG = LogManager.getLogger(StreamingDataClient.class);

  public abstract StreamSpecValidator getStreamSpecValidator();

  public abstract String varToColumnHeader(VariableSpec var);

  protected StreamingDataClient(String serviceBaseUrl, Entry<String, String> authHeader) {
    super(serviceBaseUrl, authHeader);
  }

  public static void buildAndProcessStreams(
    List<StreamSpec> requiredStreams,
    Function<StreamSpec, ResponseFuture> streamGenerator,
    FunctionalInterfaces.ConsumerWithException<Map<String, InputStream>> streamProcessor
  ) {
    AutoCloseableList<InputStream> dataStreams = buildDataStreams(requiredStreams, streamGenerator);
    processDataStreams(requiredStreams, dataStreams, streamProcessor);
  }

  public static AutoCloseableList<CloseableIterator<Map<String, String>>> buildIteratorStreams(
    Collection<StreamSpec> requiredStreams,
    Function<StreamSpec, CloseableIterator<Map<String, String>>> streamGenerator
  ) {
    return new AutoCloseableList<>(requiredStreams.stream()
      .map(streamGenerator)
      .collect(Collectors.toList()));
  }

  public static void buildAndProcessIteratorStreams(
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
          LOG.error("failed to close StreamSpec iterator {}", it.getKey(), e);
        }
      }
    }
  }

  public static void processIteratorStreams(
    List<StreamSpec> requiredStreams,
    AutoCloseableList<CloseableIterator<Map<String, String>>> streams,
    FunctionalInterfaces.ConsumerWithException<Map<String, CloseableIterator<Map<String, String>>>> streamProcessor
  ) {
    try (streams) {
      // convert auto-closeable list into a named stream map for processing
      Map<String, CloseableIterator<Map<String, String>>> streamMap = new LinkedHashMap<>();

      for (int i = 0; i < streams.size(); i++) {
        streamMap.put(requiredStreams.get(i).getStreamName(), streams.get(i));
      }
      cSwallow(streamProcessor).accept(streamMap);
    }
  }

  public static void buildAndProcessIteratorStreams(
    ArrayList<StreamSpec> requiredStreams,
    Function<StreamSpec, CloseableIterator<Map<String, String>>> streamGenerator,
    FunctionalInterfaces.ConsumerWithException<Map<String, CloseableIterator<Map<String, String>>>> streamProcessor
  ) {
    AutoCloseableList<CloseableIterator<Map<String, String>>> streams = buildIteratorStreams(requiredStreams, streamGenerator);
    processIteratorStreams(requiredStreams, streams, streamProcessor);
  }


  public static void processDataStreams(
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

  public static AutoCloseableList<InputStream> buildDataStreams(
    List<StreamSpec> requiredStreams,
    Function<StreamSpec, ResponseFuture> streamGenerator
  ) {
    AutoCloseableList<InputStream> dataStreams = new AutoCloseableList<>();
    Map<String, ResponseFuture> responses = new HashMap<>();
    try {
      // parallelize the calls
      for (StreamSpec spec : requiredStreams) {
        responses.put(spec.getStreamName(), streamGenerator.apply(spec));
      }

      // wait for all to complete
      ResponseFuture.waitForAll(responses.values());

      // get results
      for (StreamSpec spec : requiredStreams) {
        dataStreams.add(new NonEmptyResultStream(spec.getStreamName(), responses.get(spec.getStreamName()).getInputStream()));
        responses.remove(spec.getStreamName());
      }
      return dataStreams;
    }
    // Explicitly catch EmptyResult so that it can be re-thrown and caught downstream without being wrapped in generic catch.
    catch (NonEmptyResultStream.EmptyResultException e) {
      cleanDataStreams(responses, dataStreams);
      throw e;
    }
    catch (Exception e) {
      cleanDataStreams(responses, dataStreams);
      // throw as a runtime exception
      throw new RuntimeException("Unable to fetch all required data", e);
    }
  }

  private static void cleanDataStreams(Map<String, ResponseFuture> responses, AutoCloseableList<InputStream> dataStreams) {
    // if exception occurs while creating streams; need to clean up (two parts)
    // 1. cancel remaining responses not opened
    for (ResponseFuture response : responses.values()) {
      response.cancel();
    }
    // 2. close any that successfully opened
    dataStreams.close();
  }
}
