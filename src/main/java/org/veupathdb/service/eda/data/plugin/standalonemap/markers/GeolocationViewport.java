package org.veupathdb.service.eda.data.plugin.standalonemap.markers;

public class GeolocationViewport {
  private final double xMin;
  private final double xMax;
  private final double yMin;
  private final double  yMax;
  private final boolean viewportIncludesIntlDateLine;

  public GeolocationViewport(double xMin, double xMax,
                             double yMin, double yMax) {
    this.xMin = xMin;
    this.xMax = xMax;
    this.yMin = yMin;
    this.yMax = yMax;
    this.viewportIncludesIntlDateLine = yMin > yMax;
  }


  public static GeolocationViewport fromApiViewport(org.veupathdb.service.eda.generated.model.GeolocationViewport viewport) {
    return new GeolocationViewport(
      Double.parseDouble(viewport.getLatitude().getXMin()),
      Double.parseDouble(viewport.getLatitude().getXMax()),
      viewport.getLongitude().getLeft().doubleValue(),
      viewport.getLongitude().getRight().doubleValue()
    );
  }

  public Boolean containsCoordinates(double latitude, double longitude) {
    if (latitude < xMin || latitude > xMax) {
      return false;
    }
    if (viewportIncludesIntlDateLine) {
      return !(longitude < yMin) || !(longitude > yMax);
    } else {
      return !(longitude < yMin) && !(longitude > yMax);
    }
  }
}
