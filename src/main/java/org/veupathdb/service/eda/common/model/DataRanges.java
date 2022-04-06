package org.veupathdb.service.eda.common.model;

import org.gusdb.fgputil.Tuples;
import org.veupathdb.service.eda.generated.model.*;

import java.util.Optional;

public class DataRanges extends Tuples.TwoTuple<DataRange, DataRange> {
  public DataRanges(DataRange dataRange, DataRange displayRange) {
    super(dataRange, displayRange);
  }

  public DataRange getDataRange() {
    return getFirst();
  }

  public DataRange getDisplayRange() {
    return getSecond();
  }

  public static Optional<DataRanges> getDataRanges(APIVariableWithValues obj) {
    if (obj.getDataShape() != APIVariableDataShape.CONTINUOUS) {
      return Optional.empty();
    }
    switch(obj.getType()) {
      case NUMBER:
        APINumberVariable numVar = (APINumberVariable)obj;
        return Optional.of(new DataRanges(
            new DataRange(
                numVar.getRangeMin().toString(),
                numVar.getRangeMax().toString()),
            new DataRange(
                Optional.ofNullable(numVar.getDisplayRangeMin()).orElse(numVar.getRangeMin()).toString(),
                Optional.ofNullable(numVar.getDisplayRangeMax()).orElse(numVar.getRangeMax()).toString())));
      case INTEGER:
        APIIntegerVariable intVar = (APIIntegerVariable)obj;
        return Optional.of(new DataRanges(
            new DataRange(
                intVar.getRangeMin().toString(),
                intVar.getRangeMax().toString()),
            new DataRange(
                Optional.ofNullable(intVar.getDisplayRangeMin()).orElse(intVar.getRangeMin()).toString(),
                Optional.ofNullable(intVar.getDisplayRangeMax()).orElse(intVar.getRangeMax()).toString())));
      case DATE:
        APIDateVariable dateVar = (APIDateVariable)obj;
        return Optional.of(new DataRanges(
            new DataRange(
                dateVar.getRangeMin(),
                dateVar.getRangeMax()),
            new DataRange(
                Optional.ofNullable(dateVar.getDisplayRangeMin()).orElse(dateVar.getRangeMin()),
                Optional.ofNullable(dateVar.getDisplayRangeMax()).orElse(dateVar.getRangeMax()))));
      default:
        return Optional.empty();
    }
  }
  public static Optional<DataRanges> getDataRanges(APICollection obj) {
    if (obj.getDataShape() != APIVariableDataShape.CONTINUOUS) {
      return Optional.empty();
    }
    switch(obj.getType()) {
      case NUMBER:
        APINumberCollection numVar = (APINumberCollection)obj;
        return Optional.of(new DataRanges(
            new DataRange(
                numVar.getRangeMin().toString(),
                numVar.getRangeMax().toString()),
            new DataRange(
                Optional.ofNullable(numVar.getDisplayRangeMin()).orElse(numVar.getRangeMin()).toString(),
                Optional.ofNullable(numVar.getDisplayRangeMax()).orElse(numVar.getRangeMax()).toString())));
      case INTEGER:
        APIIntegerCollection intVar = (APIIntegerCollection)obj;
        return Optional.of(new DataRanges(
            new DataRange(
                intVar.getRangeMin().toString(),
                intVar.getRangeMax().toString()),
            new DataRange(
                Optional.ofNullable(intVar.getDisplayRangeMin()).orElse(intVar.getRangeMin()).toString(),
                Optional.ofNullable(intVar.getDisplayRangeMax()).orElse(intVar.getRangeMax()).toString())));
      case DATE:
        APIDateCollection dateVar = (APIDateCollection)obj;
        return Optional.of(new DataRanges(
            new DataRange(
                dateVar.getRangeMin(),
                dateVar.getRangeMax()),
            new DataRange(
                Optional.ofNullable(dateVar.getDisplayRangeMin()).orElse(dateVar.getRangeMin()),
                Optional.ofNullable(dateVar.getDisplayRangeMax()).orElse(dateVar.getRangeMax()))));
      default:
        return Optional.empty();
    }
  }
}
