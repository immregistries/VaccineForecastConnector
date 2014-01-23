package org.tch.fc.model;

import java.io.Serializable;

public class EvaluationType implements Serializable
{
  private int evaluationTypeId = 0;
  private String label = "";

  public int getEvaluationTypeId() {
    return evaluationTypeId;
  }

  public void setEvaluationTypeId(int evaluationTypeId) {
    this.evaluationTypeId = evaluationTypeId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
}
