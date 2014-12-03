package org.tch.fc.model;

import java.io.Serializable;

public enum DateSet implements Serializable {
  RELATIVE("R", "Relative"), FIXED("F", "Fixed");

  private DateSet(String dateSetCode, String label) {
    this.dateSetCode = dateSetCode;
    this.label = label;
  }

  private String dateSetCode = "";
  private String label = "";

  public String getDateSetCode() {
    return dateSetCode;
  }

  public void getDateSetCode(String dateSetCode) {
    this.dateSetCode = dateSetCode;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public static DateSet getDateSet(String dateSetCode) {
    for (DateSet dateSet : DateSet.values()) {
      if (dateSet.getDateSetCode().equalsIgnoreCase(dateSetCode)) {
        return dateSet;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return label;
  }
}
