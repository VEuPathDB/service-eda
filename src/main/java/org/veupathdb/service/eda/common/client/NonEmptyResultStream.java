package org.veupathdb.service.eda.common.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around an input stream whose content must be a newline-delimited
 * set of records, including a header row.  This stream will transfer all
 * the passed input stream's data through its read() methods, with one
 * addition: after reading the header row (and optional newline), if no
 * more data is present, a JAX-RS BadRequestException will be thrown,
 * ending processing.
 */
public class NonEmptyResultStream extends BufferedInputStream {

  public static class EmptyResultException extends RuntimeException {
    EmptyResultException(String message) { super(message); }
  }

  private final String _streamName;

  // initial state
  private boolean _foundFirstNewline = false;
  private boolean _continueChecking = true;

  private final List<Byte> firstLine;

  public NonEmptyResultStream(String streamName, InputStream in) {
    super(in);
    firstLine = new ArrayList<>();
    _streamName = streamName;
    while (_continueChecking) {
      try {
        int nextByte = super.read();
        if (nextByte == -1) throwException();
        byte b = ((Integer) nextByte).byteValue();
        firstLine.add(b);
        check(b);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public int read() throws IOException {
    if (!firstLine.isEmpty()) {
      return firstLine.remove(0);
    }
    return super.read();
  }

  @Override
  public synchronized int read(byte[] b, int off, int len) throws IOException {
    if (firstLine.isEmpty()) {
      return super.read(b, off, len);
    }

    if (len > firstLine.size()) {
      int i = off;
      while (!firstLine.isEmpty()) {
        b[i++] = firstLine.remove(0);
      }
      int bytesRead = (i - off);
      return bytesRead + super.read(b, i, len - bytesRead);
    }

    int i = off;
    int bytesRead = 0;
    while (bytesRead < len) {
      b[i++] = firstLine.remove(0);
      bytesRead++;
    }
    return bytesRead;
  }

  private void throwException() {
    throw new EmptyResultException("Requested data stream '" + _streamName + "' did not contain any data.");
  }

  private void check(byte nextByte) {
    if (_foundFirstNewline) {
      _continueChecking = false; // done!
    }
    else if (nextByte == '\n') {
      _foundFirstNewline = true;
    }
  }
}
