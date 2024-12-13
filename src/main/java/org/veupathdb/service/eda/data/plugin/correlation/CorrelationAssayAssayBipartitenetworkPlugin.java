package org.veupathdb.service.eda.data.plugin.correlation;

import org.veupathdb.service.eda.data.metadata.AppsMetadata;
import org.veupathdb.service.eda.generated.model.CorrelationAssayAssayBipartitenetworkPostRequest;
import org.veupathdb.service.eda.generated.model.CorrelationAssayAssayConfig;
import org.veupathdb.service.eda.generated.model.CorrelationNetworkSpec;

import java.util.List;

public class CorrelationAssayAssayBipartitenetworkPlugin extends AbstractCorrelationBipartiteNetwork<CorrelationAssayAssayBipartitenetworkPostRequest, CorrelationAssayAssayConfig> {

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new ClassGroup(CorrelationAssayAssayBipartitenetworkPostRequest.class, CorrelationNetworkSpec.class, CorrelationAssayAssayConfig.class);
  }

  @Override
  public List<String> getProjects() {
    return List.of(AppsMetadata.MICROBIOME_PROJECT);
  }

}
