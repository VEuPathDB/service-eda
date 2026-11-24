package eda;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.client.EdaSubsettingClient;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.APIStringSetFilterImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class EdaSubsettingClientFiltersTest {

  private static APIFilter filter(String variableId) {
    APIStringSetFilterImpl f = new APIStringSetFilterImpl();
    f.setEntityId("e");
    f.setVariableId(variableId);
    return f;
  }

  @Test
  public void overrideReplacesVariableFilters() {
    List<APIFilter> subset = List.of(filter("subsetVar"));
    List<APIFilter> override = List.of(filter("overrideVar"));
    StreamSpec spec = new StreamSpec("s", "e").setFiltersOverride(override);
    assertEquals(override, EdaSubsettingClient.resolveFilters(subset, spec));
  }

  @Test
  public void noOverrideUsesVariableFilters() {
    List<APIFilter> subset = List.of(filter("subsetVar"));
    assertSame(subset, EdaSubsettingClient.resolveFilters(subset, new StreamSpec("s", "e")));
  }
}
