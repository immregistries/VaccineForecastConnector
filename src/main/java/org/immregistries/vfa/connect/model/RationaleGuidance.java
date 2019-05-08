package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public class RationaleGuidance implements Serializable
{
  private static final long serialVersionUID = 1L;

  private int rationaleGuidanceId = 0;
  private Rationale rationale = null;
  private Guidance guidance = null;

  public int getRationaleGuidanceId() {
    return rationaleGuidanceId;
  }

  public void setRationaleGuidanceId(int rationaleGuidanceId) {
    this.rationaleGuidanceId = rationaleGuidanceId;
  }

  public Rationale getRationale() {
    return rationale;
  }

  public void setRationale(Rationale rationale) {
    this.rationale = rationale;
  }

  public Guidance getGuidance() {
    return guidance;
  }

  public void setGuidance(Guidance guidance) {
    this.guidance = guidance;
  }
}
