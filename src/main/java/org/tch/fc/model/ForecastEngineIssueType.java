package org.tch.fc.model;

public enum ForecastEngineIssueType {
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
