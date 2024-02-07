package org.veupathdb.service.eda.common.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class NonEmptyResultStreamTest {

  @Test
  public void testNonEmptyReadByteByByte() throws Exception{
    NonEmptyResultStream streamUnderTest = new NonEmptyResultStream(
        "test-1", new ByteArrayInputStream("first\nsecond".getBytes(StandardCharsets.UTF_8)));
    Assertions.assertEquals('f', streamUnderTest.read());
    Assertions.assertEquals('i', streamUnderTest.read());
    Assertions.assertEquals('r', streamUnderTest.read());
    Assertions.assertEquals('s', streamUnderTest.read());
    Assertions.assertEquals('t', streamUnderTest.read());
    Assertions.assertEquals('\n', streamUnderTest.read());
    Assertions.assertEquals('s', streamUnderTest.read());
    Assertions.assertEquals('e', streamUnderTest.read());
    Assertions.assertEquals('c', streamUnderTest.read());
    Assertions.assertEquals('o', streamUnderTest.read());
    Assertions.assertEquals('n', streamUnderTest.read());
    Assertions.assertEquals('d', streamUnderTest.read());
  }

  @Test
  public void testEmptyOnlyHeaders() {
    Assertions.assertThrows(NonEmptyResultStream.EmptyResultException.class, () -> new NonEmptyResultStream(
        "test-2", new ByteArrayInputStream("first".getBytes(StandardCharsets.UTF_8))));
  }

  @Test
  public void testEmptyNothingInStream() {
    Assertions.assertThrows(NonEmptyResultStream.EmptyResultException.class, () -> new NonEmptyResultStream(
        "test-3", new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8))));
  }

  @Test
  public void testNonEmptyReadChunkLargerThanFirstLine() throws Exception {
    byte[] buffer = new byte[8];
    NonEmptyResultStream streamUnderTest = new NonEmptyResultStream(
        "test-1", new ByteArrayInputStream("first\nsecond".getBytes(StandardCharsets.UTF_8)));
    int bytesRead = streamUnderTest.read(buffer);
    Assertions.assertEquals(8, bytesRead);
    Assertions.assertEquals("first\nse", new String(buffer, StandardCharsets.UTF_8));
  }

  @Test
  public void testNonEmptyReadChunkSmallerThanFirstLine() throws Exception {
    byte[] buffer = new byte[3];
    NonEmptyResultStream streamUnderTest = new NonEmptyResultStream(
        "test-1", new ByteArrayInputStream("first\nsecond".getBytes(StandardCharsets.UTF_8)));
    int bytesRead = streamUnderTest.read(buffer);
    Assertions.assertEquals(3, bytesRead);
    Assertions.assertEquals("fir", new String(buffer, StandardCharsets.UTF_8));
    bytesRead = streamUnderTest.read(buffer);
    Assertions.assertEquals(3, bytesRead);
    Assertions.assertEquals("st\n", new String(buffer, StandardCharsets.UTF_8));
  }
}
