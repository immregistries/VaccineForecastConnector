package org.tch.fc.model;

import java.io.Serializable;
import java.util.Date;

import org.tch.fc.util.TimePeriod;

public class RelativeRule implements Serializable
{
  public static enum BeforeOrAfter {
    BEFORE, AFTER
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private int ruleId = 0;
  private TimePeriod timePeriod = null;
  private RelativeTo relativeTo = null;
  private TestEvent testEvent = null;
  private RelativeRule andRule = null;
  private BeforeOrAfter beforeOrAfter = BeforeOrAfter.AFTER;

  public Date calculateDate() {
    if (testEvent != null && testEvent.getTestCase() != null) {
      TestCase testCase = testEvent.getTestCase();
      Date refDate;
      if (relativeTo == RelativeTo.BIRTH) {
        refDate = testCase.getPatientDob();
      } else if (relativeTo == RelativeTo.EVALUATION) {
        refDate = testCase.getEvalDate();
      } else {
        refDate = testEvent.getEventDate();
      }
      Date date = timePeriod.getDateFrom(refDate);
      if (andRule != null)
      {
        Date altDate = andRule.calculateDate();
        if (altDate.after(date))
        {
          return altDate;
        }
      }
      return date;
    }
    return null;
  }

  public boolean isZero() {
    return timePeriod != null && timePeriod.isZero();
  }

  public String getRelativeToCode() {
    return relativeTo == null ? null : relativeTo.getRelativeToCode();
  }

  public void setRelativeToCode(String relativeToCode) {
    this.relativeTo = RelativeTo.getRelativeTo(relativeToCode);
  }

  public RelativeTo getRelativeTo() {
    return relativeTo;
  }

  public void setRelativeTo(RelativeTo relativeTo) {
    this.relativeTo = relativeTo;
  }

  public RelativeRule() {
    // default
  }

  public BeforeOrAfter getBeforeOrAfter() {
    return beforeOrAfter;
  }

  public void setBeforeOrAfter(BeforeOrAfter beforeOrAfter) {
    this.beforeOrAfter = beforeOrAfter;
  }

  public void convertBeforeToAfter() {
    if (beforeOrAfter == BeforeOrAfter.BEFORE && timePeriod != null) {
      timePeriod.negate();
    }
  }

  public void convertNegativeToBeforeOrAfter() {
    if (timePeriod != null && timePeriod.isNegative()) {
      timePeriod.negate();
      beforeOrAfter = beforeOrAfter == BeforeOrAfter.BEFORE ? BeforeOrAfter.AFTER : BeforeOrAfter.BEFORE;
    }
  }

  public RelativeRule(String timePeriodString) {
    timePeriod = new TimePeriod(timePeriodString);
    relativeTo = RelativeTo.BIRTH;
  }

  public int getRuleId() {
    return ruleId;
  }

  public void setRuleId(int ruleId) {
    this.ruleId = ruleId;
  }

  public TimePeriod getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(TimePeriod timePeriod) {
    this.timePeriod = timePeriod;
  }

  public void setTimePeriodString(String timePeriodString) {
    this.timePeriod = new TimePeriod(timePeriodString);
  }

  public String getTimePeriodString() {
    return this.timePeriod.toString();
  }

  public TestEvent getTestEvent() {
    return testEvent;
  }

  public void setTestEvent(TestEvent testEvent) {
    this.testEvent = testEvent;
  }

  public RelativeRule getAndRule() {
    return andRule;
  }

  public void setAndRule(RelativeRule andRule) {
    this.andRule = andRule;
  }

}
