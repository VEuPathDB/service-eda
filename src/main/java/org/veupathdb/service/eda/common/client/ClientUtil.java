package org.veupathdb.service.eda.common.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Function;
import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.util.Header;
import org.gusdb.fgputil.AutoCloseableList;
import org.gusdb.fgputil.functional.FunctionalInterfaces.ConsumerWithException;
import org.gusdb.fgputil.json.JsonUtil;

import static org.gusdb.fgputil.functional.Functions.cSwallow;

public class ClientUtil {

  private static final Logger LOG = LogManager.getLogger(ClientUtil.class);

  public static boolean LOG_RESPONSE_HEADERS = false;

  public static <T> T getResponseObject(String url, Class<T> responseObjectClass) throws IOException {
    return new ObjectMapper().readerFor(responseObjectClass).readValue(new URL(url));
  }

  public static ResponseFuture makeAsyncGetRequest(
      String url, String expectedResponseType) {
    LOG.info("Will send following GET request to " + url);
    return makeAsyncRequest(url, expectedResponseType,
      invoker -> invoker.get());
  }

  public static ResponseFuture makeAsyncPostRequest(
      String url, Object postBodyObject, String expectedResponseType) {
    String json = JsonUtil.serializeObject(postBodyObject);
    LOG.info("Will send following POST request to " + url + "\n" + json);
    return makeAsyncRequest(url, expectedResponseType,
      invoker -> invoker.post(Entity.entity(json, MediaType.APPLICATION_JSON)));
  }

  private static ResponseFuture makeAsyncRequest(String url, String expectedResponseType,
      Function<AsyncInvoker, Future<Response>> responseProducer) {
    MultivaluedMap<String,Object> headers = new MultivaluedHashMap<>();
    headers.add(Header.Accept.toString(), "*/*");
    if (LOG_RESPONSE_HEADERS) {
      headers.add("X-Jersey-Tracing-Accept", "any");
    }
    return new ResponseFuture(
      responseProducer.apply(
        ClientBuilder.newClient()
          .target(url)
          .request(expectedResponseType)
          .headers(headers)
          .async()), LOG_RESPONSE_HEADERS);
  }

  public static void buildAndProcessStreams(
      List<StreamSpec> requiredStreams,
      Function<StreamSpec, ResponseFuture> streamGenerator,
      ConsumerWithException<Map<String, InputStream>> streamProcessor) {
    try (AutoCloseableList<InputStream> dataStreams = buildDataStreams(requiredStreams, streamGenerator)) {
      // convert auto-closeable list into a named stream map for processing
      Map<String,InputStream> streamMap = new LinkedHashMap<>();
      for (int i = 0; i < dataStreams.size(); i++) {
        streamMap.put(requiredStreams.get(i).getStreamName(), dataStreams.get(i));
      }
      cSwallow(streamProcessor).accept(streamMap);
    }
  }

  private static AutoCloseableList<InputStream> buildDataStreams(
      List<StreamSpec> requiredStreams,
      Function<StreamSpec,ResponseFuture> streamGenerator) {
    AutoCloseableList<InputStream> dataStreams = new AutoCloseableList<>();
    Map<String, ResponseFuture> responses = new HashMap<>();
    try {
      // parallelize the calls
      for (StreamSpec spec : requiredStreams) {
        responses.put(spec.getStreamName(), streamGenerator.apply(spec));
      }
      // wait for all to complete
      boolean allDone = false;
      while (!allDone) {
        allDone = true;
        for (ResponseFuture response : responses.values()) {
          if (!response.isDone()) {
            allDone = false;
            break;
          }
        }
        Thread.sleep(10); // let other threads run
      }
      // get results
      for (StreamSpec spec : requiredStreams) {
        dataStreams.add(responses.get(spec.getStreamName()).getInputStream());
        responses.remove(spec.getStreamName());
      }
      return dataStreams;
    }
    catch (Exception e) {
      // if exception occurs while creating streams; need to clean up (two parts)
      // 1. cancel remaining responses not opened
      for (ResponseFuture response : responses.values()) {
        response.cancel();
      }
      // 2. close any that successfully opened
      dataStreams.close();
      // throw as a runtime exception
      throw new RuntimeException("Unable to fetch all required data", e);
    }
  }
}
