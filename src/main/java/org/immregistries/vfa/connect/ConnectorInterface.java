package org.immregistries.vfa.connect;

import java.util.List;
import org.immregistries.vfa.connect.model.ForecastActual;
import org.immregistries.vfa.connect.model.SoftwareResult;
import org.immregistries.vfa.connect.model.TestCase;

public interface ConnectorInterface {
  /**
   * Given a test case, query the software to receive back a list of Forecast Actual results. 
   * @param forecastActualList
   * @return
   */
  public List<ForecastActual> queryForForecast(TestCase testCase, SoftwareResult softwareResult) throws Exception;
  
  public void setLogText(boolean logText);
  
  public boolean isLogText();
}
