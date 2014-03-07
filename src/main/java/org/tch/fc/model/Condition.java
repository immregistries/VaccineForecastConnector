package org.tch.fc.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public enum Condition implements Serializable {
  SUB_POTENT("S", "Sub Potent"), FORCE_VALID("F", "Force Valid");
  private String conditionCode = "";
  private String label = "";

  private Condition(String conditionCode, String label) {
    this.conditionCode = conditionCode;
    this.label = label;
  }

  public String getConditionCode() {
    return conditionCode;
  }

  public void setConditionCode(String conditionCode) {
    this.conditionCode = conditionCode;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }

  public static Condition getCondition(String conditionCode) {
    for (Condition condition : Condition.values()) {
      if (condition.getConditionCode().equalsIgnoreCase(conditionCode)) {
        return condition;
      }
    }
    return null;
  }
  
  public static List<Condition> valueList() {
    return Arrays.asList(values());
  }
}
