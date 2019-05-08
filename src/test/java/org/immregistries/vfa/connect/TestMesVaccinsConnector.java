package org.immregistries.vfa.connect;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.immregistries.vfa.connect.MesVaccinsConnector;
import org.immregistries.vfa.connect.mesvaccins.MesVaccinsDisease;
import org.immregistries.vfa.connect.mesvaccins.MesVaccinsVaccine;
import org.immregistries.vfa.connect.model.ForecastActual;
import org.immregistries.vfa.connect.model.Software;
import org.immregistries.vfa.connect.model.SoftwareResult;
import org.immregistries.vfa.connect.model.TestCase;
import org.immregistries.vfa.connect.model.TestEvent;
import org.immregistries.vfa.connect.model.VaccineGroup;
import org.junit.Test;

public class TestMesVaccinsConnector extends junit.framework.TestCase {
  String uid = "6889ffa77e02d8c378ccd825ed667ee741b7f0506e51a0c92b3f0c9efc3c1462";
  String secret = "16ebf21db15917e5c7bb6be8f94d40fbed572b0a02b36df1fd4bf73077af69ea";
  String url = "https://test.mesvaccins.net/api/v1";
  
  @Test
  public void testForecast() throws Exception {
    Software software = new Software();
    software.setServiceUserid(uid);
    software.setServicePassword(secret);
    software.setServiceUrl(url);
    MesVaccinsConnector connector =
        new MesVaccinsConnector(software, VaccineGroup.getForecastItemList());
    
    
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    TestCase testCase = new TestCase();
    testCase.setEvalDate(sdf.parse("04/25/2019"));
    testCase.setPatientSex("F");
    testCase.setPatientDob(sdf.parse("04/25/2019"));
    List<TestEvent> testEventList = new ArrayList<TestEvent>();
    testEventList.add(new TestEvent(45, sdf.parse("04/025/2019")));
    testCase.setTestEventList(testEventList);
    
    connector.setLogText(true);
    List<ForecastActual> forecastActualList = connector.queryForForecast(testCase, new SoftwareResult());
    assertNotNull(forecastActualList);
    assertEquals(5, forecastActualList.size());
    boolean foundHepB = false;
    for (ForecastActual forecastActual : forecastActualList) {
      System.out.println("-->   + " + forecastActual.getVaccineGroup());
      if (forecastActual.getVaccineGroup().getVaccineGroupId() == 5) {
        assertEquals(sdf.parse("06/01/2016"), forecastActual.getDueDate());
        assertNotNull(forecastActual.getSoftwareResult().getLogText());
        System.out.print(forecastActual.getSoftwareResult().getLogText().toString());
        foundHepB = true;
      }
    }
    assertTrue("HepB forecast not found", foundHepB);
  }

  @Test
  public void testGetVaccine() throws IOException {
    Software software = new Software();
    software.setServiceUserid(uid);
    software.setServicePassword(secret);
    software.setServiceUrl(url);
    MesVaccinsConnector mesVaccinsConnector =
        new MesVaccinsConnector(software, VaccineGroup.getForecastItemList());
    List<MesVaccinsVaccine> vaccineList = mesVaccinsConnector.getVaccines();
    List<MesVaccinsDisease> diseaseList = new ArrayList<>();
    for (MesVaccinsVaccine v : vaccineList) {
      for (MesVaccinsDisease d : v.getDiseasesSet()) {
        if (!diseaseList.contains(d)) {
          diseaseList.add(d);
        }
      }
    }
    Collections.sort(diseaseList);
    System.out.print("MesVaccines Id|MesVaccines Label");
    for (MesVaccinsDisease d : diseaseList) {
      System.out.print("|" + d.getName() + " (" + d.getId() + ")");
    }
    System.out.println();
    for (MesVaccinsVaccine v : vaccineList) {
      System.out.print(v.getId() + "|" + v.getName());
      for (MesVaccinsDisease d : diseaseList) {
        boolean found = false;
        for (MesVaccinsDisease d2 : v.getDiseasesSet()) {
          if (d2.getId() == d.getId()) {
            found = true;
            break;
          }
        }
        System.out.print("|" + (found ? "X" : ""));
      }
      System.out.println();
    }
    System.out.println("--> finished");
  }

}
