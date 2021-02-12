package org.veupathdb.service.eda.common.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.ws.rs.core.Response;
import org.gusdb.fgputil.IoUtil;
import org.gusdb.fgputil.Tuples;

public class RequestFailure extends Tuples.TwoTuple<Response.StatusType, String> {

  public RequestFailure(Response failureResponse) {
    super(failureResponse.getStatusInfo(), readResponseEntity(failureResponse));
  }

  private static String readResponseEntity(Response response) {
    try (Reader in = new InputStreamReader((InputStream)response.getEntity())) {
      return IoUtil.readAllChars(in);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Response.StatusType getStatusType() { return getFirst(); }
  public String getResponseBody() { return getSecond(); }

  public String toString() {
    return getStatusType().getStatusCode() + " " + getStatusType().getReasonPhrase() + ": " + getResponseBody();
  }
}
