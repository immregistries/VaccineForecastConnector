package org.tch.fc.model;

import java.io.Serializable;

public enum EventType implements Serializable {

  VACCINATION("V", "Vaccination"), HISTORY_OF_DISEASE("H", "History of Disease"), CONTRAINDICATION("C", "Contraindication"), CONTRAINDICATION_ALERGY_TO_PREVIOUS_DOSE(
      "1", "Contraindication - Allergy Previous Dose"), CONTRAINDICATION_ALERGY_VACCINE_COMPONENT("2",
      "Contraindication - Allergy Vaccine Component"), CONTRAINDICATION_RISK_FACTOR("3",
      "Contraindication - Risk Factor"), CONTRAINDICATION_UNSPECIFIED("4", "Contraindication - Unspecified"), IMMUNITY(
      "I", "Immunity"), PRECAUTION("P", "Precaution"), REFUSAL("R", "Refusal"), RELIGIOUS_EXEMPTION("E",
      "Religious Exemption"), SPECIAL_INDICATION("S", "Special Indication"), BIRTH("B", "Birth"), ACIP_DEFINED_CONDITION("A", "ACIP Defined Condition"), 
      CONDITION_IMPLICATION("N", "Condition Implication"), EVALUATION("L", "Evaluation");
  
  private static final long serialVersionUID = 1L;

  private String eventTypeCode = "";
  private String label = "";

  EventType(String eventTypeCode, String label) {
    this.eventTypeCode = eventTypeCode;
    this.label = label;
  }

  public String getEventTypeCode() {
    return eventTypeCode;
  }

  public void setEventTypeCode(String eventTypeCode) {
    this.eventTypeCode = eventTypeCode;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public static EventType getEventType(String eventTypeCode) {
    for (EventType eventType : values()) {
      if (eventType.getEventTypeCode().equalsIgnoreCase(eventTypeCode)) {
        return eventType;
      }
    }
    return null;
  }

}
