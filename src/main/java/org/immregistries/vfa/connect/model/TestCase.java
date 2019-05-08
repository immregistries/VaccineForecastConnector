package org.immregistries.vfa.connect.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestCase implements Serializable
{

  private static final long serialVersionUID = 1L;

  public static final String PATIENT_SEX_MALE = "M";
  public static final String PATIENT_SEX_FEMALE = "F";

  private int testCaseId = 0;
  private String label = "";
  private String description = "";
  private Date evalDate = null;
  private String patientFirst = "";
  private String patientLast = "";
  private String patientSex = "";
  private Date patientDob = null;
  private String categoryName = null;
  private String testCaseNumber = "";
  private EvaluationType evaluationType = null;
  private ForecastType forecastType = null;
  private RelativeRule evalRule = null;
  private DateSet dateSet = null;
  private VaccineGroup vaccineGroup = null;

  public VaccineGroup getVaccineGroup() {
    return vaccineGroup;
  }

  public void setVaccineGroup(VaccineGroup vaccineGroup) {
    this.vaccineGroup = vaccineGroup;
  }

  public void calculateFixedDates(Date targetEvalDate) {
    if (dateSet == DateSet.RELATIVE) {
      if (evalRule != null) {
        evalDate = targetEvalDate;
        patientDob = evalRule.getTimePeriod().getDateBefore(targetEvalDate);
      }
    }
  }

  public String getDateSetCode() {
    return dateSet == null ? null : dateSet.getDateSetCode();
  }

  public void setDateSetCode(String dateSetCode) {
    this.dateSet = DateSet.getDateSet(dateSetCode);
  }

  public RelativeRule getEvalRule() {
    return evalRule;
  }

  public void setEvalRule(RelativeRule evalRule) {
    this.evalRule = evalRule;
  }

  public DateSet getDateSet() {
    return dateSet;
  }

  public void setDateSet(DateSet dateSet) {
    this.dateSet = dateSet;
  }

  public EvaluationType getEvaluationType() {
    return evaluationType;
  }

  public void setEvaluationType(EvaluationType evaluationType) {
    this.evaluationType = evaluationType;
  }

  public ForecastType getForecastType() {
    return forecastType;
  }

  public void setForecastType(ForecastType forecastType) {
    this.forecastType = forecastType;
  }

  private List<TestEvent> testEventList = null;

  private List<TestCaseSetting> testCaseSettingList = null;

  public TestCase() {
    // default;
  }

  public TestCase(TestCase parentTestCase) {
    this.setLabel(parentTestCase.getLabel());
    this.setDescription(parentTestCase.getDescription());
    this.setEvalDate(parentTestCase.getEvalDate());
    this.setPatientFirst(parentTestCase.getPatientFirst());
    this.setPatientLast(parentTestCase.getPatientLast());
    this.setPatientSex(parentTestCase.getPatientSex());
    this.setPatientDob(parentTestCase.getPatientDob());
    this.setCategoryName(parentTestCase.getCategoryName());
    this.setTestCaseNumber(parentTestCase.getTestCaseNumber());
    if (parentTestCase.getTestEventList() != null) {
      List<TestEvent> testEventList = new ArrayList<TestEvent>(parentTestCase.getTestEventList().size());
      for (TestEvent testEvent : parentTestCase.getTestEventList()) {
        testEventList.add(new TestEvent(testEvent, this));
      }
      this.setTestEventList(testEventList);
    }
  }

  public String getTestCaseNumber() {
    return testCaseNumber;
  }

  public void setTestCaseNumber(String testCaseNumber) {
    this.testCaseNumber = testCaseNumber;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public List<TestEvent> getTestEventList() {
    return testEventList;
  }

  public void setTestEventList(List<TestEvent> testEventList) {
    this.testEventList = testEventList;
  }

  public int getTestCaseId() {
    return testCaseId;
  }

  public void setTestCaseId(int testCaseId) {
    this.testCaseId = testCaseId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getEvalDate() {
    return evalDate;
  }

  public void setEvalDate(Date evalDate) {
    this.evalDate = evalDate;
  }

  public String getPatientFirst() {
    return patientFirst;
  }

  public void setPatientFirst(String patientFirst) {
    this.patientFirst = patientFirst;
  }

  public String getPatientLast() {
    return patientLast;
  }

  public void setPatientLast(String patientLast) {
    this.patientLast = patientLast;
  }

  public String getPatientSex() {
    return patientSex;
  }

  public void setPatientSex(String patientSex) {
    this.patientSex = patientSex;
  }

  public Date getPatientDob() {
    return patientDob;
  }

  public void setPatientDob(Date patientDob) {
    this.patientDob = patientDob;
  }

  public List<TestCaseSetting> getTestCaseSettingList() {
    return testCaseSettingList;
  }

  public void setTestCaseSettingList(List<TestCaseSetting> testCaseSetting) {
    this.testCaseSettingList = testCaseSetting;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TestCase) {
      TestCase tc = (TestCase) obj;
      if (tc.getTestCaseId() == 0 || this.getTestCaseId() == 0) {
        return tc == this;
      }
      return tc.getTestCaseId() == this.getTestCaseId();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return testCaseId == 0 ? super.hashCode() : testCaseId;
  }

}
