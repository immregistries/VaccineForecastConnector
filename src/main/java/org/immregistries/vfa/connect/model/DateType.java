package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public enum DateType implements Serializable {
  START_DATE("STA", "Start Date"), ONSET_DATE("ONS", "Onset Date"), EFFECTIVE_DATE("EFF", "Effective Date"), EXPECTED_DATE(
      "EXP", "Expected Date"), END_DATE("END", "End DAte"), RESOLUTION_DATE("RES", "Resolution Date"), OBSERVATION_DATE(
      "OBS", "Observation Date");

  private DateType(String dateTypeCode, String label) {
    this.dateTypeCode = dateTypeCode;
    this.label = label;
  }

  private String dateTypeCode = "";
  private String label = "";

  public String getDateTypeCode() {
    return dateTypeCode;
  }

  public void getDateTypeCode(String dateTypeCode) {
    this.dateTypeCode = dateTypeCode;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public static DateType getDateType(String dateTypeCode) {
    for (DateType dateType : DateType.values()) {
      if (dateType.getDateTypeCode().equalsIgnoreCase(dateTypeCode)) {
        return dateType;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return label;
  }
}
