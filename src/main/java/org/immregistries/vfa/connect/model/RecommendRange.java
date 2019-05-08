package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public enum RecommendRange implements Serializable {
  TEMPORAL("T", "Temporal"), PERMANENT("P", "Permanent");

  private String recommendRangeCode = "";
  private String label = "";

  private RecommendRange(String recommendRangeCode, String label) {
    this.recommendRangeCode = recommendRangeCode;
    this.label = label;
  }

  public String getRecommendRangeCode() {
    return recommendRangeCode;
  }

  public String getLabel() {
    return label;
  }

  public static RecommendRange getRecommendRange(String recommendRangeCode) {
    for (RecommendRange recommendRange : RecommendRange.values()) {
      if (recommendRange.getRecommendRangeCode().equalsIgnoreCase(recommendRangeCode)) {
        return recommendRange;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return label;
  }

}
