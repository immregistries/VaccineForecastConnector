package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public class Resource implements Serializable
{
  private static final long serialVersionUID = 1L;

  private int resourceId = 0;
  private String resourceText = "";
  private String resourceLink = "";

  public int getResourceId() {
    return resourceId;
  }

  public void setResourceId(int resourceId) {
    this.resourceId = resourceId;
  }

  public String getResourceText() {
    return resourceText;
  }

  public void setResourceText(String resourceText) {
    this.resourceText = resourceText;
  }

  public String getResourceLink() {
    return resourceLink;
  }

  public void setResourceLink(String resourceLink) {
    this.resourceLink = resourceLink;
  }
}
