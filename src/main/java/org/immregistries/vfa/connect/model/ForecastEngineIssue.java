package org.immregistries.vfa.connect.model;

public class ForecastEngineIssue {
  private ForecastEngineIssueType issueType = null;
  private ForecastEngineIssueLevel issueLevel = null;
  private String description = "";
  private String path = "";

  public ForecastEngineIssue() {
    // default
  }

  public ForecastEngineIssue(ForecastEngineIssueType issueType, ForecastEngineIssueLevel issueLevel,
      String description, String path) {
    this.issueType = issueType;
    this.issueLevel = issueLevel;
    this.description = description;
    this.path = path;
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

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
