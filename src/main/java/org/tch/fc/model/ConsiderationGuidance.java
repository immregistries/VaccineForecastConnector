package org.tch.fc.model;

import java.io.Serializable;

public class ConsiderationGuidance implements Serializable
{

  private static final long serialVersionUID = 1L;

  private static int considerationGuidanceId = 0;
  private Consideration consideration = null;
  private Guidance guidance = null;

  public static int getConsiderationGuidanceId() {
    return considerationGuidanceId;
  }

  public static void setConsiderationGuidanceId(int considerationGuidanceId) {
    ConsiderationGuidance.considerationGuidanceId = considerationGuidanceId;
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
