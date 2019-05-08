package org.immregistries.vfa.connect.model;

import java.io.Serializable;
import org.immregistries.vfa.connect.model.TestCase;

public class TestCaseSetting implements Serializable {
  private int settingId = 0;
  private TestCase testCase = null;
  private ServiceOption serviceOption = null;
  private String optionValue = "";
  public int getSettingId() {
    return settingId;
  }
  public void setSettingId(int settingId) {
    this.settingId = settingId;
  }
  public TestCase getTestCase() {
    return testCase;
  }
  public void setTestCase(TestCase testCase) {
    this.testCase = testCase;
  }
  public ServiceOption getServiceOption() {
    return serviceOption;
  }
  public void setServiceOption(ServiceOption serviceOption) {
    this.serviceOption = serviceOption;
  }
  public String getOptionValue() {
    return optionValue;
  }
  public void setOptionValue(String optionValue) {
    this.optionValue = optionValue;
  }
}
