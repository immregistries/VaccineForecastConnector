package org.immregistries.vfa.connect;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
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
  private static String uid = "";
  private static String secret = "";
  private static String url = "";

  public void init() throws Exception {
    System.out.println("Loading mesvaccins properties...");
    InputStream input = getClass().getClassLoader().getResourceAsStream("mesvaccins.properties");

    Properties prop = new Properties();

    // load a properties file
    prop.load(input);

    // get the property value and print it out
    uid = prop.getProperty("mesvaccins.uid");
    secret = prop.getProperty("mesvaccins.secret");
    url = prop.getProperty("mesvaccins.url");

    if (url.length() == 0) {
      throw new Exception("URL is required");
    }
    System.out.println("Done loading properties");

    input.close();

  }

  @Test
  public void testForecast() throws Exception {
    init();
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
    List<ForecastActual> forecastActualList =
        connector.queryForForecast(testCase, new SoftwareResult());
    assertNotNull(forecastActualList);
    assertEquals(5, forecastActualList.size());
    boolean foundHepB = false;
    for (ForecastActual forecastActual : forecastActualList) {
      System.out.println("-->   + " + forecastActual.getVaccineGroup());
      if (forecastActual.getVaccineGroup().getVaccineGroupId() == 5) {
        //assertEquals(sdf.parse("06/01/2016"), forecastActual.getDueDate());
        //assertNotNull(forecastActual.getSoftwareResult().getLogText());
        //System.out.print(forecastActual.getSoftwareResult().getLogText().toString());
        foundHepB = true;
      }
    }
    assertTrue("HepB forecast not found", foundHepB);
  }

  @Test
  public void testGetVaccine() throws Exception {
    init();
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
