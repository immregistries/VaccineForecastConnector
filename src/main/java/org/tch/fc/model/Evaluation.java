package org.tch.fc.model;

import java.io.Serializable;

public enum Evaluation implements Serializable {
  EXTRANEOUS("E", "Extraneous"), NOT_VALID("N", "Not Valid"), VALID("V", "Valid"), SUB_STANDARD("S", "Sub-Standard"), ;
  private Evaluation(String evaluationStatus, String label) {
    this.evaluationStatus = evaluationStatus;
    this.label = label;
  }

  private String evaluationStatus = "";
  private String label = "";

  public String getEvaluationStatus() {
    return evaluationStatus;
  }

  public void setEvaluationStatus(String evaluationStatus) {
    this.evaluationStatus = evaluationStatus;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
  
  public static Evaluation getEvaluation(String evaluationStatus) {
    for (Evaluation evaluation : values()) {
      if (evaluation.getEvaluationStatus().equalsIgnoreCase(evaluationStatus)) {
        return evaluation;
      }
    }
    return null;
  }
  
  public static Evaluation getEvaluationByLabel(String label) {
	    for (Evaluation evaluation : values()) {
	      if (evaluation.getLabel().equalsIgnoreCase(label)) {
	        return evaluation;
	      }
	    }
	    return null;
	  }
}
