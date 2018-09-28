package org.tch.fc.model;

public enum ForecastEngineIssueType {
                                     AUTHENTICATION_FAILURE("01", "Authentication Failure"),
                                     ENGINE_NOT_AVAILABLE("02", "Engine Not Available"),
                                     MATCH_NOT_FOUND("03", "Match Not Found"),
                                     UNEXPECTED_FORMAT("04", "Unexpected Format"),
  ;
  
  private String id = "";
  private String description = "";

  public String getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  private ForecastEngineIssueType(String id, String description) {
    this.id = id;
    this.description = description;
  }

  @Override
  public String toString() {
    return id + ": " + description;
  }
}
