package org.veupathdb.service.eda.common.model;

import org.gusdb.fgputil.Tuples;

public class DataRange extends Tuples.TwoTuple<String, String> {
  public DataRange(String start, String end) {
    super(start, end);
  }

  public String getStart() {
    return getFirst();
  }

  public String getEnd() {
    return getSecond();
  }
}
