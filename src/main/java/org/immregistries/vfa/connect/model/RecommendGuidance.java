package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public class RecommendGuidance implements Serializable
{

  private static final long serialVersionUID = 1L;
  
  private int recommendGuidanceId = 0;
  private Recommend recommend = null;
  private Guidance guidance = null;
  public int getRecommendGuidanceId() {
    return recommendGuidanceId;
  }
  public void setRecommendGuidanceId(int recommendGuidanceId) {
    this.recommendGuidanceId = recommendGuidanceId;
  }
  public Recommend getRecommend() {
    return recommend;
  }
  public void setRecommend(Recommend recommend) {
    this.recommend = recommend;
  }
  public Guidance getGuidance() {
    return guidance;
  }
  public void setGuidance(Guidance guidance) {
    this.guidance = guidance;
  }
}
