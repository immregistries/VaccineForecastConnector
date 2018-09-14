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
import java.util.Date;
import java.util.List;

import org.tch.fc.model.EvaluationActual;
import org.tch.fc.model.Event;
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
    Software software = createSoftware();

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

  private Software createSoftware() {
    Software software = new Software();
    software.setServiceUrl("http://localhost:8765/soap");
    software.setService(Service.IIS);
    software.setServiceUserid("Mercy");
    software.setServicePassword("password123");
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

  public void testReadRSPEnvision() throws Exception {
    Software software = createSoftware();
    SoftwareResult softwareResult = new SoftwareResult();
    TestCase testCase = new TestCase();
    testCase.setTestEventList(new ArrayList<TestEvent>());
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      {
        TestEvent testEvent = new TestEvent();
        Event event = new Event();
        event.setVaccineCvx("107");
        testEvent.setEventDate(sdf.parse("20180819"));
        testEvent.setEvent(event);
        testCase.getTestEventList().add(testEvent);
      }
      {
        TestEvent testEvent = new TestEvent();
        Event event = new Event();
        event.setVaccineCvx("107");
        testEvent.setEventDate(sdf.parse("20180914"));
        testEvent.setEvent(event);
        testCase.getTestEventList().add(testEvent);
      }
    }

    IISConnector c = new IISConnector(software, VaccineGroup.getForecastItemList());
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();

    c.readRSP(forecastActualList, testCase, softwareResult, RSP_ENVISION);

    assertTrue(forecastActualList.size() > 5);
    System.out.println();
    System.out.println("VACCINE EVALUATION");
    System.out.print("| " + pad("CVX", 6));
    System.out.print("| " + pad("DATE", 12));
    System.out.print("| " + pad("CVX E", 6));
    System.out.print("| " + pad("VALID", 6));
    System.out.print("| " + pad("REASON", 30));
    System.out.println("|");
    System.out.print("|" + pad("--------------------------------------", 7));
    System.out.print("|" + pad("--------------------------------------", 13));
    System.out.print("|" + pad("--------------------------------------", 7));
    System.out.print("|" + pad("--------------------------------------", 7));
    System.out.print("|" + pad("--------------------------------------", 31));
    System.out.println("|");
    for (TestEvent testEvent : testCase.getTestEventList()) {
      if (testEvent.getEvaluationActualList() != null) {
        for (EvaluationActual evaluationActual : testEvent.getEvaluationActualList()) {
          System.out.print("| " + pad(testEvent.getEvent().getVaccineCvx(), 6));
          System.out.print("| " + pad(testEvent.getEventDate(), 12));
          System.out.print("| " + pad(evaluationActual.getVaccineCvx(), 6));
          System.out.print("| " + pad(evaluationActual.getDoseValid(), 6));
          System.out.print("| " + pad(evaluationActual.getReasonText(), 30));
          System.out.println("|");
        }
      }
    }

    System.out.println();
    System.out.println("VACCINE FORECAST");
    System.out.print("| " + pad("VACCINE", 21));
    System.out.print("| " + pad("#", 3));
    System.out.print("| " + pad("STATUS", 15));
    System.out.print("| " + pad("EARLIEST", 11));
    System.out.print("| " + pad("RECOMMEND", 11));
    System.out.print("| " + pad("PAST DUE", 11));
    System.out.print("| " + pad("LATEST", 11));
    System.out.println("|");
    System.out.print("|" + pad("--------------------------", 22));
    System.out.print("|" + pad("--------------------------", 4));
    System.out.print("|" + pad("--------------------------", 16));
    System.out.print("|" + pad("--------------------------", 12));
    System.out.print("|" + pad("--------------------------", 12));
    System.out.print("|" + pad("--------------------------", 12));
    System.out.print("|" + pad("--------------------------", 12));
    System.out.println("|");

    for (ForecastActual forecastActual : forecastActualList) {
      System.out.print("| " + pad(forecastActual.getVaccineGroup(), 21));
      System.out.print("| " + pad(forecastActual.getDoseNumber(), 3));
      System.out.print("| " + pad(forecastActual.getAdmin(), 15));
      System.out.print("| " + pad(forecastActual.getValidDate(), 11));
      System.out.print("| " + pad(forecastActual.getDueDate(), 11));
      System.out.print("| " + pad(forecastActual.getOverdueDate(), 11));
      System.out.print("| " + pad(forecastActual.getFinishedDate(), 11));
      System.out.println("|");
    }

  }

  private static String pad(Object o, int len) {
    if (o == null) {
      return pad("", len);
    }
    return pad(o.toString(), len);
  }

  private static String pad(Date date, int len) {
    if (date == null) {
      return pad("", len);
    }
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    return pad(sdf.format(date), len);
  }

  private static String pad(String s, int len) {
    if (s == null) {
      s = "";
    }
    for (int i = s.length(); i < len; i++) {
      s += " ";
    }
    if (s.length() > len) {
      s = s.substring(0, len);
    }
    return s;
  }

}
