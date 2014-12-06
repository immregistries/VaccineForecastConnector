package org.tch.fc.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestEvent implements Serializable
{

  public TestEvent() {
    // default;
  }

  public TestEvent(int eventId, Date eventDate) {
    this.event = Event.getEvent(eventId);
    this.eventDate = eventDate;
  }

  public TestEvent(TestEvent testEventParent, TestCase testCase) {
    setEvent(testEventParent.getEvent());
    setEventDate(testEventParent.getEventDate());
    setTestCase(testCase);
  }

  private static final long serialVersionUID = 1L;

  private int testEventId = 0;
  private TestCase testCase = null;
  private Event event = null;
  private Date eventDate = null;
  private List<EvaluationActual> evaluationActualList = null;
  private Condition condition = null;
  private RelativeRule eventRule = null;
  private int screenId = 0;

  public void calculateFixedDates() {
    if (testCase != null && testCase.getDateSet() == DateSet.RELATIVE) {
      if (eventRule != null) {
        eventDate = eventRule.calculateDate();
      }
    }
  }

  public String getLabelScreen() {
    if (event.getEventType() == EventType.BIRTH) {
      return event.getLabelScreen();
    } else if (event.getEventType() == EventType.EVALUATION) {
      return event.getLabelScreen();
    } else if (event.getEventType() == EventType.VACCINATION) {
      if (screenId > 0) {
        return "vaccine #" + screenId;
      } else {
        return event.getLabelScreen();
      }
    } else if (event.getEventType() == EventType.ACIP_DEFINED_CONDITION) {
      return event.getLabelScreen();
    } else if (event.getEventType() == EventType.CONDITION_IMPLICATION) {
      return event.getLabelScreen();
    } else {
      return event.getLabelScreen();
    }
  }

  public int getScreenId() {
    return screenId;
  }

  public void setScreenId(int temporaryDoseNumber) {
    this.screenId = temporaryDoseNumber;
  }

  public RelativeRule getEventRule() {
    return eventRule;
  }

  public void setEventRule(RelativeRule eventRule) {
    this.eventRule = eventRule;
  }

  public Condition getCondition() {
    return condition;
  }

  public void setCondition(Condition condition) {
    this.condition = condition;
  }

  public String getConditionCode() {
    return condition == null ? null : condition.getConditionCode();
  }

  public void setConditionCode(String conditionCode) {
    condition = Condition.getCondition(conditionCode);
  }

  public List<EvaluationActual> getEvaluationActualList() {
    return evaluationActualList;
  }

  public void setEvaluationActualList(List<EvaluationActual> evaluationActualList) {
    this.evaluationActualList = evaluationActualList;
  }

  public int getTestEventId() {
    return testEventId;
  }

  public void setTestEventId(int testEventId) {
    this.testEventId = testEventId;
  }

  public TestCase getTestCase() {
    return testCase;
  }

  public void setTestCase(TestCase testCase) {
    this.testCase = testCase;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }

  public Date getEventDate() {
    return eventDate;
  }

  public void setEventDate(Date eventDate) {
    this.eventDate = eventDate;
  }

  public String getAgeAlmost(TestCase testCase) {
    if (eventDate != null) {
      int monthsBetween = monthsYearsBetween(eventDate, testCase.getPatientDob());
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(eventDate);
      calendar.add(Calendar.DAY_OF_MONTH, 4);
      int almostMonthsBetween = monthsYearsBetween(calendar.getTime(), testCase.getPatientDob());
      if (almostMonthsBetween > monthsBetween) {
        return "Almost " + createAge(calendar.getTime(), testCase.getPatientDob());
      }
      return createAge(eventDate, testCase.getPatientDob());
    }
    return "";
  }

  private static int monthsYearsBetween(Date eventDate, Date referenceDate) {
    int months = monthsBetween(eventDate, referenceDate);
    if (months > 24) {
      months = months - (months % 12);
    }
    return months;
  }

  private static int monthsBetween(Date eventDate, Date referenceDate) {
    Calendar eventCal = Calendar.getInstance();
    Calendar referenceCal = Calendar.getInstance();
    eventCal.setTime(eventDate);
    referenceCal.setTime(referenceDate);
    int eventYear = eventCal.get(Calendar.YEAR);
    int referenceYear = referenceCal.get(Calendar.YEAR);
    int maxMonth = eventCal.getMaximum(Calendar.MONTH);
    int eventMonth = eventCal.get(Calendar.MONTH);
    int referenceMonth = referenceCal.get(Calendar.MONTH);
    int months = (eventYear - referenceYear) * (maxMonth + 1) + (eventMonth - referenceMonth);

    if (eventCal.get(Calendar.DAY_OF_MONTH) < referenceCal.get(Calendar.DAY_OF_MONTH)) {
      months--;
    }
    return months;
  }

  private static String createAge(Date eventDate, Date referenceDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(referenceDate);
    calendar.add(Calendar.YEAR, 2);
    if (calendar.getTime().after(eventDate)) {
      // child was under 2 years of age, use months
      for (int months = 0; months < 24; months++) {
        calendar = Calendar.getInstance();
        calendar.setTime(referenceDate);
        calendar.add(Calendar.MONTH, months + 1);
        if (calendar.getTime().after(eventDate)) {
          if (months == 1) {
            return "1 Month";
          }
          return months + " Months";
        }
      }
      return "23 Months";
    }
    // child is over 2 years of age, now show in years
    for (int years = 2; years < 100; years++) {
      calendar = Calendar.getInstance();
      calendar.setTime(referenceDate);
      calendar.add(Calendar.YEAR, years + 1);
      if (calendar.getTime().after(eventDate)) {
        return years + " Years";
      }
    }
    return "Over 100 Years";
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TestEvent)
    {
      return ((TestEvent) obj).getTestEventId() == getTestEventId();
    }
    return super.equals(obj);
  }
  
  @Override
  public int hashCode() {
    return testEventId;
  }

}
