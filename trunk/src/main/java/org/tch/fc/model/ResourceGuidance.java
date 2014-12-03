package org.tch.fc.model;

import java.io.Serializable;

public class ResourceGuidance implements Serializable
{
  private static final long serialVersionUID = 1L;

  private int resourceGuidanceId = 0;
  private Resource resource = null;
  private Guidance guidance = null;

  public int getResourceGuidanceId() {
    return resourceGuidanceId;
  }

  public void setResourceGuidanceId(int resourceGuidanceId) {
    this.resourceGuidanceId = resourceGuidanceId;
  }

  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
  }

  public Guidance getGuidance() {
    return guidance;
  }

  public void setGuidance(Guidance guidance) {
    this.guidance = guidance;
  }
}
