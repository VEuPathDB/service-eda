package org.veupathdb.service.eda.common.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.util.Header;
import org.gusdb.fgputil.FormatUtil;
import org.gusdb.fgputil.functional.Either;

public abstract class EdaClient {

  private static Logger LOG = LogManager.getLogger(EdaClient.class);

  public static boolean LOG_RESPONSE_HEADERS_AS_CLIENT = false;

  private final String _serviceBaseUrl;

  public EdaClient(String serviceBaseUrl) {
    _serviceBaseUrl = serviceBaseUrl;
  }

  public <T> T getResponseObject(String urlPath, Class<T> responseObjectClass) {
    try {
      return new ObjectMapper().readerFor(responseObjectClass).readValue(new URL(_serviceBaseUrl + urlPath));
    }
    catch (IOException e) {
      throw new RuntimeException("Unable to read and reserialize studies endpoint response object", e);
    }
  }

  public Either<InputStream,RequestFailure> makePostRequest(
      String urlPath, Object postBodyObject, String expectedResponseType) throws JsonProcessingException {

    String url = _serviceBaseUrl + urlPath;
    String json = serializeToJson(postBodyObject);
    LOG.info("Will send following POST request to " + url + "\n" + json);

    MultivaluedMap<String,Object> headers = new MultivaluedHashMap<>();
    headers.add(Header.Accept.toString(), "*/*");
    if (LOG_RESPONSE_HEADERS_AS_CLIENT) {
      headers.add("X-Jersey-Tracing-Accept", "any");
    }

    Response response = ClientBuilder.newClient()
      .target(url)
      .request(expectedResponseType)
      .headers(headers)
      .post(Entity.entity(json, MediaType.APPLICATION_JSON));

    if (LOG_RESPONSE_HEADERS_AS_CLIENT)
      logHeaders(response.getHeaders());

    return response.getStatusInfo().getFamily().equals(Status.Family.SUCCESSFUL) ?
        Either.left((InputStream)response.getEntity()) :
        Either.right(new RequestFailure(response));
  }

  private static void logHeaders(MultivaluedMap<String, Object> headers) {
    List<String> headerNames = new ArrayList<>(headers.keySet());
    Collections.sort(headerNames);
    for (String header : headerNames) {
      LOG.info("Header " + header + ": " + FormatUtil.join(headers.get(header).toArray(), ","));
    }
  }

  private static String serializeToJson(Object object) throws JsonProcessingException {
    return new ObjectMapper().writerFor(object.getClass()).writeValueAsString(object);
  }
}
