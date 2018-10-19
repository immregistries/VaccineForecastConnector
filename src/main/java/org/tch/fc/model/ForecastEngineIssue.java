package org.tch.fc.model;

public class ForecastEngineIssue {
  private ForecastEngineIssueType issueType = null;
  private ForecastEngineIssueLevel issueLevel = null;
  private String description = "";

  public ForecastEngineIssue() {
    // default
  }

  public ForecastEngineIssue(ForecastEngineIssueType issueType, ForecastEngineIssueLevel issueLevel,
      String description) {
    this.issueType = issueType;
    this.issueLevel = issueLevel;
    this.description = description;
  }

  public ForecastEngineIssueType getIssueType() {
    return issueType;
  }

  public void setIssueType(ForecastEngineIssueType issueType) {
    this.issueType = issueType;
  }

  public ForecastEngineIssueLevel getIssueLevel() {
    return issueLevel;
  }

  public void setIssueLevel(ForecastEngineIssueLevel issueLevel) {
    this.issueLevel = issueLevel;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
