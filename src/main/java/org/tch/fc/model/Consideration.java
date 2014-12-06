package org.tch.fc.model;

import java.io.Serializable;

public class Consideration implements Serializable
{
  private static final long serialVersionUID = 1L;

  private int considerationId = 0;
  private String considerationText = "";
  private ConsiderationType considerationType = null;

  public int getConsiderationId() {
    return considerationId;
  }

  public void setConsiderationId(int considerationId) {
    this.considerationId = considerationId;
  }

  public String getConsiderationText() {
    return considerationText;
  }

  public void setConsiderationText(String considerationText) {
    this.considerationText = considerationText;
  }

  public ConsiderationType getConsiderationType() {
    return considerationType;
  }

  public void setConsiderationType(ConsiderationType considerationType) {
    this.considerationType = considerationType;
  }

  public String getConsiderationTypeCode() {
    return considerationType == null ? null : considerationType.getConsiderationTypeCode();
  }

  public void setConsiderationTypeCode(String considerationTypeCode) {
    this.considerationType = ConsiderationType.getConsiderationType(considerationTypeCode);
  }

  @Override
  public String toString() {
    if (getConsiderationType() != null) {
      return getConsiderationText() + " [" + getConsiderationType().getLabel() + "]";
    }
    return getConsiderationText();
  }
}
