package org.tch.fc.model;

import java.io.Serializable;

public enum RelativeTo implements Serializable {
  BIRTH("B", "Birth"), EVENT("E", "Event"), EVALUATION("L", "Evaluation");

  private RelativeTo(String relativeToCode, String label) {
    this.relativeToCode = relativeToCode;
    this.label = label;
  }

  private String relativeToCode = "";
  private String label = "";

  public String getRelativeToCode() {
    return relativeToCode;
  }

  public String getLabel() {
    return label;
  }

  public static RelativeTo getRelativeTo(String relativeToCode) {
    for (RelativeTo relativeTo : RelativeTo.values()) {
      if (relativeTo.getRelativeToCode().equalsIgnoreCase(relativeToCode)) {
        return relativeTo;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return label;
  }
}
