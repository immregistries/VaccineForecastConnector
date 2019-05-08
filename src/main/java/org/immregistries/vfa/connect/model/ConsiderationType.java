package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public enum ConsiderationType implements Serializable {
  SPECIFIC("S", "Specific"), GENERAL("G", "General");

  private String considerationTypeCode = "";
  private String label = "";

  private ConsiderationType(String considerationTypeCode, String label) {
    this.considerationTypeCode = considerationTypeCode;
    this.label = label;
  }

  public String getConsiderationTypeCode() {
    return considerationTypeCode;
  }

  public String getLabel() {
    return label;
  }

  public static ConsiderationType getConsiderationType(String considerationTypeCode) {
    for (ConsiderationType considerationType : ConsiderationType.values()) {
      if (considerationType.getConsiderationTypeCode().equalsIgnoreCase(considerationTypeCode)) {
        return considerationType;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return label;
  }
}
