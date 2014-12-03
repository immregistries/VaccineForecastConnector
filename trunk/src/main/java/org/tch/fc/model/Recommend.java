package org.tch.fc.model;

import java.io.Serializable;

public class Recommend implements Serializable
{

  private static final long serialVersionUID = 1L;
  
  private int recommendId = 0;
  private String recommendText = "";
  private RecommendType recommendType = null;
  private RecommendRange recommendRange = null;
  
  public int getRecommendId() {
    return recommendId;
  }
  public void setRecommendId(int recommendId) {
    this.recommendId = recommendId;
  }
  public String getRecommendText() {
    return recommendText;
  }
  public void setRecommendText(String recommendText) {
    this.recommendText = recommendText;
  }
  public RecommendType getRecommendType() {
    return recommendType;
  }
  public void setRecommendType(RecommendType recommendType) {
    this.recommendType = recommendType;
  }
  public RecommendRange getRecommendRange() {
    return recommendRange;
  }
  public void setRecommendRange(RecommendRange recommendRange) {
    this.recommendRange = recommendRange;
  }
  public String getRecommendTypeCode() {
    return recommendType == null ? null : recommendType.getRecommendTypeCode();
  }
  public void setRecommendTypeCode(String recommendTypeCode) {
    this.recommendType = RecommendType.getRecommendType(recommendTypeCode);
  }
  public String getRecommendRangeCode() {
    return recommendRange == null ? null : recommendRange.getRecommendRangeCode();
  }
  public void setRecommendRangeCode(String recommendRangeCode) {
    this.recommendRange = RecommendRange.getRecommendRange(recommendRangeCode);
  }
}
