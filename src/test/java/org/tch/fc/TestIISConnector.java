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

import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.tch.fc.model.ForecastActual;
import org.tch.fc.model.Service;
import org.tch.fc.model.Software;
import org.tch.fc.model.SoftwareResult;
import org.tch.fc.model.SoftwareResultStatus;
import org.tch.fc.model.TestCase;
import org.tch.fc.model.TestEvent;
import org.tch.fc.model.VaccineGroup;
import org.tch.fc.util.ForecastResultPrinter;

public class TestIISConnector extends junit.framework.TestCase
{

  public void testConnectIIS() throws Exception {
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    TestCase testCase = new TestCase();
    testCase.setTestCaseNumber("2013-0002");
    testCase.setEvalDate(sdf.parse("07/12/2011"));
    testCase.setPatientSex("F");
    testCase.setPatientDob(sdf.parse("04/01/2006"));
    List<TestEvent> testEventList = new ArrayList<TestEvent>();
    testEventList.add(new TestEvent(8, sdf.parse("04/01/2006")));
    testCase.setTestEventList(testEventList);
    Software software = createSoftware();

    ConnectorInterface connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
    connector.setLogText(true);
    SoftwareResult softwareResult = new SoftwareResult();
    List<ForecastActual> forecastActualList = connector.queryForForecast(testCase, softwareResult);
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
    System.out.println(softwareResult.getLogText());

  }

  private Software createSoftware() {
    Software software = new Software();
    software.setServiceUrl("http://immlab.pagekite.me/iis-kernel/soap");
    software.setService(Service.IIS);
    software.setServiceUserid("Mercy");
    software.setServicePassword("password123");
    software.setServiceFacilityid("Mercy Healthcare");
    return software;
  }

  public void testConnectSTC() throws Exception {
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    TestCase testCase = new TestCase();
    testCase.setTestCaseNumber("2013-0002");
    testCase.setEvalDate(sdf.parse("07/12/2011"));
    testCase.setPatientSex("F");
    testCase.setPatientDob(sdf.parse("04/01/2006"));
    List<TestEvent> testEventList = new ArrayList<TestEvent>();
    testEventList.add(new TestEvent(8, sdf.parse("04/01/2006")));
    testCase.setTestEventList(testEventList);
    Software software = createSoftwareSTC();

    ConnectorInterface connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
    connector.setLogText(true);
    SoftwareResult softwareResult = new SoftwareResult();
    List<ForecastActual> forecastActualList = connector.queryForForecast(testCase, softwareResult);
    assertNotNull(forecastActualList);
    boolean foundHepB = false;
    for (ForecastActual forecastActual : forecastActualList) {
      if (forecastActual.getVaccineGroup().getVaccineGroupId() == 5
          || forecastActual.getVaccineGroup().getVaccineGroupId() == 20
          || forecastActual.getVaccineGroup().getVaccineGroupId() == 21) {
        foundHepB = true;
      }
    }
    assertEquals(SoftwareResultStatus.OK, softwareResult.getSoftwareResultStatus());
    assertTrue("HepB forecast not found", foundHepB);
    System.out.println(softwareResult.getLogText());

  }

  private Software createSoftwareSTC() {
    Software software = new Software();
    software.setServiceUrl("http://immlab.pagekite.me/aart/soap");
    software.setService(Service.IIS);
    software.setServiceUserid("TEMP_CONN");
    software.setServicePassword("B9VVFKMNBK8BZR8619F");
    software.setServiceFacilityid("72A");
    return software;
  }

  
 public void testConnectIISNotAuthenticated() throws Exception {
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    TestCase testCase = new TestCase();
    testCase.setTestCaseNumber("2013-0002");
    testCase.setEvalDate(sdf.parse("07/12/2011"));
    testCase.setPatientSex("F");
    testCase.setPatientDob(sdf.parse("04/01/2006"));
    List<TestEvent> testEventList = new ArrayList<TestEvent>();
    testEventList.add(new TestEvent(8, sdf.parse("04/01/2006")));
    testCase.setTestEventList(testEventList);
    Software software = createSoftwareNotAuthenticated();

    ConnectorInterface connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
    connector.setLogText(true);
    SoftwareResult softwareResult = new SoftwareResult();
    List<ForecastActual> forecastActualList = connector.queryForForecast(testCase, softwareResult);
    System.out.println(softwareResult.getLogText());
    assertEquals(SoftwareResultStatus.NOT_AUTHENTICATED, softwareResult.getSoftwareResultStatus());
  }
  
  private Software createSoftwareNotAuthenticated() {
    Software software = new Software();
    software.setServiceUrl("http://immlab.pagekite.me/iis-kernel/soap");
    software.setService(Service.IIS);
    software.setServiceUserid("Mercy");
    software.setServicePassword("passwordWrong");
    software.setServiceFacilityid("Mercy Healthcare");
    return software;
  }

  private static final String RSP = "MSH|^~\\&|Mercy Healthcare|Mercy Healthcare|||20180906123151-0600||RSP^K11^RSP_K11|15362587110058|P|2.5.1|||NE|NE|||||Z32^CDCPHINVS\r"
      + "MSA|AA|153625874169027\r" + "QAK|153625874169027|OK|Z34^Request a Complete Immunization History^CDCPHINVS\r"
      + "PID|1||U72HLBQDECQT^^^IIS^SR~153625874123126^^^FITS^MR||Bolotad^Anaya^Upor^^^^L|Sunde^^^^^^M|20180702|f|||347 Johnston Ave^^Canton Township^MI^48187^USA||^PRN^PH^^^734^2209966\r"
      + "NK1|1|Mecosta^Isa^^^^^L|MTH^Mother^HL70063\r" + "ORC|RE|153625874123126.1^Mercy Healthcare|22272^IIS\r"
      + "RXA|0|1|20180813||107^DTaP, unspecified formulation^CVX|999.0|mL^milliliters^UCUM||01^Historical information - source unspecified^NIP0001||||||||||||A\r"
      + "OBX|1|CE|30956-7^Vaccine type^LN|1|107^107^CVX\r" + "OBX|2|CE|59781-5^Dose validity^LN|1|Y^Y^TCH\r"
      + "ORC|RE|153625874123126.2^Mercy Healthcare|22273^IIS\r"
      + "RXA|0|1|20180906||107^DTaP, unspecified formulation^CVX|999.0|mL^milliliters^UCUM||01^Historical information - source unspecified^NIP0001||||||||||||A\r"
      + "OBX|3|CE|30956-7^Vaccine type^LN|2|107^107^CVX\r" + "OBX|4|CE|59781-5^Dose validity^LN|2|Y^Y^TCH\r"
      + "ORC|RE||999\r" + "RXA|0|1|20180906||998^No Vaccination Administered^CVX\r"
      + "OBX|5|CE|30956-7^Vaccine type^LN|3|45^HepB^CVX\r" + "OBX|6|CE|59783-1^Status in series^LN|3|O^O^TCH\r"
      + "OBX|7|DT|30981-5^Earliest date^LN|3|20180702\r" + "OBX|8|DT|30980-7^Recommended date^LN|3|20180702\r"
      + "OBX|9|DT|59778-1^Latest date^LN|3|20180730\r" + "OBX|10|CE|30956-7^Vaccine type^LN|4|17^Hib^CVX\r"
      + "OBX|11|CE|59783-1^Status in series^LN|4|D^D^TCH\r" + "OBX|12|DT|30981-5^Earliest date^LN|4|20180813\r"
      + "OBX|13|DT|30980-7^Recommended date^LN|4|20180902\r" + "OBX|14|DT|59778-1^Latest date^LN|4|20181002\r"
      + "OBX|15|CE|30956-7^Vaccine type^LN|5|152^Pneumococcal^CVX\r"
      + "OBX|16|CE|59783-1^Status in series^LN|5|D^D^TCH\r" + "OBX|17|DT|30981-5^Earliest date^LN|5|20180813\r"
      + "OBX|18|DT|30980-7^Recommended date^LN|5|20180902\r" + "OBX|19|DT|59778-1^Latest date^LN|5|20181002\r"
      + "OBX|20|CE|30956-7^Vaccine type^LN|6|152^PCV^CVX\r" + "OBX|21|CE|59783-1^Status in series^LN|6|D^D^TCH\r"
      + "OBX|22|DT|30981-5^Earliest date^LN|6|20180813\r" + "OBX|23|DT|30980-7^Recommended date^LN|6|20180902\r"
      + "OBX|24|DT|59778-1^Latest date^LN|6|20181002\r" + "OBX|25|CE|30956-7^Vaccine type^LN|7|89^Polio^CVX\r"
      + "OBX|26|CE|59783-1^Status in series^LN|7|D^D^TCH\r" + "OBX|27|DT|30981-5^Earliest date^LN|7|20180813\r"
      + "OBX|28|DT|30980-7^Recommended date^LN|7|20180902\r" + "OBX|29|DT|59778-1^Latest date^LN|7|20181002\r"
      + "OBX|30|CE|30956-7^Vaccine type^LN|8|122^Rotavirus^CVX\r" + "OBX|31|CE|59783-1^Status in series^LN|8|D^D^TCH\r"
      + "OBX|32|DT|30981-5^Earliest date^LN|8|20180813\r" + "OBX|33|DT|30980-7^Recommended date^LN|8|20180902\r"
      + "OBX|34|DT|59778-1^Latest date^LN|8|20181002\r" + "OBX|35|CE|30956-7^Vaccine type^LN|9|20^DTaP^CVX\r"
      + "OBX|36|CE|59783-1^Status in series^LN|9|L^L^TCH\r" + "OBX|37|DT|30981-5^Earliest date^LN|9|20181008\r"
      + "OBX|38|DT|30980-7^Recommended date^LN|9|20190102\r" + "OBX|39|DT|59778-1^Latest date^LN|9|20190202\r"
      + "OBX|40|CE|30956-7^Vaccine type^LN|10|20^DTaP, Tdap or Td^CVX\r"
      + "OBX|41|CE|59783-1^Status in series^LN|10|L^L^TCH\r" + "OBX|42|DT|30981-5^Earliest date^LN|10|20181008\r"
      + "OBX|43|DT|30980-7^Recommended date^LN|10|20190102\r" + "OBX|44|DT|59778-1^Latest date^LN|10|20190202\r"
      + "OBX|45|CE|30956-7^Vaccine type^LN|11|03^MMR^CVX\r" + "OBX|46|CE|59783-1^Status in series^LN|11|L^L^TCH\r"
      + "OBX|47|DT|30981-5^Earliest date^LN|11|20190702\r" + "OBX|48|DT|30980-7^Recommended date^LN|11|20190702\r"
      + "OBX|49|DT|59778-1^Latest date^LN|11|20191102\r" + "OBX|50|CE|30956-7^Vaccine type^LN|12|21^Varicella^CVX\r"
      + "OBX|51|CE|59783-1^Status in series^LN|12|L^L^TCH\r" + "OBX|52|DT|30981-5^Earliest date^LN|12|20190702\r"
      + "OBX|53|DT|30980-7^Recommended date^LN|12|20190702\r" + "OBX|54|DT|59778-1^Latest date^LN|12|20191102\r"
      + "OBX|55|CE|30956-7^Vaccine type^LN|13|108^Meningococcal^CVX\r"
      + "OBX|56|CE|59783-1^Status in series^LN|13|L^L^TCH\r" + "OBX|57|DT|30981-5^Earliest date^LN|13|20180827\r"
      + "OBX|58|DT|30980-7^Recommended date^LN|13|20290702\r" + "OBX|59|DT|59778-1^Latest date^LN|13|20310702\r"
      + "OBX|60|CE|30956-7^Vaccine type^LN|14|85^HepA^CVX\r" + "OBX|61|CE|59783-1^Status in series^LN|14|L^L^TCH\r"
      + "OBX|62|DT|30981-5^Earliest date^LN|14|20190702\r" + "OBX|63|DT|30980-7^Recommended date^LN|14|20190702\r"
      + "OBX|64|DT|59778-1^Latest date^LN|14|20200702\r" + "OBX|65|CE|30956-7^Vaccine type^LN|15|88^Influenza^CVX\r"
      + "OBX|66|CE|59783-1^Status in series^LN|15|L^L^TCH\r" + "OBX|67|DT|30981-5^Earliest date^LN|15|20190102\r"
      + "OBX|68|DT|30980-7^Recommended date^LN|15|20190102\r" + "OBX|69|DT|59778-1^Latest date^LN|15|20190102\r"
      + "OBX|70|CE|30956-7^Vaccine type^LN|16|137^HPV^CVX\r" + "OBX|71|CE|59783-1^Status in series^LN|16|L^L^TCH\r"
      + "OBX|72|DT|30981-5^Earliest date^LN|16|20270702\r" + "OBX|73|DT|30980-7^Recommended date^LN|16|20290702\r"
      + "OBX|74|DT|59778-1^Latest date^LN|16|20310702\r" + "OBX|75|CE|30956-7^Vaccine type^LN|17|121^HerpesZoster^CVX\r"
      + "OBX|76|CE|59783-1^Status in series^LN|17|L^L^TCH\r" + "OBX|77|DT|30981-5^Earliest date^LN|17|20680702\r"
      + "OBX|78|DT|30980-7^Recommended date^LN|17|20680702\r" + "OBX|79|DT|59778-1^Latest date^LN|17|20690702\r"
      + "OBX|80|CE|30956-7^Vaccine type^LN|18|187^RZV (Shingrix)^CVX\r"
      + "OBX|81|CE|59783-1^Status in series^LN|18|L^L^TCH\r" + "OBX|82|DT|30981-5^Earliest date^LN|18|20680702\r"
      + "OBX|83|DT|30980-7^Recommended date^LN|18|20680702\r" + "OBX|84|DT|59778-1^Latest date^LN|18|20690702\r";

  public void testReadRSP() throws Exception {
    Software software = createSoftware();
    SoftwareResult softwareResult = new SoftwareResult();
    TestCase testCase = new TestCase();
    IISConnector c = new IISConnector(software, VaccineGroup.getForecastItemList());
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();

    c.readRSP(forecastActualList, testCase, softwareResult, RSP);

    assertTrue(forecastActualList.size() > 5);

  }
  
  private static String RSP_NOT_FOUND = "MSH|^~\\&|74A^^|74A^^|^^|^^|20181029121553||RSP^K11^RSP_K11|7173854844.100120050|P|2.5.1|||||||||Z33^CDCPHINVS^^|\r" + 
      "MSA|AA|15408441522834675|\r" + 
      "ERR|||0|I||||No patients found for this query|\r" + 
      "QAK|15408441522834675|NF|Z44^Request Evaluated History and Forecast^HL70471|\r" + 
      "QPD|Z44^Request Evaluated History and Forecast^HL70471|15408441522834675|15408441491274674^^^FITS^MR|Garfield^Azland^Beck|Lyon^Cynthia^^^^^M|20180824|F|385 Custer St^^Ellsworth^MI^49729^USA^P|";
  
  public void testReadRSPNotFound() throws Exception{
    Software software = createSoftware();
    SoftwareResult softwareResult = new SoftwareResult();
    TestCase testCase = new TestCase();
    IISConnector c = new IISConnector(software, VaccineGroup.getForecastItemList());
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();

    c.readRSP(forecastActualList, testCase, softwareResult, RSP_NOT_FOUND);

    assertTrue(forecastActualList.size()  == 0);
    assertEquals(SoftwareResultStatus.NOT_FOUND, softwareResult.getSoftwareResultStatus());
  }

  private static String RSP_ENVISION = "MSH|^~\\&|WebIZ.18.1.20180629|NV0000||NV1001|20180914122905-0700||RSP^K11^RSP_K11|NV000020180914290578|D|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AE|1536953345100139\r"
      + "ERR||QPD^1^3^1^1|999^ApplicationError^HL70357|W|3^Illogical Value error^HL70533^WEBIZ-501^Internal Parser Conformance Violation Occured^L||NumericPath: QPD[1].3[1].1, NamePath: QPD/PatientIdentifierList[0]/IDNumber, RuleId: , ApplicationErrorCode: WEBIZ-501|Internal Parser Error: Field too long: 16 > 15\r"
      + "QAK|1536953345100139|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|1536953345100139|1536953340266138^^^FITS^MR|Okmulgee^Bly^Rocco^^^^L|Taylor^Iva^^^^^M|20180711|f|229 Washington Pl^^Midland^MI^48642^USA^P\r"
      + "PID|1||3982562^^^NV0000^SR||OKMULGEE^BLY^ROCCO^^^^L|Taylor^^^^^^M|20180711|F|||||^PRN^PH^^^989^6054828|||||||||||N|||||||||20180914\r"
      + "ORC|RE||41377439^NV0000\r"
      + "RXA|0|1|20180819|20180819|107^DTap, UF^CVX|999|||01^Historical Information - Source Unspecified^NIP001||AIRA^^^NV1001^^^^^4150 TECHNOLOGY WAY UNIT 210^^Carson City^NV^89706|||||||||CP|A|20180914\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|107^DTaP, UF^CVX||||||F|||20180819\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20180819\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|1|NA^Not Applicable^HL70353|||||F|||20180819\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180819\r"
      + "ORC|RE||41377440^NV0000\r"
      + "RXA|0|1|20180914|20180914|107^DTap, UF^CVX|999|||01^Historical Information - Source Unspecified^NIP001||AIRA^^^NV1001^^^^^4150 TECHNOLOGY WAY UNIT 210^^Carson City^NV^89706|||||||||CP|A|20180914\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|107^DTaP, UF^CVX||||||F|||20180914\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|N||||||F|||20180914\r"
      + "OBX|3|ST|30982-3^Reason Code^LN|1|Age: Too young, Preferable Interval: Grace period||||||F|||20180914\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "ORC|RE||9999^NV0000\r" + "RXA|0|1|20180914|20180914|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|08^Hep B, ped/adol^CVX||||||F|||20180914\r"
      + "OBX|2|DT|30981-5^Earliest date dose should be given^LN|1|20180711||||||F|||20180914\r"
      + "OBX|3|DT|30980-7^Date Vaccine Due^LN|1|20180711||||||F|||20180914\r"
      + "OBX|4|DT|59777-3^Latest date next dose should be given^LN|1|20370710||||||F|||20180914\r"
      + "OBX|5|DT|59778-1^Date dose is overdue^LN|1|20180807||||||F|||20180914\r"
      + "OBX|6|CE|59783-1^Series Status^LN|1|LA13423-1^Overdue^LN||||||F|||20180914\r"
      + "OBX|7|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|8|CE|30956-7^Vaccine Type^LN|2|133^PCV-13 (Prevnar 13)^CVX||||||F|||20180914\r"
      + "OBX|9|DT|30981-5^Earliest date dose should be given^LN|2|20180822||||||F|||20180914\r"
      + "OBX|10|DT|30980-7^Date Vaccine Due^LN|2|20180911||||||F|||20180914\r"
      + "OBX|11|DT|59777-3^Latest date next dose should be given^LN|2|20230710||||||F|||20180914\r"
      + "OBX|12|DT|59778-1^Date dose is overdue^LN|2|20181107||||||F|||20180914\r"
      + "OBX|13|CE|59783-1^Series Status^LN|2|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|14|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|15|CE|30956-7^Vaccine Type^LN|3|10^IPV^CVX||||||F|||20180914\r"
      + "OBX|16|DT|30981-5^Earliest date dose should be given^LN|3|20180822||||||F|||20180914\r"
      + "OBX|17|DT|30980-7^Date Vaccine Due^LN|3|20180911||||||F|||20180914\r"
      + "OBX|18|DT|59777-3^Latest date next dose should be given^LN|3|20360710||||||F|||20180914\r"
      + "OBX|19|DT|59778-1^Date dose is overdue^LN|3|20181107||||||F|||20180914\r"
      + "OBX|20|CE|59783-1^Series Status^LN|3|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|21|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|22|CE|30956-7^Vaccine Type^LN|4|48^Hib (PRP-T)^CVX||||||F|||20180914\r"
      + "OBX|23|DT|30981-5^Earliest date dose should be given^LN|4|20180822||||||F|||20180914\r"
      + "OBX|24|DT|30980-7^Date Vaccine Due^LN|4|20180911||||||F|||20180914\r"
      + "OBX|25|DT|59777-3^Latest date next dose should be given^LN|4|20230710||||||F|||20180914\r"
      + "OBX|26|DT|59778-1^Date dose is overdue^LN|4|20181107||||||F|||20180914\r"
      + "OBX|27|CE|59783-1^Series Status^LN|4|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|28|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|29|CE|30956-7^Vaccine Type^LN|5|116^Rotavirus (Rotateq)^CVX||||||F|||20180914\r"
      + "OBX|30|DT|30981-5^Earliest date dose should be given^LN|5|20180822||||||F|||20180914\r"
      + "OBX|31|DT|30980-7^Date Vaccine Due^LN|5|20180911||||||F|||20180914\r"
      + "OBX|32|DT|59777-3^Latest date next dose should be given^LN|5|20181023||||||F|||20180914\r"
      + "OBX|33|CE|59783-1^Series Status^LN|5|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|34|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|35|CE|30956-7^Vaccine Type^LN|6|08^Hep B, ped/adol^CVX||||||F|||20180914\r"
      + "OBX|36|DT|30981-5^Earliest date dose should be given^LN|6|20181012||||||F|||20180914\r"
      + "OBX|37|DT|30980-7^Date Vaccine Due^LN|6|20181012||||||F|||20180914\r"
      + "OBX|38|DT|59778-1^Date dose is overdue^LN|6|20181107||||||F|||20180914\r"
      + "OBX|39|CE|59783-1^Series Status^LN|6|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|40|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|41|CE|30956-7^Vaccine Type^LN|7|133^PCV-13 (Prevnar 13)^CVX||||||F|||20180914\r"
      + "OBX|42|DT|30981-5^Earliest date dose should be given^LN|7|20181012||||||F|||20180914\r"
      + "OBX|43|DT|30980-7^Date Vaccine Due^LN|7|20181111||||||F|||20180914\r"
      + "OBX|44|DT|59777-3^Latest date next dose should be given^LN|7|20230710||||||F|||20180914\r"
      + "OBX|45|DT|59778-1^Date dose is overdue^LN|7|20190107||||||F|||20180914\r"
      + "OBX|46|CE|59783-1^Series Status^LN|7|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|47|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|48|CE|30956-7^Vaccine Type^LN|8|10^IPV^CVX||||||F|||20180914\r"
      + "OBX|49|DT|30981-5^Earliest date dose should be given^LN|8|20181012||||||F|||20180914\r"
      + "OBX|50|DT|30980-7^Date Vaccine Due^LN|8|20181111||||||F|||20180914\r"
      + "OBX|51|DT|59777-3^Latest date next dose should be given^LN|8|20360710||||||F|||20180914\r"
      + "OBX|52|DT|59778-1^Date dose is overdue^LN|8|20190107||||||F|||20180914\r"
      + "OBX|53|CE|59783-1^Series Status^LN|8|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|54|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|55|CE|30956-7^Vaccine Type^LN|9|20^DTaP^CVX||||||F|||20180914\r"
      + "OBX|56|DT|30981-5^Earliest date dose should be given^LN|9|20181012||||||F|||20180914\r"
      + "OBX|57|DT|30980-7^Date Vaccine Due^LN|9|20181111||||||F|||20180914\r"
      + "OBX|58|DT|59778-1^Date dose is overdue^LN|9|20190107||||||F|||20180914\r"
      + "OBX|59|CE|59783-1^Series Status^LN|9|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|60|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|61|CE|30956-7^Vaccine Type^LN|10|48^Hib (PRP-T)^CVX||||||F|||20180914\r"
      + "OBX|62|DT|30981-5^Earliest date dose should be given^LN|10|20181012||||||F|||20180914\r"
      + "OBX|63|DT|30980-7^Date Vaccine Due^LN|10|20181111||||||F|||20180914\r"
      + "OBX|64|DT|59777-3^Latest date next dose should be given^LN|10|20230710||||||F|||20180914\r"
      + "OBX|65|DT|59778-1^Date dose is overdue^LN|10|20190107||||||F|||20180914\r"
      + "OBX|66|CE|59783-1^Series Status^LN|10|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|67|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|68|CE|30956-7^Vaccine Type^LN|11|116^Rotavirus (Rotateq)^CVX||||||F|||20180914\r"
      + "OBX|69|DT|30981-5^Earliest date dose should be given^LN|11|20181012||||||F|||20180914\r"
      + "OBX|70|DT|30980-7^Date Vaccine Due^LN|11|20181111||||||F|||20180914\r"
      + "OBX|71|DT|59777-3^Latest date next dose should be given^LN|11|20190311||||||F|||20180914\r"
      + "OBX|72|DT|59778-1^Date dose is overdue^LN|11|20190107||||||F|||20180914\r"
      + "OBX|73|CE|59783-1^Series Status^LN|11|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|74|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|75|CE|30956-7^Vaccine Type^LN|12|150^Influenza Quad Inj P^CVX||||||F|||20180914\r"
      + "OBX|76|DT|30981-5^Earliest date dose should be given^LN|12|20190111||||||F|||20180914\r"
      + "OBX|77|DT|30980-7^Date Vaccine Due^LN|12|20190111||||||F|||20180914\r"
      + "OBX|78|CE|59783-1^Series Status^LN|12|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|79|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|80|CE|30956-7^Vaccine Type^LN|13|03^MMR^CVX||||||F|||20180914\r"
      + "OBX|81|DT|30981-5^Earliest date dose should be given^LN|13|20190711||||||F|||20180914\r"
      + "OBX|82|DT|30980-7^Date Vaccine Due^LN|13|20190711||||||F|||20180914\r"
      + "OBX|83|DT|59778-1^Date dose is overdue^LN|13|20191208||||||F|||20180914\r"
      + "OBX|84|CE|59783-1^Series Status^LN|13|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|85|CE|59779-9^Immunization Schedule Used^LN|13|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|86|CE|30956-7^Vaccine Type^LN|14|21^CPOX (Varicella)^CVX||||||F|||20180914\r"
      + "OBX|87|DT|30981-5^Earliest date dose should be given^LN|14|20190711||||||F|||20180914\r"
      + "OBX|88|DT|30980-7^Date Vaccine Due^LN|14|20190711||||||F|||20180914\r"
      + "OBX|89|DT|59778-1^Date dose is overdue^LN|14|20191208||||||F|||20180914\r"
      + "OBX|90|CE|59783-1^Series Status^LN|14|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|91|CE|59779-9^Immunization Schedule Used^LN|14|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|92|CE|30956-7^Vaccine Type^LN|15|83^Hep A, ped/adol^CVX||||||F|||20180914\r"
      + "OBX|93|DT|30981-5^Earliest date dose should be given^LN|15|20190711||||||F|||20180914\r"
      + "OBX|94|DT|30980-7^Date Vaccine Due^LN|15|20190711||||||F|||20180914\r"
      + "OBX|95|DT|59777-3^Latest date next dose should be given^LN|15|20370710||||||F|||20180914\r"
      + "OBX|96|DT|59778-1^Date dose is overdue^LN|15|20200807||||||F|||20180914\r"
      + "OBX|97|CE|59783-1^Series Status^LN|15|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|98|CE|59779-9^Immunization Schedule Used^LN|15|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|99|CE|30956-7^Vaccine Type^LN|16|114^MCV4P (MENACTRA)^CVX||||||F|||20180914\r"
      + "OBX|100|DT|30981-5^Earliest date dose should be given^LN|16|20290711||||||F|||20180914\r"
      + "OBX|101|DT|30980-7^Date Vaccine Due^LN|16|20290711||||||F|||20180914\r"
      + "OBX|102|DT|59777-3^Latest date next dose should be given^LN|16|20400710||||||F|||20180914\r"
      + "OBX|103|DT|59778-1^Date dose is overdue^LN|16|20310807||||||F|||20180914\r"
      + "OBX|104|CE|59783-1^Series Status^LN|16|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|105|CE|59779-9^Immunization Schedule Used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|106|CE|30956-7^Vaccine Type^LN|17|165^HPV9^CVX||||||F|||20180914\r"
      + "OBX|107|DT|30981-5^Earliest date dose should be given^LN|17|20270711||||||F|||20180914\r"
      + "OBX|108|DT|30980-7^Date Vaccine Due^LN|17|20290711||||||F|||20180914\r"
      + "OBX|109|DT|59777-3^Latest date next dose should be given^LN|17|20450710||||||F|||20180914\r"
      + "OBX|110|DT|59778-1^Date dose is overdue^LN|17|20310807||||||F|||20180914\r"
      + "OBX|111|CE|59783-1^Series Status^LN|17|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|112|CE|59779-9^Immunization Schedule Used^LN|17|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r"
      + "OBX|113|CE|30956-7^Vaccine Type^LN|18|187^Zoster Recombinant^CVX||||||F|||20180914\r"
      + "OBX|114|DT|30981-5^Earliest date dose should be given^LN|18|20680711||||||F|||20180914\r"
      + "OBX|115|DT|30980-7^Date Vaccine Due^LN|18|20680711||||||F|||20180914\r"
      + "OBX|116|CE|59783-1^Series Status^LN|18|LA13422-3^On Schedule^LN||||||F|||20180914\r"
      + "OBX|117|CE|59779-9^Immunization Schedule Used^LN|18|VXC16^ACIP^CDCPHINVS||||||F|||20180914\r";

  @Test
  public void testReadRSPEnvision() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_ENVISION);

    assertEquals("", 2, testCase.getTestEventList().size());
    assertTrue(forecastActualList.size() > 5);
    PrintStream out = System.out;
    ForecastResultPrinter.printOutResultInFixedWidth(forecastActualList, testCase, out);

  }

  private TestCase run(List<ForecastActual> forecastActualList, String rsp) throws IOException, ParseException {
    Software software = createSoftware();
    IISConnector c = new IISConnector(software, VaccineGroup.getForecastItemList());
    TestCase testCase = IISConnector.recreateTestCase(rsp);
    SoftwareResult softwareResult = new SoftwareResult();
    c.readRSP(forecastActualList, testCase, softwareResult, rsp);
    return testCase;
  }

  private static final String RSP_STC = ""
      + "MSH|^~\\&|^^|^^|^^|^^|20180701172446||RSP^K11^RSP_K11|1724687711.100023793|P|2.5.1|||||||||Z42^CDCPHINVS^^|\r"
      + "MSA|AA|2aYO-GM-2.1-Q|\r" + "QAK|1530476519196.64097|OK|Z44^Request Evaluated History and Forecast^HL70471|\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|1530476519196.64097|W76Q645055^^^AIRA^MR|Comanche^Emanuel^Emogene|Pacific^Bara^^^^^M|20170618|F|309 Clark St^^Port Hope^MI^48468^USA^P|^PRN^PH^^^989^7330179|\r"
      + "PID|1||6330269^^^^SR~~~~~W76Q645055^^^^MR||COMANCHE^EMANUEL^EMOGENE^^^^L|PACIFIC|20170618|F|||309 CLARK ST^^PORT HOPE^MICHIGAN^48468^United States^M^^HURON||9897330179^PRN^PH^^^989^7330179|||||||||2186-5^not Hispanic or Latino^HL70189||N|1|||||N|\r"
      + "ORC|RE||6330269.54.20180630|\r"
      + "RXA|0|999|20180630|20180630|94^MMRV^CVX^90710^MMRV^CPT|.5|ML^mL^ISO+||00^New immunization record^NIP001||IRMS-1033||||Q3110HZ||MSD^Merck and Co., Inc.^HL70227||||A|20180701172446|\r"
      + "RXR|SC^Subcutaneous^CDCPHINVS|LT^Left Thigh^CDCPHINVS|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|20180630||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V01^Not VFC eligible^HL70064||||||F|||20180630|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V01^Not VFC eligible^HL70064||||||F|||20180630|||CVX40^per imm^CDCPHINVS|\r"
      + "OBX|1|TS|29768-9^VIS Publication Date^LN|1|20100521||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|45^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20170618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20170618||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170918||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|107^DTAP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20170818||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20170730||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170918||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|17^HIB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20170818||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20170730||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170918||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|133^PneumoPCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20170818||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20170730||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20220618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170918||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|88^FLU^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20171218||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20171218||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20180827||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|85^HepA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20180618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180618||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20190618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|89^POLIO^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20180728||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180728||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20180827||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|03^MMR^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|2||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20210618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180728||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20240618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|21^VARICELLA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|2||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20210618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180922||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20240618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|137^HPV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20280618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20260618||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21170618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20300618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|147^MENING^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20280618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20270618||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20300618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";

  @Test
  public void testReadRSPSTC() throws Exception {
    List forecastActualList = new ArrayList();
    TestCase testCase = run(forecastActualList, RSP_STC);

    assertEquals("Not all test events read", 1, testCase.getTestEventList().size());
  }

  private static final String RSP_IRIS_1 = ""
      + "MSH|^~\\&|IRIS IIS|IRIS||12908|20180628||RSP^K11^RSP_K11|Hc-GM-2.2-Q|P|2.5.1|||||||||Z32^CDCPHINVS\r"
      + "MSA|AA|Hc-GM-2.2-Q||0||0^Message Accepted^HL70357\r" + "QAK|Hc-GM-2.2-Q|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|1530191002770.4810|Y91B56113^^^AIRA^MR|Glorinn^Miranda^Ahneta^^^^L|Roach^Gabriela^^^^^M|20020616|F|8 Karle Pl^^Oakley^MI^48649^USA^P|^PRN^PH^^^989^6970185|N\r"
      + "PID|1||1139388^^^^SR~Y91B56113^^^^PI||GLORINN^MIRANDA^AHNETA^^^^L|ROACH^GABRIELA|20020616|F||2106-3|8 KARLE PL^^OAKLEY^MI^48649^^L||^PRN^PH^^^989^6970185|||||||||2186-5||N|0\r"
      + "PD1|||||||||||02|N||||A\r"
      + "NK1|1|GLORINN^ROACH^MARION|MTH|8 KARLE PL^^OAKLEY^MI^48649^^M|^PRN^PH^^^989^6970185\r" + "ORC|RE||10471065\r"
      + "RXA|0|1|20060627|20060627|03^MMR^CVX|1.0|||01\r"
      + "OBX|1|CE|30956-7^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|1||||||F\r" + "ORC|RE||0\r"
      + "RXA|0|1|20020616|20020616|998^No Vaccine Administered^CVX|999\r"
      + "OBX|1|CE|30979-9^Vaccines Due Next^LN|0|85^HepA^CVX^90730^HepA^CPT||||||F\r"
      + "OBX|2|TS|30980-7^Date Vaccine Due^LN|0|20030616||||||F\r"
      + "OBX|3|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|4|TS|30981-5^Earliest date to give^LN|0|20030616||||||F\r"
      + "OBX|5|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r"
      + "OBX|6|CE|30979-9^Vaccines Due Next^LN|1|45^HEPB, unspecified formulation^CVX||||||F\r"
      + "OBX|7|TS|30980-7^Date Vaccine Due^LN|1|20020616||||||F\r"
      + "OBX|8|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|9|TS|30981-5^Earliest date to give^LN|1|20020616||||||F\r"
      + "OBX|10|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|11|CE|30979-9^Vaccines Due Next^LN|2|137^HPV^CVX||||||F\r"
      + "OBX|12|TS|30980-7^Date Vaccine Due^LN|2|20130616||||||F\r"
      + "OBX|13|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|14|TS|30981-5^Earliest date to give^LN|2|20110616||||||F\r"
      + "OBX|15|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|16|CE|30979-9^Vaccines Due Next^LN|3|88^Influenza-seasnl^CVX^90724^Influenza-seasnl^CPT||||||F\r"
      + "OBX|17|TS|30980-7^Date Vaccine Due^LN|3|20170701||||||F\r"
      + "OBX|18|NM|30973-2^Vaccine due next dose number^LN|3|3||||||F\r"
      + "OBX|19|TS|30981-5^Earliest date to give^LN|3|20170701||||||F\r"
      + "OBX|20|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|21|CE|30979-9^Vaccines Due Next^LN|4|164^Meningococcal B^CVX||||||F\r"
      + "OBX|22|TS|30980-7^Date Vaccine Due^LN|4|20180616||||||F\r"
      + "OBX|23|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|4|20120616||||||F\r"
      + "OBX|25|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|26|CE|30979-9^Vaccines Due Next^LN|5|108^Meningo^CVX||||||F\r"
      + "OBX|27|TS|30980-7^Date Vaccine Due^LN|5|20180616||||||F\r"
      + "OBX|28|NM|30973-2^Vaccine due next dose number^LN|5|2||||||F\r"
      + "OBX|29|TS|30981-5^Earliest date to give^LN|5|20180616||||||F\r"
      + "OBX|30|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|31|CE|30979-9^Vaccines Due Next^LN|6|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|32|TS|30980-7^Date Vaccine Due^LN|6|20060725||||||F\r"
      + "OBX|33|NM|30973-2^Vaccine due next dose number^LN|6|2||||||F\r"
      + "OBX|34|TS|30981-5^Earliest date to give^LN|6|20060725||||||F\r"
      + "OBX|35|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r"
      + "OBX|36|CE|30979-9^Vaccines Due Next^LN|7|89^Polio^CVX||||||F\r"
      + "OBX|37|TS|30980-7^Date Vaccine Due^LN|7|20020816||||||F\r"
      + "OBX|38|NM|30973-2^Vaccine due next dose number^LN|7|1||||||F\r"
      + "OBX|39|TS|30981-5^Earliest date to give^LN|7|20020728||||||F\r"
      + "OBX|40|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|7|ACIP schedule||||||F\r"
      + "OBX|41|CE|30979-9^Vaccines Due Next^LN|8|139^Td/Tdap^CVX||||||F\r"
      + "OBX|42|TS|30980-7^Date Vaccine Due^LN|8|20090616||||||F\r"
      + "OBX|43|NM|30973-2^Vaccine due next dose number^LN|8|1||||||F\r"
      + "OBX|44|TS|30981-5^Earliest date to give^LN|8|20090616||||||F\r"
      + "OBX|45|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|8|ACIP schedule||||||F\r"
      + "OBX|46|CE|30979-9^Vaccines Due Next^LN|9|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|47|TS|30980-7^Date Vaccine Due^LN|9|20150616||||||F\r"
      + "OBX|48|NM|30973-2^Vaccine due next dose number^LN|9|1||||||F\r"
      + "OBX|49|TS|30981-5^Earliest date to give^LN|9|20150616||||||F\r"
      + "OBX|50|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|9|ACIP schedule||||||F\r";

  @Test
  public void testRSP_IRIS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_IRIS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 1, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 1, testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Not all forecasts read", 11, forecastActualList.size());
    assertEquals("Forecast not found", "HepA", forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "06/16/2003", sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "06/16/2003", sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "HepB", forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "06/16/2002", sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "06/16/2002", sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HPV", forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "06/16/2011", sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "06/16/2013", sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "Influenza", forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "07/01/2017", sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "07/01/2017", sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal", forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "06/16/2012", sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "06/16/2018", sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal", forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "06/16/2018", sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "06/16/2018", sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "MMR", forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "07/25/2006", sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "07/25/2006", sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Polio", forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "07/28/2002", sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/16/2002", sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "Td or Tdap", forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "06/16/2009", sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "06/16/2009", sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Forecast not found", "Td Only", forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "06/16/2009", sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "06/16/2009", sdf.format(forecastActualList.get(9).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Forecast not found", "Varicella", forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "06/16/2015", sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "06/16/2015", sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());

  }

  private static final String RSP_MIIC_1 = "" + "FHS|^~\\&|MIIC|MIIC||ATEST|20180705185443.159||3306556.response\r"
      + "BHS|^~\\&|MIIC|MIIC||ATEST|20180705185443.159\r"
      + "MSH|^~\\&|MIIC|MIIC||ATEST|20180705185443.279||RSP^K11^RSP_K11|1YaW-GM-2.3-Q|P|2.5.1|||||||||Z42^CDCPHINVS\r"
      + "MSA|AA|1YaW-GM-2.3-Q||0||0^Message Accepted^HL70357\r" + "QAK|1YaW-GM-2.3-Q|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|1530821469870.9543|B82X73438^^^AIRA^MR|Finn^Orana^Fascienne^^^^L|Frontier^Marcy^^^^^M|20170323|F|383 Uvalde Ave^^Deford^MI^48729^USA^P|^PRN^PH^^^989^5006903|N\r"
      + "PID|1||8802461^^^^SR~B82X73438^^^^PI||FINN^ORANA^FASCIENNE|FRONTIER^MARCY|20170323|F|||383 UVALDE AVE^^DEFORD^MI^48729^^L|||||||||||||N|0\r"
      + "PD1|||||||||||02|N||||A\r"
      + "NK1|1|FINN^FRONTIER^MARION|32|383 UVALDE AVE^^DEFORD^MI^48729^^M|^^PH^^^989^5006903\r" + "ORC|RE||51754921\r"
      + "RXA|0|1|20170323|20170323|45^HepB^CVX^90731^HepB^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170323|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754922\r" + "RXA|0|1|20170523|20170523|45^HepB^CVX^90731^HepB^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170523|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754923\r"
      + "RXA|0|1|20170523|20170523|122^Rotavirus, unspecified formulation^CVX^RV^^WVGC|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170523|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754924\r"
      + "RXA|0|1|20170523|20170523|107^DTaP, unspecified formulation^CVX^90700^DTaP, 5 pertussis antigens^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170523|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754925\r" + "RXA|0|1|20170523|20170523|17^Hib^CVX^90737^Hib^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170523|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754926\r"
      + "RXA|0|1|20170523|20170523|152^Pneumococcal Conjugate, unspec form^CVX^PNCN^^WVGC|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170523|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754927\r" + "RXA|0|1|20170523|20170523|10^Polio injectable^CVX^90713^Polio-Inject^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170523|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754928\r"
      + "RXA|0|1|20170723|20170723|122^Rotavirus, unspecified formulation^CVX^RV^^WVGC|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170723|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754929\r"
      + "RXA|0|1|20170723|20170723|107^DTaP, unspecified formulation^CVX^90700^DTaP, 5 pertussis antigens^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170723|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754930\r" + "RXA|0|1|20170723|20170723|17^Hib^CVX^90737^Hib^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170723|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754931\r"
      + "RXA|0|1|20170723|20170723|152^Pneumococcal Conjugate, unspec form^CVX^PNCN^^WVGC|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170723|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754932\r" + "RXA|0|1|20170723|20170723|10^Polio injectable^CVX^90713^Polio-Inject^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170723|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754933\r" + "RXA|0|1|20170922|20170922|45^HepB^CVX^90731^HepB^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170922|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754934\r"
      + "RXA|0|1|20170922|20170922|122^Rotavirus, unspecified formulation^CVX^RV^^WVGC|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170922|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754935\r"
      + "RXA|0|1|20170922|20170922|107^DTaP, unspecified formulation^CVX^90700^DTaP, 5 pertussis antigens^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170922|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754936\r" + "RXA|0|1|20170922|20170922|17^Hib^CVX^90737^Hib^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170922|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754937\r"
      + "RXA|0|1|20170922|20170922|152^Pneumococcal Conjugate, unspec form^CVX^PNCN^^WVGC|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20170922|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754938\r"
      + "RXA|0|1|20180704|20180704|120^Pentacel^CVX^90698^Pentacel^CPT|1.0|mL||00||||||983BG|20190329|PMC^Sanofi Pasteur^MVX\r"
      + "RXR||LT\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V01^Insured^HL70064||||||F|||20180704|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754939\r"
      + "RXA|0|1|20180704|20180704|133^Pneumo-PCV13^CVX^90670^Pneumo-PCV13^CPT|1.0|mL||00||||||A1345C|20190329|PFR^Pfizer, Inc.^MVX\r"
      + "RXR||LT\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V01^Insured^HL70064||||||F|||20180704|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754940\r"
      + "RXA|0|1|20180704|20180704|94^MMRV^CVX^90710^MMRV^CPT|1.0|mL||00||||||Q3110HZ|20190329|MSD^Merck and Co., Inc.^MVX\r"
      + "RXR||LT\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V01^Insured^HL70064||||||F|||20180704|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51754941\r"
      + "RXA|0|1|20180704|20180704|83^HepA-Ped 2 Dose^CVX^90633^HepA-Ped 2 Dose^CPT|1.0|mL||00||||||825AR23|20190329|SKB^GlaxoSmithKline^MVX\r"
      + "RXR||LT\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V01^Insured^HL70064||||||F|||20180704|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||0\r" + "RXA|0|1|20170323|20170323|998^No Vaccine Administered^CVX|999\r"
      + "OBX|1|CE|30979-9^Vaccines Due Next^LN|0|20^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r"
      + "OBX|2|TS|30980-7^Date Vaccine Due^LN|0|20210323||||||F\r"
      + "OBX|3|NM|30973-2^Vaccine due next dose number^LN|0|0||||||F\r"
      + "OBX|4|TS|30981-5^Earliest date to give^LN|0|20210323||||||F\r"
      + "OBX|5|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r"
      + "OBX|6|CE|30979-9^Vaccines Due Next^LN|1|31^HepA^CVX||||||F\r"
      + "OBX|7|TS|30980-7^Date Vaccine Due^LN|1|20190104||||||F\r"
      + "OBX|8|NM|30973-2^Vaccine due next dose number^LN|1|0||||||F\r"
      + "OBX|9|TS|30981-5^Earliest date to give^LN|1|20190104||||||F\r"
      + "OBX|10|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|11|CE|30979-9^Vaccines Due Next^LN|2|88^Influenza^CVX^90724^Influenza^CPT||||||F\r"
      + "OBX|12|TS|30980-7^Date Vaccine Due^LN|2|20180701||||||F\r"
      + "OBX|13|NM|30973-2^Vaccine due next dose number^LN|2|0||||||F\r"
      + "OBX|14|TS|30981-5^Earliest date to give^LN|2|20170923||||||F\r"
      + "OBX|15|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|16|CE|30979-9^Vaccines Due Next^LN|3|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|17|TS|30980-7^Date Vaccine Due^LN|3|20210323||||||F\r"
      + "OBX|18|NM|30973-2^Vaccine due next dose number^LN|3|0||||||F\r"
      + "OBX|19|TS|30981-5^Earliest date to give^LN|3|20180801||||||F\r"
      + "OBX|20|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|21|CE|30979-9^Vaccines Due Next^LN|4|109^Pneumo-conj^CVX||||||F\r"
      + "OBX|22|TS|30980-7^Date Vaccine Due^LN|4|20180829||||||F\r"
      + "OBX|23|NM|30973-2^Vaccine due next dose number^LN|4|0||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|4|20180829||||||F\r"
      + "OBX|25|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|26|CE|30979-9^Vaccines Due Next^LN|5|89^Polio^CVX||||||F\r"
      + "OBX|27|TS|30980-7^Date Vaccine Due^LN|5|20210323||||||F\r"
      + "OBX|28|NM|30973-2^Vaccine due next dose number^LN|5|0||||||F\r"
      + "OBX|29|TS|30981-5^Earliest date to give^LN|5|20210323||||||F\r"
      + "OBX|30|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|31|CE|30979-9^Vaccines Due Next^LN|6|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|32|TS|30980-7^Date Vaccine Due^LN|6|20210323||||||F\r"
      + "OBX|33|NM|30973-2^Vaccine due next dose number^LN|6|0||||||F\r"
      + "OBX|34|TS|30981-5^Earliest date to give^LN|6|20180926||||||F\r"
      + "OBX|35|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r"
      + "BTS|1\r" + "FTS|1\r";

  @Test
  public void testRSP_MIIC_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_MIIC_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 21, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 8, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP", forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "03/23/2021", sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "03/23/2021", sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td", forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "03/23/2021", sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "03/23/2021", sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA", forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "01/04/2019", sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "01/04/2019", sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "Influenza", forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2017", sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "07/01/2018", sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "MMR", forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/01/2018", sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "03/23/2021", sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal", forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/29/2018", sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/29/2018", sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "Polio", forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "03/23/2021", sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "03/23/2021", sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Varicella", forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2018", sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "03/23/2021", sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());

  }

  private static final String RSP_NESIIS_1 = ""
      + "MSH|^~\\&|^^|^^|^^|^^|20180701172446||RSP^K11^RSP_K11|1724687711.100023793|P|2.5.1|||||||||Z42^CDCPHINVS^^|\r"
      + "MSA|AA|2aYO-GM-2.1-Q|\r" + "QAK|1530476519196.64097|OK|Z44^Request Evaluated History and Forecast^HL70471|\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|1530476519196.64097|W76Q645055^^^AIRA^MR|Comanche^Emanuel^Emogene|Pacific^Bara^^^^^M|20170618|F|309 Clark St^^Port Hope^MI^48468^USA^P|^PRN^PH^^^989^7330179|\r"
      + "PID|1||6330269^^^^SR~~~~~W76Q645055^^^^MR||COMANCHE^EMANUEL^EMOGENE^^^^L|PACIFIC|20170618|F|||309 CLARK ST^^PORT HOPE^MICHIGAN^48468^United States^M^^HURON||9897330179^PRN^PH^^^989^7330179|||||||||2186-5^not Hispanic or Latino^HL70189||N|1|||||N|\r"
      + "ORC|RE||6330269.54.20180630|\r"
      + "RXA|0|999|20180630|20180630|94^MMRV^CVX^90710^MMRV^CPT|.5|ML^mL^ISO+||00^New immunization record^NIP001||IRMS-1033||||Q3110HZ||MSD^Merck and Co., Inc.^HL70227||||A|20180701172446|\r"
      + "RXR|SC^Subcutaneous^CDCPHINVS|LT^Left Thigh^CDCPHINVS|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|20180630||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V01^Not VFC eligible^HL70064||||||F|||20180630|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V01^Not VFC eligible^HL70064||||||F|||20180630|||CVX40^per imm^CDCPHINVS|\r"
      + "OBX|1|TS|29768-9^VIS Publication Date^LN|1|20100521||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|45^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20170618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20170618||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170918||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|107^DTAP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20170818||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20170730||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170918||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|17^HIB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20170818||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20170730||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170918||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|133^PneumoPCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20170818||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20170730||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20220618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170918||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|88^FLU^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20171218||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20171218||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20180827||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|85^HepA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20180618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180618||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20190618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|89^POLIO^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20180728||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180728||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20180827||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|03^MMR^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|2||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20210618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180728||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20240618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|21^VARICELLA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|2||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20210618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180922||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20240618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|137^HPV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20280618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20260618||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21170618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20300618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|147^MENING^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20280618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20270618||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20300618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";

  @Test
  public void testRSP_NESIIS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_NESIIS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 1, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 13, forecastActualList.size());
    assertEquals("Forecast not found", "HepB", forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "06/18/2017", sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "06/18/2017", sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/18/2017", sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP", forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "07/30/2017", sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "08/18/2017", sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/18/2017", sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td", forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "07/30/2017", sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/18/2017", sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/18/2017", sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "Hib", forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "07/30/2017", sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/18/2017", sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/18/2017", sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "PCV", forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "07/30/2017", sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/18/2017", sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/18/2017", sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal", forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "07/30/2017", sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/18/2017", sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/18/2017", sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza", forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "12/18/2017", sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "12/18/2017", sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/27/2018", sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "HepA", forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "06/18/2018", sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "06/18/2018", sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "06/18/2019", sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "Polio", forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "07/28/2018", sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "07/28/2018", sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/27/2018", sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "MMR", forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "07/28/2018", sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "06/18/2021", sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "06/18/2024", sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Varicella", forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/22/2018", sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "06/18/2021", sdf.format(forecastActualList.get(10).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Wrong overdue date found", "06/18/2024", sdf.format(forecastActualList.get(10).getOverdueDate()));
    assertEquals("Forecast not found", "HPV", forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(11).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(11).getValidDate());
    assertEquals("Wrong earliest date found", "06/18/2026", sdf.format(forecastActualList.get(11).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(11).getDueDate());
    assertEquals("Wrong due date found", "06/18/2028", sdf.format(forecastActualList.get(11).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Wrong overdue date found", "06/18/2030", sdf.format(forecastActualList.get(11).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal", forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "06/18/2027", sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "06/18/2028", sdf.format(forecastActualList.get(12).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Wrong overdue date found", "06/18/2030", sdf.format(forecastActualList.get(12).getOverdueDate()));
  }
}
