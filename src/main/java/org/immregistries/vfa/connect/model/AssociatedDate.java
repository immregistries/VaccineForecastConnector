package org.immregistries.vfa.connect.model;

import java.io.Serializable;
import java.util.Date;

public class AssociatedDate implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private int associatedDateId = 0;
  private TestEvent testEvent = null;
  private Date dateValue = null;
  private RelativeRule dateRule = null;
  private DateType dateType = null;

  public int getAssociatedDateId() {
    return associatedDateId;
  }

  public void setAssociatedDateId(int associatedDateId) {
    this.associatedDateId = associatedDateId;
  }

  public TestEvent getTestEvent() {
    return testEvent;
  }

  public void setTestEvent(TestEvent testEvent) {
    this.testEvent = testEvent;
  }

  public Date getDateValue() {
    return dateValue;
  }

  public void setDateValue(Date dateValue) {
    this.dateValue = dateValue;
  }

  public RelativeRule getDateRule() {
    return dateRule;
  }

  public void setDateRule(RelativeRule dateRule) {
    this.dateRule = dateRule;
  }

  public DateType getDateType() {
    return dateType;
  }

  public void setDateType(DateType dateType) {
    this.dateType = dateType;
  }

  public String getDateTypeCode() {
    return this.dateType == null ? null : this.dateType.toString();
  }

  public void setDateTypeCode(String dateTypeCode) {
    this.dateType = DateType.getDateType(dateTypeCode);
  }
}
