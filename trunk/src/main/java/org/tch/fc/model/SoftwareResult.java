package org.tch.fc.model;

import java.io.Serializable;
import java.util.Date;

public class SoftwareResult implements Serializable
{
  private int softwareResultId = 0;
  private Software software = null;
  private Date runDate = null;
  private String logText = null;
  protected TestCase testCase = null;

  public int getSoftwareResultId() {
    return softwareResultId;
  }

  public void setSoftwareResultId(int softwareResultId) {
    this.softwareResultId = softwareResultId;
  }

  public Software getSoftware() {
    return software;
  }

  public void setSoftware(Software software) {
    this.software = software;
  }

  public Date getRunDate() {
    return runDate;
  }

  public void setRunDate(Date runDate) {
    this.runDate = runDate;
  }

  public String getLogText() {
    return logText;
  }

  public void setLogText(String logText) {
    this.logText = logText;
  }

  public TestCase getTestCase() {
    return testCase;
  }

  public void setTestCase(TestCase testCase) {
    this.testCase = testCase;
  }
}
