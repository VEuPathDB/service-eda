package org.veupathdb.service.eda.merge.plugins.transforms;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Builds the vocabulary of a recoder: the distinct output values in rule order,
 * followed by the unmapped value only if it is non-empty and not already listed.
 */
public final class RecodingVocabulary {

  private RecodingVocabulary() { }

  public static List<String> of(List<String> outputValues, String unmappedValueOrNull) {
    LinkedHashSet<String> vocab = new LinkedHashSet<>(outputValues);
    if (unmappedValueOrNull != null && !unmappedValueOrNull.isEmpty()) {
      vocab.add(unmappedValueOrNull);
    }
    return new ArrayList<>(vocab);
  }
}
