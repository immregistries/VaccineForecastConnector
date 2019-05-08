package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public class Rationale implements Serializable
{
  private static final long serialVersionUID = 1L;

  private int rationaleId = 0;
  private String rationaleText = "";

  public int getRationaleId() {
    return rationaleId;
  }

  public void setRationaleId(int rationaleId) {
    this.rationaleId = rationaleId;
  }

  public String getRationaleText() {
    return rationaleText;
  }

  public void setRationaleText(String rationaleText) {
    this.rationaleText = rationaleText;
  }
}
