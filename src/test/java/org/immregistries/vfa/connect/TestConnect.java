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
package org.immregistries.vfa.connect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.immregistries.vfa.connect.ConnectFactory;
import org.immregistries.vfa.connect.ConnectorInterface;
import org.immregistries.vfa.connect.model.EvaluationActual;
import org.immregistries.vfa.connect.model.Event;
import org.immregistries.vfa.connect.model.EventType;
import org.immregistries.vfa.connect.model.ForecastActual;
import org.immregistries.vfa.connect.model.Service;
import org.immregistries.vfa.connect.model.Software;
import org.immregistries.vfa.connect.model.SoftwareResult;
import org.immregistries.vfa.connect.model.TestCase;
import org.immregistries.vfa.connect.model.TestEvent;
import org.immregistries.vfa.connect.model.VaccineGroup;

public class TestConnect extends junit.framework.TestCase
{
  
  public void testTCHShingrix() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    TestCase testCase = new TestCase();
    testCase.setEvalDate(sdf.parse("01/01/1999"));
    testCase.setPatientSex("M");
    testCase.setPatientDob(sdf.parse("01/01/1950"));
    List<TestEvent> testEventList = new ArrayList<TestEvent>();
    testEventList.add(new TestEvent(187, sdf.parse("12/09/1999") ));
    testEventList.add(new TestEvent(187, sdf.parse("12/15/1999") ));
    testCase.setTestEventList(testEventList);
    Software software = new Software();
    software.setServiceUrl("http://tchforecasttester.org/fv/forecast");
    software.setService(Service.TCH);

    ConnectorInterface connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
    connector.setLogText(true);
    List<ForecastActual> forecastActualList = connector.queryForForecast(testCase, new SoftwareResult());
    assertForecasts(VaccineGroup.ID_HEPB, forecastActualList);
    assertForecasts(VaccineGroup.ID_SHINGRIX_ZOSTER, forecastActualList);
    assertForecasts(VaccineGroup.ID_ZOSTER, forecastActualList);
   }

  private void assertForecasts(int id, List<ForecastActual> forecastActualList) {
    boolean found = false;
    for (ForecastActual forecastActual: forecastActualList)
    {
      if (forecastActual.getVaccineGroup().getVaccineGroupId() == id)
      {
        found = true;
      }
    }
    assertTrue(found);
  }

  public void testTCHDoseNumberProblem() throws Exception {

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    TestCase testCase = new TestCase();
    testCase.setEvalDate(sdf.parse("03/11/2011"));
    testCase.setPatientSex("M");
    testCase.setPatientDob(sdf.parse("01/01/2012"));
    List<TestEvent> testEventList = new ArrayList<TestEvent>();
    TestEvent testEvent1 = new TestEvent(8, sdf.parse("01/02/2011"));
    TestEvent testEvent2 = new TestEvent(42, sdf.parse("01/02/2011"));
    TestEvent testEvent3 = new TestEvent(53, sdf.parse("01/03/2011"));
    testEventList.add(testEvent1);
    testEventList.add(testEvent2);
    testEventList.add(testEvent3);
    testCase.setTestEventList(testEventList);
    Software software = new Software();
    software.setServiceUrl("http://tchforecasttester.org/fv/forecast");
    software.setService(Service.TCH);

    ConnectorInterface connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
    connector.setLogText(true);
    connector.queryForForecast(testCase, new SoftwareResult());
    assertNotNull(testEvent1.getEvaluationActualList());
    assertEquals(1, testEvent1.getEvaluationActualList().size());
    assertEquals("1", testEvent1.getEvaluationActualList().get(0).getDoseNumber());
  }

  public void testConnectSWP() throws Exception {

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    TestCase testCase = new TestCase();
    testCase.setEvalDate(sdf.parse("07/12/2011"));
    testCase.setPatientSex("F");
    testCase.setPatientDob(sdf.parse("04/01/2006"));
    List<TestEvent> testEventList = new ArrayList<TestEvent>();
    testEventList.add(new TestEvent(8, sdf.parse("04/01/2006")));
    {
      TestEvent testEvent = new TestEvent();
      Event event = new Event();
      event.setEventType(EventType.CONTRAINDICATION_ALERGY_TO_PREVIOUS_DOSE);
      event.setEventId(Event.EVENT_ID_RANGE_1_MIIS + 188);
      testEvent.setEvent(event);
      testEventList.add(testEvent);
    }
    {
      TestEvent testEvent = new TestEvent();
      Event event = new Event();
      event.setEventType(EventType.CONTRAINDICATION_ALERGY_TO_PREVIOUS_DOSE);
      event.setEventId(Event.EVENT_ID_RANGE_1_MIIS + 148);
      testEvent.setEvent(event);
      testEventList.add(testEvent);
    }
    testCase.setTestEventList(testEventList);
    Software software = new Software();
    software.setServiceUrl("http://69.64.70.10:8080/vfmservice/VFMWebService");
    software.setService(Service.SWP);

    ConnectorInterface connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
    List<ForecastActual> forecastActualList = connector.queryForForecast(testCase, new SoftwareResult());
    assertNotNull(forecastActualList);
    boolean foundHepB = false;
    for (ForecastActual forecastActual : forecastActualList) {
      System.out.print("--> " + forecastActual.getVaccineGroup().getLabel());
      System.out.print(" " + forecastActual.getAdmin().getLabel());
      if (forecastActual.getDoseNumber() != null) {
        System.out.print(" Dose " + forecastActual.getDoseNumber());
      }
      if (forecastActual.getDueDate() != null) {
        System.out.print(" Due " + sdf.format(forecastActual.getDueDate()));
      }
      if (forecastActual.getValidDate() != null) {
        System.out.print(" Valid " + sdf.format(forecastActual.getValidDate()));
      }
      if (forecastActual.getOverdueDate() != null) {
        System.out.print(" Overdue " + sdf.format(forecastActual.getOverdueDate()));
      }
      System.out.println();
      if (forecastActual.getVaccineGroup().getVaccineGroupId() == 5) {
        assertEquals(sdf.parse("05/01/2006"), forecastActual.getDueDate());
        foundHepB = true;
      }
    }
    assertTrue("HepB forecast not found", foundHepB);
  }

  public void testConnectTCH() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    TestCase testCase = new TestCase();
    testCase.setEvalDate(sdf.parse("07/12/2011"));
    testCase.setPatientSex("F");
    testCase.setPatientDob(sdf.parse("04/01/2006"));
    List<TestEvent> testEventList = new ArrayList<TestEvent>();
    testEventList.add(new TestEvent(8, sdf.parse("04/01/2006")));
    testCase.setTestEventList(testEventList);
    Software software = new Software();
    software.setServiceUrl("http://tchforecasttester.org/fv/forecast");
    software.setService(Service.TCH);

    ConnectorInterface connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
    connector.setLogText(true);
    List<ForecastActual> forecastActualList = connector.queryForForecast(testCase, new SoftwareResult());
    assertNotNull(forecastActualList);
    boolean foundHepB = false;
    for (ForecastActual forecastActual : forecastActualList) {
      if (forecastActual.getVaccineGroup().getVaccineGroupId() == 5) {
        assertEquals(sdf.parse("05/01/2006"), forecastActual.getDueDate());
        assertNotNull(forecastActual.getSoftwareResult().getLogText());
        System.out.print(forecastActual.getSoftwareResult().getLogText().toString());
        foundHepB = true;
      }
    }
    assertTrue("HepB forecast not found", foundHepB);

  }

  public void testConnectSTC() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    TestCase testCase = new TestCase();
    testCase.setEvalDate(sdf.parse("07/12/2011"));
    testCase.setPatientSex("F");
    testCase.setPatientDob(sdf.parse("04/01/2006"));
    List<TestEvent> testEventList = new ArrayList<TestEvent>();
    testEventList.add(new TestEvent(8, sdf.parse("04/01/2006")));
    testCase.setTestEventList(testEventList);
    Software software = new Software();
    software.setServiceUrl("http://epicenter.stchome.com/safdemo/soa/forecast/getForecast.wsdl");
    software.setService(Service.STC);

    ConnectorInterface connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
    connector.setLogText(true);
    List<ForecastActual> forecastActualList = connector.queryForForecast(testCase, new SoftwareResult());
    assertNotNull(forecastActualList);
    boolean foundHepB = false;
    for (ForecastActual forecastActual : forecastActualList) {
      if (forecastActual.getVaccineGroup().getVaccineGroupId() == 5) {
        assertEquals(sdf.parse("05/01/2006"), forecastActual.getDueDate());
        assertNotNull(forecastActual.getSoftwareResult().getLogText());
        System.out.print(forecastActual.getSoftwareResult().getLogText().toString());
        foundHepB = true;
      }
    }
    assertTrue("HepB forecast not found", foundHepB);

  }

}
