package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public class GuidanceActual implements Serializable
{
  private static final long serialVersionUID = 1L;

  private int guidanceActualId = 0;
  private SoftwareResult softwareResult = null;
  private Guidance guidance = null;

  public int getGuidanceActualId() {
    return guidanceActualId;
  }

  public void setGuidanceActualId(int guidanceActualId) {
    this.guidanceActualId = guidanceActualId;
  }

  public SoftwareResult getSoftwareResult() {
    return softwareResult;
  }

  public void setSoftwareResult(SoftwareResult softwareResult) {
    this.softwareResult = softwareResult;
  }

  public Guidance getGuidance() {
    return guidance;
  }

  public void setGuidance(Guidance guidance) {
    this.guidance = guidance;
  }

}
