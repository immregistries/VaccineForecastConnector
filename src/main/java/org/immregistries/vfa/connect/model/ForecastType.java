package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public class ForecastType implements Serializable
{
  private int forecastTypeId = 0;
  private String label = "";
  public int getForecastTypeId() {
    return forecastTypeId;
  }
  public void setForecastTypeId(int forecastTypeId) {
    this.forecastTypeId = forecastTypeId;
  }
  public String getLabel() {
    return label;
  }
  public void setLabel(String label) {
    this.label = label;
  }
}
