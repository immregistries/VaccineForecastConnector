package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public enum RecommendType implements Serializable {

  CONTRAINIDATION("C", "Do Not Administer"), PRECAUTION("P", "Precaution"), DISEASE("D", "Disease"), INDICATION("I",
      "Administer"), CONDITIONALLY_ADMINISTER("A", "Conditionally Administer");

  private String recommendTypeCode = "";
  private String label = "";

  private RecommendType(String recommendTypeCode, String label) {
    this.recommendTypeCode = recommendTypeCode;
    this.label = label;
  }

  public String getRecommendTypeCode() {
    return recommendTypeCode;
  }

  public String getLabel() {
    return label;
  }

  public static RecommendType getRecommendType(String recommendTypeCode) {
    for (RecommendType recommendType : RecommendType.values()) {
      if (recommendType.getRecommendTypeCode().equalsIgnoreCase(recommendTypeCode)) {
        return recommendType;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return label;
  }
}
