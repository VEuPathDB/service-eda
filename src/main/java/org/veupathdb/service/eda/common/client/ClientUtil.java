package org.veupathdb.service.eda.common.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
import org.gusdb.fgputil.FormatUtil;
import org.gusdb.fgputil.functional.Either;
import org.gusdb.fgputil.functional.FunctionalInterfaces;
import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.web.ResponseEntityInputStream;

import static org.gusdb.fgputil.functional.Functions.cSwallow;

public class ClientUtil {

  private static final Logger LOG = LogManager.getLogger(ClientUtil.class);

  public static boolean LOG_RESPONSE_HEADERS = false;

  public static <T> T getResponseObject(String url, Class<T> responseObjectClass) throws IOException {
    return new ObjectMapper().readerFor(responseObjectClass).readValue(new URL(url));
  }

  public static Either<Optional<InputStream>, RequestFailure> makePostRequest(
      String url, Object postBodyObject, String expectedResponseType) {

    String json = JsonUtil.serializeObject(postBodyObject);
    LOG.info("Will send following POST request to " + url + "\n" + json);

    MultivaluedMap<String,Object> headers = new MultivaluedHashMap<>();
    headers.add(Header.Accept.toString(), "*/*");
    if (LOG_RESPONSE_HEADERS) {
      headers.add("X-Jersey-Tracing-Accept", "any");
    }

    Response response = ClientBuilder.newClient()
      .target(url)
      .request(expectedResponseType)
      .headers(headers)
      .post(Entity.entity(json, MediaType.APPLICATION_JSON));

    if (LOG_RESPONSE_HEADERS)
      logHeaders(response.getHeaders());

    Optional<InputStream> responseBody = !response.hasEntity() ? Optional.empty() :
        Optional.of(new ResponseEntityInputStream(response));

    return response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL) ?
        Either.left(responseBody) :
        Either.right(new RequestFailure(response));
  }

  private static void logHeaders(MultivaluedMap<String, Object> headers) {
    List<String> headerNames = new ArrayList<>(headers.keySet());
    Collections.sort(headerNames);
    for (String header : headerNames) {
      LOG.info("Header " + header + ": " + FormatUtil.join(headers.get(header).toArray(), ","));
    }
  }

  public static void buildAndProcessStreams(
      List<StreamSpec> requiredStreams,
      Function<StreamSpec, InputStream> streamGenerator,
      FunctionalInterfaces.ConsumerWithException<Map<String, InputStream>> streamProcessor) {
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
      List<StreamSpec> requiredStreams, Function<StreamSpec,InputStream> streamGenerator) {
    AutoCloseableList<InputStream> dataStreams = new AutoCloseableList<>();
    try {
      for (StreamSpec spec : requiredStreams) {
        dataStreams.add(streamGenerator.apply(spec));
      }
      return dataStreams;
    }
    catch (Exception e) {
      // if exception occurs while creating streams; close any that successfully opened, then throw
      dataStreams.close();
      throw new RuntimeException("Unable to fetch all required data", e);
    }
  }
}
