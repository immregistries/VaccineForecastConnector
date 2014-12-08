package org.tch.fc.model;

import java.io.Serializable;

public class EvaluationResult implements Serializable
{
  private Evaluation evaluation = null;
  private String evaluationReason = "";
  private String evaluationReasonCode = "";
  private VaccineGroup vaccineGroup = null;
  private String vaccineCvx = "";
  private String seriesUsedCode = "";
  private String seriesUsedText = "";
  private String reasonCode = "";
  private String reasonText = "";
  private String doseNumber = "";
  private String doseValid = "";

  public String getEvaluationReasonCode() {
    return evaluationReasonCode;
  }

  public void setEvaluationReasonCode(String evaluationReasonCode) {
    this.evaluationReasonCode = evaluationReasonCode;
  }

  public String getVaccineCvx() {
    return vaccineCvx;
  }

  public void setVaccineCvx(String vaccineCvx) {
    this.vaccineCvx = vaccineCvx;
  }

  public String getSeriesUsedCode() {
    return seriesUsedCode;
  }

  public void setSeriesUsedCode(String seriesUsedCode) {
    this.seriesUsedCode = seriesUsedCode;
  }

  public String getSeriesUsedText() {
    return seriesUsedText;
  }

  public void setSeriesUsedText(String seriesUsedText) {
    this.seriesUsedText = seriesUsedText;
  }

  public String getReasonCode() {
    return reasonCode;
  }

  public void setReasonCode(String reasonCode) {
    this.reasonCode = reasonCode;
  }

  public String getReasonText() {
    return reasonText;
  }

  public void setReasonText(String reasonText) {
    this.reasonText = reasonText;
  }

  public String getDoseNumber() {
    return doseNumber;
  }

  public void setDoseNumber(String doseNumber) {
    this.doseNumber = doseNumber;
  }

  public String getDoseValid() {
    return doseValid;
  }

  public void setDoseValid(String doseValid) {
    this.doseValid = doseValid;
  }

  public Evaluation getEvaluation() {
    return evaluation;
  }

  public void setEvaluation(Evaluation evaluation) {
    this.evaluation = evaluation;
  }

  public String getEvaluationStatus() {
    return evaluation == null ? null : evaluation.getEvaluationStatus();
  }

  public void setEvaluationStatus(String evaluationStatus) {
    this.evaluation = Evaluation.getEvaluation(evaluationStatus);
  }

  public String getEvaluationReason() {
    return evaluationReason;
  }

  public void setEvaluationReason(String evaluationReason) {
    this.evaluationReason = evaluationReason;
  }

  public VaccineGroup getVaccineGroup() {
    return vaccineGroup;
  }

  public void setVaccineGroup(VaccineGroup vaccineGroup) {
    this.vaccineGroup = vaccineGroup;
  }

}
