package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public class EvaluationActual extends EvaluationResult implements Serializable
{
  private int evaluationActualId = 0;
  private SoftwareResult softwareResult = null;
  private TestEvent testEvent = null;

  public SoftwareResult getSoftwareResult() {
    return softwareResult;
  }
  
  public void setSoftwareResult(SoftwareResult softwareResult) {
    this.softwareResult = softwareResult;
  }
  
  public TestEvent getTestEvent() {
    return testEvent;
  }

  public void setTestEvent(TestEvent testEvent) {
    this.testEvent = testEvent;
  }

  public int getEvaluationActualId() {
    return evaluationActualId;
  }

  public void setEvaluationActualId(int evaluationActualId) {
    this.evaluationActualId = evaluationActualId;
  }

  

}
