package org.veupathdb.service.eda.data.plugin.correlation;

import org.veupathdb.service.eda.data.metadata.AppsMetadata;
import org.veupathdb.service.eda.generated.model.CorrelationBipartitenetworkPostRequest;
import org.veupathdb.service.eda.generated.model.CorrelationConfig;
import org.veupathdb.service.eda.generated.model.CorrelationNetworkSpec;

import java.util.List;

public class CorrelationBipartitenetworkPlugin extends AbstractCorrelationBipartiteNetwork<CorrelationBipartitenetworkPostRequest, CorrelationConfig> {

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new ClassGroup(CorrelationBipartitenetworkPostRequest.class, CorrelationNetworkSpec.class, CorrelationConfig.class);
  }

  @Override
  public List<String> getProjects() {
    return AppsMetadata.NON_VB_GENOMICS_PROJECTS;
  }

}
