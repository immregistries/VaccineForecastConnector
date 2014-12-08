package org.tch.fc.model;

import java.io.Serializable;

public class ConsiderationGuidance implements Serializable
{

  private static final long serialVersionUID = 1L;

  private int considerationGuidanceId = 0;
  private Consideration consideration = null;
  private Guidance guidance = null;

  public int getConsiderationGuidanceId() {
    return considerationGuidanceId;
  }

  public void setConsiderationGuidanceId(int considerationGuidanceId) {
    this.considerationGuidanceId = considerationGuidanceId;
  }

  public Consideration getConsideration() {
    return consideration;
  }

  public void setConsideration(Consideration consideration) {
    this.consideration = consideration;
  }

  public Guidance getGuidance() {
    return guidance;
  }

  public void setGuidance(Guidance guidance) {
    this.guidance = guidance;
  }
}
