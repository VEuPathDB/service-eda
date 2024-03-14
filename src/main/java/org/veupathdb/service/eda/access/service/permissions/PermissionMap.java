package org.veupathdb.service.eda.access.service.permissions;

import java.util.HashMap;

import org.veupathdb.service.eda.generated.model.DatasetPermissionEntry;
import org.veupathdb.service.eda.generated.model.PermissionsGetResponse;

public class PermissionMap
  extends HashMap<String, DatasetPermissionEntry>
  implements PermissionsGetResponse.PerDatasetType
{
}
