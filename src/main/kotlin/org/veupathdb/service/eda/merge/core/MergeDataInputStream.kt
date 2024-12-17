package org.veupathdb.service.eda.merge.core

import org.gusdb.fgputil.iterator.CloseableIterator
import org.veupathdb.service.eda.merge.core.stream.RootStreamingEntityNode
import java.io.InputStream

class MergeDataInputStream(
  private val targetEntityStream: RootStreamingEntityNode,
  private val dataStreams: Map<String, CloseableIterator<Map<String, String>>>,
) : InputStream() {
  private var nextLine: ByteArray
  private var lineIndex = 0

  private inline val remainingBytes get() = nextLine.size - lineIndex
  private inline val haveMoreBytes get() = remainingBytes > 0

  init {
    // distribute the streams to their processors and make sure they all get claimed
    val distributionMap = HashMap(dataStreams) // make a copy which will get cleared out

    targetEntityStream.acceptDataStreams(distributionMap)

    check(distributionMap.isEmpty()) {
      "Not all requested data streams were claimed by the processor tree.  " +
        "Remaining: " + java.lang.String.join(", ", distributionMap.keys)
    }

    // write the header row
    nextLine = targetEntityStream.orderedOutputVars.joinToString("\t", postfix = "\n").toByteArray()
  }

  override fun read(): Int {
    return when {
      haveMoreBytes -> nextLine[lineIndex++].toInt()
      tryQueueNextLine() -> read()
      else -> -1
    }
  }

  override fun read(b: ByteArray): Int {
    if (!haveMoreBytes && !tryQueueNextLine())
      return -1

    return if (b.size >= remainingBytes) {
      nextLine.copyInto(b, 0, lineIndex)
        .let { remainingBytes }
        .also { lineIndex += it }
    } else {
      nextLine.copyInto(b, 0, lineIndex, b.size)
        .let { b.size }
        .also { lineIndex += it }
    }
  }

  override fun read(b: ByteArray, off: Int, len: Int): Int {
    if (off !in b.indices || len > b.size - off)
      throw IndexOutOfBoundsException()
    if (len < 0)
      throw IllegalArgumentException()
    if (len == 0 || off == b.size-1)
      return 0

    if (!haveMoreBytes && !tryQueueNextLine())
      return -1

    return if (len >= remainingBytes) {
      nextLine.copyInto(b, off, lineIndex)
        .let { remainingBytes }
        .also { lineIndex += it }
    } else {
      nextLine.copyInto(b, off, lineIndex, len)
        .let { len }
        .also { lineIndex += it }
    }
  }

  private fun tryQueueNextLine(): Boolean {
    if (!targetEntityStream.hasNext())
      return false

    nextLine = targetEntityStream.next().values.joinToString("\t", postfix = "\n").toByteArray()
    lineIndex = 0

    return true
  }
}
