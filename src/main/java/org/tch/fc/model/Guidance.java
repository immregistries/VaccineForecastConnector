package org.tch.fc.model;

import java.io.Serializable;
import java.util.Date;

public class Guidance implements Serializable
{
  private static final long serialVersionUID = 1L;

  private int guidanceId = 0;
  private VaccineGroup vaccineGroup = null;
  private Date effectiveDate = null;
  private Date expirationDate = null;

  public int getGuidanceId() {
    return guidanceId;
  }

  public void setGuidanceId(int guidanceId) {
    this.guidanceId = guidanceId;
  }

  public VaccineGroup getVaccineGroup() {
    return vaccineGroup;
  }

  public void setVaccineGroup(VaccineGroup vaccineGroup) {
    this.vaccineGroup = vaccineGroup;
  }

  public Date getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(Date effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  public Date getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }

}
