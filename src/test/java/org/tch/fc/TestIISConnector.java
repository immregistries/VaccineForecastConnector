/*
 * Copyright 2013 - Texas Children's Hospital
 * 
 *   Texas Children's Hospital licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 */
package org.tch.fc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.tch.fc.model.ForecastActual;
import org.tch.fc.model.Service;
import org.tch.fc.model.Software;
import org.tch.fc.model.SoftwareResult;
import org.tch.fc.model.TestCase;
import org.tch.fc.model.TestEvent;
import org.tch.fc.model.VaccineGroup;

public class TestIISConnector extends junit.framework.TestCase
{

  public void testConnectIIS() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    TestCase testCase = new TestCase();
    testCase.setEvalDate(sdf.parse("07/12/2011"));
    testCase.setPatientSex("F");
    testCase.setPatientDob(sdf.parse("04/01/2006"));
    List<TestEvent> testEventList = new ArrayList<TestEvent>();
    testEventList.add(new TestEvent(8, sdf.parse("04/01/2006")));
    testCase.setTestEventList(testEventList);
    Software software = new Software();
    software.setServiceUrl("http://localhost:8765/soap");
    software.setService(Service.IIS);
    software.setServiceUserid("Mercy");
    software.setServicePassword("password123");
    software.setServiceFacilityid("Mercy Healthcare");

    ConnectorInterface connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
    connector.setLogText(true);
    List<ForecastActual> forecastActualList = connector.queryForForecast(testCase, new SoftwareResult());
    assertNotNull(forecastActualList);
    boolean foundHepB = false;
    for (ForecastActual forecastActual : forecastActualList) {
      if (forecastActual.getVaccineGroup().getVaccineGroupId() == 5
          || forecastActual.getVaccineGroup().getVaccineGroupId() == 20
          || forecastActual.getVaccineGroup().getVaccineGroupId() == 21) {
        foundHepB = true;
      }
    }
    assertTrue("HepB forecast not found", foundHepB);

  }

}
