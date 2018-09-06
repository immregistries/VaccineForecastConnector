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
    IISConnector c = new IISConnector(software, VaccineGroup.getForecastItemList());
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();

    c.readRSP(forecastActualList, RSP);

    assertTrue(forecastActualList.size() > 5);
    
  }
}
