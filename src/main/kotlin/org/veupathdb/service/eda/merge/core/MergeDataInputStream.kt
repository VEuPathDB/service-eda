package org.veupathdb.service.eda.merge.core

import org.gusdb.fgputil.iterator.CloseableIterator
import org.veupathdb.service.eda.merge.core.stream.RootStreamingEntityNode
import org.veupathdb.service.eda.util.logger
import java.io.InputStream
import kotlin.math.min

class MergeDataInputStream(
  private val targetEntityStream: RootStreamingEntityNode,
  private val dataStreams: Map<String, CloseableIterator<Map<String, String>>>,
) : InputStream() {
  private var nextLine: ByteArray
  private var lineIndex = 0

  private inline val remainingBytes get() = nextLine.size - lineIndex
  private inline val moreBytesInBuffer get() = remainingBytes > 0

  init {
    // distribute the streams to their processors and make sure they all get claimed
    val distributionMap = HashMap(dataStreams) // make a copy which will get cleared out

    targetEntityStream.acceptDataStreams(distributionMap)

    check(distributionMap.isEmpty()) {
      "Not all requested data streams were claimed by the processor tree.  " +
        "Remaining: " + distributionMap.keys.joinToString(", ")
    }

    // write the header row
    nextLine = targetEntityStream.orderedOutputVars.joinToString("\t", postfix = "\n").toByteArray()
  }

  override fun read(): Int {
    return when {
      moreBytesInBuffer  -> nextLine[lineIndex++].toInt()
      tryQueueNextLine() -> read()
      else -> -1
    }
  }

  override fun read(b: ByteArray): Int {
    if (atEOF())
      return -1

    return if (b.size > remainingBytes) {
      fill(b, 0, b.size)
    } else if (b.size == remainingBytes) {
      nextLine.copyInto(b, 0, lineIndex)
      lineIndex = nextLine.size
      b.size
    } else {
      nextLine.copyInto(b, 0, lineIndex, lineIndex + b.size)
      lineIndex += b.size
      b.size
    }
  }

  override fun read(b: ByteArray, off: Int, len: Int): Int {
    if (off !in b.indices || len > b.size - off)
      throw IndexOutOfBoundsException()
    if (len < 0)
      throw IllegalArgumentException()
    if (atEOF())
      return -1
    if (len == 0 || off == b.size - 1)
      return 0

    return if (len > remainingBytes) {
      fill(b, off, len)
    } else if (len == remainingBytes) {
      nextLine.copyInto(b, off, lineIndex)
      lineIndex = nextLine.size
      len
    } else {
      nextLine.copyInto(b, off, lineIndex, lineIndex + len)
      lineIndex += len
      len
    }
  }

  override fun close() {
    dataStreams.values.forEach { try {
      it.close()
    } catch (e: Throwable) {
      logger().error("failed to close iterator", e)
    } }
  }

  private fun fill(buffer: ByteArray, initialOffset: Int, max: Int): Int {
    nextLine.copyInto(buffer, initialOffset, lineIndex)

    var readTotal = remainingBytes
    var offset = initialOffset + remainingBytes
    lineIndex = nextLine.size

    while (readTotal < max) {
      if (!tryQueueNextLine())
        return readTotal

      lineIndex = min(nextLine.size, max - readTotal)
      nextLine.copyInto(buffer, offset, 0, lineIndex)
      readTotal += lineIndex
      offset += lineIndex
    }

    return readTotal
  }

  private fun atEOF(): Boolean =
    !(moreBytesInBuffer || tryQueueNextLine())

  private fun tryQueueNextLine(): Boolean {
    if (!targetEntityStream.hasNext())
      return false

    nextLine = targetEntityStream.next().values.joinToString("\t", postfix = "\n").toByteArray()
    lineIndex = 0

    return true
  }
}
