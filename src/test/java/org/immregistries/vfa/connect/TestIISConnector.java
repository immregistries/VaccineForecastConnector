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

import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.immregistries.vfa.connect.model.ForecastActual;
import org.immregistries.vfa.connect.model.Service;
import org.immregistries.vfa.connect.model.Software;
import org.immregistries.vfa.connect.model.SoftwareResult;
import org.immregistries.vfa.connect.model.SoftwareResultStatus;
import org.immregistries.vfa.connect.model.TestCase;
import org.immregistries.vfa.connect.model.TestEvent;
import org.immregistries.vfa.connect.model.VaccineGroup;
import org.immregistries.vfa.connect.util.ForecastResultPrinter;
import org.junit.Test;

public class TestIISConnector extends junit.framework.TestCase {

  private static boolean ENABLE_LIVE_TESTS = false;

  private static final String RSP =
      "MSH|^~\\&|Mercy Healthcare|Mercy Healthcare|||20180906123151-0600||RSP^K11^RSP_K11|15362587110058|P|2.5.1|||NE|NE|||||Z32^CDCPHINVS\r"
          + "MSA|AA|153625874169027\r"
          + "QAK|153625874169027|OK|Z34^Request a Complete Immunization History^CDCPHINVS\r"
          + "PID|1||U72HLBQDECQT^^^IIS^SR~153625874123126^^^FITS^MR||Bolotad^Anaya^Upor^^^^L|Sunde^^^^^^M|20180702|f|||347 Johnston Ave^^Canton Township^MI^48187^USA||^PRN^PH^^^734^2209966\r"
          + "NK1|1|Mecosta^Isa^^^^^L|MTH^Mother^HL70063\r"
          + "ORC|RE|153625874123126.1^Mercy Healthcare|22272^IIS\r"
          + "RXA|0|1|20180813||107^DTaP, unspecified formulation^CVX|999.0|mL^milliliters^UCUM||01^Historical information - source unspecified^NIP0001||||||||||||A\r"
          + "OBX|1|CE|30956-7^Vaccine type^LN|1|107^107^CVX\r"
          + "OBX|2|CE|59781-5^Dose validity^LN|1|Y^Y^LSVF\r"
          + "ORC|RE|153625874123126.2^Mercy Healthcare|22273^IIS\r"
          + "RXA|0|1|20180906||107^DTaP, unspecified formulation^CVX|999.0|mL^milliliters^UCUM||01^Historical information - source unspecified^NIP0001||||||||||||A\r"
          + "OBX|3|CE|30956-7^Vaccine type^LN|2|107^107^CVX\r"
          + "OBX|4|CE|59781-5^Dose validity^LN|2|Y^Y^LSVF\r" + "ORC|RE||999\r"
          + "RXA|0|1|20180906||998^No Vaccination Administered^CVX\r"
          + "OBX|5|CE|30956-7^Vaccine type^LN|3|45^HepB^CVX\r"
          + "OBX|6|CE|59783-1^Status in series^LN|3|O^O^LSVF\r"
          + "OBX|7|DT|30981-5^Earliest date^LN|3|20180702\r"
          + "OBX|8|DT|30980-7^Recommended date^LN|3|20180702\r"
          + "OBX|9|DT|59778-1^Latest date^LN|3|20180730\r"
          + "OBX|10|CE|30956-7^Vaccine type^LN|4|17^Hib^CVX\r"
          + "OBX|11|CE|59783-1^Status in series^LN|4|D^D^LSVF\r"
          + "OBX|12|DT|30981-5^Earliest date^LN|4|20180813\r"
          + "OBX|13|DT|30980-7^Recommended date^LN|4|20180902\r"
          + "OBX|14|DT|59778-1^Latest date^LN|4|20181002\r"
          + "OBX|15|CE|30956-7^Vaccine type^LN|5|152^Pneumococcal^CVX\r"
          + "OBX|16|CE|59783-1^Status in series^LN|5|D^D^LSVF\r"
          + "OBX|17|DT|30981-5^Earliest date^LN|5|20180813\r"
          + "OBX|18|DT|30980-7^Recommended date^LN|5|20180902\r"
          + "OBX|19|DT|59778-1^Latest date^LN|5|20181002\r"
          + "OBX|20|CE|30956-7^Vaccine type^LN|6|152^PCV^CVX\r"
          + "OBX|21|CE|59783-1^Status in series^LN|6|D^D^LSVF\r"
          + "OBX|22|DT|30981-5^Earliest date^LN|6|20180813\r"
          + "OBX|23|DT|30980-7^Recommended date^LN|6|20180902\r"
          + "OBX|24|DT|59778-1^Latest date^LN|6|20181002\r"
          + "OBX|25|CE|30956-7^Vaccine type^LN|7|89^Polio^CVX\r"
          + "OBX|26|CE|59783-1^Status in series^LN|7|D^D^LSVF\r"
          + "OBX|27|DT|30981-5^Earliest date^LN|7|20180813\r"
          + "OBX|28|DT|30980-7^Recommended date^LN|7|20180902\r"
          + "OBX|29|DT|59778-1^Latest date^LN|7|20181002\r"
          + "OBX|30|CE|30956-7^Vaccine type^LN|8|122^Rotavirus^CVX\r"
          + "OBX|31|CE|59783-1^Status in series^LN|8|D^D^LSVF\r"
          + "OBX|32|DT|30981-5^Earliest date^LN|8|20180813\r"
          + "OBX|33|DT|30980-7^Recommended date^LN|8|20180902\r"
          + "OBX|34|DT|59778-1^Latest date^LN|8|20181002\r"
          + "OBX|35|CE|30956-7^Vaccine type^LN|9|20^DTaP^CVX\r"
          + "OBX|36|CE|59783-1^Status in series^LN|9|L^L^LSVF\r"
          + "OBX|37|DT|30981-5^Earliest date^LN|9|20181008\r"
          + "OBX|38|DT|30980-7^Recommended date^LN|9|20190102\r"
          + "OBX|39|DT|59778-1^Latest date^LN|9|20190202\r"
          + "OBX|40|CE|30956-7^Vaccine type^LN|10|20^DTaP, Tdap or Td^CVX\r"
          + "OBX|41|CE|59783-1^Status in series^LN|10|L^L^LSVF\r"
          + "OBX|42|DT|30981-5^Earliest date^LN|10|20181008\r"
          + "OBX|43|DT|30980-7^Recommended date^LN|10|20190102\r"
          + "OBX|44|DT|59778-1^Latest date^LN|10|20190202\r"
          + "OBX|45|CE|30956-7^Vaccine type^LN|11|03^MMR^CVX\r"
          + "OBX|46|CE|59783-1^Status in series^LN|11|L^L^LSVF\r"
          + "OBX|47|DT|30981-5^Earliest date^LN|11|20190702\r"
          + "OBX|48|DT|30980-7^Recommended date^LN|11|20190702\r"
          + "OBX|49|DT|59778-1^Latest date^LN|11|20191102\r"
          + "OBX|50|CE|30956-7^Vaccine type^LN|12|21^Varicella^CVX\r"
          + "OBX|51|CE|59783-1^Status in series^LN|12|L^L^LSVF\r"
          + "OBX|52|DT|30981-5^Earliest date^LN|12|20190702\r"
          + "OBX|53|DT|30980-7^Recommended date^LN|12|20190702\r"
          + "OBX|54|DT|59778-1^Latest date^LN|12|20191102\r"
          + "OBX|55|CE|30956-7^Vaccine type^LN|13|108^Meningococcal^CVX\r"
          + "OBX|56|CE|59783-1^Status in series^LN|13|L^L^LSVF\r"
          + "OBX|57|DT|30981-5^Earliest date^LN|13|20180827\r"
          + "OBX|58|DT|30980-7^Recommended date^LN|13|20290702\r"
          + "OBX|59|DT|59778-1^Latest date^LN|13|20310702\r"
          + "OBX|60|CE|30956-7^Vaccine type^LN|14|85^HepA^CVX\r"
          + "OBX|61|CE|59783-1^Status in series^LN|14|L^L^LSVF\r"
          + "OBX|62|DT|30981-5^Earliest date^LN|14|20190702\r"
          + "OBX|63|DT|30980-7^Recommended date^LN|14|20190702\r"
          + "OBX|64|DT|59778-1^Latest date^LN|14|20200702\r"
          + "OBX|65|CE|30956-7^Vaccine type^LN|15|88^Influenza^CVX\r"
          + "OBX|66|CE|59783-1^Status in series^LN|15|L^L^LSVF\r"
          + "OBX|67|DT|30981-5^Earliest date^LN|15|20190102\r"
          + "OBX|68|DT|30980-7^Recommended date^LN|15|20190102\r"
          + "OBX|69|DT|59778-1^Latest date^LN|15|20190102\r"
          + "OBX|70|CE|30956-7^Vaccine type^LN|16|137^HPV^CVX\r"
          + "OBX|71|CE|59783-1^Status in series^LN|16|L^L^LSVF\r"
          + "OBX|72|DT|30981-5^Earliest date^LN|16|20270702\r"
          + "OBX|73|DT|30980-7^Recommended date^LN|16|20290702\r"
          + "OBX|74|DT|59778-1^Latest date^LN|16|20310702\r"
          + "OBX|75|CE|30956-7^Vaccine type^LN|17|121^HerpesZoster^CVX\r"
          + "OBX|76|CE|59783-1^Status in series^LN|17|L^L^LSVF\r"
          + "OBX|77|DT|30981-5^Earliest date^LN|17|20680702\r"
          + "OBX|78|DT|30980-7^Recommended date^LN|17|20680702\r"
          + "OBX|79|DT|59778-1^Latest date^LN|17|20690702\r"
          + "OBX|80|CE|30956-7^Vaccine type^LN|18|187^RZV (Shingrix)^CVX\r"
          + "OBX|81|CE|59783-1^Status in series^LN|18|L^L^LSVF\r"
          + "OBX|82|DT|30981-5^Earliest date^LN|18|20680702\r"
          + "OBX|83|DT|30980-7^Recommended date^LN|18|20680702\r"
          + "OBX|84|DT|59778-1^Latest date^LN|18|20690702\r";

  private static final String RSP_AK_VACTRAK_1 = ""
      + "MSH|^~\\&|^^|^^|^^|^^|20180824092236||RSP^K11^RSP_K11|2013560138.100002658|P|2.5.1|||||||||Z42^CDCPHINVS^^|\r"
      + "MSA|AA|u27-QA.2.1.2|\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^HL70471|\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|37374859|K46G60659^^^AIRA^MR|Payne^Jarvia^Damita||20140812|F|L^^Muskegon^MI^49440|\r"
      + "PID|1||2408355^^^^SR~~~~~K46G60659^^^^MR||PAYNE^JARVIA^DAMITA^^^^L|BOHNE|20140812|F|||65 GIBBONS CIR^^MUSKEGON^MICHIGAN^49440^United States^M^^MUSKEGON||2315326723^PRN^PH^^^231^5326723|||||||||2186-5^not Hispanic or Latino^HL70189||N|1|||||N|\r"
      + "ORC|RE||2408355.54.20150823|\r"
      + "RXA|0|999|20150823|20150823|94^MMRV^CVX^90710^MMRV^CPT|999|||01^Historical information - source unspecified^NIP001||^^^BUNKER_FAC1||||||||||A|20180824092236|\r"
      + "RXR|IM^Intramuscular^CDCPHINVS|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20150823|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20150823|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||2408355.54.20180823|\r"
      + "RXA|0|999|20180823|20180823|94^MMRV^CVX^90710^MMRV^CPT|999|||01^Historical information - source unspecified^NIP001||^^^BUNKER_FAC1||||||||||A|20180824092236|\r"
      + "RXR|IM^Intramuscular^CDCPHINVS|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20180823|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20180823|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824092236|20180824092236|998^no vaccine administered^CVX|0||||||||||||||NA||20180824092236|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|45^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824092236|20180824092236|998^no vaccine administered^CVX|0||||||||||||||NA||20180824092236|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|107^DTAP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141012||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140923||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824092236|20180824092236|998^no vaccine administered^CVX|0||||||||||||||NA||20180824092236|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|17^HIB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141012||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140923||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141212||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824092236|20180824092236|998^no vaccine administered^CVX|0||||||||||||||NA||20180824092236|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|133^PneumoPCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141012||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140923||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824092236|20180824092236|998^no vaccine administered^CVX|0||||||||||||||NA||20180824092236|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|89^POLIO^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141012||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140923||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824092236|20180824092236|998^no vaccine administered^CVX|0||||||||||||||NA||20180824092236|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|88^FLU^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150212||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150212||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150314||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824092236|20180824092236|998^no vaccine administered^CVX|0||||||||||||||NA||20180824092236|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|85^HepA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20160812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824092236|20180824092236|998^no vaccine administered^CVX|0||||||||||||||NA||20180824092236|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|137^HPV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20230812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21140812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824092236|20180824092236|998^no vaccine administered^CVX|0||||||||||||||NA||20180824092236|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|147^MENING^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20240812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";


  private static final String RSP_AL_IMMPRINT_1 = ""
      + "MSH|^~\\&|AL-IIS|AL-IIS|1270|9250|20180824122409-0600||RSP^K11^RSP_K11|20180824122409|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|vXj-QA.2.1.2-D\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|F59N60716^^^AIRA^MR|Glasscock^Aba^Yungara^^^^L||20140812|F|317 York Ave^^Webster^MI^48130|\r"
      + "PID|1||F59N60716^^^AIRA^MR~14163941^^^ALA^SR||GLASSCOCK^ABA^^^^^U||20140812|F|||\r"
      + "ORC|RE||20180824^AL-IIS|\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|03^MMR^CVX||||||F|\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|1|NA^NA^HL70353|||||F|\r"
      + "OBX|3|ID|59781-5^Dose Validity^LN|1|Y||||||F|\r"
      + "OBX|4|CE|30956-7^vaccine type^LN|2|21^VARICELLA^CVX||||||F|\r"
      + "OBX|5|NM|30973-2^Dose number in series^LN|2|1|NA^NA^HL70353|||||F|\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|\r" + "ORC|RE||20180824^AL-IIS|\r"
      + "RXA|0|1|20180823|20180823|94^MMRV^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|03^MMR^CVX||||||F|\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|2|NA^NA^HL70353|||||F|\r"
      + "OBX|3|ID|59781-5^Dose Validity^LN|1|Y||||||F|\r"
      + "OBX|4|CE|30956-7^vaccine type^LN|2|21^VARICELLA^CVX||||||F|\r"
      + "OBX|5|NM|30973-2^Dose number in series^LN|2|2|NA^NA^HL70353|||||F|\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|\r" + "ORC|RE||9999^AL-IIS|\r"
      + "RXA|0|1|20180824|20180824|998^No vaccine administered^CVX|999||||||||||||||NA|A|\r"
      + "OBX|1|CE|30979-9^Vaccine due next^LN|1|10^IPV^CVX||||||F|||20180824|\r"
      + "OBX|2|DT|30980-7^Date vaccine due^LN|1|20141012||||||F|||20180824|\r"
      + "OBX|3|NM|30973-2^Vaccine due next dose number^LN|1|1|NA^NA^HL70353|||||F|||20180824|\r"
      + "OBX|4|CE|59783-1^Status in immunization series^LN|1|LA13423-1^Overdue^LN||||||F|\r"
      + "OBX|5|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824|\r"
      + "OBX|6|CE|30979-9^Vaccine due next^LN|2|45^HEP B, UNSPECIFIED FORMULATION^CVX||||||F|||20180824|\r"
      + "OBX|7|DT|30980-7^Date vaccine due^LN|2|20141012||||||F|||20180824|\r"
      + "OBX|8|NM|30973-2^Vaccine due next dose number^LN|2|1|NA^NA^HL70353|||||F|||20180824|\r"
      + "OBX|9|CE|59783-1^Status in immunization series^LN|2|LA13423-1^Overdue^LN||||||F|\r"
      + "OBX|10|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180824|\r"
      + "OBX|11|CE|30979-9^Vaccine due next^LN|3|107^DTAP UNSPECIFIED FORMULATION^CVX||||||F|||20180824|\r"
      + "OBX|12|DT|30980-7^Date vaccine due^LN|3|20141012||||||F|||20180824|\r"
      + "OBX|13|NM|30973-2^Vaccine due next dose number^LN|3|1|NA^NA^HL70353|||||F|||20180824|\r"
      + "OBX|14|CE|59783-1^Status in immunization series^LN|3|LA13423-1^Overdue^LN||||||F|\r"
      + "OBX|15|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180824|\r"
      + "OBX|16|CE|30979-9^Vaccine due next^LN|4|152^PNEUMOCOCCAL CONJUGATE, UNSPECIFIED FORMULATION^CVX||||||F|||20180824|\r"
      + "OBX|17|DT|30980-7^Date vaccine due^LN|4|20141012||||||F|||20180824|\r"
      + "OBX|18|NM|30973-2^Vaccine due next dose number^LN|4|1|NA^NA^HL70353|||||F|||20180824|\r"
      + "OBX|19|CE|59783-1^Status in immunization series^LN|4|LA13423-1^Overdue^LN||||||F|\r"
      + "OBX|20|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180824|\r"
      + "OBX|21|CE|30979-9^Vaccine due next^LN|5|17^HIB, UNSPECIFIED FORMULATION^CVX||||||F|||20180824|\r"
      + "OBX|22|DT|30980-7^Date vaccine due^LN|5|20151112||||||F|||20180824|\r"
      + "OBX|23|NM|30973-2^Vaccine due next dose number^LN|5|4|NA^NA^HL70353|||||F|||20180824|\r"
      + "OBX|24|CE|59783-1^Status in immunization series^LN|5|LA13423-1^Overdue^LN||||||F|\r"
      + "OBX|25|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180824|\r"
      + "OBX|26|CE|30979-9^Vaccine due next^LN|6|147^MENINGOCOCCAL MCV4, UNSPECIFIED FORMULATION^CVX||||||F|||20180824|\r"
      + "OBX|27|DT|30980-7^Date vaccine due^LN|6|20250812||||||F|||20180824|\r"
      + "OBX|28|NM|30973-2^Vaccine due next dose number^LN|6|1|NA^NA^HL70353|||||F|||20180824|\r"
      + "OBX|29|CE|59783-1^Status in immunization series^LN|6|LA13422-3^On schedule^LN||||||F|\r"
      + "OBX|30|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180824|\r"
      + "OBX|31|CE|30979-9^Vaccine due next^LN|7|165^HUMAN PAPILLOMAVIRUS 9-VALENT VACCINE^CVX||||||F|||20180824|\r"
      + "OBX|32|DT|30980-7^Date vaccine due^LN|7|20250812||||||F|||20180824|\r"
      + "OBX|33|NM|30973-2^Vaccine due next dose number^LN|7|1|NA^NA^HL70353|||||F|||20180824|\r"
      + "OBX|34|CE|59783-1^Status in immunization series^LN|7|LA13422-3^On schedule^LN||||||F|\r"
      + "OBX|35|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180824|\r"
      + "OBX|36|CE|30979-9^Vaccine due next^LN|8|33^PNEUMOCOCCAL POLYSACCHARIDE PPV23^CVX||||||F|||20180824|\r"
      + "OBX|37|DT|30980-7^Date vaccine due^LN|8|20790812||||||F|||20180824|\r"
      + "OBX|38|NM|30973-2^Vaccine due next dose number^LN|8|1|NA^NA^HL70353|||||F|||20180824|\r"
      + "OBX|39|CE|59783-1^Status in immunization series^LN|8|LA13422-3^On schedule^LN||||||F|\r"
      + "OBX|40|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180824|\r"
      + "OBX|41|CE|30979-9^Vaccine due next^LN|9|03^MMR^CVX||||||F|||20180824|\r"
      + "OBX|42|CE|59783-1^Status in immunization  series^LN|9|LA13421-5^Not Recommended - Series Complete^LN||||||F|\r"
      + "OBX|43|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180824|\r"
      + "OBX|44|CE|30979-9^Vaccine due next^LN|10|122^ROTAVIRUS, UNSPECIFIED FORMULATION^CVX||||||F|||20180824|\r"
      + "OBX|45|CE|59783-1^Status in immunization  series^LN|10|LA13424-9^Not Recommended - Too old^LN||||||F|\r"
      + "OBX|46|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180824|\r"
      + "OBX|47|CE|30979-9^Vaccine due next^LN|11|21^VARICELLA^CVX||||||F|||20180824|\r"
      + "OBX|48|CE|59783-1^Status in immunization  series^LN|11|LA13421-5^Not Recommended - Series Complete^LN||||||F|\r"
      + "OBX|49|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180824|\r";

  private static final String RSP_AR_WEBIZ_1 = ""
      + "MSH|^~\\&|WebIZ.18.1.20180629|AR0000||AR0112|20180824122349-0500||RSP^K11^RSP_K11|AR000020180824234968|D|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|yyH-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|J90N60777^^^AIRA^MR|Sanders^Vanda^Katherine^^^^L||20140812|F|393 Washington Ln^^Eckerman^MI^49790\r"
      + "PID|1||3509575^^^AR0000^SR~J90N60777^^^AR0112^MR||SANDERS^VANDA^KATHERINE^^^^L|McClean^^^^^^M|20140812|F||2106-3^White^CDCREC|393 WASHINGTON LN^^ECKERMAN^MI^49790^USA^P||^PRN^PH^^^906^8249535|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|||||||||20180824\r"
      + "ORC|RE||35216771^AR0000\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||PR^^^PR|||||||||CP|A|20180824\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20150823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20150823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20150823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20150823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "ORC|RE||35216772^AR0000\r"
      + "RXA|0|1|20180823|20180823|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||PR^^^PR|||||||||CP|A|20180824\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20180823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20180823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20180823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20180823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "ORC|RE||9999^AR0000\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|08^Hep B, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|2|DT|30981-5^Earliest date dose should be given^LN|1|20140812||||||F|||20180824\r"
      + "OBX|3|DT|30980-7^Date Vaccine Due^LN|1|20140812||||||F|||20180824\r"
      + "OBX|4|DT|59777-3^Latest date next dose should be given^LN|1|20330811||||||F|||20180824\r"
      + "OBX|5|DT|59778-1^Date dose is overdue^LN|1|20140908||||||F|||20180824\r"
      + "OBX|6|CE|59783-1^Series Status^LN|1|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|7|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|8|CE|30956-7^Vaccine Type^LN|2|133^PCV13^CVX||||||F|||20180824\r"
      + "OBX|9|DT|30981-5^Earliest date dose should be given^LN|2|20140923||||||F|||20180824\r"
      + "OBX|10|DT|30980-7^Date Vaccine Due^LN|2|20141012||||||F|||20180824\r"
      + "OBX|11|DT|59777-3^Latest date next dose should be given^LN|2|20190811||||||F|||20180824\r"
      + "OBX|12|DT|59778-1^Date dose is overdue^LN|2|20141209||||||F|||20180824\r"
      + "OBX|13|CE|59783-1^Series Status^LN|2|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|14|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|15|CE|30956-7^Vaccine Type^LN|3|10^Polio-IPV^CVX||||||F|||20180824\r"
      + "OBX|16|DT|30981-5^Earliest date dose should be given^LN|3|20140923||||||F|||20180824\r"
      + "OBX|17|DT|30980-7^Date Vaccine Due^LN|3|20141012||||||F|||20180824\r"
      + "OBX|18|DT|59777-3^Latest date next dose should be given^LN|3|20320811||||||F|||20180824\r"
      + "OBX|19|DT|59778-1^Date dose is overdue^LN|3|20141209||||||F|||20180824\r"
      + "OBX|20|CE|59783-1^Series Status^LN|3|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|21|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|22|CE|30956-7^Vaccine Type^LN|4|20^DTaP^CVX||||||F|||20180824\r"
      + "OBX|23|DT|30981-5^Earliest date dose should be given^LN|4|20140923||||||F|||20180824\r"
      + "OBX|24|DT|30980-7^Date Vaccine Due^LN|4|20141012||||||F|||20180824\r"
      + "OBX|25|DT|59778-1^Date dose is overdue^LN|4|20141209||||||F|||20180824\r"
      + "OBX|26|CE|59783-1^Series Status^LN|4|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|27|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|28|CE|30956-7^Vaccine Type^LN|5|48^Hib (PRP-T) (ActHIB)^CVX||||||F|||20180824\r"
      + "OBX|29|DT|30981-5^Earliest date dose should be given^LN|5|20140923||||||F|||20180824\r"
      + "OBX|30|DT|30980-7^Date Vaccine Due^LN|5|20141012||||||F|||20180824\r"
      + "OBX|31|DT|59777-3^Latest date next dose should be given^LN|5|20190811||||||F|||20180824\r"
      + "OBX|32|DT|59778-1^Date dose is overdue^LN|5|20141209||||||F|||20180824\r"
      + "OBX|33|CE|59783-1^Series Status^LN|5|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|34|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|35|CE|30956-7^Vaccine Type^LN|6|83^Hep A, ped/adol, 2D^CVX||||||F|||20180824\r"
      + "OBX|36|DT|30981-5^Earliest date dose should be given^LN|6|20150812||||||F|||20180824\r"
      + "OBX|37|DT|30980-7^Date Vaccine Due^LN|6|20150812||||||F|||20180824\r"
      + "OBX|38|DT|59777-3^Latest date next dose should be given^LN|6|20330811||||||F|||20180824\r"
      + "OBX|39|DT|59778-1^Date dose is overdue^LN|6|20160908||||||F|||20180824\r"
      + "OBX|40|CE|59783-1^Series Status^LN|6|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|41|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|42|CE|30956-7^Vaccine Type^LN|7|141^Influenza, Seasonal^CVX||||||F|||20180824\r"
      + "OBX|43|DT|30981-5^Earliest date dose should be given^LN|7|20180920||||||F|||20180824\r"
      + "OBX|44|DT|30980-7^Date Vaccine Due^LN|7|20180920||||||F|||20180824\r"
      + "OBX|45|CE|59783-1^Series Status^LN|7|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|46|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|47|CE|30956-7^Vaccine Type^LN|8|08^Hep B, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|48|DT|30981-5^Earliest date dose should be given^LN|8|20180921||||||F|||20180824\r"
      + "OBX|49|DT|30980-7^Date Vaccine Due^LN|8|20180921||||||F|||20180824\r"
      + "OBX|50|DT|59778-1^Date dose is overdue^LN|8|20180921||||||F|||20180824\r"
      + "OBX|51|CE|59783-1^Series Status^LN|8|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|52|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|53|CE|30956-7^Vaccine Type^LN|9|10^Polio-IPV^CVX||||||F|||20180824\r"
      + "OBX|54|DT|30981-5^Earliest date dose should be given^LN|9|20180921||||||F|||20180824\r"
      + "OBX|55|DT|30980-7^Date Vaccine Due^LN|9|20180921||||||F|||20180824\r"
      + "OBX|56|DT|59777-3^Latest date next dose should be given^LN|9|20320811||||||F|||20180824\r"
      + "OBX|57|DT|59778-1^Date dose is overdue^LN|9|20180921||||||F|||20180824\r"
      + "OBX|58|CE|59783-1^Series Status^LN|9|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|59|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|60|CE|30956-7^Vaccine Type^LN|10|20^DTaP^CVX||||||F|||20180824\r"
      + "OBX|61|DT|30981-5^Earliest date dose should be given^LN|10|20180921||||||F|||20180824\r"
      + "OBX|62|DT|30980-7^Date Vaccine Due^LN|10|20180921||||||F|||20180824\r"
      + "OBX|63|DT|59778-1^Date dose is overdue^LN|10|20180921||||||F|||20180824\r"
      + "OBX|64|CE|59783-1^Series Status^LN|10|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|65|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|66|CE|30956-7^Vaccine Type^LN|11|83^Hep A, ped/adol, 2D^CVX||||||F|||20180824\r"
      + "OBX|67|DT|30981-5^Earliest date dose should be given^LN|11|20190224||||||F|||20180824\r"
      + "OBX|68|DT|30980-7^Date Vaccine Due^LN|11|20190224||||||F|||20180824\r"
      + "OBX|69|DT|59778-1^Date dose is overdue^LN|11|20190224||||||F|||20180824\r"
      + "OBX|70|CE|59783-1^Series Status^LN|11|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|71|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|72|CE|30956-7^Vaccine Type^LN|12|114^MCV4 (Menactra)^CVX||||||F|||20180824\r"
      + "OBX|73|DT|30981-5^Earliest date dose should be given^LN|12|20250812||||||F|||20180824\r"
      + "OBX|74|DT|30980-7^Date Vaccine Due^LN|12|20250812||||||F|||20180824\r"
      + "OBX|75|DT|59777-3^Latest date next dose should be given^LN|12|20360811||||||F|||20180824\r"
      + "OBX|76|DT|59778-1^Date dose is overdue^LN|12|20270908||||||F|||20180824\r"
      + "OBX|77|CE|59783-1^Series Status^LN|12|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|78|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|79|CE|30956-7^Vaccine Type^LN|13|165^HPV9^CVX||||||F|||20180824\r"
      + "OBX|80|DT|30981-5^Earliest date dose should be given^LN|13|20230812||||||F|||20180824\r"
      + "OBX|81|DT|30980-7^Date Vaccine Due^LN|13|20250812||||||F|||20180824\r"
      + "OBX|82|DT|59777-3^Latest date next dose should be given^LN|13|20410811||||||F|||20180824\r"
      + "OBX|83|DT|59778-1^Date dose is overdue^LN|13|20270908||||||F|||20180824\r"
      + "OBX|84|CE|59783-1^Series Status^LN|13|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|85|CE|59779-9^Immunization Schedule Used^LN|13|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|86|CE|30956-7^Vaccine Type^LN|14|187^Recombinant Zoster^CVX||||||F|||20180824\r"
      + "OBX|87|DT|30981-5^Earliest date dose should be given^LN|14|20640812||||||F|||20180824\r"
      + "OBX|88|DT|30980-7^Date Vaccine Due^LN|14|20640812||||||F|||20180824\r"
      + "OBX|89|CE|59783-1^Series Status^LN|14|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|90|CE|59779-9^Immunization Schedule Used^LN|14|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|91|CE|30956-7^Vaccine Type^LN|15|133^PCV13^CVX||||||F|||20180824\r"
      + "OBX|92|DT|30981-5^Earliest date dose should be given^LN|15|20790812||||||F|||20180824\r"
      + "OBX|93|DT|30980-7^Date Vaccine Due^LN|15|20790812||||||F|||20180824\r"
      + "OBX|94|CE|59783-1^Series Status^LN|15|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|95|CE|59779-9^Immunization Schedule Used^LN|15|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|96|CE|30956-7^Vaccine Type^LN|16|122^Rotavirus, UF^CVX||||||F|||20180824\r"
      + "OBX|97|CE|59783-1^Series Status^LN|16|LA13424-9^Too Old^LN||||||F|||20180824\r"
      + "OBX|98|ST|30982-3^Reason Code^LN|16|Patient has exceeded the maximum age||||||F|||20180824\r"
      + "OBX|99|CE|59779-9^Immunization Schedule Used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|100|CE|30956-7^Vaccine Type^LN|17|03^MMR^CVX||||||F|||20180824\r"
      + "OBX|101|CE|59783-1^Series Status^LN|17|LA13421-5^Complete^LN||||||F|||20180824\r"
      + "OBX|102|ST|30982-3^Reason Code^LN|17|Patient Series is complete||||||F|||20180824\r"
      + "OBX|103|CE|59779-9^Immunization Schedule Used^LN|17|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|104|CE|30956-7^Vaccine Type^LN|18|21^Varicella^CVX||||||F|||20180824\r"
      + "OBX|105|CE|59783-1^Series Status^LN|18|LA13421-5^Complete^LN||||||F|||20180824\r"
      + "OBX|106|ST|30982-3^Reason Code^LN|18|Patient Series is complete||||||F|||20180824\r"
      + "OBX|107|CE|59779-9^Immunization Schedule Used^LN|18|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r";


  private static final String RSP_AZ_ASIIS_1 = ""
      + "MSH|^~\\&|^^|^^|EHRSYSTEM^^|69001^^|20180827115640||RSP^K11^RSP_K11|2002809199.100375088|P|2.5.1|||||||||Z42^CDCPHINVS^^|\r"
      + "MSA|AA|3JKU-QA.2.1.2|\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^HL70471|\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|37374859|L21Y78560^^^AIRA^MR|Lindblom^Smriti^Carensa||20140815|F|L^^Berrien Springs^MI^49103|\r"
      + "PID|1||8668855^^^^SR~~~~~L21Y78560||LINDBLOM^SMRITI^CARENSA^^^^L|HODGE|20140815|F|||9 PENISTON AVE^^BERRIEN SPRINGS^MICHIGAN^49103^United States^M^^BERRIEN||2697227891^PRN^PH^^^269^7227891|||||||||2186-5^not Hispanic or Latino^HL70189||N|1|||||N|\r"
      + "ORC|RE||8668855.54.20150826|\r"
      + "RXA|0|999|20150826|20150826|94^MMRV^CVX^90710^MMRV^CPT~54^MMR/Varicella^STC0292|999|||01^Historical information - source unspecified^NIP001||^^^AIRA AART TESTING^^^^^150 N 18TH AVE^^PHOENIX^AZ^85007||||||||||A|20180827115640|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20150826|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20150826|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||8668855.54.20180826|\r"
      + "RXA|0|999|20180826|20180826|94^MMRV^CVX^90710^MMRV^CPT~54^MMR/Varicella^STC0292|999|||01^Historical information - source unspecified^NIP001||^^^AIRA AART TESTING^^^^^150 N 18TH AVE^^PHOENIX^AZ^85007||||||||||A|20180827115640|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20180826|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20180826|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827115640|20180827115640|998^no vaccine administered^CVX|0||||||||||||||NA||20180827115640|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|45^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827115640|20180827115640|998^no vaccine administered^CVX|0||||||||||||||NA||20180827115640|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|107^DTAP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827115640|20180827115640|998^no vaccine administered^CVX|0||||||||||||||NA||20180827115640|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|17^HIB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141215||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827115640|20180827115640|998^no vaccine administered^CVX|0||||||||||||||NA||20180827115640|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|133^PneumoPCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827115640|20180827115640|998^no vaccine administered^CVX|0||||||||||||||NA||20180827115640|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|89^POLIO^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827115640|20180827115640|998^no vaccine administered^CVX|0||||||||||||||NA||20180827115640|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|88^FLU^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150215||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150215||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150317||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827115640|20180827115640|998^no vaccine administered^CVX|0||||||||||||||NA||20180827115640|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|85^HepA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20160815||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827115640|20180827115640|998^no vaccine administered^CVX|0||||||||||||||NA||20180827115640|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|137^HPV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20230815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21140815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270815||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827115640|20180827115640|998^no vaccine administered^CVX|0||||||||||||||NA||20180827115640|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|147^MENING^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20240815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270815||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";

  private static final String RSP_CA_CAIR2_1 = ""
      + "MSH|^~\\&|CAIR IIS|CAIR IIS||NATHAN|20180824||RSP^K11^RSP_K11|BHt-QA.2.1.2-D|P|2.5.1|||||||||Z32^CDCPHINVS|CAIR IIS|NATHAN\r"
      + "MSA|AA|BHt-QA.2.1.2-D||0||0^Message Accepted^HL70357\r" + "QAK|37374859|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|T02D60891^^^AIRA^MR|Castro^Rae^Zsa-Zsa^^^^L||20140812|F|203 Madison Ln^^Pearl Beach^MI^48001\r"
      + "PID|1||433194^^^ORA^SR~T02D60891^^^ORA^MR||CASTRO^RAE^ZSA-ZSA|DELTA^VAIDA|20140812|F||2106-3|203 MADISON LN^^PEARL BEACH^MI^48001^^H||^PRN^PH^^^810^2757706|||||||||2186-5||N|0\r"
      + "PD1|||||||||||02|N||||A\r"
      + "NK1|1|CASTRO^DELTA^MARION|MTH|203 MADISON LN^^PEARL BEACH^MI^48001^^P|^^PH^^^810^2757706\r"
      + "ORC|RE||13983505||||||||||||||NATHAN\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX|1.0|||01|||||||||||CP\r"
      + "OBX|1|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|3|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|1||||||F\r"
      + "ORC|RE||13983506||||||||||||||NATHAN\r"
      + "RXA|0|1|20180823|20180823|94^MMRV^CVX|1.0|||01|||||||||||CP\r"
      + "OBX|5|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|6|NM|30973-2^Dose number in series^LN|1|2||||||F\r"
      + "OBX|7|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|8|NM|30973-2^Dose number in series^LN|2|2||||||F\r" + "ORC|RE||0\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999\r"
      + "OBX|9|CE|30979-9^Vaccines Due Next^LN|0|107^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r"
      + "OBX|10|TS|30980-7^Date Vaccine Due^LN|0|20141012||||||F\r"
      + "OBX|11|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|12|TS|30981-5^Earliest date to give^LN|0|20140923||||||F\r"
      + "OBX|13|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r"
      + "OBX|14|CE|30979-9^Vaccines Due Next^LN|1|85^HepA^CVX^90730^HepA^CPT||||||F\r"
      + "OBX|15|TS|30980-7^Date Vaccine Due^LN|1|20150812||||||F\r"
      + "OBX|16|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|17|TS|30981-5^Earliest date to give^LN|1|20150812||||||F\r"
      + "OBX|18|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|19|CE|30979-9^Vaccines Due Next^LN|2|45^HepB^CVX^90731^HepB^CPT||||||F\r"
      + "OBX|20|TS|30980-7^Date Vaccine Due^LN|2|20140812||||||F\r"
      + "OBX|21|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|22|TS|30981-5^Earliest date to give^LN|2|20140812||||||F\r"
      + "OBX|23|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|24|CE|30979-9^Vaccines Due Next^LN|3|17^Hib^CVX^90737^Hib^CPT||||||F\r"
      + "OBX|25|TS|30980-7^Date Vaccine Due^LN|3|20151112||||||F\r"
      + "OBX|26|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|27|TS|30981-5^Earliest date to give^LN|3|20151112||||||F\r"
      + "OBX|28|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|29|CE|30979-9^Vaccines Due Next^LN|4|88^Influenza-seasnl^CVX^90724^Influenza-seasnl^CPT||||||F\r"
      + "OBX|30|TS|30980-7^Date Vaccine Due^LN|4|20180901||||||F\r"
      + "OBX|31|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|32|TS|30981-5^Earliest date to give^LN|4|20180212||||||F\r"
      + "OBX|33|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|34|CE|30979-9^Vaccines Due Next^LN|5|133^PneumoConjugate^CVX^90670^PneumoConjugate^CPT||||||F\r"
      + "OBX|35|TS|30980-7^Date Vaccine Due^LN|5|20160811||||||F\r"
      + "OBX|36|NM|30973-2^Vaccine due next dose number^LN|5|1||||||F\r"
      + "OBX|37|TS|30981-5^Earliest date to give^LN|5|20160811||||||F\r"
      + "OBX|38|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|39|CE|30979-9^Vaccines Due Next^LN|6|89^Polio^CVX||||||F\r"
      + "OBX|40|TS|30980-7^Date Vaccine Due^LN|6|20141012||||||F\r"
      + "OBX|41|NM|30973-2^Vaccine due next dose number^LN|6|1||||||F\r"
      + "OBX|42|TS|30981-5^Earliest date to give^LN|6|20140923||||||F\r"
      + "OBX|43|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r";

  private static final String RSP_CT_WIZ_1 = ""
      + "MSH|^~\\&|WebIZ.18.1.20180727|CT0000||AART|20180827145802-0400||RSP^K11^RSP_K11|CT000020180827580206|D|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|41zv-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|C39K78647^^^AIRA^MR|Butte^Dianthe^Mariam^^^^L||20140815|F|44 Iversen Ave^^Daggett^MI^49821\r"
      + "PID|1||867287^^^CT0000^SR~C39K78647^^^AART^MR||BUTTE^DIANTHE^MARIAM^^^^L|Blaine^^^^^^M|20140815|F||2106-3^White^CDCREC|44 IVERSEN AVE^^DAGGETT^MI^49821^USA^P||^PRN^PH^^^906^2695696|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|||||||||20180827\r"
      + "ORC|RE||14368948^CT0000\r"
      + "RXA|0|1|20150826|20150826|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||C0000PR^^^PR|||||||||CP|A|20180827\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20150826\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20150826\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|1|NA^Not Applicable^HL70353|||||F|||20150826\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20150826\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20150826\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20150826\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|1|NA^Not Applicable^HL70353|||||F|||20150826\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20150826\r"
      + "ORC|RE||14368949^CT0000\r"
      + "RXA|0|1|20180826|20180826|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||C0000PR^^^PR|||||||||CP|A|20180827\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20180826\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20180826\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|2|NA^Not Applicable^HL70353|||||F|||20180826\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180826\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20180826\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20180826\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|2|NA^Not Applicable^HL70353|||||F|||20180826\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180826\r"
      + "ORC|RE||9999^CT0000\r"
      + "RXA|0|1|20180827|20180827|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|08^Hep B, ped/adol^CVX||||||F|||20180827\r"
      + "OBX|2|DT|30981-5^Earliest date dose should be given^LN|1|20140815||||||F|||20180827\r"
      + "OBX|3|DT|30980-7^Date Vaccine Due^LN|1|20140815||||||F|||20180827\r"
      + "OBX|4|DT|59777-3^Latest date next dose should be given^LN|1|20330814||||||F|||20180827\r"
      + "OBX|5|DT|59778-1^Date dose is overdue^LN|1|20140911||||||F|||20180827\r"
      + "OBX|6|CE|59783-1^Series Status^LN|1|LA13423-1^Overdue^LN||||||F|||20180827\r"
      + "OBX|7|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|8|CE|30956-7^Vaccine Type^LN|2|133^PCV13^CVX||||||F|||20180827\r"
      + "OBX|9|DT|30981-5^Earliest date dose should be given^LN|2|20140926||||||F|||20180827\r"
      + "OBX|10|DT|30980-7^Date Vaccine Due^LN|2|20141015||||||F|||20180827\r"
      + "OBX|11|DT|59777-3^Latest date next dose should be given^LN|2|20190814||||||F|||20180827\r"
      + "OBX|12|DT|59778-1^Date dose is overdue^LN|2|20141212||||||F|||20180827\r"
      + "OBX|13|CE|59783-1^Series Status^LN|2|LA13423-1^Overdue^LN||||||F|||20180827\r"
      + "OBX|14|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|15|CE|30956-7^Vaccine Type^LN|3|10^Polio-IPV^CVX||||||F|||20180827\r"
      + "OBX|16|DT|30981-5^Earliest date dose should be given^LN|3|20140926||||||F|||20180827\r"
      + "OBX|17|DT|30980-7^Date Vaccine Due^LN|3|20141015||||||F|||20180827\r"
      + "OBX|18|DT|59777-3^Latest date next dose should be given^LN|3|20320814||||||F|||20180827\r"
      + "OBX|19|DT|59778-1^Date dose is overdue^LN|3|20141212||||||F|||20180827\r"
      + "OBX|20|CE|59783-1^Series Status^LN|3|LA13423-1^Overdue^LN||||||F|||20180827\r"
      + "OBX|21|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|22|CE|30956-7^Vaccine Type^LN|4|20^DTaP^CVX||||||F|||20180827\r"
      + "OBX|23|DT|30981-5^Earliest date dose should be given^LN|4|20140926||||||F|||20180827\r"
      + "OBX|24|DT|30980-7^Date Vaccine Due^LN|4|20141015||||||F|||20180827\r"
      + "OBX|25|DT|59778-1^Date dose is overdue^LN|4|20141212||||||F|||20180827\r"
      + "OBX|26|CE|59783-1^Series Status^LN|4|LA13423-1^Overdue^LN||||||F|||20180827\r"
      + "OBX|27|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|28|CE|30956-7^Vaccine Type^LN|5|48^Hib (PRP-T)^CVX||||||F|||20180827\r"
      + "OBX|29|DT|30981-5^Earliest date dose should be given^LN|5|20140926||||||F|||20180827\r"
      + "OBX|30|DT|30980-7^Date Vaccine Due^LN|5|20141015||||||F|||20180827\r"
      + "OBX|31|DT|59777-3^Latest date next dose should be given^LN|5|20190814||||||F|||20180827\r"
      + "OBX|32|DT|59778-1^Date dose is overdue^LN|5|20141212||||||F|||20180827\r"
      + "OBX|33|CE|59783-1^Series Status^LN|5|LA13423-1^Overdue^LN||||||F|||20180827\r"
      + "OBX|34|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|35|CE|30956-7^Vaccine Type^LN|6|83^Hep A, ped/adol, 2D^CVX||||||F|||20180827\r"
      + "OBX|36|DT|30981-5^Earliest date dose should be given^LN|6|20150815||||||F|||20180827\r"
      + "OBX|37|DT|30980-7^Date Vaccine Due^LN|6|20150815||||||F|||20180827\r"
      + "OBX|38|DT|59777-3^Latest date next dose should be given^LN|6|20330814||||||F|||20180827\r"
      + "OBX|39|DT|59778-1^Date dose is overdue^LN|6|20160911||||||F|||20180827\r"
      + "OBX|40|CE|59783-1^Series Status^LN|6|LA13423-1^Overdue^LN||||||F|||20180827\r"
      + "OBX|41|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|42|CE|30956-7^Vaccine Type^LN|7|141^Influenza, Seasonal^CVX||||||F|||20180827\r"
      + "OBX|43|DT|30981-5^Earliest date dose should be given^LN|7|20180923||||||F|||20180827\r"
      + "OBX|44|DT|30980-7^Date Vaccine Due^LN|7|20180923||||||F|||20180827\r"
      + "OBX|45|CE|59783-1^Series Status^LN|7|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|46|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|47|CE|30956-7^Vaccine Type^LN|8|08^Hep B, ped/adol^CVX||||||F|||20180827\r"
      + "OBX|48|DT|30981-5^Earliest date dose should be given^LN|8|20180924||||||F|||20180827\r"
      + "OBX|49|DT|30980-7^Date Vaccine Due^LN|8|20180924||||||F|||20180827\r"
      + "OBX|50|DT|59778-1^Date dose is overdue^LN|8|20180924||||||F|||20180827\r"
      + "OBX|51|CE|59783-1^Series Status^LN|8|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|52|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|53|CE|30956-7^Vaccine Type^LN|9|10^Polio-IPV^CVX||||||F|||20180827\r"
      + "OBX|54|DT|30981-5^Earliest date dose should be given^LN|9|20180924||||||F|||20180827\r"
      + "OBX|55|DT|30980-7^Date Vaccine Due^LN|9|20180924||||||F|||20180827\r"
      + "OBX|56|DT|59777-3^Latest date next dose should be given^LN|9|20320814||||||F|||20180827\r"
      + "OBX|57|DT|59778-1^Date dose is overdue^LN|9|20180924||||||F|||20180827\r"
      + "OBX|58|CE|59783-1^Series Status^LN|9|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|59|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|60|CE|30956-7^Vaccine Type^LN|10|20^DTaP^CVX||||||F|||20180827\r"
      + "OBX|61|DT|30981-5^Earliest date dose should be given^LN|10|20180924||||||F|||20180827\r"
      + "OBX|62|DT|30980-7^Date Vaccine Due^LN|10|20180924||||||F|||20180827\r"
      + "OBX|63|DT|59778-1^Date dose is overdue^LN|10|20180924||||||F|||20180827\r"
      + "OBX|64|CE|59783-1^Series Status^LN|10|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|65|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|66|CE|30956-7^Vaccine Type^LN|11|83^Hep A, ped/adol, 2D^CVX||||||F|||20180827\r"
      + "OBX|67|DT|30981-5^Earliest date dose should be given^LN|11|20190227||||||F|||20180827\r"
      + "OBX|68|DT|30980-7^Date Vaccine Due^LN|11|20190227||||||F|||20180827\r"
      + "OBX|69|DT|59778-1^Date dose is overdue^LN|11|20190227||||||F|||20180827\r"
      + "OBX|70|CE|59783-1^Series Status^LN|11|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|71|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|72|CE|30956-7^Vaccine Type^LN|12|114^MCV4 (Menactra)^CVX||||||F|||20180827\r"
      + "OBX|73|DT|30981-5^Earliest date dose should be given^LN|12|20250815||||||F|||20180827\r"
      + "OBX|74|DT|30980-7^Date Vaccine Due^LN|12|20250815||||||F|||20180827\r"
      + "OBX|75|DT|59777-3^Latest date next dose should be given^LN|12|20360814||||||F|||20180827\r"
      + "OBX|76|DT|59778-1^Date dose is overdue^LN|12|20270911||||||F|||20180827\r"
      + "OBX|77|CE|59783-1^Series Status^LN|12|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|78|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|79|CE|30956-7^Vaccine Type^LN|13|165^HPV9^CVX||||||F|||20180827\r"
      + "OBX|80|DT|30981-5^Earliest date dose should be given^LN|13|20230815||||||F|||20180827\r"
      + "OBX|81|DT|30980-7^Date Vaccine Due^LN|13|20250815||||||F|||20180827\r"
      + "OBX|82|DT|59777-3^Latest date next dose should be given^LN|13|20410814||||||F|||20180827\r"
      + "OBX|83|DT|59778-1^Date dose is overdue^LN|13|20270911||||||F|||20180827\r"
      + "OBX|84|CE|59783-1^Series Status^LN|13|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|85|CE|59779-9^Immunization Schedule Used^LN|13|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|86|CE|30956-7^Vaccine Type^LN|14|187^Recombinant Zoster^CVX||||||F|||20180827\r"
      + "OBX|87|DT|30981-5^Earliest date dose should be given^LN|14|20640815||||||F|||20180827\r"
      + "OBX|88|DT|30980-7^Date Vaccine Due^LN|14|20640815||||||F|||20180827\r"
      + "OBX|89|CE|59783-1^Series Status^LN|14|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|90|CE|59779-9^Immunization Schedule Used^LN|14|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|91|CE|30956-7^Vaccine Type^LN|15|133^PCV13^CVX||||||F|||20180827\r"
      + "OBX|92|DT|30981-5^Earliest date dose should be given^LN|15|20790815||||||F|||20180827\r"
      + "OBX|93|DT|30980-7^Date Vaccine Due^LN|15|20790815||||||F|||20180827\r"
      + "OBX|94|CE|59783-1^Series Status^LN|15|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|95|CE|59779-9^Immunization Schedule Used^LN|15|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|96|CE|30956-7^Vaccine Type^LN|16|122^Rotavirus, UF^CVX||||||F|||20180827\r"
      + "OBX|97|CE|59783-1^Series Status^LN|16|LA13424-9^Too Old^LN||||||F|||20180827\r"
      + "OBX|98|ST|30982-3^Reason Code^LN|16|Patient has exceeded the maximum age||||||F|||20180827\r"
      + "OBX|99|CE|59779-9^Immunization Schedule Used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|100|CE|30956-7^Vaccine Type^LN|17|03^MMR^CVX||||||F|||20180827\r"
      + "OBX|101|CE|59783-1^Series Status^LN|17|LA13421-5^Complete^LN||||||F|||20180827\r"
      + "OBX|102|ST|30982-3^Reason Code^LN|17|Patient Series is complete||||||F|||20180827\r"
      + "OBX|103|CE|59779-9^Immunization Schedule Used^LN|17|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|104|CE|30956-7^Vaccine Type^LN|18|21^Varicella^CVX||||||F|||20180827\r"
      + "OBX|105|CE|59783-1^Series Status^LN|18|LA13421-5^Complete^LN||||||F|||20180827\r"
      + "OBX|106|ST|30982-3^Reason Code^LN|18|Patient Series is complete||||||F|||20180827\r"
      + "OBX|107|CE|59779-9^Immunization Schedule Used^LN|18|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r";

  private static final String RSP_DE_DELVAX_1 = ""
      + "MSH|^~\\&|WebIZ.18.1.20180629|DE0000||DE0003|20180824132628-0400||RSP^K11^RSP_K11|DE000020180824262879|D|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|1buL-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|E96O61066^^^AIRA^MR|Henry^Zabrina^Ulva^^^^L||20140812|F|202 Cochran Cir^^Clifford^MI^48727\r"
      + "PID|1||1019154^^^DE0000^SR~E96O61066^^^DE0003^MR||HENRY^ZABRINA^ULVA^^^^L|Hood^^^^^^M|20140812|F||2106-3^White^CDCREC|202 COCHRAN CIR^^CLIFFORD^MI^48727^USA^P||^PRN^PH^^^989^6171778|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|||||||||20180824\r"
      + "ORC|RE||10507422^DE0000\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||AIRA^^^DE0003^^^^^1155 F ST NW, UNIT #1050^^WASHINGTON^DC^20004|||||||||CP|A|20180824\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20150823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20150823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20150823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20150823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "ORC|RE||10507423^DE0000\r"
      + "RXA|0|1|20180823|20180823|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||AIRA^^^DE0003^^^^^1155 F ST NW, UNIT #1050^^WASHINGTON^DC^20004|||||||||CP|A|20180824\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20180823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20180823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20180823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20180823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "ORC|RE||9999^DE0000\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|08^Hep B, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|2|DT|30981-5^Earliest date dose should be given^LN|1|20140812||||||F|||20180824\r"
      + "OBX|3|DT|30980-7^Date Vaccine Due^LN|1|20140812||||||F|||20180824\r"
      + "OBX|4|DT|59777-3^Latest date next dose should be given^LN|1|20330811||||||F|||20180824\r"
      + "OBX|5|DT|59778-1^Date dose is overdue^LN|1|20140908||||||F|||20180824\r"
      + "OBX|6|CE|59783-1^Series Status^LN|1|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|7|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|8|CE|30956-7^Vaccine Type^LN|2|133^PCV13^CVX||||||F|||20180824\r"
      + "OBX|9|DT|30981-5^Earliest date dose should be given^LN|2|20140923||||||F|||20180824\r"
      + "OBX|10|DT|30980-7^Date Vaccine Due^LN|2|20141012||||||F|||20180824\r"
      + "OBX|11|DT|59777-3^Latest date next dose should be given^LN|2|20190811||||||F|||20180824\r"
      + "OBX|12|DT|59778-1^Date dose is overdue^LN|2|20141209||||||F|||20180824\r"
      + "OBX|13|CE|59783-1^Series Status^LN|2|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|14|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|15|CE|30956-7^Vaccine Type^LN|3|10^Polio-IPV^CVX||||||F|||20180824\r"
      + "OBX|16|DT|30981-5^Earliest date dose should be given^LN|3|20140923||||||F|||20180824\r"
      + "OBX|17|DT|30980-7^Date Vaccine Due^LN|3|20141012||||||F|||20180824\r"
      + "OBX|18|DT|59777-3^Latest date next dose should be given^LN|3|20320811||||||F|||20180824\r"
      + "OBX|19|DT|59778-1^Date dose is overdue^LN|3|20141209||||||F|||20180824\r"
      + "OBX|20|CE|59783-1^Series Status^LN|3|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|21|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|22|CE|30956-7^Vaccine Type^LN|4|20^DTaP^CVX||||||F|||20180824\r"
      + "OBX|23|DT|30981-5^Earliest date dose should be given^LN|4|20140923||||||F|||20180824\r"
      + "OBX|24|DT|30980-7^Date Vaccine Due^LN|4|20141012||||||F|||20180824\r"
      + "OBX|25|DT|59778-1^Date dose is overdue^LN|4|20141209||||||F|||20180824\r"
      + "OBX|26|CE|59783-1^Series Status^LN|4|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|27|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|28|CE|30956-7^Vaccine Type^LN|5|48^Hib (PRP-T)^CVX||||||F|||20180824\r"
      + "OBX|29|DT|30981-5^Earliest date dose should be given^LN|5|20140923||||||F|||20180824\r"
      + "OBX|30|DT|30980-7^Date Vaccine Due^LN|5|20141012||||||F|||20180824\r"
      + "OBX|31|DT|59777-3^Latest date next dose should be given^LN|5|20190811||||||F|||20180824\r"
      + "OBX|32|DT|59778-1^Date dose is overdue^LN|5|20141209||||||F|||20180824\r"
      + "OBX|33|CE|59783-1^Series Status^LN|5|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|34|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|35|CE|30956-7^Vaccine Type^LN|6|83^Hep A, ped/adol, 2D^CVX||||||F|||20180824\r"
      + "OBX|36|DT|30981-5^Earliest date dose should be given^LN|6|20150812||||||F|||20180824\r"
      + "OBX|37|DT|30980-7^Date Vaccine Due^LN|6|20150812||||||F|||20180824\r"
      + "OBX|38|DT|59777-3^Latest date next dose should be given^LN|6|20330811||||||F|||20180824\r"
      + "OBX|39|DT|59778-1^Date dose is overdue^LN|6|20160908||||||F|||20180824\r"
      + "OBX|40|CE|59783-1^Series Status^LN|6|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|41|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|42|CE|30956-7^Vaccine Type^LN|7|141^Influenza, Seasonal^CVX||||||F|||20180824\r"
      + "OBX|43|DT|30981-5^Earliest date dose should be given^LN|7|20180920||||||F|||20180824\r"
      + "OBX|44|DT|30980-7^Date Vaccine Due^LN|7|20180920||||||F|||20180824\r"
      + "OBX|45|CE|59783-1^Series Status^LN|7|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|46|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|47|CE|30956-7^Vaccine Type^LN|8|08^Hep B, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|48|DT|30981-5^Earliest date dose should be given^LN|8|20180921||||||F|||20180824\r"
      + "OBX|49|DT|30980-7^Date Vaccine Due^LN|8|20180921||||||F|||20180824\r"
      + "OBX|50|DT|59778-1^Date dose is overdue^LN|8|20180921||||||F|||20180824\r"
      + "OBX|51|CE|59783-1^Series Status^LN|8|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|52|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|53|CE|30956-7^Vaccine Type^LN|9|10^Polio-IPV^CVX||||||F|||20180824\r"
      + "OBX|54|DT|30981-5^Earliest date dose should be given^LN|9|20180921||||||F|||20180824\r"
      + "OBX|55|DT|30980-7^Date Vaccine Due^LN|9|20180921||||||F|||20180824\r"
      + "OBX|56|DT|59777-3^Latest date next dose should be given^LN|9|20320811||||||F|||20180824\r"
      + "OBX|57|DT|59778-1^Date dose is overdue^LN|9|20180921||||||F|||20180824\r"
      + "OBX|58|CE|59783-1^Series Status^LN|9|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|59|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|60|CE|30956-7^Vaccine Type^LN|10|20^DTaP^CVX||||||F|||20180824\r"
      + "OBX|61|DT|30981-5^Earliest date dose should be given^LN|10|20180921||||||F|||20180824\r"
      + "OBX|62|DT|30980-7^Date Vaccine Due^LN|10|20180921||||||F|||20180824\r"
      + "OBX|63|DT|59778-1^Date dose is overdue^LN|10|20180921||||||F|||20180824\r"
      + "OBX|64|CE|59783-1^Series Status^LN|10|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|65|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|66|CE|30956-7^Vaccine Type^LN|11|83^Hep A, ped/adol, 2D^CVX||||||F|||20180824\r"
      + "OBX|67|DT|30981-5^Earliest date dose should be given^LN|11|20190224||||||F|||20180824\r"
      + "OBX|68|DT|30980-7^Date Vaccine Due^LN|11|20190224||||||F|||20180824\r"
      + "OBX|69|DT|59778-1^Date dose is overdue^LN|11|20190224||||||F|||20180824\r"
      + "OBX|70|CE|59783-1^Series Status^LN|11|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|71|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|72|CE|30956-7^Vaccine Type^LN|12|114^MCV4 (Menactra)^CVX||||||F|||20180824\r"
      + "OBX|73|DT|30981-5^Earliest date dose should be given^LN|12|20250812||||||F|||20180824\r"
      + "OBX|74|DT|30980-7^Date Vaccine Due^LN|12|20250812||||||F|||20180824\r"
      + "OBX|75|DT|59777-3^Latest date next dose should be given^LN|12|20360811||||||F|||20180824\r"
      + "OBX|76|DT|59778-1^Date dose is overdue^LN|12|20270908||||||F|||20180824\r"
      + "OBX|77|CE|59783-1^Series Status^LN|12|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|78|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|79|CE|30956-7^Vaccine Type^LN|13|165^HPV9^CVX||||||F|||20180824\r"
      + "OBX|80|DT|30981-5^Earliest date dose should be given^LN|13|20230812||||||F|||20180824\r"
      + "OBX|81|DT|30980-7^Date Vaccine Due^LN|13|20250812||||||F|||20180824\r"
      + "OBX|82|DT|59777-3^Latest date next dose should be given^LN|13|20410811||||||F|||20180824\r"
      + "OBX|83|DT|59778-1^Date dose is overdue^LN|13|20270908||||||F|||20180824\r"
      + "OBX|84|CE|59783-1^Series Status^LN|13|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|85|CE|59779-9^Immunization Schedule Used^LN|13|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|86|CE|30956-7^Vaccine Type^LN|14|187^Recombinant Zoster^CVX||||||F|||20180824\r"
      + "OBX|87|DT|30981-5^Earliest date dose should be given^LN|14|20640812||||||F|||20180824\r"
      + "OBX|88|DT|30980-7^Date Vaccine Due^LN|14|20640812||||||F|||20180824\r"
      + "OBX|89|CE|59783-1^Series Status^LN|14|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|90|CE|59779-9^Immunization Schedule Used^LN|14|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|91|CE|30956-7^Vaccine Type^LN|15|133^PCV13^CVX||||||F|||20180824\r"
      + "OBX|92|DT|30981-5^Earliest date dose should be given^LN|15|20790812||||||F|||20180824\r"
      + "OBX|93|DT|30980-7^Date Vaccine Due^LN|15|20790812||||||F|||20180824\r"
      + "OBX|94|CE|59783-1^Series Status^LN|15|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|95|CE|59779-9^Immunization Schedule Used^LN|15|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|96|CE|30956-7^Vaccine Type^LN|16|122^Rotavirus, UF^CVX||||||F|||20180824\r"
      + "OBX|97|CE|59783-1^Series Status^LN|16|LA13424-9^Too Old^LN||||||F|||20180824\r"
      + "OBX|98|ST|30982-3^Reason Code^LN|16|Patient has exceeded the maximum age||||||F|||20180824\r"
      + "OBX|99|CE|59779-9^Immunization Schedule Used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|100|CE|30956-7^Vaccine Type^LN|17|03^MMR^CVX||||||F|||20180824\r"
      + "OBX|101|CE|59783-1^Series Status^LN|17|LA13421-5^Complete^LN||||||F|||20180824\r"
      + "OBX|102|ST|30982-3^Reason Code^LN|17|Patient Series is complete||||||F|||20180824\r"
      + "OBX|103|CE|59779-9^Immunization Schedule Used^LN|17|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|104|CE|30956-7^Vaccine Type^LN|18|21^Varicella^CVX||||||F|||20180824\r"
      + "OBX|105|CE|59783-1^Series Status^LN|18|LA13421-5^Complete^LN||||||F|||20180824\r"
      + "OBX|106|ST|30982-3^Reason Code^LN|18|Patient Series is complete||||||F|||20180824\r"
      + "OBX|107|CE|59779-9^Immunization Schedule Used^LN|18|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r";

  private static String RSP_ENVISION =
      "MSH|^~\\&|WebIZ.18.1.20180629|NV0000||NV1001|20180914122905-0700||RSP^K11^RSP_K11|NV000020180914290578|D|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
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
          + "ORC|RE||9999^NV0000\r"
          + "RXA|0|1|20180914|20180914|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
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

  private static final String RSP_ENVISION_DEV_1 = ""
      + "MSH|^~\\&||EnvisionAlpha||AART|20170224071046-0007||RSP^K11^RSP_K11|EnvisionAlpha20170224104652|D|2.5.1|||NE|NE|||||Z42^CDCPHINVS|EnvisionAlpha|AART\r"
      + "MSA|AA|2.1.2\r" + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|P83Z5^^^AIRA^MR|Garfield^Orelia^Zelda^^^^L||20130212|F|258 Pocahontas Ln^^Sumner^MI^48889\r"
      + "PID|1||570467^^^EnvisionAlpha^SR~P83Z5^^^AART^MR||GARFIELD^ORELIA^ZELDA^^^^L|La Paz^^^^^^M|20130212|F||2106-3^White^CDCREC|258 POCAHONTAS LN^^SUMNER^MI^48889^USA^P||^PRN^PH^^^989^4992693|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|||||||||20170224\r"
      + "ORC|RE||987622^EnvisionAlpha\r"
      + "RXA|0|1|20140223|20140223|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||EASTSIDE^^^AART^^^^^123 ANY^^REDMOND^WA^98072|||||||||CP|A|20170224\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20140223\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20140223\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|1|NA^Not Applicable^HL70353|||||F|||20140223\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20140223\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20140223\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20140223\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|1|NA^Not Applicable^HL70353|||||F|||20140223\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20140223\r"
      + "ORC|RE||987623^EnvisionAlpha\r"
      + "RXA|0|1|20170223|20170223|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||PROLD^^^PR|||||||||CP|A|20170224\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20170223\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20170223\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|2|NA^Not Applicable^HL70353|||||F|||20170223\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20170223\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20170223\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20170223\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|2|NA^Not Applicable^HL70353|||||F|||20170223\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20170223\r"
      + "ORC|RE||9999^EnvisionAlpha\r"
      + "RXA|0|1|20170224|20170224|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|08^Hep B, ped/adol^CVX||||||F|||20170224\r"
      + "OBX|2|TS|30980-7^Date Vaccine Due^LN|1|20170224||||||F|||20170224\r"
      + "OBX|3|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|4|CE|30956-7^Vaccine Type^LN|2|10^Polio-IPV^CVX||||||F|||20170224\r"
      + "OBX|5|TS|30980-7^Date Vaccine Due^LN|2|20170224||||||F|||20170224\r"
      + "OBX|6|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|7|CE|30956-7^Vaccine Type^LN|3|20^DTaP^CVX||||||F|||20170224\r"
      + "OBX|8|TS|30980-7^Date Vaccine Due^LN|3|20170224||||||F|||20170224\r"
      + "OBX|9|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|10|CE|30956-7^Vaccine Type^LN|4|48^Hib (PRP-T)^CVX||||||F|||20170224\r"
      + "OBX|11|TS|30980-7^Date Vaccine Due^LN|4|20170224||||||F|||20170224\r"
      + "OBX|12|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|13|CE|30956-7^Vaccine Type^LN|5|133^PCV13^CVX||||||F|||20170224\r"
      + "OBX|14|TS|30980-7^Date Vaccine Due^LN|5|20170224||||||F|||20170224\r"
      + "OBX|15|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|16|CE|30956-7^Vaccine Type^LN|6|83^Hep A, ped/adol, 2D^CVX||||||F|||20170224\r"
      + "OBX|17|TS|30980-7^Date Vaccine Due^LN|6|20170224||||||F|||20170224\r"
      + "OBX|18|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|19|CE|30956-7^Vaccine Type^LN|7|141^Influenza, Seasonal^CVX||||||F|||20170224\r"
      + "OBX|20|TS|30980-7^Date Vaccine Due^LN|7|20170323||||||F|||20170224\r"
      + "OBX|21|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|22|CE|30956-7^Vaccine Type^LN|8|08^Hep B, ped/adol^CVX||||||F|||20170224\r"
      + "OBX|23|TS|30980-7^Date Vaccine Due^LN|8|20170324||||||F|||20170224\r"
      + "OBX|24|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|25|CE|30956-7^Vaccine Type^LN|9|10^Polio-IPV^CVX||||||F|||20170224\r"
      + "OBX|26|TS|30980-7^Date Vaccine Due^LN|9|20170324||||||F|||20170224\r"
      + "OBX|27|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|28|CE|30956-7^Vaccine Type^LN|10|20^DTaP^CVX||||||F|||20170224\r"
      + "OBX|29|TS|30980-7^Date Vaccine Due^LN|10|20170324||||||F|||20170224\r"
      + "OBX|30|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|31|CE|30956-7^Vaccine Type^LN|11|83^Hep A, ped/adol, 2D^CVX||||||F|||20170224\r"
      + "OBX|32|TS|30980-7^Date Vaccine Due^LN|11|20170824||||||F|||20170224\r"
      + "OBX|33|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|34|CE|30956-7^Vaccine Type^LN|12|114^MCV4P (Menactra)^CVX||||||F|||20170224\r"
      + "OBX|35|TS|30980-7^Date Vaccine Due^LN|12|20240212||||||F|||20170224\r"
      + "OBX|36|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r"
      + "OBX|37|CE|30956-7^Vaccine Type^LN|13|165^HPV9^CVX||||||F|||20170224\r"
      + "OBX|38|TS|30980-7^Date Vaccine Due^LN|13|20240212||||||F|||20170224\r"
      + "OBX|39|CE|59779-9^Immunization Schedule Used^LN|13|VXC16^ACIP^CDCPHINVS||||||F|||20170224\r";

  private static final String RSP_FL_SHOTS_1 = ""
      + "MSH|^~\\&||||DMC53427|20180824132654.204-0400||RSP^K11^RSP_K11|36101|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|1dai-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|R55O61123^^^AIRA^MR|Pike^Lois^Filomene^^^^L||20140812|F|222 Hardin Ln^^Standale^MI^49534\r"
      + "PID|1||6900149377^^^FLSHOTS^SR~R55O61123^^^DMC53427^MR||PIKE^LOIS^FILOMENE||20140812|F||2106-3^WHITE^HL70005|222 HARDIN LN^^STANDALE^MI^49534^^H||^PRN^^^^616^6268480|||||||||||N\r"
      + "ORC|RE||6900149377.50.20150823.1^FLSHOTS\r"
      + "RXA|0|1|20150823||94^MMRV^CVX|999|||01^Historical Information - source unspecified^NIP001\r"
      + "OBX|1|CE|59780-7^Series Name^99FLS|1|VZV^CHICKEN POX||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|3|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|4|CE|59780-7^Series Name^99FLS|2|MEASLES^MEASLES||||||F\r"
      + "OBX|5|NM|30973-2^Dose number in series^LN|2|1||||||F\r"
      + "OBX|6|ID|59781-5^Dose validity^LN|2|Y||||||F\r"
      + "OBX|7|CE|59780-7^Series Name^99FLS|3|MUMPS^MUMPS||||||F\r"
      + "OBX|8|NM|30973-2^Dose number in series^LN|3|1||||||F\r"
      + "OBX|9|ID|59781-5^Dose validity^LN|3|Y||||||F\r"
      + "OBX|10|CE|59780-7^Series Name^99FLS|4|RUBELLA^RUBELLA||||||F\r"
      + "OBX|11|NM|30973-2^Dose number in series^LN|4|1||||||F\r"
      + "OBX|12|ID|59781-5^Dose validity^LN|4|Y||||||F\r"
      + "ORC|RE||6900149377.50.20180823.1^FLSHOTS\r"
      + "RXA|0|1|20180823||94^MMRV^CVX|999|||01^Historical Information - source unspecified^NIP001\r"
      + "OBX|1|CE|59780-7^Series Name^99FLS|1|VZV^CHICKEN POX||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|2||||||F\r"
      + "OBX|3|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|4|CE|59780-7^Series Name^99FLS|2|MEASLES^MEASLES||||||F\r"
      + "OBX|5|NM|30973-2^Dose number in series^LN|2|2||||||F\r"
      + "OBX|6|ID|59781-5^Dose validity^LN|2|Y||||||F\r"
      + "OBX|7|CE|59780-7^Series Name^99FLS|3|MUMPS^MUMPS||||||F\r"
      + "OBX|8|NM|30973-2^Dose number in series^LN|3|2||||||F\r"
      + "OBX|9|ID|59781-5^Dose validity^LN|3|Y||||||F\r"
      + "OBX|10|CE|59780-7^Series Name^99FLS|4|RUBELLA^RUBELLA||||||F\r"
      + "OBX|11|NM|30973-2^Dose number in series^LN|4|2||||||F\r"
      + "OBX|12|ID|59781-5^Dose validity^LN|4|Y||||||F\r"
      + "ORC|RE||6900149377.0.20180824^FLSHOTS\r"
      + "RXA|0|1|20180824||998^No vaccine administered^CVX|999\r"
      + "OBX|1|CE|59780-7^Series Name^99FLS|1|HEP B^HEPATITIS B||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|3|DT|30981-5^Minimum Due Date^LN|1|20140812||||||F\r"
      + "OBX|4|DT|30980-7^Recommended Due Date^LN|1|20140812||||||F\r"
      + "OBX|5|CE|59783-1^Status in immunization series^99FLS|1|Overdue^Overdue||||||F\r"
      + "OBX|6|CE|59780-7^Series Name^99FLS|2|HIB^H INFLUENZA TYPE B||||||F\r"
      + "OBX|7|NM|30973-2^Dose number in series^LN|2|1||||||F\r"
      + "OBX|8|DT|30981-5^Minimum Due Date^LN|2|20151112||||||F\r"
      + "OBX|9|DT|30980-7^Recommended Due Date^LN|2|20151112||||||F\r"
      + "OBX|10|CE|59783-1^Status in immunization series^99FLS|2|Overdue^Overdue||||||F\r"
      + "OBX|11|CE|59780-7^Series Name^99FLS|3|POLIO^POLIO||||||F\r"
      + "OBX|12|NM|30973-2^Dose number in series^LN|3|1||||||F\r"
      + "OBX|13|DT|30981-5^Minimum Due Date^LN|3|20140923||||||F\r"
      + "OBX|14|DT|30980-7^Recommended Due Date^LN|3|20141012||||||F\r"
      + "OBX|15|CE|59783-1^Status in immunization series^99FLS|3|Overdue^Overdue||||||F\r"
      + "OBX|16|CE|59780-7^Series Name^99FLS|4|VZV^CHICKEN POX||||||F\r"
      + "OBX|17|CE|59783-1^Status in immunization series^99FLS|4|Complete^Complete||||||F\r"
      + "OBX|18|CE|59780-7^Series Name^99FLS|5|PNEUCON^PNEUMOCOCCAL CONJUGATE||||||F\r"
      + "OBX|19|NM|30973-2^Dose number in series^LN|5|1||||||F\r"
      + "OBX|20|DT|30981-5^Minimum Due Date^LN|5|20160812||||||F\r"
      + "OBX|21|DT|30980-7^Recommended Due Date^LN|5|20160812||||||F\r"
      + "OBX|22|CE|59783-1^Status in immunization series^99FLS|5|Overdue^Overdue||||||F\r"
      + "OBX|23|CE|59780-7^Series Name^99FLS|6|MEASLES^MEASLES||||||F\r"
      + "OBX|24|CE|59783-1^Status in immunization series^99FLS|6|Complete^Complete||||||F\r"
      + "OBX|25|CE|59780-7^Series Name^99FLS|7|MUMPS^MUMPS||||||F\r"
      + "OBX|26|CE|59783-1^Status in immunization series^99FLS|7|Complete^Complete||||||F\r"
      + "OBX|27|CE|59780-7^Series Name^99FLS|8|RUBELLA^RUBELLA||||||F\r"
      + "OBX|28|CE|59783-1^Status in immunization series^99FLS|8|Complete^Complete||||||F\r"
      + "OBX|29|CE|59780-7^Series Name^99FLS|9|ROTAVIRUS^ROTAVIRUS||||||F\r"
      + "OBX|30|CE|59783-1^Status in immunization series^99FLS|9|Complete^Complete||||||F\r"
      + "OBX|31|CE|59780-7^Series Name^99FLS|10|HPV^HPV||||||F\r"
      + "OBX|32|NM|30973-2^Dose number in series^LN|10|1||||||F\r"
      + "OBX|33|DT|30981-5^Minimum Due Date^LN|10|20230812||||||F\r"
      + "OBX|34|DT|30980-7^Recommended Due Date^LN|10|20250812||||||F\r"
      + "OBX|35|CE|59783-1^Status in immunization series^99FLS|10|Up to Date^Up to Date||||||F\r"
      + "OBX|36|CE|59780-7^Series Name^99FLS|11|DIPHTHERIA^DIPHTHERIA||||||F\r"
      + "OBX|37|NM|30973-2^Dose number in series^LN|11|1||||||F\r"
      + "OBX|38|DT|30981-5^Minimum Due Date^LN|11|20140923||||||F\r"
      + "OBX|39|DT|30980-7^Recommended Due Date^LN|11|20141012||||||F\r"
      + "OBX|40|CE|59783-1^Status in immunization series^99FLS|11|Overdue^Overdue||||||F\r"
      + "OBX|41|CE|59780-7^Series Name^99FLS|12|PERTUSSIS^PERTUSSIS||||||F\r"
      + "OBX|42|NM|30973-2^Dose number in series^LN|12|1||||||F\r"
      + "OBX|43|DT|30981-5^Minimum Due Date^LN|12|20140923||||||F\r"
      + "OBX|44|DT|30980-7^Recommended Due Date^LN|12|20141012||||||F\r"
      + "OBX|45|CE|59783-1^Status in immunization series^99FLS|12|Overdue^Overdue||||||F\r"
      + "OBX|46|CE|59780-7^Series Name^99FLS|13|TETANUS^TETANUS||||||F\r"
      + "OBX|47|NM|30973-2^Dose number in series^LN|13|1||||||F\r"
      + "OBX|48|DT|30981-5^Minimum Due Date^LN|13|20140923||||||F\r"
      + "OBX|49|DT|30980-7^Recommended Due Date^LN|13|20141012||||||F\r"
      + "OBX|50|CE|59783-1^Status in immunization series^99FLS|13|Overdue^Overdue||||||F\r";

  private static final String RSP_GA_GRITS_1 = ""
      + "MSH|^~\\&|GRITS|GRITS||AIRA|20180824132713.251||RSP^K11^RSP_K11|1eID-QA.2.1.2-D|P|2.5.1|||||||||Z32^CDCPHINVS\r"
      + "MSA|AA|1eID-QA.2.1.2-D||0||0^Message Accepted^HL70357\r"
      + "QAK|1eID-QA.2.1.2-D|OK|Z44|1|5\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|I44D61180^^^AIRA^MR|Sykes^Lesh^Vanja^^^^L||20140812|F|215 Nelsen Ave^^Grosse Pointe Woods^MI^48230\r"
      + "PID|1||10119814^^^^SR~I44D61180^^^^PI||SYKES^LESH^VANJA|HOGG^LYDIA|20140812|F||2106-3|215 NELSEN AVE^^GROSSE POINTE WOODS^MI^48230^^L||^^PH^^^313^8155983|||||||||2186-5||N|0\r"
      + "PD1|||||||||||02|N||||A\r"
      + "NK1|1|SYKES^HOGG^MARION|MTH|215 NELSEN AVE^^GROSSE POINTE WOODS^MI^48230^^M|^^PH^^^313^8155983\r"
      + "ORC|RE||94744845\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX^90710^PROQUAD under 13Y^CPT|1.0|||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN||V01^^GRITS||||||F\r" + "ORC|RE||94744846\r"
      + "RXA|0|1|20180823|20180823|94^MMRV^CVX^90710^PROQUAD under 13Y^CPT|1.0|||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN||V01^^GRITS||||||F\r" + "ORC|RE||0\r"
      + "RXA|0|1|20140812|20140812|998^No Vaccine Administered^CVX|999\r"
      + "OBX|1|CE|30979-9^Vaccines Due Next^LN|1|107^DTaP-Unspecified^CVX||||||F\r"
      + "OBX|2|TS|30980-7^Date Vaccine Due^LN|1|20141012||||||F\r"
      + "OBX|3|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|4|TS|30981-5^Earliest date to give^LN|1|20140923||||||F\r"
      + "OBX|5|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|6|CE|30979-9^Vaccines Due Next^LN|2|85^HepA-Unspecified^CVX||||||F\r"
      + "OBX|7|TS|30980-7^Date Vaccine Due^LN|2|20150812||||||F\r"
      + "OBX|8|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|9|TS|30981-5^Earliest date to give^LN|2|20150812||||||F\r"
      + "OBX|10|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|11|CE|30979-9^Vaccines Due Next^LN|3|45^HepB-Unspecified^CVX||||||F\r"
      + "OBX|12|TS|30980-7^Date Vaccine Due^LN|3|20140812||||||F\r"
      + "OBX|13|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|14|TS|30981-5^Earliest date to give^LN|3|20140812||||||F\r"
      + "OBX|15|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|16|CE|30979-9^Vaccines Due Next^LN|4|17^Hib-Unspecified^CVX||||||F\r"
      + "OBX|17|TS|30980-7^Date Vaccine Due^LN|4|20141012||||||F\r"
      + "OBX|18|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|19|TS|30981-5^Earliest date to give^LN|4|20140923||||||F\r"
      + "OBX|20|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|21|CE|30979-9^Vaccines Due Next^LN|5|88^FLU-Unspecified^CVX||||||F\r"
      + "OBX|22|TS|30980-7^Date Vaccine Due^LN|5|20150212||||||F\r"
      + "OBX|23|NM|30973-2^Vaccine due next dose number^LN|5|1||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|5|20150212||||||F\r"
      + "OBX|25|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|26|CE|30979-9^Vaccines Due Next^LN|6|109^Pneumococcal-Unspecified^CVX||||||F\r"
      + "OBX|27|TS|30980-7^Date Vaccine Due^LN|6|20141012||||||F\r"
      + "OBX|28|NM|30973-2^Vaccine due next dose number^LN|6|1||||||F\r"
      + "OBX|29|TS|30981-5^Earliest date to give^LN|6|20140923||||||F\r"
      + "OBX|30|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r"
      + "OBX|31|CE|30979-9^Vaccines Due Next^LN|7|89^Polio-Unspecified^CVX||||||F\r"
      + "OBX|32|TS|30980-7^Date Vaccine Due^LN|7|20141012||||||F\r"
      + "OBX|33|NM|30973-2^Vaccine due next dose number^LN|7|1||||||F\r"
      + "OBX|34|TS|30981-5^Earliest date to give^LN|7|20140923||||||F\r"
      + "OBX|35|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|7|ACIP schedule||||||F\r";


  private static final String RSP_HI_HIR_1 = ""
      + "MSH|^~\\&|HIR|HIR||AIRA|20180509054215||RSP^K11^RSP_K11|SoX-QA.2.1.2-D|P|2.5.1|||||||||Z32^CDCPHINVS\r"
      + "MSA|AA|SoX-QA.2.1.2-D||0||0^Message Accepted^HL70357\r" + "QAK|SoX-QA.2.1.2-D|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|J72Q45530^^^AIRA^MR|Laen^Sevilla^Brielle^^^^L||20140427|F|168 Ganes Cir^^Franklin^MI^48025\r"
      + "PID|1||246137^^^^SR~J72Q45530^^^^PI||LAEN^SEVILLA^BRIELLE|MARTIN^WINTER|20140427|F||2106-3|168 GANES CIR^^FRANKLIN^MI^48025^^L||^PRN^^^^248^2097836|||||||||2186-5||N|0\r"
      + "PD1|||||||||||02|N||||A\r"
      + "NK1|1|LAEN^MARTIN^MARION|MTH|168 GANES CIR^^FRANKLIN^MI^48025^^M|^PRN^PH^^^248^2097836\r"
      + "ORC|RE||558380\r" + "RXA|0|1|20150508|20150508|94^Proquad^CVX^90710^Proquad^CPT|1.0|||01\r"
      + "OBX|1|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|3|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|1||||||F\r" + "ORC|RE||558381\r"
      + "RXA|0|1|20180508|20180508|94^Proquad^CVX^90710^Proquad^CPT|1.0|||01\r"
      + "OBX|1|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|2||||||F\r"
      + "OBX|3|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|2||||||F\r" + "ORC|RE||0\r"
      + "RXA|0|1|20140427|20140427|998^No Vaccine Administered^CVX|999\r"
      + "OBX|1|CE|30979-9^Vaccines Due Next^LN|0|20^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r"
      + "OBX|2|TS|30980-7^Date Vaccine Due^LN|0|20140627||||||F\r"
      + "OBX|3|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|4|TS|30981-5^Earliest date to give^LN|0|20140608||||||F\r"
      + "OBX|5|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r"
      + "OBX|6|CE|30979-9^Vaccines Due Next^LN|1|85^HepA^CVX^90730^HepA^CPT||||||F\r"
      + "OBX|7|TS|30980-7^Date Vaccine Due^LN|1|20150427||||||F\r"
      + "OBX|8|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|9|TS|30981-5^Earliest date to give^LN|1|20150427||||||F\r"
      + "OBX|10|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|11|CE|30979-9^Vaccines Due Next^LN|2|45^HepB^CVX^90731^HepB^CPT||||||F\r"
      + "OBX|12|TS|30980-7^Date Vaccine Due^LN|2|20140427||||||F\r"
      + "OBX|13|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|14|TS|30981-5^Earliest date to give^LN|2|20140427||||||F\r"
      + "OBX|15|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|16|CE|30979-9^Vaccines Due Next^LN|3|17^Hib^CVX^90737^Hib^CPT||||||F\r"
      + "OBX|17|TS|30980-7^Date Vaccine Due^LN|3|20150727||||||F\r"
      + "OBX|18|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|19|TS|30981-5^Earliest date to give^LN|3|20150727||||||F\r"
      + "OBX|20|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|21|CE|30979-9^Vaccines Due Next^LN|4|88^Influenza^CVX^90724^Influenza^CPT||||||F\r"
      + "OBX|22|TS|30980-7^Date Vaccine Due^LN|4|20141027||||||F\r"
      + "OBX|23|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|4|20141027||||||F\r"
      + "OBX|25|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|26|CE|30979-9^Vaccines Due Next^LN|5|||||||F\r"
      + "OBX|27|TS|30980-7^Date Vaccine Due^LN|5|20140627||||||F\r"
      + "OBX|28|NM|30973-2^Vaccine due next dose number^LN|5|1||||||F\r"
      + "OBX|29|TS|30981-5^Earliest date to give^LN|5|20140608||||||F\r"
      + "OBX|30|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|31|CE|30979-9^Vaccines Due Next^LN|6|||||||F\r"
      + "OBX|32|TS|30980-7^Date Vaccine Due^LN|6|20140627||||||F\r"
      + "OBX|33|NM|30973-2^Vaccine due next dose number^LN|6|1||||||F\r"
      + "OBX|34|TS|30981-5^Earliest date to give^LN|6|20140608||||||F\r"
      + "OBX|35|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r";

  private static final String RSP_IA_IRIS_1 = ""
      + "MSH|^~\\&|IRIS IIS|IRIS||12908|20180828||RSP^K11^RSP_K11|4bR7-QA.2.1.2-D|P|2.5.1|||||||||Z32^CDCPHINVS\r"
      + "MSA|AA|4bR7-QA.2.1.2-D||0||0^Message Accepted^HL70357\r" + "QAK|4bR7-QA.2.1.2-D|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|M04G92621^^^AIRA^MR|Carter^Eustacia^Sadhana^^^^L||20140816|F|156 Somerset Ln^^Ferndale^MI^48220\r"
      + "PID|1||1154820^^^^SR~M04G92621^^^^PI||CARTER^EUSTACIA^SADHANA^^^^L|YAVAPAI^KEYANNA|20140816|F||2106-3|156 SOMERSET LN^^FERNDALE^MI^48220^^L||^PRN^PH^^^248^3786000|||||||||2186-5||N|0\r"
      + "PD1|||||||||||02|N||||A\r"
      + "NK1|1|CARTER^YAVAPAI^MARION|MTH|156 SOMERSET LN^^FERNDALE^MI^48220^^M|^PRN^PH^^^248^3786000\r"
      + "ORC|RE||10778391\r" + "RXA|0|1|20180827|20180827|94^MMRV^CVX|1.0|||01\r"
      + "OBX|1|CE|30956-7^COMPONENT VACCINE TYPE^LN|1|94^MMRV^CVX^90710^MMRV^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|2||||||F\r"
      + "OBX|3|CE|30956-7^COMPONENT VACCINE TYPE^LN|2|94^MMRV^CVX^90710^MMRV^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|2||||||F\r" + "ORC|RE||10778390\r"
      + "RXA|0|1|20150827|20150827|94^MMRV^CVX|1.0|||01\r"
      + "OBX|1|CE|30956-7^COMPONENT VACCINE TYPE^LN|1|94^MMRV^CVX^90710^MMRV^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|3|CE|30956-7^COMPONENT VACCINE TYPE^LN|2|94^MMRV^CVX^90710^MMRV^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|1||||||F\r" + "ORC|RE||0\r"
      + "RXA|0|1|20140816|20140816|998^No Vaccine Administered^CVX|999\r"
      + "OBX|1|CE|30979-9^Vaccines Due Next^LN|0|107^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r"
      + "OBX|2|TS|30980-7^Date Vaccine Due^LN|0|20141016||||||F\r"
      + "OBX|3|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|4|TS|30981-5^Earliest date to give^LN|0|20140927||||||F\r"
      + "OBX|5|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r"
      + "OBX|6|CE|30979-9^Vaccines Due Next^LN|1|85^HepA^CVX^90730^HepA^CPT||||||F\r"
      + "OBX|7|TS|30980-7^Date Vaccine Due^LN|1|20150816||||||F\r"
      + "OBX|8|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|9|TS|30981-5^Earliest date to give^LN|1|20150816||||||F\r"
      + "OBX|10|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|11|CE|30979-9^Vaccines Due Next^LN|2|45^HEPB, unspecified formulation^CVX||||||F\r"
      + "OBX|12|TS|30980-7^Date Vaccine Due^LN|2|20140816||||||F\r"
      + "OBX|13|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|14|TS|30981-5^Earliest date to give^LN|2|20140816||||||F\r"
      + "OBX|15|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|16|CE|30979-9^Vaccines Due Next^LN|3|17^Hib, unspecified formulation^CVX||||||F\r"
      + "OBX|17|TS|30980-7^Date Vaccine Due^LN|3|20151116||||||F\r"
      + "OBX|18|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|19|TS|30981-5^Earliest date to give^LN|3|20151116||||||F\r"
      + "OBX|20|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|21|CE|30979-9^Vaccines Due Next^LN|4|88^Influenza-seasnl^CVX^90724^Influenza-seasnl^CPT||||||F\r"
      + "OBX|22|TS|30980-7^Date Vaccine Due^LN|4|20180701||||||F\r"
      + "OBX|23|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|4|20150216||||||F\r"
      + "OBX|25|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|26|CE|30979-9^Vaccines Due Next^LN|5|100^PneumoConjugate^CVX^90669^PneumoConjugate^CPT||||||F\r"
      + "OBX|27|TS|30980-7^Date Vaccine Due^LN|5|20160816||||||F\r"
      + "OBX|28|NM|30973-2^Vaccine due next dose number^LN|5|4||||||F\r"
      + "OBX|29|TS|30981-5^Earliest date to give^LN|5|20160816||||||F\r"
      + "OBX|30|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|31|CE|30979-9^Vaccines Due Next^LN|6|89^Polio^CVX||||||F\r"
      + "OBX|32|TS|30980-7^Date Vaccine Due^LN|6|20141016||||||F\r"
      + "OBX|33|NM|30973-2^Vaccine due next dose number^LN|6|1||||||F\r"
      + "OBX|34|TS|30981-5^Earliest date to give^LN|6|20140927||||||F\r"
      + "OBX|35|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r";

  private static final String RSP_ID_IRIS_1 = ""
      + "MSH|^~\\&|IRIS2.4|IRIS||P58605|20180824112732.998||RSP^K11^RSP_K11|1gCL-QA.2.1.2|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS|IRIS|P58605\r"
      + "MSA|AA|1gCL-QA.2.1.2||0\r"
      + "ERR||QPD^1^8^1^7|101^Required field missing^HL70357|W||||Information error - Required field QPD-8.7 missing.\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|C74G61237^^^IDA^MR|Stark^Calantha^Druella^^^^L||201408120000|F|276 Jantti Pl^^Karlin^MI^49643\r"
      + "PID|1||1230922^^^IDA^SR~C74G61237^^^P58605^PI||STARK^CALANTHA^DRUELLA^^^^L|DUVAL^HUA^^^^^L|20140812|F||2106-3^White^CDCREC|276 JANTTI PL^^KARLIN^MI^49643^US^P||^PRN^PH^^^231^7513259|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|0|||||N\r"
      + "PD1|||||||||||02^Reminder/recall^HL70215|N||||A\r"
      + "NK1|1|STARK^DUVAL^MARION^^^^L|MTH^Mother^HL70063|276 JANTTI PL^^KARLIN^MI^49643^^P|^PRN^PH^^^231^7513259\r"
      + "ORC|RE||10809003||||||||||||||IRIS^Idaho IIS^HL70362\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX^90710^MMRV^CPT|999|||05^From other registry^NIP001||||||||UNK^UNKNOWN^MVX||||A\r"
      + "OBX|1|CE|30956-7^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|3|CE|30956-7^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|1||||||F\r"
      + "ORC|RE||10809004||||||||||||||IRIS^Idaho IIS^HL70362\r"
      + "RXA|0|1|20180823|20180823|94^MMRV^CVX^90710^MMRV^CPT|999|||05^From other registry^NIP001||||||||UNK^UNKNOWN^MVX||||A\r"
      + "OBX|5|CE|30956-7^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|6|NM|30973-2^Dose number in series^LN|1|2||||||F\r"
      + "OBX|7|CE|30956-7^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|8|NM|30973-2^Dose number in series^LN|2|2||||||F\r" + "ORC|RE||9999\r"
      + "RXA|0|1|20140812|20140812|998^No Vaccine Administered^CVX|999||||||||||||||NA|A\r"
      + "OBX|9|CE|30979-9^Vaccines Due Next^LN|0|107^DTP/aP^CVX||||||F\r"
      + "OBX|10|TS|30980-7^Date Vaccine Due^LN|0|20141012||||||F\r"
      + "OBX|11|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|12|TS|30981-5^Earliest date to give^LN|0|20140923||||||F\r"
      + "OBX|13|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r"
      + "OBX|14|CE|30979-9^Vaccines Due Next^LN|1|85^HepA^CVX^90730^HepA^CPT||||||F\r"
      + "OBX|15|TS|30980-7^Date Vaccine Due^LN|1|20150812||||||F\r"
      + "OBX|16|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|17|TS|30981-5^Earliest date to give^LN|1|20150812||||||F\r"
      + "OBX|18|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|19|CE|30979-9^Vaccines Due Next^LN|2|45^HepB^CVX^90731^HepB^CPT||||||F\r"
      + "OBX|20|TS|30980-7^Date Vaccine Due^LN|2|20140812||||||F\r"
      + "OBX|21|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|22|TS|30981-5^Earliest date to give^LN|2|20140812||||||F\r"
      + "OBX|23|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|24|CE|30979-9^Vaccines Due Next^LN|3|17^Hib^CVX^90737^Hib^CPT||||||F\r"
      + "OBX|25|TS|30980-7^Date Vaccine Due^LN|3|20141012||||||F\r"
      + "OBX|26|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|27|TS|30981-5^Earliest date to give^LN|3|20140923||||||F\r"
      + "OBX|28|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|29|CE|30979-9^Vaccines Due Next^LN|4|137^HPV^CVX||||||F\r"
      + "OBX|30|TS|30980-7^Date Vaccine Due^LN|4|20250812||||||F\r"
      + "OBX|31|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|32|TS|30981-5^Earliest date to give^LN|4|20230812||||||F\r"
      + "OBX|33|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|34|CE|30979-9^Vaccines Due Next^LN|5|88^Influenza^CVX^90724^Influenza^CPT||||||F\r"
      + "OBX|35|TS|30980-7^Date Vaccine Due^LN|5|20180801||||||F\r"
      + "OBX|36|NM|30973-2^Vaccine due next dose number^LN|5|1||||||F\r"
      + "OBX|37|TS|30981-5^Earliest date to give^LN|5|20150212||||||F\r"
      + "OBX|38|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|39|CE|30979-9^Vaccines Due Next^LN|6|108^MeningACWY^CVX||||||F\r"
      + "OBX|40|TS|30980-7^Date Vaccine Due^LN|6|20250812||||||F\r"
      + "OBX|41|NM|30973-2^Vaccine due next dose number^LN|6|1||||||F\r"
      + "OBX|42|TS|30981-5^Earliest date to give^LN|6|20250812||||||F\r"
      + "OBX|43|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r"
      + "OBX|44|CE|30979-9^Vaccines Due Next^LN|7|115^Pertussis (Tdap)^CVX^90715^Pertussis (Tdap)^CPT||||||F\r"
      + "OBX|45|TS|30980-7^Date Vaccine Due^LN|7|20250812||||||F\r"
      + "OBX|46|NM|30973-2^Vaccine due next dose number^LN|7|1||||||F\r"
      + "OBX|47|TS|30981-5^Earliest date to give^LN|7|20210812||||||F\r"
      + "OBX|48|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|7|ACIP schedule||||||F\r"
      + "OBX|49|CE|30979-9^Vaccines Due Next^LN|8|109^Pneumococcal^CVX||||||F\r"
      + "OBX|50|TS|30980-7^Date Vaccine Due^LN|8|20141012||||||F\r"
      + "OBX|51|NM|30973-2^Vaccine due next dose number^LN|8|1||||||F\r"
      + "OBX|52|TS|30981-5^Earliest date to give^LN|8|20140923||||||F\r"
      + "OBX|53|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|8|ACIP schedule||||||F\r"
      + "OBX|54|CE|30979-9^Vaccines Due Next^LN|9|89^Polio^CVX||||||F\r"
      + "OBX|55|TS|30980-7^Date Vaccine Due^LN|9|20141012||||||F\r"
      + "OBX|56|NM|30973-2^Vaccine due next dose number^LN|9|1||||||F\r"
      + "OBX|57|TS|30981-5^Earliest date to give^LN|9|20140923||||||F\r"
      + "OBX|58|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|9|ACIP schedule||||||F\r"
      + "OBX|59|CE|30979-9^Vaccines Due Next^LN|10|139^Td^CVX||||||F\r"
      + "OBX|60|TS|30980-7^Date Vaccine Due^LN|10|20210812||||||F\r"
      + "OBX|61|NM|30973-2^Vaccine due next dose number^LN|10|1||||||F\r"
      + "OBX|62|TS|30981-5^Earliest date to give^LN|10|20210812||||||F\r"
      + "OBX|63|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|10|ACIP schedule||||||F\r";

  private static final String RSP_IL_I_CARE_1 = ""
      + "MSH|^~\\&||ICARE|TEST||20180824122750-0500||RSP^K11^RSP_K11|954942|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|1icQ-QA.2.1.2\r"
      + "QAK|1icQ-QA.2.1.2|OK|Z44^Request Evaluated Immunization History^CDCPHINVS|\r"
      + "QPD|Z44^Request Evaluated Immunization History^CDCPHINVS|37374859|S95E61294^^^AIRA^MR|Kent^Sherrie^Felicia^^^^L||20140812|F|265 Cochran St^^Painesdale^MI^49955|\r"
      + "PID|1||S95E61294^^^^PT^^^^^TEST&&&1103~71541168^^^^SR~S95E61294^^^^MR||KENT^SHERRIE^FELICIA^^^^L|MARNE TILLMAN|20140812|F^Female^HL70001||2106-3^White^HL70005|265 COCHRAN ST^^PAINESDALE^MI^49955^^P^^|||||||||||||N|1|||||N|||20180824120854|\r"
      + "PD1||||||||||||N||||A\r" + "ORC|RE||71541175^ICARE\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX|999|||N||^^^1103&ICARE TRAINING SITE1^^C^^^500 E MONROE STREET^^SPRINGFIELD^IL^62701^^B||||||UNK|||CP||20180824120854\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|8|03^MMR^CVX||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "OBX|3|NM|30973-2^dose number in series^LN|8|1||||||F\r"
      + "OBX|4|ID|59781-5^dose validity^LN|8|Y||||||F|||20150823\r"
      + "OBX|5|CE|30956-7^vaccine type^LN|16|21^VAR^CVX||||||F\r"
      + "OBX|6|CE|59779-9^Immunization Schedule used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "OBX|7|NM|30973-2^dose number in series^LN|16|1||||||F\r"
      + "OBX|8|ID|59781-5^dose validity^LN|16|Y||||||F|||20150823\r" + "ORC|RE||71541176^ICARE\r"
      + "RXA|0|1|20180823|20180823|94^MMRV^CVX|999|||N||^^^1103&ICARE TRAINING SITE1^^C^^^500 E MONROE STREET^^SPRINGFIELD^IL^62701^^B||||||UNK|||CP||20180824120854\r"
      + "OBX|9|CE|30956-7^vaccine type^LN|8|03^MMR^CVX||||||F\r"
      + "OBX|10|CE|59779-9^Immunization Schedule used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "OBX|11|NM|30973-2^dose number in series^LN|8|2||||||F\r"
      + "OBX|12|ID|59781-5^dose validity^LN|8|Y||||||F|||20180823\r"
      + "OBX|13|CE|30956-7^vaccine type^LN|16|21^VAR^CVX||||||F\r"
      + "OBX|14|CE|59779-9^Immunization Schedule used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "OBX|15|NM|30973-2^dose number in series^LN|16|2||||||F\r"
      + "OBX|16|ID|59781-5^dose validity^LN|16|Y||||||F|||20180823\r" + "ORC|RE||9999^ICARE\r"
      + "RXA|0|1|20140812|20140812|998^No vaccine administered^CVX|999\r"
      + "OBX|17|CE|30956-7^vaccine type^LN|4|42^HBV^CVX||||||F\r"
      + "OBX|18|CE|59779-9^Immunization Schedule used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20140812\r"
      + "OBX|19|DT|30981-5^Earliest date dose should be given^LN|4|20140812||||||F\r"
      + "OBX|20|DT|30980-7^Date next dose recommended^LN|4|20140812||||||F\r"
      + "OBX|21|DT|59778-1^Date dose is overdue^LN|4|20140813||||||F\r"
      + "OBX|22|ST|30982-3^Reason applied^LN|4|HBV: 1st shot forecasting - newborn||||||F|||20140812\r"
      + "ORC|RE||9999^ICARE\r" + "RXA|0|1|20141012|20141012|998^No vaccine administered^CVX|999\r"
      + "OBX|23|CE|30956-7^vaccine type^LN|2|22^HIB^CVX||||||F\r"
      + "OBX|24|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20141012\r"
      + "OBX|25|DT|30981-5^Earliest date dose should be given^LN|2|20141012||||||F\r"
      + "OBX|26|DT|30980-7^Date next dose recommended^LN|2|20141012||||||F\r"
      + "OBX|27|DT|59778-1^Date dose is overdue^LN|2|20141013||||||F\r"
      + "OBX|28|ST|30982-3^Reason applied^LN|2|HIB: 1st shot forecasting - 2 months old||||||F|||20141012\r"
      + "ORC|RE||9999^ICARE\r" + "RXA|0|1|20141012|20141012|998^No vaccine administered^CVX|999\r"
      + "OBX|29|CE|30956-7^vaccine type^LN|1|28^DTP^CVX||||||F\r"
      + "OBX|30|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20141012\r"
      + "OBX|31|DT|30981-5^Earliest date dose should be given^LN|1|20141012||||||F\r"
      + "OBX|32|DT|30980-7^Date next dose recommended^LN|1|20141012||||||F\r"
      + "OBX|33|DT|59778-1^Date dose is overdue^LN|1|20141013||||||F\r"
      + "OBX|34|ST|30982-3^Reason applied^LN|1|DTP: 1st shot forecasting - 2 months old||||||F|||20141012\r"
      + "ORC|RE||9999^ICARE\r" + "RXA|0|1|20141012|20141012|998^No vaccine administered^CVX|999\r"
      + "OBX|35|CE|30956-7^vaccine type^LN|11|100^PNE^CVX||||||F\r"
      + "OBX|36|CE|59779-9^Immunization Schedule used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20141012\r"
      + "OBX|37|DT|30981-5^Earliest date dose should be given^LN|11|20141012||||||F\r"
      + "OBX|38|DT|30980-7^Date next dose recommended^LN|11|20141012||||||F\r"
      + "OBX|39|DT|59778-1^Date dose is overdue^LN|11|20141013||||||F\r"
      + "OBX|40|ST|30982-3^Reason applied^LN|11|PNE: 1st shot forecasting - 2 months old||||||F|||20141012\r"
      + "ORC|RE||9999^ICARE\r" + "RXA|0|1|20141012|20141012|998^No vaccine administered^CVX|999\r"
      + "OBX|41|CE|30956-7^vaccine type^LN|12|10^POL^CVX||||||F\r"
      + "OBX|42|CE|59779-9^Immunization Schedule used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20141012\r"
      + "OBX|43|DT|30981-5^Earliest date dose should be given^LN|12|20141012||||||F\r"
      + "OBX|44|DT|30980-7^Date next dose recommended^LN|12|20141012||||||F\r"
      + "OBX|45|DT|59778-1^Date dose is overdue^LN|12|20141013||||||F\r"
      + "OBX|46|ST|30982-3^Reason applied^LN|12|POL: 1st shot forecasting - 2 months old||||||F|||20141012\r"
      + "ORC|RE||9999^ICARE\r" + "RXA|0|1|20150812|20150812|998^No vaccine administered^CVX|999\r"
      + "OBX|47|CE|30956-7^vaccine type^LN|3|52^HAV^CVX||||||F\r"
      + "OBX|48|CE|59779-9^Immunization Schedule used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20150812\r"
      + "OBX|49|DT|30981-5^Earliest date dose should be given^LN|3|20150812||||||F\r"
      + "OBX|50|DT|30980-7^Date next dose recommended^LN|3|20150812||||||F\r"
      + "OBX|51|DT|59778-1^Date dose is overdue^LN|3|20150813||||||F\r"
      + "OBX|52|ST|30982-3^Reason applied^LN|3|HAV: 1st shot forecasting - 12 months old||||||F|||20150812\r"
      + "ORC|RE||9999^ICARE\r" + "RXA|0|1|20180901|20180901|998^No vaccine administered^CVX|999\r"
      + "OBX|53|CE|30956-7^vaccine type^LN|6|16^FLU^CVX||||||F\r"
      + "OBX|54|CE|59779-9^Immunization Schedule used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180901\r"
      + "OBX|55|DT|30981-5^Earliest date dose should be given^LN|6|20180901||||||F\r"
      + "OBX|56|DT|30980-7^Date next dose recommended^LN|6|20180901||||||F\r"
      + "OBX|57|DT|59778-1^Date dose is overdue^LN|6|20180902||||||F\r"
      + "OBX|58|ST|30982-3^Reason applied^LN|6|FLU: forecasting - Seasonal shot||||||F|||20180901\r"
      + "ORC|RE||9999^ICARE\r" + "RXA|0|1|20250812|20250812|998^No vaccine administered^CVX|999\r"
      + "OBX|59|CE|30956-7^vaccine type^LN|9|108^MEN^CVX||||||F\r"
      + "OBX|60|CE|59779-9^Immunization Schedule used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20250812\r"
      + "OBX|61|DT|30981-5^Earliest date dose should be given^LN|9|20250812||||||F\r"
      + "OBX|62|DT|30980-7^Date next dose recommended^LN|9|20250812||||||F\r"
      + "OBX|63|DT|59778-1^Date dose is overdue^LN|9|20250813||||||F\r"
      + "OBX|64|ST|30982-3^Reason applied^LN|9|MEN: forecasting - 11 years old||||||F|||20250812\r";

  private static final String RSP_KS_WEBIZ_1 = ""
      + "MSH|^~\\&|WebIZ.18.1.20180727|KS0000||KS9898|20180824122756-0500||RSP^K11^RSP_K11|KS000020180824275682|T|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|1liv-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|Y88S61355^^^AIRA^MR|Meigs^Beckie^Genevieve^^^^L||20140812|F|46 Jasper Pl^^Ruby^MI^48049\r"
      + "PID|1||2995737^^^KS0000^SR~Y88S61355^^^KS9898^MR||MEIGS^BECKIE^GENEVIEVE^^^^L|Hancock^^^^^^M|20140812|F||2106-3^White^CDCREC|46 JASPER PL^^RUBY^MI^48049^USA^P||^PRN^PH^^^810^2035753|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|||||||||20180824\r"
      + "ORC|RE||52776731^KS0000\r"
      + "RXA|0|1|20150823|20150823|94^MMRV(ProQuad)^CVX|999|||01^Historical Information - Source Unspecified^NIP001||PR^^^PR|||||||||CP|A|20180824\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20150823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20150823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20150823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20150823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "ORC|RE||52776732^KS0000\r"
      + "RXA|0|1|20180823|20180823|94^MMRV(ProQuad)^CVX|999|||01^Historical Information - Source Unspecified^NIP001||PR^^^PR|||||||||CP|A|20180824\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20180823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20180823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20180823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20180823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "ORC|RE||9999^KS0000\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|08^HepB, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|2|DT|30981-5^Earliest date dose should be given^LN|1|20140812||||||F|||20180824\r"
      + "OBX|3|DT|30980-7^Date Vaccine Due^LN|1|20140812||||||F|||20180824\r"
      + "OBX|4|DT|59777-3^Latest date next dose should be given^LN|1|20330811||||||F|||20180824\r"
      + "OBX|5|DT|59778-1^Date dose is overdue^LN|1|20140908||||||F|||20180824\r"
      + "OBX|6|CE|59783-1^Series Status^LN|1|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|7|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|8|CE|30956-7^Vaccine Type^LN|2|133^PCV-13 (Prevnar)^CVX||||||F|||20180824\r"
      + "OBX|9|DT|30981-5^Earliest date dose should be given^LN|2|20140923||||||F|||20180824\r"
      + "OBX|10|DT|30980-7^Date Vaccine Due^LN|2|20141012||||||F|||20180824\r"
      + "OBX|11|DT|59777-3^Latest date next dose should be given^LN|2|20190811||||||F|||20180824\r"
      + "OBX|12|DT|59778-1^Date dose is overdue^LN|2|20141209||||||F|||20180824\r"
      + "OBX|13|CE|59783-1^Series Status^LN|2|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|14|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|15|CE|30956-7^Vaccine Type^LN|3|106^DTaP (Daptacel)^CVX||||||F|||20180824\r"
      + "OBX|16|DT|30981-5^Earliest date dose should be given^LN|3|20140923||||||F|||20180824\r"
      + "OBX|17|DT|30980-7^Date Vaccine Due^LN|3|20141012||||||F|||20180824\r"
      + "OBX|18|DT|59778-1^Date dose is overdue^LN|3|20141209||||||F|||20180824\r"
      + "OBX|19|CE|59783-1^Series Status^LN|3|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|20|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|21|CE|30956-7^Vaccine Type^LN|4|10^Polio-IPV^CVX||||||F|||20180824\r"
      + "OBX|22|DT|30981-5^Earliest date dose should be given^LN|4|20140923||||||F|||20180824\r"
      + "OBX|23|DT|30980-7^Date Vaccine Due^LN|4|20141012||||||F|||20180824\r"
      + "OBX|24|DT|59777-3^Latest date next dose should be given^LN|4|20320811||||||F|||20180824\r"
      + "OBX|25|DT|59778-1^Date dose is overdue^LN|4|20141209||||||F|||20180824\r"
      + "OBX|26|CE|59783-1^Series Status^LN|4|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|27|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|28|CE|30956-7^Vaccine Type^LN|5|48^Hib Prp-t^CVX||||||F|||20180824\r"
      + "OBX|29|DT|30981-5^Earliest date dose should be given^LN|5|20140923||||||F|||20180824\r"
      + "OBX|30|DT|30980-7^Date Vaccine Due^LN|5|20141012||||||F|||20180824\r"
      + "OBX|31|DT|59777-3^Latest date next dose should be given^LN|5|20190811||||||F|||20180824\r"
      + "OBX|32|DT|59778-1^Date dose is overdue^LN|5|20141209||||||F|||20180824\r"
      + "OBX|33|CE|59783-1^Series Status^LN|5|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|34|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|35|CE|30956-7^Vaccine Type^LN|6|83^HepA, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|36|DT|30981-5^Earliest date dose should be given^LN|6|20150812||||||F|||20180824\r"
      + "OBX|37|DT|30980-7^Date Vaccine Due^LN|6|20150812||||||F|||20180824\r"
      + "OBX|38|DT|59777-3^Latest date next dose should be given^LN|6|20330811||||||F|||20180824\r"
      + "OBX|39|DT|59778-1^Date dose is overdue^LN|6|20160908||||||F|||20180824\r"
      + "OBX|40|CE|59783-1^Series Status^LN|6|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|41|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|42|CE|30956-7^Vaccine Type^LN|7|141^Influenza IIV3 MDV^CVX||||||F|||20180824\r"
      + "OBX|43|DT|30981-5^Earliest date dose should be given^LN|7|20180920||||||F|||20180824\r"
      + "OBX|44|DT|30980-7^Date Vaccine Due^LN|7|20180920||||||F|||20180824\r"
      + "OBX|45|CE|59783-1^Series Status^LN|7|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|46|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|47|CE|30956-7^Vaccine Type^LN|8|106^DTaP (Daptacel)^CVX||||||F|||20180824\r"
      + "OBX|48|DT|30981-5^Earliest date dose should be given^LN|8|20180921||||||F|||20180824\r"
      + "OBX|49|DT|30980-7^Date Vaccine Due^LN|8|20180921||||||F|||20180824\r"
      + "OBX|50|DT|59778-1^Date dose is overdue^LN|8|20180921||||||F|||20180824\r"
      + "OBX|51|CE|59783-1^Series Status^LN|8|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|52|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|53|CE|30956-7^Vaccine Type^LN|9|08^HepB, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|54|DT|30981-5^Earliest date dose should be given^LN|9|20180921||||||F|||20180824\r"
      + "OBX|55|DT|30980-7^Date Vaccine Due^LN|9|20180921||||||F|||20180824\r"
      + "OBX|56|DT|59778-1^Date dose is overdue^LN|9|20180921||||||F|||20180824\r"
      + "OBX|57|CE|59783-1^Series Status^LN|9|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|58|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|59|CE|30956-7^Vaccine Type^LN|10|10^Polio-IPV^CVX||||||F|||20180824\r"
      + "OBX|60|DT|30981-5^Earliest date dose should be given^LN|10|20180921||||||F|||20180824\r"
      + "OBX|61|DT|30980-7^Date Vaccine Due^LN|10|20180921||||||F|||20180824\r"
      + "OBX|62|DT|59777-3^Latest date next dose should be given^LN|10|20320811||||||F|||20180824\r"
      + "OBX|63|DT|59778-1^Date dose is overdue^LN|10|20180921||||||F|||20180824\r"
      + "OBX|64|CE|59783-1^Series Status^LN|10|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|65|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|66|CE|30956-7^Vaccine Type^LN|11|83^HepA, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|67|DT|30981-5^Earliest date dose should be given^LN|11|20190224||||||F|||20180824\r"
      + "OBX|68|DT|30980-7^Date Vaccine Due^LN|11|20190224||||||F|||20180824\r"
      + "OBX|69|DT|59778-1^Date dose is overdue^LN|11|20190224||||||F|||20180824\r"
      + "OBX|70|CE|59783-1^Series Status^LN|11|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|71|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|72|CE|30956-7^Vaccine Type^LN|12|114^MCV4 (Menactra)^CVX||||||F|||20180824\r"
      + "OBX|73|DT|30981-5^Earliest date dose should be given^LN|12|20250812||||||F|||20180824\r"
      + "OBX|74|DT|30980-7^Date Vaccine Due^LN|12|20250812||||||F|||20180824\r"
      + "OBX|75|DT|59777-3^Latest date next dose should be given^LN|12|20360811||||||F|||20180824\r"
      + "OBX|76|DT|59778-1^Date dose is overdue^LN|12|20270908||||||F|||20180824\r"
      + "OBX|77|CE|59783-1^Series Status^LN|12|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|78|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|79|CE|30956-7^Vaccine Type^LN|13|165^HPV9 (Gardasil-9)^CVX||||||F|||20180824\r"
      + "OBX|80|DT|30981-5^Earliest date dose should be given^LN|13|20230812||||||F|||20180824\r"
      + "OBX|81|DT|30980-7^Date Vaccine Due^LN|13|20250812||||||F|||20180824\r"
      + "OBX|82|DT|59777-3^Latest date next dose should be given^LN|13|20410811||||||F|||20180824\r"
      + "OBX|83|DT|59778-1^Date dose is overdue^LN|13|20270908||||||F|||20180824\r"
      + "OBX|84|CE|59783-1^Series Status^LN|13|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|85|CE|59779-9^Immunization Schedule Used^LN|13|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|86|CE|30956-7^Vaccine Type^LN|14|187^Recombinant Zoster^CVX||||||F|||20180824\r"
      + "OBX|87|DT|30981-5^Earliest date dose should be given^LN|14|20640812||||||F|||20180824\r"
      + "OBX|88|DT|30980-7^Date Vaccine Due^LN|14|20640812||||||F|||20180824\r"
      + "OBX|89|CE|59783-1^Series Status^LN|14|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|90|CE|59779-9^Immunization Schedule Used^LN|14|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|91|CE|30956-7^Vaccine Type^LN|15|133^PCV-13 (Prevnar)^CVX||||||F|||20180824\r"
      + "OBX|92|DT|30981-5^Earliest date dose should be given^LN|15|20790812||||||F|||20180824\r"
      + "OBX|93|DT|30980-7^Date Vaccine Due^LN|15|20790812||||||F|||20180824\r"
      + "OBX|94|CE|59783-1^Series Status^LN|15|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|95|CE|59779-9^Immunization Schedule Used^LN|15|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|96|CE|30956-7^Vaccine Type^LN|16|03^MMR^CVX||||||F|||20180824\r"
      + "OBX|97|CE|59783-1^Series Status^LN|16|LA13421-5^Complete^LN||||||F|||20180824\r"
      + "OBX|98|ST|30982-3^Reason Code^LN|16|Patient Series is complete||||||F|||20180824\r"
      + "OBX|99|CE|59779-9^Immunization Schedule Used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|100|CE|30956-7^Vaccine Type^LN|17|122^Rotavirus, UF^CVX||||||F|||20180824\r"
      + "OBX|101|CE|59783-1^Series Status^LN|17|LA13424-9^Too Old^LN||||||F|||20180824\r"
      + "OBX|102|ST|30982-3^Reason Code^LN|17|Patient has exceeded the maximum age||||||F|||20180824\r"
      + "OBX|103|CE|59779-9^Immunization Schedule Used^LN|17|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|104|CE|30956-7^Vaccine Type^LN|18|21^Varicella^CVX||||||F|||20180824\r"
      + "OBX|105|CE|59783-1^Series Status^LN|18|LA13421-5^Complete^LN||||||F|||20180824\r"
      + "OBX|106|ST|30982-3^Reason Code^LN|18|Patient Series is complete||||||F|||20180824\r"
      + "OBX|107|CE|59779-9^Immunization Schedule Used^LN|18|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r";

  private static final String RSP_KY_KYIR_1 = ""
      + "MSH|^~\\&|KY0000|KY0000|ACS-EHR|AmericanImmRegAssoc^9014000947^ISO|20180829152801-0400||RSP^K11^RSP_K11|KY000020180829280164|D|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|2kwv-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|Y02T97202^^^AIRA^MR|McMullen^Penelope^Suksma^^^^L||20140817|F|79 Hodge Ave^^Flint^MI^48556\r"
      + "PID|1||2758536^^^KY0000^SR~Y02T97202^^^AmericanImmRegAssoc^MR||MCMULLEN^PENELOPE^SUKSMA^^^^L|Wettiland^^^^^^M|20140817|F||2106-3^White^CDCREC|79 HODGE AVE^^FLINT^MI^48556^USA^P||^PRN^PH^^^810^9524174|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|||||||||20180829\r"
      + "ORC|RE||6683493^KY0000\r"
      + "RXA|0|1|20150828|20150828|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||PR^^^PR|||||||||CP|A|20180829\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20150828\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20150828\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|1|NA^Not Applicable^HL70353|||||F|||20150828\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20150828\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20150828\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20150828\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|1|NA^Not Applicable^HL70353|||||F|||20150828\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20150828\r"
      + "ORC|RE||6683494^KY0000\r"
      + "RXA|0|1|20170827|20170827|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||PR^^^PR|||||||||CP|A|20180829\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20170827\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20170827\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|2|NA^Not Applicable^HL70353|||||F|||20170827\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20170827\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20170827\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20170827\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|2|NA^Not Applicable^HL70353|||||F|||20170827\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20170827\r"
      + "ORC|RE||9999^KY0000\r"
      + "RXA|0|1|20180829|20180829|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|08^Hep B, ped/adol^CVX||||||F|||20180829\r"
      + "OBX|2|DT|30981-5^Earliest date dose should be given^LN|1|20140817||||||F|||20180829\r"
      + "OBX|3|DT|30980-7^Date Vaccine Due^LN|1|20140817||||||F|||20180829\r"
      + "OBX|4|DT|59777-3^Latest date next dose should be given^LN|1|20330816||||||F|||20180829\r"
      + "OBX|5|DT|59778-1^Date dose is overdue^LN|1|20140913||||||F|||20180829\r"
      + "OBX|6|CE|59783-1^Series Status^LN|1|LA13423-1^Overdue^LN||||||F|||20180829\r"
      + "OBX|7|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|8|CE|30956-7^Vaccine Type^LN|2|133^PCV13^CVX||||||F|||20180829\r"
      + "OBX|9|DT|30981-5^Earliest date dose should be given^LN|2|20140928||||||F|||20180829\r"
      + "OBX|10|DT|30980-7^Date Vaccine Due^LN|2|20141017||||||F|||20180829\r"
      + "OBX|11|DT|59777-3^Latest date next dose should be given^LN|2|20190816||||||F|||20180829\r"
      + "OBX|12|DT|59778-1^Date dose is overdue^LN|2|20141214||||||F|||20180829\r"
      + "OBX|13|CE|59783-1^Series Status^LN|2|LA13423-1^Overdue^LN||||||F|||20180829\r"
      + "OBX|14|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|15|CE|30956-7^Vaccine Type^LN|3|10^Polio-IPV^CVX||||||F|||20180829\r"
      + "OBX|16|DT|30981-5^Earliest date dose should be given^LN|3|20140928||||||F|||20180829\r"
      + "OBX|17|DT|30980-7^Date Vaccine Due^LN|3|20141017||||||F|||20180829\r"
      + "OBX|18|DT|59777-3^Latest date next dose should be given^LN|3|20320816||||||F|||20180829\r"
      + "OBX|19|DT|59778-1^Date dose is overdue^LN|3|20141214||||||F|||20180829\r"
      + "OBX|20|CE|59783-1^Series Status^LN|3|LA13423-1^Overdue^LN||||||F|||20180829\r"
      + "OBX|21|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|22|CE|30956-7^Vaccine Type^LN|4|20^DTaP (Infanrix)^CVX||||||F|||20180829\r"
      + "OBX|23|DT|30981-5^Earliest date dose should be given^LN|4|20140928||||||F|||20180829\r"
      + "OBX|24|DT|30980-7^Date Vaccine Due^LN|4|20141017||||||F|||20180829\r"
      + "OBX|25|DT|59778-1^Date dose is overdue^LN|4|20141214||||||F|||20180829\r"
      + "OBX|26|CE|59783-1^Series Status^LN|4|LA13423-1^Overdue^LN||||||F|||20180829\r"
      + "OBX|27|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|28|CE|30956-7^Vaccine Type^LN|5|48^Hib (PRP-T)^CVX||||||F|||20180829\r"
      + "OBX|29|DT|30981-5^Earliest date dose should be given^LN|5|20140928||||||F|||20180829\r"
      + "OBX|30|DT|30980-7^Date Vaccine Due^LN|5|20141017||||||F|||20180829\r"
      + "OBX|31|DT|59777-3^Latest date next dose should be given^LN|5|20190816||||||F|||20180829\r"
      + "OBX|32|DT|59778-1^Date dose is overdue^LN|5|20141214||||||F|||20180829\r"
      + "OBX|33|CE|59783-1^Series Status^LN|5|LA13423-1^Overdue^LN||||||F|||20180829\r"
      + "OBX|34|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|35|CE|30956-7^Vaccine Type^LN|6|83^Hep A, ped/adol, 2D^CVX||||||F|||20180829\r"
      + "OBX|36|DT|30981-5^Earliest date dose should be given^LN|6|20150817||||||F|||20180829\r"
      + "OBX|37|DT|30980-7^Date Vaccine Due^LN|6|20150817||||||F|||20180829\r"
      + "OBX|38|DT|59777-3^Latest date next dose should be given^LN|6|20330816||||||F|||20180829\r"
      + "OBX|39|DT|59778-1^Date dose is overdue^LN|6|20160913||||||F|||20180829\r"
      + "OBX|40|CE|59783-1^Series Status^LN|6|LA13423-1^Overdue^LN||||||F|||20180829\r"
      + "OBX|41|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|42|CE|30956-7^Vaccine Type^LN|7|141^Influenza, Seasonal^CVX||||||F|||20180829\r"
      + "OBX|43|DT|30981-5^Earliest date dose should be given^LN|7|20180701||||||F|||20180829\r"
      + "OBX|44|DT|30980-7^Date Vaccine Due^LN|7|20180701||||||F|||20180829\r"
      + "OBX|45|CE|59783-1^Series Status^LN|7|LA13422-3^On Schedule^LN||||||F|||20180829\r"
      + "OBX|46|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|47|CE|30956-7^Vaccine Type^LN|8|08^Hep B, ped/adol^CVX||||||F|||20180829\r"
      + "OBX|48|DT|30981-5^Earliest date dose should be given^LN|8|20180926||||||F|||20180829\r"
      + "OBX|49|DT|30980-7^Date Vaccine Due^LN|8|20180926||||||F|||20180829\r"
      + "OBX|50|DT|59778-1^Date dose is overdue^LN|8|20180926||||||F|||20180829\r"
      + "OBX|51|CE|59783-1^Series Status^LN|8|LA13422-3^On Schedule^LN||||||F|||20180829\r"
      + "OBX|52|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|53|CE|30956-7^Vaccine Type^LN|9|10^Polio-IPV^CVX||||||F|||20180829\r"
      + "OBX|54|DT|30981-5^Earliest date dose should be given^LN|9|20180926||||||F|||20180829\r"
      + "OBX|55|DT|30980-7^Date Vaccine Due^LN|9|20180926||||||F|||20180829\r"
      + "OBX|56|DT|59777-3^Latest date next dose should be given^LN|9|20320816||||||F|||20180829\r"
      + "OBX|57|DT|59778-1^Date dose is overdue^LN|9|20180926||||||F|||20180829\r"
      + "OBX|58|CE|59783-1^Series Status^LN|9|LA13422-3^On Schedule^LN||||||F|||20180829\r"
      + "OBX|59|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|60|CE|30956-7^Vaccine Type^LN|10|20^DTaP (Infanrix)^CVX||||||F|||20180829\r"
      + "OBX|61|DT|30981-5^Earliest date dose should be given^LN|10|20180926||||||F|||20180829\r"
      + "OBX|62|DT|30980-7^Date Vaccine Due^LN|10|20180926||||||F|||20180829\r"
      + "OBX|63|DT|59778-1^Date dose is overdue^LN|10|20180926||||||F|||20180829\r"
      + "OBX|64|CE|59783-1^Series Status^LN|10|LA13422-3^On Schedule^LN||||||F|||20180829\r"
      + "OBX|65|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|66|CE|30956-7^Vaccine Type^LN|11|141^Influenza, Seasonal^CVX||||||F|||20180829\r"
      + "OBX|67|DT|30981-5^Earliest date dose should be given^LN|11|20180926||||||F|||20180829\r"
      + "OBX|68|DT|30980-7^Date Vaccine Due^LN|11|20180926||||||F|||20180829\r"
      + "OBX|69|CE|59783-1^Series Status^LN|11|LA13422-3^On Schedule^LN||||||F|||20180829\r"
      + "OBX|70|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|71|CE|30956-7^Vaccine Type^LN|12|83^Hep A, ped/adol, 2D^CVX||||||F|||20180829\r"
      + "OBX|72|DT|30981-5^Earliest date dose should be given^LN|12|20190301||||||F|||20180829\r"
      + "OBX|73|DT|30980-7^Date Vaccine Due^LN|12|20190301||||||F|||20180829\r"
      + "OBX|74|DT|59778-1^Date dose is overdue^LN|12|20190301||||||F|||20180829\r"
      + "OBX|75|CE|59783-1^Series Status^LN|12|LA13422-3^On Schedule^LN||||||F|||20180829\r"
      + "OBX|76|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|77|CE|30956-7^Vaccine Type^LN|13|114^MCV4 (Menactra)^CVX||||||F|||20180829\r"
      + "OBX|78|DT|30981-5^Earliest date dose should be given^LN|13|20250817||||||F|||20180829\r"
      + "OBX|79|DT|30980-7^Date Vaccine Due^LN|13|20250817||||||F|||20180829\r"
      + "OBX|80|DT|59777-3^Latest date next dose should be given^LN|13|20360816||||||F|||20180829\r"
      + "OBX|81|DT|59778-1^Date dose is overdue^LN|13|20270913||||||F|||20180829\r"
      + "OBX|82|CE|59783-1^Series Status^LN|13|LA13422-3^On Schedule^LN||||||F|||20180829\r"
      + "OBX|83|CE|59779-9^Immunization Schedule Used^LN|13|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|84|CE|30956-7^Vaccine Type^LN|14|187^Recombinant Zoster^CVX||||||F|||20180829\r"
      + "OBX|85|DT|30981-5^Earliest date dose should be given^LN|14|20640817||||||F|||20180829\r"
      + "OBX|86|DT|30980-7^Date Vaccine Due^LN|14|20640817||||||F|||20180829\r"
      + "OBX|87|CE|59783-1^Series Status^LN|14|LA13422-3^On Schedule^LN||||||F|||20180829\r"
      + "OBX|88|CE|59779-9^Immunization Schedule Used^LN|14|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|89|CE|30956-7^Vaccine Type^LN|15|133^PCV13^CVX||||||F|||20180829\r"
      + "OBX|90|DT|30981-5^Earliest date dose should be given^LN|15|20790817||||||F|||20180829\r"
      + "OBX|91|DT|30980-7^Date Vaccine Due^LN|15|20790817||||||F|||20180829\r"
      + "OBX|92|CE|59783-1^Series Status^LN|15|LA13422-3^On Schedule^LN||||||F|||20180829\r"
      + "OBX|93|CE|59779-9^Immunization Schedule Used^LN|15|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|94|CE|30956-7^Vaccine Type^LN|16|122^Rotavirus, UF^CVX||||||F|||20180829\r"
      + "OBX|95|CE|59783-1^Series Status^LN|16|LA13424-9^Too Old^LN||||||F|||20180829\r"
      + "OBX|96|ST|30982-3^Reason Code^LN|16|Patient has exceeded the maximum age||||||F|||20180829\r"
      + "OBX|97|CE|59779-9^Immunization Schedule Used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|98|CE|30956-7^Vaccine Type^LN|17|03^MMR^CVX||||||F|||20180829\r"
      + "OBX|99|CE|59783-1^Series Status^LN|17|LA13421-5^Complete^LN||||||F|||20180829\r"
      + "OBX|100|ST|30982-3^Reason Code^LN|17|Patient Series is complete||||||F|||20180829\r"
      + "OBX|101|CE|59779-9^Immunization Schedule Used^LN|17|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|102|CE|30956-7^Vaccine Type^LN|18|21^Varicella^CVX||||||F|||20180829\r"
      + "OBX|103|CE|59783-1^Series Status^LN|18|LA13421-5^Complete^LN||||||F|||20180829\r"
      + "OBX|104|ST|30982-3^Reason Code^LN|18|Patient Series is complete||||||F|||20180829\r"
      + "OBX|105|CE|59779-9^Immunization Schedule Used^LN|18|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r"
      + "OBX|106|CE|30956-7^Vaccine Type^LN|19|137^HPV, UF^CVX||||||F|||20180829\r"
      + "OBX|107|CE|59783-1^Series Status^LN|19|LA13422-3^On Schedule^LN||||||F|||20180829\r"
      + "OBX|108|ST|30982-3^Reason Code^LN|19|Patient Series is not complete||||||F|||20180829\r"
      + "OBX|109|CE|59779-9^Immunization Schedule Used^LN|19|VXC16^ACIP^CDCPHINVS||||||F|||20180829\r";

  private static final String RSP_LA_LINKS_1 = ""
      + "MSH|^~\\&|^^|^^|^^|^^|20180824122847||RSP^K11^RSP_K11|1987315245.101178439|P|2.5.1|||||||||Z42^CDCPHINVS^^|\r"
      + "MSA|AA|1mVP-QA.2.1.2|\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^HL70471|\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|37374859|P16Q61412^^^AIRA^MR|Oxford^Daksha^Micah||20140812|F|L^^Eastmanville^MI^49404|\r"
      + "PID|1||7124696^^^^SR~~~~~P16Q61412^^^^MR||OXFORD^DAKSHA^MICAH^^^^L|NORDBY|20140812|F|||60 SMITH LN^^EASTMANVILLE^MICHIGAN^49404^United States^M^^OTTAWA||6169844654^PRN^PH^^^616^9844654|||||||||2186-5^not Hispanic or Latino^HL70189||N|1|||||N|\r"
      + "ORC|RE||7124696.54.20150823|\r"
      + "RXA|0|999|20150823|20150823|94^MMRV^CVX^90710^MMRV^CPT~54^MMR/Varicella^STC0292|999|||01^Historical information - source unspecified^NIP001||^^^AIRA TEST SITE^^^^^1155 F STREET NW, SUITE 1050^^WASHINGTON^DC^20004||||||||||A|20180824122847|\r"
      + "RXR|IM^Intramuscular^CDCPHINVS|LT^Left Thigh^CDCPHINVS|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20150823|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20150823|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||7124696.54.20180823|\r"
      + "RXA|0|999|20180823|20180823|94^MMRV^CVX^90710^MMRV^CPT~54^MMR/Varicella^STC0292|999|||01^Historical information - source unspecified^NIP001||^^^AIRA TEST SITE^^^^^1155 F STREET NW, SUITE 1050^^WASHINGTON^DC^20004||||||||||A|20180824122847|\r"
      + "RXR|IM^Intramuscular^CDCPHINVS|LT^Left Thigh^CDCPHINVS|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20180823|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20180823|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824122847|20180824122847|998^no vaccine administered^CVX|0||||||||||||||NA||20180824122847|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|45^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824122847|20180824122847|998^no vaccine administered^CVX|0||||||||||||||NA||20180824122847|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|107^DTAP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141012||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140923||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824122847|20180824122847|998^no vaccine administered^CVX|0||||||||||||||NA||20180824122847|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|17^HIB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141012||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140923||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141212||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824122847|20180824122847|998^no vaccine administered^CVX|0||||||||||||||NA||20180824122847|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|89^POLIO^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141012||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140923||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824122847|20180824122847|998^no vaccine administered^CVX|0||||||||||||||NA||20180824122847|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|88^FLU^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150212||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150212||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150314||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824122847|20180824122847|998^no vaccine administered^CVX|0||||||||||||||NA||20180824122847|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|147^MENING^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20250812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20250912||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";

  private static final String RSP_MA_MIIS_1 = "" + "BHS|^~\\&|\r"
      + "MSH|^~\\&|MIIS|99990|CDCTEST|12345|20181031070610||RSP^K11^RSP_K11|ACK-20181031-070610-415|P|2.5.1|||NE|AL|||||Z42^CDCPHINVS\r"
      + "MSA|AA|154098396925819076\r" + "QAK|154098396925819076|OK\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|154098396925819076|154098396536919074^^^FITS^MR|Danielson^Norberta^Bernardo^^^^L|Teton^Zoleen^^^^^M|20170430|f|384 San Joaquin Pl^^Springfield Township^MI^48350^USA^P\r"
      + "PID|1||154098396536919074^^^^MR~8628817^^^^SR||DANIELSON^NORBERTA^BERNARDO|Teton^^^^^^M|20170430|U||^Unknown/undetermined^HL70005|384 San Joaquin Pl^^Springfield Township^MI^48350^^H||^PRN^PH^^^248^4534414|||||||||^Unknown^HL70189||||||||N\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181030||133^PCV13^CVX|999|||01|^Lily Jackson||||||||||||20181031\r"
      + "RXR|C28161^Intramuscular^NCIT\r"
      + "OBX|1|CE|38890-0^Component Vaccine Type^LN|1|133^PCV13^CVX||||||F\r"
      + "OBX|2|ID|59781-5^Dose validity^LN|1|Y||||||F\r" + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|3|CE|30979-9^Vaccines due next^LN|1|10^Polio^CVX||||||F\r"
      + "OBX|4|CE|59780-7^Immunization Series name^LN|1|10^IPV^CVX||||||F\r"
      + "OBX|5|TS_NZ|59777-3^Latest date next dose should be given^LN|1|20360429||||||F\r"
      + "OBX|6|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20170611||||||F\r"
      + "OBX|7|TS_NZ|59778-1^Date dose is overdue^LN|1|20170730||||||F\r"
      + "OBX|8|TS_NZ|30980-7^Date next dose recommended^LN|1|20170630||||||F\r"
      + "OBX|9|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|10|CE|30979-9^Vaccines due next^LN|1|33^Pneumo-Poly^CVX||||||F\r"
      + "OBX|11|CE|59780-7^Immunization Series name^LN|1|33^PPSV23^CVX||||||F\r"
      + "OBX|12|TS_NZ|59777-3^Latest date next dose should be given^LN|1|21370430||||||F\r"
      + "OBX|13|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20820430||||||F\r"
      + "OBX|14|TS_NZ|59778-1^Date dose is overdue^LN|1|20820530||||||F\r"
      + "OBX|15|TS_NZ|30980-7^Date next dose recommended^LN|1|20820430||||||F\r"
      + "OBX|16|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|17|CE|30979-9^Vaccines due next^LN|1|62^HPV^CVX||||||F\r"
      + "OBX|18|CE|59780-7^Immunization Series name^LN|1|62^HPV4v^CVX||||||F\r"
      + "OBX|19|TS_NZ|59777-3^Latest date next dose should be given^LN|1|20440429||||||F\r"
      + "OBX|20|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20260430||||||F\r"
      + "OBX|21|TS_NZ|59778-1^Date dose is overdue^LN|1|20300430||||||F\r"
      + "OBX|22|TS_NZ|30980-7^Date next dose recommended^LN|1|20280430||||||F\r"
      + "OBX|23|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|24|CE|30979-9^Vaccines due next^LN|1|115^Td/Tdap^CVX||||||F\r"
      + "OBX|25|CE|59780-7^Immunization Series name^LN|1|115^Tdap^CVX||||||F\r"
      + "OBX|26|TS_NZ|59777-3^Latest date next dose should be given^LN|1|21370430||||||F\r"
      + "OBX|27|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20240430||||||F\r"
      + "OBX|28|TS_NZ|59778-1^Date dose is overdue^LN|1|20240530||||||F\r"
      + "OBX|29|TS_NZ|30980-7^Date next dose recommended^LN|1|20240430||||||F\r"
      + "OBX|30|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|31|CE|30979-9^Vaccines due next^LN|1|187^Zoster - RZV^CVX||||||F\r"
      + "OBX|32|CE|59780-7^Immunization Series name^LN|1|187^RZV (Shingrix)^CVX||||||F\r"
      + "OBX|33|TS_NZ|59777-3^Latest date next dose should be given^LN|1|21370430||||||F\r"
      + "OBX|34|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20670430||||||F\r"
      + "OBX|35|TS_NZ|59778-1^Date dose is overdue^LN|1|20680430||||||F\r"
      + "OBX|36|TS_NZ|30980-7^Date next dose recommended^LN|1|20670430||||||F\r"
      + "OBX|37|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|38|CE|30979-9^Vaccines due next^LN|1|88^Influenza^CVX||||||F\r"
      + "OBX|39|CE|59780-7^Immunization Series name^LN|1|88^Flu, unspecified^CVX||||||F\r"
      + "OBX|40|TS_NZ|59777-3^Latest date next dose should be given^LN|1|21370430||||||F\r"
      + "OBX|41|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20180801||||||F\r"
      + "OBX|42|TS_NZ|59778-1^Date dose is overdue^LN|1|20180901||||||F\r"
      + "OBX|43|TS_NZ|30980-7^Date next dose recommended^LN|1|20180801||||||F\r"
      + "OBX|44|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|45|CE|30979-9^Vaccines due next^LN|1|21^Varicella^CVX||||||F\r"
      + "OBX|46|CE|59780-7^Immunization Series name^LN|1|21^Varicella^CVX||||||F\r"
      + "OBX|47|TS_NZ|59777-3^Latest date next dose should be given^LN|1|20770430||||||F\r"
      + "OBX|48|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20180430||||||F\r"
      + "OBX|49|TS_NZ|59778-1^Date dose is overdue^LN|1|20180830||||||F\r"
      + "OBX|50|TS_NZ|30980-7^Date next dose recommended^LN|1|20180430||||||F\r"
      + "OBX|51|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|52|CE|30979-9^Vaccines due next^LN|1|17^Hib^CVX||||||F\r"
      + "OBX|53|CE|59780-7^Immunization Series name^LN|1|17^Hib, unspecified^CVX||||||F\r"
      + "OBX|54|TS_NZ|59777-3^Latest date next dose should be given^LN|1|20220429||||||F\r"
      + "OBX|55|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20170611||||||F\r"
      + "OBX|56|TS_NZ|59778-1^Date dose is overdue^LN|1|20170730||||||F\r"
      + "OBX|57|TS_NZ|30980-7^Date next dose recommended^LN|1|20170630||||||F\r"
      + "OBX|58|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|59|CE|30979-9^Vaccines due next^LN|1|45^HepB^CVX||||||F\r"
      + "OBX|60|CE|59780-7^Immunization Series name^LN|1|45^HepB, unspecified^CVX||||||F\r"
      + "OBX|61|TS_NZ|59777-3^Latest date next dose should be given^LN|1|20360429||||||F\r"
      + "OBX|62|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20170430||||||F\r"
      + "OBX|63|TS_NZ|59778-1^Date dose is overdue^LN|1|20170514||||||F\r"
      + "OBX|64|TS_NZ|30980-7^Date next dose recommended^LN|1|20170430||||||F\r"
      + "OBX|65|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|66|CE|30979-9^Vaccines due next^LN|1|133^Pneumonia^CVX||||||F\r"
      + "OBX|67|CE|59780-7^Immunization Series name^LN|1|133^PCV13^CVX||||||F\r"
      + "OBX|68|TS_NZ|59777-3^Latest date next dose should be given^LN|1|20220429||||||F\r"
      + "OBX|69|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20181225||||||F\r"
      + "OBX|70|TS_NZ|59778-1^Date dose is overdue^LN|1|20190125||||||F\r"
      + "OBX|71|TS_NZ|30980-7^Date next dose recommended^LN|1|20181225||||||F\r"
      + "OBX|72|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|73|CE|30979-9^Vaccines due next^LN|1|31^HepA^CVX||||||F\r"
      + "OBX|74|CE|59780-7^Immunization Series name^LN|1|31^Hep A Peds, unspecified^CVX||||||F\r"
      + "OBX|75|TS_NZ|59777-3^Latest date next dose should be given^LN|1|20360429||||||F\r"
      + "OBX|76|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20180430||||||F\r"
      + "OBX|77|TS_NZ|59778-1^Date dose is overdue^LN|1|20190430||||||F\r"
      + "OBX|78|TS_NZ|30980-7^Date next dose recommended^LN|1|20180430||||||F\r"
      + "OBX|79|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|80|CE|30979-9^Vaccines due next^LN|1|20^DTP^CVX||||||F\r"
      + "OBX|81|CE|59780-7^Immunization Series name^LN|1|20^DTaP^CVX||||||F\r"
      + "OBX|82|TS_NZ|59777-3^Latest date next dose should be given^LN|1|20240429||||||F\r"
      + "OBX|83|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20170611||||||F\r"
      + "OBX|84|TS_NZ|59778-1^Date dose is overdue^LN|1|20170730||||||F\r"
      + "OBX|85|TS_NZ|30980-7^Date next dose recommended^LN|1|20170630||||||F\r"
      + "OBX|86|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|87|CE|30979-9^Vaccines due next^LN|1|3^MMR^CVX||||||F\r"
      + "OBX|88|CE|59780-7^Immunization Series name^LN|1|3^MMR^CVX||||||F\r"
      + "OBX|89|TS_NZ|59777-3^Latest date next dose should be given^LN|1|21370430||||||F\r"
      + "OBX|90|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20180430||||||F\r"
      + "OBX|91|TS_NZ|59778-1^Date dose is overdue^LN|1|20180830||||||F\r"
      + "OBX|92|TS_NZ|30980-7^Date next dose recommended^LN|1|20180430||||||F\r"
      + "OBX|93|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "ORC|RE||8628817\r"
      + "RXA|0|1|20181031|20181031|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|94|CE|30979-9^Vaccines due next^LN|1|114^MCV4^CVX||||||F\r"
      + "OBX|95|CE|59780-7^Immunization Series name^LN|1|114^MCV4-D (Menactra)^CVX||||||F\r"
      + "OBX|96|TS_NZ|59777-3^Latest date next dose should be given^LN|1|20390429||||||F\r"
      + "OBX|97|TS_NZ|30981-5^Earliest date dose should be given^LN|1|20280430||||||F\r"
      + "OBX|98|TS_NZ|59778-1^Date dose is overdue^LN|1|20290430||||||F\r"
      + "OBX|99|TS_NZ|30980-7^Date next dose recommended^LN|1|20280430||||||F\r"
      + "OBX|100|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "BTS|1|\r";

  private static final String RSP_MD_IMMUNET_1 = ""
      + "MSH|^~\\&|IMMUNET2.18.1|IMMUNET||AIRA|20180824132938-0400||RSP^K11^RSP_K11|1pRN-QA.2.1.2|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|1pRN-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated Immunization History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|I84B61526^^^AIRA^MR|Fayette^Acadia^Mariamne^^^^L||20140812|F|113 Dickinson Cir^^Pointe Aux Pins^MI^49775\r"
      + "PID|1||705214^^^^SR~I84B61526^^^^MR||FAYETTE^ACADIA^MARIAMNE^^^^L|TRUMBULL^LEALA^^^^^M|20140812|F||2106-3|113 DICKINSON CIR^^POINTE AUX PINS^MI^49775^USA^P||^PRN^PH^^^231^7394798|||||||||2186-5||N|0|||||N\r"
      + "ORC|RE|AI84B61526.1|3777272\r"
      + "RXA|0|1|20150823|20150823|94^Proquad^CVX^90710^Proquad^CPT|1.0|||01|||||||||||CP|A\r"
      + "OBX|1|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|3|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|1||||||F\r" + "ORC|RE|AI84B61526.2|3777273\r"
      + "RXA|0|1|20180823|20180823|94^Proquad^CVX^90710^Proquad^CPT|1.0|||01|||||||||||CP|A\r"
      + "OBX|1|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|2||||||F\r"
      + "OBX|3|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|2||||||F\r" + "ORC|RE||0\r"
      + "RXA|0|1|20140812|20140812|998^No Vaccine Administered^CVX|999|||||||||||||||A\r"
      + "OBX|1|CE|30979-9^Vaccines Due Next^LN|0|107^DTP/aP^CVX||||||F\r"
      + "OBX|2|DT_T|30980-7^Date Vaccine Due^LN|0|20141012||||||F\r"
      + "OBX|3|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|4|DT_T|30981-5^Earliest date to give^LN|0|20140923||||||F\r"
      + "OBX|5|DT_T|59777-3^Vaccine latest Date^LN|0|20210811||||||F\r"
      + "OBX|6|DT_T|59778-1^Vaccine Overdue Date^LN|0|20141112||||||F\r"
      + "OBX|7|CE|59779-9^Immunization Schedule used^LN|0|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|8|CE|30979-9^Vaccines Due Next^LN|1|45^HepB^CVX^90731^HepB^CPT||||||F\r"
      + "OBX|9|DT_T|30980-7^Date Vaccine Due^LN|1|20140812||||||F\r"
      + "OBX|10|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|11|DT_T|30981-5^Earliest date to give^LN|1|20140812||||||F\r"
      + "OBX|12|DT_T|59778-1^Vaccine Overdue Date^LN|1|20141112||||||F\r"
      + "OBX|13|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|14|CE|30979-9^Vaccines Due Next^LN|2|17^Hib^CVX^90737^Hib^CPT||||||F\r"
      + "OBX|15|DT_T|30980-7^Date Vaccine Due^LN|2|20141012||||||F\r"
      + "OBX|16|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|17|DT_T|30981-5^Earliest date to give^LN|2|20140923||||||F\r"
      + "OBX|18|DT_T|59777-3^Vaccine latest Date^LN|2|20190811||||||F\r"
      + "OBX|19|DT_T|59778-1^Vaccine Overdue Date^LN|2|20141112||||||F\r"
      + "OBX|20|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|21|CE|30979-9^Vaccines Due Next^LN|3|88^Influenza^CVX^90724^Influenza^CPT||||||F\r"
      + "OBX|22|DT_T|30980-7^Date Vaccine Due^LN|3|20150212||||||F\r"
      + "OBX|23|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|24|DT_T|30981-5^Earliest date to give^LN|3|20150212||||||F\r"
      + "OBX|25|DT_T|59778-1^Vaccine Overdue Date^LN|3|20150812||||||F\r"
      + "OBX|26|CE|59779-9^Immunization Schedule used^LN|3|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|27|CE|30979-9^Vaccines Due Next^LN|4|109^Pneumococcal^CVX||||||F\r"
      + "OBX|28|DT_T|30980-7^Date Vaccine Due^LN|4|20141012||||||F\r"
      + "OBX|29|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|30|DT_T|30981-5^Earliest date to give^LN|4|20140923||||||F\r"
      + "OBX|31|DT_T|59777-3^Vaccine latest Date^LN|4|20190811||||||F\r"
      + "OBX|32|DT_T|59778-1^Vaccine Overdue Date^LN|4|20141112||||||F\r"
      + "OBX|33|CE|59779-9^Immunization Schedule used^LN|4|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|34|CE|30979-9^Vaccines Due Next^LN|5|89^Polio^CVX||||||F\r"
      + "OBX|35|DT_T|30980-7^Date Vaccine Due^LN|5|20141012||||||F\r"
      + "OBX|36|NM|30973-2^Vaccine due next dose number^LN|5|1||||||F\r"
      + "OBX|37|DT_T|30981-5^Earliest date to give^LN|5|20140923||||||F\r"
      + "OBX|38|DT_T|59777-3^Vaccine latest Date^LN|5|20320811||||||F\r"
      + "OBX|39|DT_T|59778-1^Vaccine Overdue Date^LN|5|20141112||||||F\r"
      + "OBX|40|CE|59779-9^Immunization Schedule used^LN|5|VXC16^ACIP^CDCPHINVS||||||F\r";

  private static final String RSP_ME_IMMPACT2_1 = ""
      + "MSH|^~\\&|ImmPact IIS|ImmPact IIS||AART|20180824||RSP^K11^RSP_K11|1rrw-QA.2.1.2-D|P|2.5.1|||||||||Z32^CDCPHINVS|ImmPact IIS|AART\r"
      + "MSA|AA|1rrw-QA.2.1.2-D||0||0^Message Accepted^HL70357\r" + "QAK|37374859|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|F48W61583^^^AIRA^MR|Clark^Ideh^Lani^^^^L||20140812|F|376 Thomas Ave^^Saint Clair Shores^MI^48080\r"
      + "PID|1||336122^^^ORA^SR~F48W61583^^^ORA^MR||CLARK^IDEH^LANI|MAGRI^AMIAH|20140812|F||2106-3|376 THOMAS AVE^^SAINT CLAIR SHORES^MI^48080^^H||^PRN^PH^^^586^5244621|||||||||2186-5||N|0\r"
      + "PD1|||||||||||02|N||||A\r"
      + "NK1|1|CLARK^MAGRI^MARION|MTH|376 THOMAS AVE^^SAINT CLAIR SHORES^MI^48080^^P|^^PH^^^586^5244621\r"
      + "ORC|RE||10786489\r" + "RXA|0|1|20180823|20180823|94^MMRV^CVX|1.0|||01|||||||||||CP\r"
      + "OBX|1|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|2||||||F\r"
      + "OBX|3|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|2||||||F\r" + "ORC|RE||10786488\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX|1.0|||01|||||||||||CP\r"
      + "OBX|5|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|6|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|7|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|8|NM|30973-2^Dose number in series^LN|2|1||||||F\r" + "ORC|RE||0\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999\r"
      + "OBX|9|CE|30979-9^Vaccines Due Next^LN|0|107^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r"
      + "OBX|10|TS|30980-7^Date Vaccine Due^LN|0|20141012||||||F\r"
      + "OBX|11|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|12|TS|30981-5^Earliest date to give^LN|0|20140923||||||F\r"
      + "OBX|13|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r"
      + "OBX|14|CE|30979-9^Vaccines Due Next^LN|1|85^HepA^CVX^90730^HepA^CPT||||||F\r"
      + "OBX|15|TS|30980-7^Date Vaccine Due^LN|1|20150812||||||F\r"
      + "OBX|16|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|17|TS|30981-5^Earliest date to give^LN|1|20150812||||||F\r"
      + "OBX|18|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|19|CE|30979-9^Vaccines Due Next^LN|2|45^HepB^CVX^90731^HepB^CPT||||||F\r"
      + "OBX|20|TS|30980-7^Date Vaccine Due^LN|2|20140812||||||F\r"
      + "OBX|21|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|22|TS|30981-5^Earliest date to give^LN|2|20140812||||||F\r"
      + "OBX|23|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|24|CE|30979-9^Vaccines Due Next^LN|3|17^Hib^CVX^90737^Hib^CPT||||||F\r"
      + "OBX|25|TS|30980-7^Date Vaccine Due^LN|3|20151112||||||F\r"
      + "OBX|26|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|27|TS|30981-5^Earliest date to give^LN|3|20151112||||||F\r"
      + "OBX|28|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|29|CE|30979-9^Vaccines Due Next^LN|4|88^Influenza-seasnl^CVX^90724^Influenza-seasnl^CPT||||||F\r"
      + "OBX|30|TS|30980-7^Date Vaccine Due^LN|4|20180801||||||F\r"
      + "OBX|31|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|32|TS|30981-5^Earliest date to give^LN|4|20150212||||||F\r"
      + "OBX|33|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|34|CE|30979-9^Vaccines Due Next^LN|5|133^PneumoConjugate^CVX^90670^PneumoConjugate^CPT||||||F\r"
      + "OBX|35|TS|30980-7^Date Vaccine Due^LN|5|20160811||||||F\r"
      + "OBX|36|NM|30973-2^Vaccine due next dose number^LN|5|1||||||F\r"
      + "OBX|37|TS|30981-5^Earliest date to give^LN|5|20160811||||||F\r"
      + "OBX|38|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|39|CE|30979-9^Vaccines Due Next^LN|6|89^Polio^CVX||||||F\r"
      + "OBX|40|TS|30980-7^Date Vaccine Due^LN|6|20141012||||||F\r"
      + "OBX|41|NM|30973-2^Vaccine due next dose number^LN|6|1||||||F\r"
      + "OBX|42|TS|30981-5^Earliest date to give^LN|6|20140923||||||F\r"
      + "OBX|43|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r";

  private static final String RSP_MI_MCIR_1 = ""
      + "MSH|^~\\&|MCIR|MDCH|MDCH Data Hub Test|1255-60-20|20180531144917033||RSP^K11^RSP_K11|20180531144917.033|P|2.5.1|||NE|AL|||||Z42^CDCPHINVS|\"\"|\"\"\r"
      + "MSA|AA|GRM-QA.2.1.2|Confirmation: -1527792557024\r" + "QAK|37374859|OK\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|Z65N249510^^^AIRA^MR|Coshocton^Ganesa^Zama^^^^L||20140519|F|20 Lake St^^Boyne Falls^MI^49713\r"
      + "PID|1|36411746978^^^MIA|36411746978^^^MIA~Z65N249510^^^MIA||Coshocton^Ganesa^Zama||20140519|F|||20 Lake St ^^Boyne Falls^Michigan^49713^USA||||||||||||||||||||||20180531\r"
      + "NK1|1|Coshocton^Oxford^M|^GRD|20 Lake St ^^Boyne Falls^Michigan^49713^USA\r"
      + "ORC|RE||77608812\r"
      + "RXA|0|1|20150530|20150530|94^MMRV (ProQuad)^CVX|0|mL||01^Historical Information||||||||UNK^Unknown^MVX|||CP\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|94^MMRV (ProQuad)^CVX||||||F\r"
      + "OBX|2|CE|38890-0^Component Vaccine Type^LN|1|94^MMRV (ProQuad)^CVX||||||F|||20180531\r"
      + "OBX|3|CE|59779-9^Vaccine Schedule Used^LN|1|CDCPHINVS||||||F|||20180531\r"
      + "OBX|4|CE|30973-2^Dose Number in Series^LN|1|1||||||F|||20180531\r"
      + "OBX|5|CE|59781-5^Dose Validity^LN|1|Y||||||F|||20180531\r"
      + "OBX|6|CE|38890-0^Component Vaccine Type^LN|2|94^MMRV (ProQuad)^CVX||||||F|||20180531\r"
      + "OBX|7|CE|59779-9^Vaccine Schedule Used^LN|2|CDCPHINVS||||||F|||20180531\r"
      + "OBX|8|CE|30973-2^Dose Number in Series^LN|2|1||||||F|||20180531\r"
      + "OBX|9|CE|59781-5^Dose Validity^LN|2|Y||||||F|||20180531\r" + "ORC|RE||77608813\r"
      + "RXA|0|1|20180530|20180530|94^MMRV (ProQuad)^CVX|0|mL||01^Historical Information||||||||UNK^Unknown^MVX|||CP\r"
      + "OBX|10|CE|30956-7^Vaccine Type^LN|3|94^MMRV (ProQuad)^CVX||||||F\r"
      + "OBX|11|CE|38890-0^Component Vaccine Type^LN|3|94^MMRV (ProQuad)^CVX||||||F|||20180531\r"
      + "OBX|12|CE|59779-9^Vaccine Schedule Used^LN|3|CDCPHINVS||||||F|||20180531\r"
      + "OBX|13|CE|30973-2^Dose Number in Series^LN|3|2||||||F|||20180531\r"
      + "OBX|14|CE|59781-5^Dose Validity^LN|3|Y||||||F|||20180531\r"
      + "OBX|15|CE|38890-0^Component Vaccine Type^LN|4|94^MMRV (ProQuad)^CVX||||||F|||20180531\r"
      + "OBX|16|CE|59779-9^Vaccine Schedule Used^LN|4|CDCPHINVS||||||F|||20180531\r"
      + "OBX|17|CE|30973-2^Dose Number in Series^LN|4|2||||||F|||20180531\r"
      + "OBX|18|CE|59781-5^Dose Validity^LN|4|Y||||||F|||20180531\r" + "ORC|RE||9999\r"
      + "RXA|0|1|20180531|20180531|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|19|CE|30979-9^Vaccine Due Next^LN|5|20^DTaP (pediatric)^CVX||||||F|||20180531\r"
      + "OBX|20|CE|59779-9^Vaccine Schedule Used^LN|5|CDCPHINVS||||||F|||20180531\r"
      + "OBX|21|TS|30980-7^Date Vaccine Due^LN|5|20140719||||||F|||20180531\r"
      + "OBX|22|TS|30981-5^Earliest Date to Give^LN|5|20140630||||||F|||20180531\r"
      + "OBX|23|CE|59783-1^Status in immunization series^LN|5|4^Overdue^eval_result_id||||||F|||20180531\r"
      + "ORC|RE||9999\r"
      + "RXA|0|1|20180531|20180531|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|24|CE|30979-9^Vaccine Due Next^LN|6|10^IPV (polio)^CVX||||||F|||20180531\r"
      + "OBX|25|CE|59779-9^Vaccine Schedule Used^LN|6|CDCPHINVS||||||F|||20180531\r"
      + "OBX|26|TS|30980-7^Date Vaccine Due^LN|6|20140719||||||F|||20180531\r"
      + "OBX|27|TS|30981-5^Earliest Date to Give^LN|6|20140630||||||F|||20180531\r"
      + "OBX|28|CE|59783-1^Status in immunization series^LN|6|4^Overdue^eval_result_id||||||F|||20180531\r"
      + "ORC|RE||9999\r"
      + "RXA|0|1|20180531|20180531|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|29|CE|30979-9^Vaccine Due Next^LN|7|03^MMR^CVX||||||F|||20180531\r"
      + "OBX|30|CE|59779-9^Vaccine Schedule Used^LN|7|CDCPHINVS||||||F|||20180531\r"
      + "OBX|31|CE|59783-1^Status in immunization series^LN|7|1^Complete^eval_result_id||||||F|||20180531\r"
      + "ORC|RE||9999\r"
      + "RXA|0|1|20180531|20180531|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|32|CE|30979-9^Vaccine Due Next^LN|8|17^Hib (historical)^CVX||||||F|||20180531\r"
      + "OBX|33|CE|59779-9^Vaccine Schedule Used^LN|8|CDCPHINVS||||||F|||20180531\r"
      + "OBX|34|TS|30980-7^Date Vaccine Due^LN|8|20140719||||||F|||20180531\r"
      + "OBX|35|TS|30981-5^Earliest Date to Give^LN|8|20140630||||||F|||20180531\r"
      + "OBX|36|CE|59783-1^Status in immunization series^LN|8|4^Overdue^eval_result_id||||||F|||20180531\r"
      + "ORC|RE||9999\r"
      + "RXA|0|1|20180531|20180531|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|37|CE|30979-9^Vaccine Due Next^LN|9|21^Varicella (Varivax)^CVX||||||F|||20180531\r"
      + "OBX|38|CE|59779-9^Vaccine Schedule Used^LN|9|CDCPHINVS||||||F|||20180531\r"
      + "OBX|39|CE|59783-1^Status in immunization series^LN|9|1^Complete^eval_result_id||||||F|||20180531\r"
      + "ORC|RE||9999\r"
      + "RXA|0|1|20180531|20180531|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|40|CE|30979-9^Vaccine Due Next^LN|10|133^PCV13 (Prevnar13)^CVX||||||F|||20180531\r"
      + "OBX|41|CE|59779-9^Vaccine Schedule Used^LN|10|CDCPHINVS||||||F|||20180531\r"
      + "OBX|42|TS|30980-7^Date Vaccine Due^LN|10|20140719||||||F|||20180531\r"
      + "OBX|43|TS|30981-5^Earliest Date to Give^LN|10|20140630||||||F|||20180531\r"
      + "OBX|44|CE|59783-1^Status in immunization series^LN|10|4^Overdue^eval_result_id||||||F|||20180531\r"
      + "ORC|RE||9999\r"
      + "RXA|0|1|20180531|20180531|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|45|CE|30979-9^Vaccine Due Next^LN|11|85^Hep A (historical)^CVX||||||F|||20180531\r"
      + "OBX|46|CE|59779-9^Vaccine Schedule Used^LN|11|CDCPHINVS||||||F|||20180531\r"
      + "OBX|47|TS|30980-7^Date Vaccine Due^LN|11|20150519||||||F|||20180531\r"
      + "OBX|48|TS|30981-5^Earliest Date to Give^LN|11|20150519||||||F|||20180531\r"
      + "OBX|49|CE|59783-1^Status in immunization series^LN|11|4^Overdue^eval_result_id||||||F|||20180531\r"
      + "ORC|RE||9999\r"
      + "RXA|0|1|20180531|20180531|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|50|CE|30979-9^Vaccine Due Next^LN|12|88^Influenza (Historical)^CVX||||||F|||20180531\r"
      + "OBX|51|CE|59779-9^Vaccine Schedule Used^LN|12|CDCPHINVS||||||F|||20180531\r"
      + "OBX|52|TS|30980-7^Date Vaccine Due^LN|12|20170801||||||F|||20180531\r"
      + "OBX|53|TS|30981-5^Earliest Date to Give^LN|12|20170701||||||F|||20180531\r"
      + "OBX|54|CE|59783-1^Status in immunization series^LN|12|4^Overdue^eval_result_id||||||F|||20180531\r";

  private static final String RSP_MN_MIIC_1 = ""
      + "FHS|^~\\&|MIIC|MIIC||ATEST|20180827143932.277||3540740.response\r"
      + "BHS|^~\\&|MIIC|MIIC||ATEST|20180827143932.277\r"
      + "MSH|^~\\&|MIIC|MIIC||ATEST|20180827143932.442||RSP^K11^RSP_K11|2Oqh-QA.2.1.2|P|2.5.1|||||||||Z42^CDCPHINVS\r"
      + "MSA|AA|2Oqh-QA.2.1.2||0||0^Message Accepted^HL70357\r" + "QAK|2Oqh-QA.2.1.2|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|L46U79434^^^AIRA^MR|Armstrong^Taci^Freda^^^^L||20140815|F|308 Calloway Ln^^Standish^MI^48658||||||||Hello\r"
      + "PID|1||8822621^^^^SR~L46U79434^^^^PI||ARMSTRONG^TACI^FREDA|ULSETH^SIDDHI|20140815|F|||308 CALLOWAY LN^^STANDISH^MI^48658^^L|||||||||||||N|0\r"
      + "PD1|||||||||||02|N||||A\r"
      + "NK1|1|ARMSTRONG^ULSETH^MARION|32|308 CALLOWAY LN^^STANDISH^MI^48658^^M|^^PH^^^989^2286304\r"
      + "ORC|RE||51803333\r" + "RXA|0|1|20150826|20150826|94^MMRV^CVX^90710^MMRV^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20150826|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||51803334\r" + "RXA|0|1|20180826|20180826|94^MMRV^CVX^90710^MMRV^CPT|1.0|mL||01\r"
      + "OBX|1|CE|64994-7^Vaccine Eligibility Code^LN|1|V00^Unknown/Not determined^HL70064||||||F|||20180826|||VXC40^per immunization^CDCPHINVS\r"
      + "ORC|RE||0\r" + "RXA|0|1|20140815|20140815|998^No Vaccine Administered^CVX|999\r"
      + "OBX|1|CE|30979-9^Vaccines Due Next^LN|0|20^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r"
      + "OBX|2|TS|30980-7^Date Vaccine Due^LN|0|20141015||||||F\r"
      + "OBX|3|NM|30973-2^Vaccine due next dose number^LN|0|0||||||F\r"
      + "OBX|4|TS|30981-5^Earliest date to give^LN|0|20140926||||||F\r"
      + "OBX|5|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r"
      + "OBX|6|CE|30979-9^Vaccines Due Next^LN|1|31^HepA^CVX||||||F\r"
      + "OBX|7|TS|30980-7^Date Vaccine Due^LN|1|20150815||||||F\r"
      + "OBX|8|NM|30973-2^Vaccine due next dose number^LN|1|0||||||F\r"
      + "OBX|9|TS|30981-5^Earliest date to give^LN|1|20150815||||||F\r"
      + "OBX|10|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|11|CE|30979-9^Vaccines Due Next^LN|2|45^HepB^CVX^90731^HepB^CPT||||||F\r"
      + "OBX|12|TS|30980-7^Date Vaccine Due^LN|2|20140815||||||F\r"
      + "OBX|13|NM|30973-2^Vaccine due next dose number^LN|2|0||||||F\r"
      + "OBX|14|TS|30981-5^Earliest date to give^LN|2|20140815||||||F\r"
      + "OBX|15|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|16|CE|30979-9^Vaccines Due Next^LN|3|17^Hib^CVX^90737^Hib^CPT||||||F\r"
      + "OBX|17|TS|30980-7^Date Vaccine Due^LN|3|20151115||||||F\r"
      + "OBX|18|NM|30973-2^Vaccine due next dose number^LN|3|0||||||F\r"
      + "OBX|19|TS|30981-5^Earliest date to give^LN|3|20151115||||||F\r"
      + "OBX|20|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|21|CE|30979-9^Vaccines Due Next^LN|4|88^Influenza^CVX^90724^Influenza^CPT||||||F\r"
      + "OBX|22|TS|30980-7^Date Vaccine Due^LN|4|20180701||||||F\r"
      + "OBX|23|NM|30973-2^Vaccine due next dose number^LN|4|0||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|4|20150215||||||F\r"
      + "OBX|25|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|26|CE|30979-9^Vaccines Due Next^LN|5|109^Pneumo-conj^CVX||||||F\r"
      + "OBX|27|TS|30980-7^Date Vaccine Due^LN|5|20160815||||||F\r"
      + "OBX|28|NM|30973-2^Vaccine due next dose number^LN|5|0||||||F\r"
      + "OBX|29|TS|30981-5^Earliest date to give^LN|5|20160815||||||F\r"
      + "OBX|30|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|31|CE|30979-9^Vaccines Due Next^LN|6|89^Polio^CVX||||||F\r"
      + "OBX|32|TS|30980-7^Date Vaccine Due^LN|6|20141015||||||F\r"
      + "OBX|33|NM|30973-2^Vaccine due next dose number^LN|6|0||||||F\r"
      + "OBX|34|TS|30981-5^Earliest date to give^LN|6|20140926||||||F\r"
      + "OBX|35|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r"
      + "BTS|1\r" + "FTS|1\r";

  private static final String RSP_MS_MIIX_1 = ""
      + "MSH|^~\\&|^^|^^|^^|70860^^|20180827143939||RSP^K11^RSP_K11|1914051677.100087824|P|2.5.1|||||||||Z42^CDCPHINVS^^|\r"
      + "MSA|AA|2Ug0-QA.2.1.2|\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^HL70471|\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|37374859|J76Y79521^^^AIRA^MR|Baker^Alodie^Wilma||20140815|F|L^^Hillsdale^MI^49242|\r"
      + "PID|1||3886300^^^^SR~~~~~J76Y79521^^^^MR||BAKER^ALODIE^WILMA^^^^L|GEAUGA|20140815|F|||21 SOWDEN LN^^HILLSDALE^MICHIGAN^49242^United States^M^^HILLSDALE||5179351466^PRN^PH^^^517^9351466|||||||||2186-5^not Hispanic or Latino^HL70189||N|1|||||N|\r"
      + "ORC|RE||3886300.54.20150826|\r"
      + "RXA|0|999|20150826|20150826|94^MMRV^CVX^90710^MMRV^CPT|999|||01^Historical information - source unspecified^NIP001||^^^AIRA TEST^^^^^570 EAST WOODROW WILSON^^JACKSON^MS^39215||||||||||A|20180827143939|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20150826|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20150826|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||3886300.54.20180826|\r"
      + "RXA|0|999|20180826|20180826|94^MMRV^CVX^90710^MMRV^CPT|999|||01^Historical information - source unspecified^NIP001||^^^AIRA TEST^^^^^570 EAST WOODROW WILSON^^JACKSON^MS^39215||||||||||A|20180827143939|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20180826|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20180826|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827143939|20180827143939|998^no vaccine administered^CVX|0||||||||||||||NA||20180827143939|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|45^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827143939|20180827143939|998^no vaccine administered^CVX|0||||||||||||||NA||20180827143939|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|107^DTAP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827143939|20180827143939|998^no vaccine administered^CVX|0||||||||||||||NA||20180827143939|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|17^HIB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141215||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827143939|20180827143939|998^no vaccine administered^CVX|0||||||||||||||NA||20180827143939|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|133^PneumoPCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827143939|20180827143939|998^no vaccine administered^CVX|0||||||||||||||NA||20180827143939|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|89^POLIO^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827143939|20180827143939|998^no vaccine administered^CVX|0||||||||||||||NA||20180827143939|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|85^HepA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20160815||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827143939|20180827143939|998^no vaccine administered^CVX|0||||||||||||||NA||20180827143939|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|137^HPV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20230815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21140815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270815||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827143939|20180827143939|998^no vaccine administered^CVX|0||||||||||||||NA||20180827143939|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|147^MENING^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20240815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270815||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";

  private static final String RSP_MT_IMMTRAX_1 = ""
      + "MSH|^~\\&|^^|^^|^^|^^|20180824113619||RSP^K11^RSP_K11|2859302798.100001142|P|2.5.1|||||||||Z42^CDCPHINVS^^|\r"
      + "MSA|AA|1L0Z-QA.2.1.2|\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^HL70471|\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|37374859|N77E61701^^^AIRA^MR|Iversen^Lilliana^Artemis||20140812|F|L^^Kingston^MI^48741|\r"
      + "PID|1||7328750^^^^SR~~~~~N77E61701||IVERSEN^LILLIANA^ARTEMIS^^^^L|ATOKA|20140812|F||2106-3^White^CDCPHINVS|314 Brazoria Pl^^Kingston^MI^48741^US^M^^NONMT~~^^^^^US^BDL|||||||||||2186-5^not Hispanic or Latino^HL70189||N|1|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140912||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140909||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141212||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^DTP/aP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141012||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140923||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Hib^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141012||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140923||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141212||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^PCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141012||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140923||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Polio^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141012||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140923||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^DTP/aP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141212||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20141109||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Hib^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141212||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20141109||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^PCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141212||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20141109||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Polio^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141212||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20141109||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150112||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^DTP/aP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150212||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150109||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150314||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Influenza^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150212||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150212||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150314||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150212||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150127||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20160312||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Influenza^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150312||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150312||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150411||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Polio^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150612||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150612||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20160312||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^DTP/aP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20160312||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Hep A^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20160812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Hib^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150911||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^MMR^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20151212||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^PCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150911||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Varicella^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20151212||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Influenza^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20151001||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150701||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20151031||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Hib^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20151007||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20151007||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20151212||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^PCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20151007||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20151007||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20151212||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Hep A^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20160212||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20160212||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170312||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^DTP/aP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20180812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20210812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^MMR^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20180812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150909||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20210812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Polio^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20180812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20210812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^Varicella^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20180812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20151104||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20210812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^HPV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20230812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21140812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^MCV4^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20240812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^HPV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20260212||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20260112||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21140812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20260812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^MCV4^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20300812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20300812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20330812||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^MenB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20300812||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20240812||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20400812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20300911||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180824113619|20180824113619|998^no vaccine administered^CVX|0||||||||||||||NA||20180824113620|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|0^MenB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|0||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20300909||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20300909||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20400812||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20301009||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";

  private static final String RSP_NC_NCIR_1 = ""
      + "MSH|^~\\&|NCIR10.2.0|NCIR|CDCAIRAT|CDCAIRAT^^|20180828113440-0400||RSP^K11^RSP_K11|20180828113440RM01|P^|2.5.1|||NE|NE|||||Z42^CDCPHINVS|NCIR|CDCAIRAT\r"
      + "MSA|AA|1SxL-QA.2.1.2|||\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS|||\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|R31V92783^^^AIRA^MR|Carroll^Susila^Pyrena^^^^L||20140816|F|301 Morris Ln^^Oak Grove^MI^48863|\r"
      + "PID|1||13022694^^^NCA^SR^~R31V92783^^^CDCAIRAT^PI^||CARROLL^SUSILA^PYRENA^^^^L^|EGLAND^FELCIA^^^^^M^|20140816|F||2106-3^White^CDCREC^^^||||||||||||2186-5^not Hispanic or Latino^CDCREC^^^||N|0|||||N|||||||||\r"
      + "ORC|RE||105520648||||||||||||||||||||||||||||\r"
      + "RXA|0|1|20150827|20150827|94^MMRV^CVX^90710^MMRV^C4|999|||01^Historical information - source unspecified^NIP001^^^||^^^23337^^^^^^^^^^^||||||UNK^UNKNOWN^MVX^^^|||CP|A|||||\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|94^Measles, Mumps, Rubella^CVX^90710^MMRV^CPT||||||F|||20180828|||||||||||\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180828|||||||||||\r"
      + "OBX|3|NM|30973-2^Dose number in series^LN|1|1|NA^^HL70353|||||F|||20180828|||||||||||\r"
      + "OBX|4|CE|30956-7^Vaccine Type^LN|2|94^Varicella^CVX^90710^MMRV^CPT||||||F|||20180828|||||||||||\r"
      + "OBX|5|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180828|||||||||||\r"
      + "OBX|6|NM|30973-2^Dose number in series^LN|2|1|NA^^HL70353|||||F|||20180828|||||||||||\r"
      + "ORC|RE||105520649||||||||||||||||||||||||||||\r"
      + "RXA|0|1|20180827|20180827|94^MMRV^CVX^90710^MMRV^C4|999|||01^Historical information - source unspecified^NIP001^^^||^^^23337^^^^^^^^^^^||||||UNK^UNKNOWN^MVX^^^|||CP|A|||||\r"
      + "OBX|7|CE|30956-7^Vaccine Type^LN|1|94^Measles, Mumps, Rubella^CVX^90710^MMRV^CPT||||||F|||20180828|||||||||||\r"
      + "OBX|8|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180828|||||||||||\r"
      + "OBX|9|NM|30973-2^Dose number in series^LN|1|2|NA^^HL70353|||||F|||20180828|||||||||||\r"
      + "OBX|10|CE|30956-7^Vaccine Type^LN|2|94^Varicella^CVX^90710^MMRV^CPT||||||F|||20180828|||||||||||\r"
      + "OBX|11|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180828|||||||||||\r"
      + "OBX|12|NM|30973-2^Dose number in series^LN|2|2|NA^^HL70353|||||F|||20180828|||||||||||\r"
      + "ORC|RE||9999||||||||||\r"
      + "RXA|0|1|20140816|20140816|998^No Vaccine Administered^CVX|999|\r"
      + "OBX|13|CE|30956-7^Vaccine Type^LN|0|107^DTaP, unspecified formulation^CVX||||||F|||20180828|||||||||||\r"
      + "OBX|14|CE|59779-9^Immunization Schedule used^LN|0|VXC16^ACIP^CDCPHINVS||||||F|||20180828|||||||||||\r"
      + "OBX|15|NM|30973-2^Dose number in series^LN|0|1|NA^^HL70353|||||F|||20180828|||||||||||\r"
      + "OBX|16|DT|30981-5^Earliest date to give^LN|0|20140927||||||F|||20180828|||||||||||\r"
      + "OBX|17|DT|30980-7^Date vaccine due^LN|0|20141016||||||F|||20180828|||||||||||\r"
      + "OBX|18|DT|59778-1^Date when overdue^LN|0|20141116||||||F|||20180828|||||||||||\r"
      + "OBX|19|DT|59777-3^Latest date next dose may be given^LN|0|20210815||||||F|||20180828|||||||||||\r"
      + "OBX|20|CE|30956-7^Vaccine Type^LN|1|85^HepA, unspecified formulation^CVX||||||F|||20180828|||||||||||\r"
      + "OBX|21|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180828|||||||||||\r"
      + "OBX|22|NM|30973-2^Dose number in series^LN|1|1|NA^^HL70353|||||F|||20180828|||||||||||\r"
      + "OBX|23|DT|30981-5^Earliest date to give^LN|1|20150816||||||F|||20180828|||||||||||\r"
      + "OBX|24|DT|30980-7^Date vaccine due^LN|1|20150816||||||F|||20180828|||||||||||\r"
      + "OBX|25|DT|59778-1^Date when overdue^LN|1|20160816||||||F|||20180828|||||||||||\r"
      + "OBX|26|CE|30956-7^Vaccine Type^LN|2|45^HepB, unspecified formulation^CVX||||||F|||20180828|||||||||||\r"
      + "OBX|27|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180828|||||||||||\r"
      + "OBX|28|NM|30973-2^Dose number in series^LN|2|1|NA^^HL70353|||||F|||20180828|||||||||||\r"
      + "OBX|29|DT|30981-5^Earliest date to give^LN|2|20140816||||||F|||20180828|||||||||||\r"
      + "OBX|30|DT|30980-7^Date vaccine due^LN|2|20140816||||||F|||20180828|||||||||||\r"
      + "OBX|31|DT|59778-1^Date when overdue^LN|2|20141116||||||F|||20180828|||||||||||\r"
      + "OBX|32|CE|30956-7^Vaccine Type^LN|3|17^Hib, unspecified formulation^CVX||||||F|||20180828|||||||||||\r"
      + "OBX|33|CE|59779-9^Immunization Schedule used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180828|||||||||||\r"
      + "OBX|34|NM|30973-2^Dose number in series^LN|3|1|NA^^HL70353|||||F|||20180828|||||||||||\r"
      + "OBX|35|DT|30981-5^Earliest date to give^LN|3|20151116||||||F|||20180828|||||||||||\r"
      + "OBX|36|DT|30980-7^Date vaccine due^LN|3|20151116||||||F|||20180828|||||||||||\r"
      + "OBX|37|DT|59778-1^Date when overdue^LN|3|20151216||||||F|||20180828|||||||||||\r"
      + "OBX|38|DT|59777-3^Latest date next dose may be given^LN|3|20190815||||||F|||20180828|||||||||||\r"
      + "OBX|39|CE|30956-7^Vaccine Type^LN|4|88^Influenza, unspecified formulation^CVX||||||F|||20180828|||||||||||\r"
      + "OBX|40|CE|59779-9^Immunization Schedule used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180828|||||||||||\r"
      + "OBX|41|NM|30973-2^Dose number in series^LN|4|1|NA^^HL70353|||||F|||20180828|||||||||||\r"
      + "OBX|42|DT|30981-5^Earliest date to give^LN|4|20150216||||||F|||20180828|||||||||||\r"
      + "OBX|43|DT|30980-7^Date vaccine due^LN|4|20150216||||||F|||20180828|||||||||||\r"
      + "OBX|44|DT|59778-1^Date when overdue^LN|4|20150816||||||F|||20180828|||||||||||\r"
      + "OBX|45|CE|30956-7^Vaccine Type^LN|5|109^Pneumococcal, unspecified formulation^CVX||||||F|||20180828|||||||||||\r"
      + "OBX|46|CE|59779-9^Immunization Schedule used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180828|||||||||||\r"
      + "OBX|47|NM|30973-2^Dose number in series^LN|5|1|NA^^HL70353|||||F|||20180828|||||||||||\r"
      + "OBX|48|DT|30981-5^Earliest date to give^LN|5|20140927||||||F|||20180828|||||||||||\r"
      + "OBX|49|DT|30980-7^Date vaccine due^LN|5|20141016||||||F|||20180828|||||||||||\r"
      + "OBX|50|DT|59778-1^Date when overdue^LN|5|20141116||||||F|||20180828|||||||||||\r"
      + "OBX|51|DT|59777-3^Latest date next dose may be given^LN|5|20190815||||||F|||20180828|||||||||||\r"
      + "OBX|52|CE|30956-7^Vaccine Type^LN|6|89^Polio, unspecified formulation^CVX||||||F|||20180828|||||||||||\r"
      + "OBX|53|CE|59779-9^Immunization Schedule used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180828|||||||||||\r"
      + "OBX|54|NM|30973-2^Dose number in series^LN|6|1|NA^^HL70353|||||F|||20180828|||||||||||\r"
      + "OBX|55|DT|30981-5^Earliest date to give^LN|6|20140927||||||F|||20180828|||||||||||\r"
      + "OBX|56|DT|30980-7^Date vaccine due^LN|6|20141016||||||F|||20180828|||||||||||\r"
      + "OBX|57|DT|59778-1^Date when overdue^LN|6|20141116||||||F|||20180828|||||||||||\r";

  private static final String RSP_ND_SIIS_1 = ""
      + "MSH|^~\\&|ND000|NDIIS|IHS00048|101901|20180824121617||RSP^K11^RSP_K11|2018082412161700001|P|2.5.1|||||||||Z42^CDCPHHINVS\r"
      + "MSA|AA|1PYW-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|D73L61815^^^AIRA^MR|Worley^Sigrid^Revati|^|20140812|F|32 LaRue Pl^^^Schaffer^MI^49807^^|^^^^^^|||\r"
      + "PID|1||0001479566^^^NDIIS^SR||Worley^Sigrid^Revati|^Power^|20140812|F||2106-3^^HL70005|32 LaRue Pl^^Schaffer^MI^49807^^^^|||||||||||2186-5^^HL70189||||||||\r"
      + "PD1||||||||||||N|20180824|||||\r" + "NK1|1|Worley^Power^|MTH^MOTHER^HL70063||\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180823|20180823|94^MMRV^CVX|999|||01^Historical information - source unspecified^NIP0001||^^^300 - IHS-BELCOURT||||||UNK^Unknown^MVX||||\r"
      + "RXR|^HL70162\r"
      + "OBX|1|CE|30963-3^funding source for immunization^LN|1|PHC70^Privately funded^CDCPHINVS||||||F|||20180823\r"
      + "OBX|1|TS|38890-0^Component vaccine type^LN|1|94^MMRV^CVX||||||F\r"
      + "OBX|1|ID|59781-5^Dose Validity^LN|1|Y||||||F\r"
      + "OBX|1|ID|59781-5^Dose Validity^LN|2|Y||||||F\r"
      + "OBX|2|ID|64994-7^Vaccine funding program eligibility category^LN||V01^NOT VFC ELIGIBLE^HL70064||||||F\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX|999|||01^Historical information - source unspecified^NIP0001||^^^300 - IHS-BELCOURT||||||UNK^Unknown^MVX||||\r"
      + "RXR|^HL70162\r"
      + "OBX|1|CE|30963-3^funding source for immunization^LN|1|PHC70^Privately funded^CDCPHINVS||||||F|||20150823\r"
      + "OBX|1|TS|38890-0^Component vaccine type^LN|1|94^MMRV^CVX||||||F\r"
      + "OBX|1|ID|59781-5^Dose Validity^LN|1|Y||||||F\r"
      + "OBX|1|ID|59781-5^Dose Validity^LN|2|Y||||||F\r"
      + "OBX|2|ID|64994-7^Vaccine funding program eligibility category^LN||V01^NOT VFC ELIGIBLE^HL70064||||||F\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|045^Hep B^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20140812||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|20340811||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20140812||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|010^Polio^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20141012||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|20320811||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20140923||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|020^DTaP^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20141012||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|20210811||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20140923||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|017^Hib^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20141012||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|20190811||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20140923||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|133^PCV^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20141012||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|20190811||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20140923||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|085^Hep A^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20150812||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|20330811||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20150812||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|088^Influenza^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20180801||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|21340812||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20180801||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|115^Td/Tdap^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20210812||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|21340812||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20210812||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|114^MCV4^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20250812||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|20360811||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20250812||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|062^HPV^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20250812||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|20410811||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20230812||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r"
      + "OBX||TS|30956-7^vaccine type^LN|1|164^Meningococcal B^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20300812||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|20380811||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20300812||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|187^Shingles^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20640812||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|21340812||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20640812||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r"
      + "ORC|RE||0001479566^NDIIS||||||||||||||\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999|||||||||||||||\r"
      + "RXR|^HL70162\r" + "OBX||TS|30956-7^vaccine type^LN|1|033^PPV^CVX||||||F||20180824\r"
      + "OBX||CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX||DT|30980-7^Date vaccination due^LN|1|20790812||||||F|||20180824\r"
      + "OBX||DT|59777-3^Latest date to give vaccine^LN|1|21340812||||||F|||20180824\r"
      + "OBX||DT|30981-5^Earliest date to give^LN|1|20790812||||||F|||20180824\r"
      + "OBX||NM|30973-2^Dose number in series^LN|1|1||||||F|||20180824\r";

  private static final String RSP_NE_NESIIS_1 = ""
      + "MSH|^~\\&|NESIIS|NESIIS||WPIH|20180830||RSP^K11^RSP_K11|878263.1|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS||WPIH\r"
      + "MSA|AA|44vv-QA.2.1.2||0||0^Message Accepted^HL70357\r" + "QAK|37374859|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|U20V97921^^^AIRA^MR|Norman^Helena^Katelyn^^^^L||20140818|F|189 Siskiyou Ln^^Mi Department of Revenue^MI^48956\r"
      + "PID|1||7358563^^^NEA^SR~U20V97921^^^WPIH^MR||NORMAN^HELENA^KATELYN|BANDERA^NICOLA|20140818|F||2106-3|189 SISKIYOU LN^^MI DEPARTMENT OF REVENUE^MI^48956^^P||^^PH^^^517^3960533|||||||||2186-5||N|0\r"
      + "ORC|RE||41671149\r" + "RXA|0|1|20150829|20150829|94^MMRV^CVX^MMR^^WVGC|1.0|||01\r"
      + "OBX|1|CE|30956-7^VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|2|CE|59779-9^Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|3|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|4|CE|30956-7^VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|5|CE|59779-9^Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|6|NM|30973-2^Dose number in series^LN|2|1||||||F\r" + "ORC|RE||41671150\r"
      + "RXA|0|1|20170828|20170828|94^MMRV^CVX^MMR^^WVGC|1.0|||01\r"
      + "OBX|7|CE|30956-7^VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|8|CE|59779-9^Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|9|NM|30973-2^Dose number in series^LN|1|2||||||F\r"
      + "OBX|10|CE|30956-7^VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|11|CE|59779-9^Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|12|NM|30973-2^Dose number in series^LN|2|2||||||F\r" + "ORC|RE||0\r"
      + "RXA|0|1|20140818|20140818|998^No Vaccine Administered^CVX|999\r"
      + "OBX|13|CE|30956-7^VACCINE_TYPE^LN|0|107^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r"
      + "OBX|14|TS|30980-7^Date Vaccine Due^LN|0|20141018||||||F\r"
      + "OBX|15|TS|59778-1^Date Dose Is Overdue^LN|0|20141118||||||F\r"
      + "OBX|16|TS|59777-3^Latest Next Date Dose Should Be Given^LN|0|20210817||||||F\r"
      + "OBX|17|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|18|TS|30981-5^Earliest date to give^LN|0|20140929||||||F\r"
      + "OBX|19|CE|59779-9^Schedule Used^LN|0|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|20|CE|30956-7^VACCINE_TYPE^LN|1|85^HepA^CVX^90730^HepA^CPT||||||F\r"
      + "OBX|21|TS|30980-7^Date Vaccine Due^LN|1|20150818||||||F\r"
      + "OBX|22|TS|59778-1^Date Dose Is Overdue^LN|1|20160818||||||F\r"
      + "OBX|23|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|1|20150818||||||F\r"
      + "OBX|25|CE|59779-9^Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|26|CE|30956-7^VACCINE_TYPE^LN|2|45^HepB^CVX^90731^HepB^CPT||||||F\r"
      + "OBX|27|TS|30980-7^Date Vaccine Due^LN|2|20140818||||||F\r"
      + "OBX|28|TS|59778-1^Date Dose Is Overdue^LN|2|20140915||||||F\r"
      + "OBX|29|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|30|TS|30981-5^Earliest date to give^LN|2|20140818||||||F\r"
      + "OBX|31|CE|59779-9^Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|32|CE|30956-7^VACCINE_TYPE^LN|3|17^Hib^CVX^90737^Hib^CPT||||||F\r"
      + "OBX|33|TS|30980-7^Date Vaccine Due^LN|3|20141018||||||F\r"
      + "OBX|34|TS|59778-1^Date Dose Is Overdue^LN|3|20141118||||||F\r"
      + "OBX|35|TS|59777-3^Latest Next Date Dose Should Be Given^LN|3|20190817||||||F\r"
      + "OBX|36|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|37|TS|30981-5^Earliest date to give^LN|3|20140929||||||F\r"
      + "OBX|38|CE|59779-9^Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|39|CE|30956-7^VACCINE_TYPE^LN|4|88^Influenza^CVX^90724^Influenza^CPT||||||F\r"
      + "OBX|40|TS|30980-7^Date Vaccine Due^LN|4|20180801||||||F\r"
      + "OBX|41|TS|59778-1^Date Dose Is Overdue^LN|4|20150818||||||F\r"
      + "OBX|42|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|43|TS|30981-5^Earliest date to give^LN|4|20150218||||||F\r"
      + "OBX|44|CE|59779-9^Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|45|CE|30956-7^VACCINE_TYPE^LN|5|108^Meningo^CVX||||||F\r"
      + "OBX|46|TS|30980-7^Date Vaccine Due^LN|5|20250818||||||F\r"
      + "OBX|47|TS|59778-1^Date Dose Is Overdue^LN|5|20270818||||||F\r"
      + "OBX|48|TS|59777-3^Latest Next Date Dose Should Be Given^LN|5|20360817||||||F\r"
      + "OBX|49|NM|30973-2^Vaccine due next dose number^LN|5|1||||||F\r"
      + "OBX|50|TS|30981-5^Earliest date to give^LN|5|20250818||||||F\r"
      + "OBX|51|CE|59779-9^Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|52|CE|30956-7^VACCINE_TYPE^LN|6|109^Pneumococcal^CVX||||||F\r"
      + "OBX|53|TS|30980-7^Date Vaccine Due^LN|6|20141018||||||F\r"
      + "OBX|54|TS|59778-1^Date Dose Is Overdue^LN|6|20141118||||||F\r"
      + "OBX|55|TS|59777-3^Latest Next Date Dose Should Be Given^LN|6|20190817||||||F\r"
      + "OBX|56|NM|30973-2^Vaccine due next dose number^LN|6|1||||||F\r"
      + "OBX|57|TS|30981-5^Earliest date to give^LN|6|20140929||||||F\r"
      + "OBX|58|CE|59779-9^Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|59|CE|30956-7^VACCINE_TYPE^LN|7|89^Polio^CVX||||||F\r"
      + "OBX|60|TS|30980-7^Date Vaccine Due^LN|7|20141018||||||F\r"
      + "OBX|61|TS|59778-1^Date Dose Is Overdue^LN|7|20141118||||||F\r"
      + "OBX|62|NM|30973-2^Vaccine due next dose number^LN|7|1||||||F\r"
      + "OBX|63|TS|30981-5^Earliest date to give^LN|7|20140929||||||F\r"
      + "OBX|64|CE|59779-9^Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F\r";

  private static final String RSP_NH_VAXNH_1 = ""
      + "MSH|^~\\&|^^|^^|^^|141^^|20180508072428||RSP^K11^RSP_K11|9794716861.100000850|P|2.5.1|||||||||Z42^CDCPHINVS^^|\r"
      + "MSA|AA|hRf-QA.2.1.2|\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^HL70471|\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|37374859|Q65G28032^^^AIRA^MR|Gage^Georgia^Danica||20140426|F|L^^Eastport^MI^49627|\r"
      + "PID|1||12729^^^^SR~~~~~Q65G28032^^^^MR||GAGE^GEORGIA^DANICA^^^^L|PIMA|20140426|F|||88 SCHLEICHER AVE^^EASTPORT^MICHIGAN^49627^United States^M^^ANTRIM||2315997081^PRN^PH^^^231^5997081|||||||||2186-5^not Hispanic or Latino^HL70189||N|1|||||N|\r"
      + "ORC|RE||12729.54.20150507|\r"
      + "RXA|0|999|20150507|20150507|94^MMRV^CVX^90710^MMRV^CPT|999|||01^Historical information - source unspecified^NIP001||^^^AIRA-AART^^^^^123 MAIN STREEET^^CONCORD^NH^03301||||||||||A|20180508072428|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20150507|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20150507|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||12729.54.20180507|\r"
      + "RXA|0|999|20180507|20180507|94^MMRV^CVX^90710^MMRV^CPT|999|||01^Historical information - source unspecified^NIP001||^^^AIRA-AART^^^^^123 MAIN STREEET^^CONCORD^NH^03301||||||||||A|20180508072428|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20180507|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20180507|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180508072428|20180508072428|998^no vaccine administered^CVX|0||||||||||||||NA||20180508072428|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|45^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140426||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140426||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340426||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20140526||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180508072428|20180508072428|998^no vaccine administered^CVX|0||||||||||||||NA||20180508072428|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|107^DTAP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140626||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140607||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340426||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20140726||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180508072428|20180508072428|998^no vaccine administered^CVX|0||||||||||||||NA||20180508072428|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|17^HIB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140626||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140607||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340426||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20140726||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180508072428|20180508072428|998^no vaccine administered^CVX|0||||||||||||||NA||20180508072428|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|133^PneumoPCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140626||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140607||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190426||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20140726||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180508072428|20180508072428|998^no vaccine administered^CVX|0||||||||||||||NA||20180508072428|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|89^POLIO^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140626||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140607||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340426||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20140726||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180508072428|20180508072428|998^no vaccine administered^CVX|0||||||||||||||NA||20180508072428|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|88^FLU^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141026||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20141026||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340426||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20180704||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180508072428|20180508072428|998^no vaccine administered^CVX|0||||||||||||||NA||20180508072428|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|147^MENING^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250426||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20240426||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340426||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270426||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";

  private static final String RSP_NM_NMSIIS_1 = ""
      + "MSH|^~\\&|WebIZ.18.1.20180629|NMSIIS|RPMS|AART|20180824111720-0600||RSP^K11^RSP_K11|NMSIIS20180824172069|D|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|1Vjg-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|B39E61929^^^AIRA^MR|Mecosta^Guinevere^Sandya^^^^L||20140812|F|141 Connor Ave^^Woodhaven^MI^48183\r"
      + "PID|1||2384537^^^NMSIIS^SR~B39E61929^^^AART^MR||MECOSTA^GUINEVERE^SANDYA^^^^L|King^^^^^^M|20140812|F||2106-3^White^CDCREC|141 CONNOR AVE^^WOODHAVEN^MI^48183^USA^P||^PRN^PH^^^734^6240484|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|||||||||20180824\r"
      + "ORC|RE||23890036^NMSIIS\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||PR^^^PR|||||||||CP|A|20180824\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20150823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20150823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20150823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20150823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "ORC|RE||23890037^NMSIIS\r"
      + "RXA|0|1|20180823|20180823|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||PR^^^PR|||||||||CP|A|20180824\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20180823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20180823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20180823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20180823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "ORC|RE||9999^NMSIIS\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|08^Hep B, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|2|DT|30981-5^Earliest date dose should be given^LN|1|20140812||||||F|||20180824\r"
      + "OBX|3|DT|30980-7^Date Vaccine Due^LN|1|20140812||||||F|||20180824\r"
      + "OBX|4|DT|59777-3^Latest date next dose should be given^LN|1|20330811||||||F|||20180824\r"
      + "OBX|5|DT|59778-1^Date dose is overdue^LN|1|20140908||||||F|||20180824\r"
      + "OBX|6|CE|59783-1^Series Status^LN|1|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|7|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|8|CE|30956-7^Vaccine Type^LN|2|133^PCV13^CVX||||||F|||20180824\r"
      + "OBX|9|DT|30981-5^Earliest date dose should be given^LN|2|20140923||||||F|||20180824\r"
      + "OBX|10|DT|30980-7^Date Vaccine Due^LN|2|20141012||||||F|||20180824\r"
      + "OBX|11|DT|59777-3^Latest date next dose should be given^LN|2|20190811||||||F|||20180824\r"
      + "OBX|12|DT|59778-1^Date dose is overdue^LN|2|20141209||||||F|||20180824\r"
      + "OBX|13|CE|59783-1^Series Status^LN|2|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|14|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|15|CE|30956-7^Vaccine Type^LN|3|10^Polio-IPV^CVX||||||F|||20180824\r"
      + "OBX|16|DT|30981-5^Earliest date dose should be given^LN|3|20140923||||||F|||20180824\r"
      + "OBX|17|DT|30980-7^Date Vaccine Due^LN|3|20141012||||||F|||20180824\r"
      + "OBX|18|DT|59777-3^Latest date next dose should be given^LN|3|20320811||||||F|||20180824\r"
      + "OBX|19|DT|59778-1^Date dose is overdue^LN|3|20141209||||||F|||20180824\r"
      + "OBX|20|CE|59783-1^Series Status^LN|3|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|21|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|22|CE|30956-7^Vaccine Type^LN|4|20^DTaP^CVX||||||F|||20180824\r"
      + "OBX|23|DT|30981-5^Earliest date dose should be given^LN|4|20140923||||||F|||20180824\r"
      + "OBX|24|DT|30980-7^Date Vaccine Due^LN|4|20141012||||||F|||20180824\r"
      + "OBX|25|DT|59778-1^Date dose is overdue^LN|4|20141209||||||F|||20180824\r"
      + "OBX|26|CE|59783-1^Series Status^LN|4|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|27|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|28|CE|30956-7^Vaccine Type^LN|5|48^Hib (PRP-T)^CVX||||||F|||20180824\r"
      + "OBX|29|DT|30981-5^Earliest date dose should be given^LN|5|20140923||||||F|||20180824\r"
      + "OBX|30|DT|30980-7^Date Vaccine Due^LN|5|20141012||||||F|||20180824\r"
      + "OBX|31|DT|59777-3^Latest date next dose should be given^LN|5|20190811||||||F|||20180824\r"
      + "OBX|32|DT|59778-1^Date dose is overdue^LN|5|20141209||||||F|||20180824\r"
      + "OBX|33|CE|59783-1^Series Status^LN|5|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|34|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|35|CE|30956-7^Vaccine Type^LN|6|83^Hep A, ped/adol, 2D^CVX||||||F|||20180824\r"
      + "OBX|36|DT|30981-5^Earliest date dose should be given^LN|6|20150812||||||F|||20180824\r"
      + "OBX|37|DT|30980-7^Date Vaccine Due^LN|6|20150812||||||F|||20180824\r"
      + "OBX|38|DT|59777-3^Latest date next dose should be given^LN|6|20330811||||||F|||20180824\r"
      + "OBX|39|DT|59778-1^Date dose is overdue^LN|6|20160908||||||F|||20180824\r"
      + "OBX|40|CE|59783-1^Series Status^LN|6|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|41|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|42|CE|30956-7^Vaccine Type^LN|7|141^Influenza, Seasonal^CVX||||||F|||20180824\r"
      + "OBX|43|DT|30981-5^Earliest date dose should be given^LN|7|20180920||||||F|||20180824\r"
      + "OBX|44|DT|30980-7^Date Vaccine Due^LN|7|20180920||||||F|||20180824\r"
      + "OBX|45|CE|59783-1^Series Status^LN|7|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|46|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|47|CE|30956-7^Vaccine Type^LN|8|08^Hep B, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|48|DT|30981-5^Earliest date dose should be given^LN|8|20180921||||||F|||20180824\r"
      + "OBX|49|DT|30980-7^Date Vaccine Due^LN|8|20180921||||||F|||20180824\r"
      + "OBX|50|DT|59778-1^Date dose is overdue^LN|8|20180921||||||F|||20180824\r"
      + "OBX|51|CE|59783-1^Series Status^LN|8|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|52|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|53|CE|30956-7^Vaccine Type^LN|9|10^Polio-IPV^CVX||||||F|||20180824\r"
      + "OBX|54|DT|30981-5^Earliest date dose should be given^LN|9|20180921||||||F|||20180824\r"
      + "OBX|55|DT|30980-7^Date Vaccine Due^LN|9|20180921||||||F|||20180824\r"
      + "OBX|56|DT|59777-3^Latest date next dose should be given^LN|9|20320811||||||F|||20180824\r"
      + "OBX|57|DT|59778-1^Date dose is overdue^LN|9|20180921||||||F|||20180824\r"
      + "OBX|58|CE|59783-1^Series Status^LN|9|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|59|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|60|CE|30956-7^Vaccine Type^LN|10|20^DTaP^CVX||||||F|||20180824\r"
      + "OBX|61|DT|30981-5^Earliest date dose should be given^LN|10|20180921||||||F|||20180824\r"
      + "OBX|62|DT|30980-7^Date Vaccine Due^LN|10|20180921||||||F|||20180824\r"
      + "OBX|63|DT|59778-1^Date dose is overdue^LN|10|20180921||||||F|||20180824\r"
      + "OBX|64|CE|59783-1^Series Status^LN|10|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|65|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|66|CE|30956-7^Vaccine Type^LN|11|83^Hep A, ped/adol, 2D^CVX||||||F|||20180824\r"
      + "OBX|67|DT|30981-5^Earliest date dose should be given^LN|11|20190224||||||F|||20180824\r"
      + "OBX|68|DT|30980-7^Date Vaccine Due^LN|11|20190224||||||F|||20180824\r"
      + "OBX|69|DT|59778-1^Date dose is overdue^LN|11|20190224||||||F|||20180824\r"
      + "OBX|70|CE|59783-1^Series Status^LN|11|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|71|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|72|CE|30956-7^Vaccine Type^LN|12|114^MCV4P (Menactra)^CVX||||||F|||20180824\r"
      + "OBX|73|DT|30981-5^Earliest date dose should be given^LN|12|20250812||||||F|||20180824\r"
      + "OBX|74|DT|30980-7^Date Vaccine Due^LN|12|20250812||||||F|||20180824\r"
      + "OBX|75|DT|59777-3^Latest date next dose should be given^LN|12|20360811||||||F|||20180824\r"
      + "OBX|76|DT|59778-1^Date dose is overdue^LN|12|20270908||||||F|||20180824\r"
      + "OBX|77|CE|59783-1^Series Status^LN|12|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|78|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|79|CE|30956-7^Vaccine Type^LN|13|165^HPV9^CVX||||||F|||20180824\r"
      + "OBX|80|DT|30981-5^Earliest date dose should be given^LN|13|20230812||||||F|||20180824\r"
      + "OBX|81|DT|30980-7^Date Vaccine Due^LN|13|20250812||||||F|||20180824\r"
      + "OBX|82|DT|59777-3^Latest date next dose should be given^LN|13|20410811||||||F|||20180824\r"
      + "OBX|83|DT|59778-1^Date dose is overdue^LN|13|20270908||||||F|||20180824\r"
      + "OBX|84|CE|59783-1^Series Status^LN|13|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|85|CE|59779-9^Immunization Schedule Used^LN|13|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|86|CE|30956-7^Vaccine Type^LN|14|187^Recombinant Zoster^CVX||||||F|||20180824\r"
      + "OBX|87|DT|30981-5^Earliest date dose should be given^LN|14|20640812||||||F|||20180824\r"
      + "OBX|88|DT|30980-7^Date Vaccine Due^LN|14|20640812||||||F|||20180824\r"
      + "OBX|89|CE|59783-1^Series Status^LN|14|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|90|CE|59779-9^Immunization Schedule Used^LN|14|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|91|CE|30956-7^Vaccine Type^LN|15|133^PCV13^CVX||||||F|||20180824\r"
      + "OBX|92|DT|30981-5^Earliest date dose should be given^LN|15|20790812||||||F|||20180824\r"
      + "OBX|93|DT|30980-7^Date Vaccine Due^LN|15|20790812||||||F|||20180824\r"
      + "OBX|94|CE|59783-1^Series Status^LN|15|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|95|CE|59779-9^Immunization Schedule Used^LN|15|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|96|CE|30956-7^Vaccine Type^LN|16|122^Rotavirus, UF^CVX||||||F|||20180824\r"
      + "OBX|97|CE|59783-1^Series Status^LN|16|LA13424-9^Too Old^LN||||||F|||20180824\r"
      + "OBX|98|ST|30982-3^Reason Code^LN|16|Patient has exceeded the maximum age||||||F|||20180824\r"
      + "OBX|99|CE|59779-9^Immunization Schedule Used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|100|CE|30956-7^Vaccine Type^LN|17|03^MMR^CVX||||||F|||20180824\r"
      + "OBX|101|CE|59783-1^Series Status^LN|17|LA13421-5^Complete^LN||||||F|||20180824\r"
      + "OBX|102|ST|30982-3^Reason Code^LN|17|Patient Series is complete||||||F|||20180824\r"
      + "OBX|103|CE|59779-9^Immunization Schedule Used^LN|17|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|104|CE|30956-7^Vaccine Type^LN|18|21^Varicella^CVX||||||F|||20180824\r"
      + "OBX|105|CE|59783-1^Series Status^LN|18|LA13421-5^Complete^LN||||||F|||20180824\r"
      + "OBX|106|ST|30982-3^Reason Code^LN|18|Patient Series is complete||||||F|||20180824\r"
      + "OBX|107|CE|59779-9^Immunization Schedule Used^LN|18|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r";



  private static String RSP_NOT_FOUND =
      "MSH|^~\\&|74A^^|74A^^|^^|^^|20181029121553||RSP^K11^RSP_K11|7173854844.100120050|P|2.5.1|||||||||Z33^CDCPHINVS^^|\r"
          + "MSA|AA|15408441522834675|\r" + "ERR|||0|I||||No patients found for this query|\r"
          + "QAK|15408441522834675|NF|Z44^Request Evaluated History and Forecast^HL70471|\r"
          + "QPD|Z44^Request Evaluated History and Forecast^HL70471|15408441522834675|15408441491274674^^^FITS^MR|Garfield^Azland^Beck|Lyon^Cynthia^^^^^M|20180824|F|385 Custer St^^Ellsworth^MI^49729^USA^P|";

  private static final String RSP_NV_WEBIZ_1 = ""
      + "MSH|^~\\&|WebIZ.18.1.20180629|NV0000||NV1001|20180827132135-0700||RSP^K11^RSP_K11|NV000020180827213565|D|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|Pd3-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|P54U91278^^^AIRA^MR|Lackawanna^Orvokki^Eranthe^^^^L||20140815|F|123 Lamar St^^Waterford^MI^48327\r"
      + "PID|1||3981580^^^NV0000^SR~P54U91278^^^NV1001^MR||LACKAWANNA^ORVOKKI^ERANTHE^^^^L|Mason^^^^^^M|20140815|F||2106-3^White^CDCREC|123 LAMAR ST^^WATERFORD^MI^48327^USA^P||^PRN^PH^^^248^5623585|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|||||||||20180827\r"
      + "ORC|RE||41367495^NV0000\r"
      + "RXA|0|1|20150826|20150826|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||AIRA^^^NV1001^^^^^4150 TECHNOLOGY WAY UNIT 210^^Carson City^NV^89706|||||||||CP|A|20180827\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20150826\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20150826\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|1|NA^Not Applicable^HL70353|||||F|||20150826\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20150826\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20150826\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20150826\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|1|NA^Not Applicable^HL70353|||||F|||20150826\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20150826\r"
      + "ORC|RE||41367496^NV0000\r"
      + "RXA|0|1|20180826|20180826|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||AIRA^^^NV1001^^^^^4150 TECHNOLOGY WAY UNIT 210^^Carson City^NV^89706|||||||||CP|A|20180827\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20180826\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20180826\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|2|NA^Not Applicable^HL70353|||||F|||20180826\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180826\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20180826\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20180826\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|2|NA^Not Applicable^HL70353|||||F|||20180826\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180826\r"
      + "ORC|RE||9999^NV0000\r"
      + "RXA|0|1|20180827|20180827|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|08^Hep B, ped/adol^CVX||||||F|||20180827\r"
      + "OBX|2|DT|30981-5^Earliest date dose should be given^LN|1|20140815||||||F|||20180827\r"
      + "OBX|3|DT|30980-7^Date Vaccine Due^LN|1|20140815||||||F|||20180827\r"
      + "OBX|4|DT|59777-3^Latest date next dose should be given^LN|1|20330814||||||F|||20180827\r"
      + "OBX|5|DT|59778-1^Date dose is overdue^LN|1|20140911||||||F|||20180827\r"
      + "OBX|6|CE|59783-1^Series Status^LN|1|LA13423-1^Overdue^LN||||||F|||20180827\r"
      + "OBX|7|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|8|CE|30956-7^Vaccine Type^LN|2|133^PCV-13 (Prevnar 13)^CVX||||||F|||20180827\r"
      + "OBX|9|DT|30981-5^Earliest date dose should be given^LN|2|20140926||||||F|||20180827\r"
      + "OBX|10|DT|30980-7^Date Vaccine Due^LN|2|20141015||||||F|||20180827\r"
      + "OBX|11|DT|59777-3^Latest date next dose should be given^LN|2|20190814||||||F|||20180827\r"
      + "OBX|12|DT|59778-1^Date dose is overdue^LN|2|20141212||||||F|||20180827\r"
      + "OBX|13|CE|59783-1^Series Status^LN|2|LA13423-1^Overdue^LN||||||F|||20180827\r"
      + "OBX|14|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|15|CE|30956-7^Vaccine Type^LN|3|10^IPV^CVX||||||F|||20180827\r"
      + "OBX|16|DT|30981-5^Earliest date dose should be given^LN|3|20140926||||||F|||20180827\r"
      + "OBX|17|DT|30980-7^Date Vaccine Due^LN|3|20141015||||||F|||20180827\r"
      + "OBX|18|DT|59777-3^Latest date next dose should be given^LN|3|20320814||||||F|||20180827\r"
      + "OBX|19|DT|59778-1^Date dose is overdue^LN|3|20141212||||||F|||20180827\r"
      + "OBX|20|CE|59783-1^Series Status^LN|3|LA13423-1^Overdue^LN||||||F|||20180827\r"
      + "OBX|21|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|22|CE|30956-7^Vaccine Type^LN|4|20^DTaP^CVX||||||F|||20180827\r"
      + "OBX|23|DT|30981-5^Earliest date dose should be given^LN|4|20140926||||||F|||20180827\r"
      + "OBX|24|DT|30980-7^Date Vaccine Due^LN|4|20141015||||||F|||20180827\r"
      + "OBX|25|DT|59778-1^Date dose is overdue^LN|4|20141212||||||F|||20180827\r"
      + "OBX|26|CE|59783-1^Series Status^LN|4|LA13423-1^Overdue^LN||||||F|||20180827\r"
      + "OBX|27|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|28|CE|30956-7^Vaccine Type^LN|5|48^Hib (PRP-T)^CVX||||||F|||20180827\r"
      + "OBX|29|DT|30981-5^Earliest date dose should be given^LN|5|20140926||||||F|||20180827\r"
      + "OBX|30|DT|30980-7^Date Vaccine Due^LN|5|20141015||||||F|||20180827\r"
      + "OBX|31|DT|59777-3^Latest date next dose should be given^LN|5|20190814||||||F|||20180827\r"
      + "OBX|32|DT|59778-1^Date dose is overdue^LN|5|20141212||||||F|||20180827\r"
      + "OBX|33|CE|59783-1^Series Status^LN|5|LA13423-1^Overdue^LN||||||F|||20180827\r"
      + "OBX|34|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|35|CE|30956-7^Vaccine Type^LN|6|83^Hep A, ped/adol^CVX||||||F|||20180827\r"
      + "OBX|36|DT|30981-5^Earliest date dose should be given^LN|6|20180923||||||F|||20180827\r"
      + "OBX|37|DT|30980-7^Date Vaccine Due^LN|6|20180923||||||F|||20180827\r"
      + "OBX|38|DT|59777-3^Latest date next dose should be given^LN|6|20330814||||||F|||20180827\r"
      + "OBX|39|DT|59778-1^Date dose is overdue^LN|6|20180923||||||F|||20180827\r"
      + "OBX|40|CE|59783-1^Series Status^LN|6|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|41|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|42|CE|30956-7^Vaccine Type^LN|7|141^Influenza w/preserv.^CVX||||||F|||20180827\r"
      + "OBX|43|DT|30981-5^Earliest date dose should be given^LN|7|20180923||||||F|||20180827\r"
      + "OBX|44|DT|30980-7^Date Vaccine Due^LN|7|20180923||||||F|||20180827\r"
      + "OBX|45|CE|59783-1^Series Status^LN|7|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|46|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|47|CE|30956-7^Vaccine Type^LN|8|08^Hep B, ped/adol^CVX||||||F|||20180827\r"
      + "OBX|48|DT|30981-5^Earliest date dose should be given^LN|8|20180924||||||F|||20180827\r"
      + "OBX|49|DT|30980-7^Date Vaccine Due^LN|8|20180924||||||F|||20180827\r"
      + "OBX|50|DT|59778-1^Date dose is overdue^LN|8|20180924||||||F|||20180827\r"
      + "OBX|51|CE|59783-1^Series Status^LN|8|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|52|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|53|CE|30956-7^Vaccine Type^LN|9|10^IPV^CVX||||||F|||20180827\r"
      + "OBX|54|DT|30981-5^Earliest date dose should be given^LN|9|20180924||||||F|||20180827\r"
      + "OBX|55|DT|30980-7^Date Vaccine Due^LN|9|20180924||||||F|||20180827\r"
      + "OBX|56|DT|59777-3^Latest date next dose should be given^LN|9|20320814||||||F|||20180827\r"
      + "OBX|57|DT|59778-1^Date dose is overdue^LN|9|20180924||||||F|||20180827\r"
      + "OBX|58|CE|59783-1^Series Status^LN|9|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|59|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|60|CE|30956-7^Vaccine Type^LN|10|20^DTaP^CVX||||||F|||20180827\r"
      + "OBX|61|DT|30981-5^Earliest date dose should be given^LN|10|20180924||||||F|||20180827\r"
      + "OBX|62|DT|30980-7^Date Vaccine Due^LN|10|20180924||||||F|||20180827\r"
      + "OBX|63|DT|59778-1^Date dose is overdue^LN|10|20180924||||||F|||20180827\r"
      + "OBX|64|CE|59783-1^Series Status^LN|10|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|65|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|66|CE|30956-7^Vaccine Type^LN|11|114^MCV4P (MENACTRA)^CVX||||||F|||20180827\r"
      + "OBX|67|DT|30981-5^Earliest date dose should be given^LN|11|20250815||||||F|||20180827\r"
      + "OBX|68|DT|30980-7^Date Vaccine Due^LN|11|20250815||||||F|||20180827\r"
      + "OBX|69|DT|59777-3^Latest date next dose should be given^LN|11|20360814||||||F|||20180827\r"
      + "OBX|70|DT|59778-1^Date dose is overdue^LN|11|20270911||||||F|||20180827\r"
      + "OBX|71|CE|59783-1^Series Status^LN|11|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|72|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|73|CE|30956-7^Vaccine Type^LN|12|165^HPV9^CVX||||||F|||20180827\r"
      + "OBX|74|DT|30981-5^Earliest date dose should be given^LN|12|20230815||||||F|||20180827\r"
      + "OBX|75|DT|30980-7^Date Vaccine Due^LN|12|20250815||||||F|||20180827\r"
      + "OBX|76|DT|59777-3^Latest date next dose should be given^LN|12|20410814||||||F|||20180827\r"
      + "OBX|77|DT|59778-1^Date dose is overdue^LN|12|20270911||||||F|||20180827\r"
      + "OBX|78|CE|59783-1^Series Status^LN|12|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|79|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|80|CE|30956-7^Vaccine Type^LN|13|187^Zoster Recombinant^CVX||||||F|||20180827\r"
      + "OBX|81|DT|30981-5^Earliest date dose should be given^LN|13|20640815||||||F|||20180827\r"
      + "OBX|82|DT|30980-7^Date Vaccine Due^LN|13|20640815||||||F|||20180827\r"
      + "OBX|83|CE|59783-1^Series Status^LN|13|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|84|CE|59779-9^Immunization Schedule Used^LN|13|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|85|CE|30956-7^Vaccine Type^LN|14|133^PCV-13 (Prevnar 13)^CVX||||||F|||20180827\r"
      + "OBX|86|DT|30981-5^Earliest date dose should be given^LN|14|20790815||||||F|||20180827\r"
      + "OBX|87|DT|30980-7^Date Vaccine Due^LN|14|20790815||||||F|||20180827\r"
      + "OBX|88|CE|59783-1^Series Status^LN|14|LA13422-3^On Schedule^LN||||||F|||20180827\r"
      + "OBX|89|CE|59779-9^Immunization Schedule Used^LN|14|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|90|CE|30956-7^Vaccine Type^LN|15|03^MMR^CVX||||||F|||20180827\r"
      + "OBX|91|CE|59783-1^Series Status^LN|15|LA13421-5^Complete^LN||||||F|||20180827\r"
      + "OBX|92|ST|30982-3^Reason Code^LN|15|Patient Series is complete||||||F|||20180827\r"
      + "OBX|93|CE|59779-9^Immunization Schedule Used^LN|15|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|94|CE|30956-7^Vaccine Type^LN|16|122^Rotavirus, UF^CVX||||||F|||20180827\r"
      + "OBX|95|CE|59783-1^Series Status^LN|16|LA13424-9^Too Old^LN||||||F|||20180827\r"
      + "OBX|96|ST|30982-3^Reason Code^LN|16|Patient has exceeded the maximum age||||||F|||20180827\r"
      + "OBX|97|CE|59779-9^Immunization Schedule Used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r"
      + "OBX|98|CE|30956-7^Vaccine Type^LN|17|21^Varicella^CVX||||||F|||20180827\r"
      + "OBX|99|CE|59783-1^Series Status^LN|17|LA13421-5^Complete^LN||||||F|||20180827\r"
      + "OBX|100|ST|30982-3^Reason Code^LN|17|Patient Series is complete||||||F|||20180827\r"
      + "OBX|101|CE|59779-9^Immunization Schedule Used^LN|17|VXC16^ACIP^CDCPHINVS||||||F|||20180827\r";

  private static final String RSP_NY_CIR_1 = ""
      + "MSH|^~\\&|CIR HL7 Web Service 2.15 (POSTGRES MIGRATION-PRODUCTION: 2018/08/08)|NYC DOHMH||0000X00|20180824131649-0400||RSP^K11^RSP_K11|20180824131649-0400CIR-WS|T|2.5.1|||NE|NE|||||Z32^CDCPHINVS|\r"
      + "MSA|AE|1ZYB-QA.2.1.2-D|\r"
      + "ERR||QPD^1^1^1^1|103^Table value not found^HL70357|W|TableValueNotFound^^HL70357|||Message_Query_Name_Id: TableValueNotFound|\r"
      + "ERR||QPD^1^1^1^3|102^Data type error^HL70357|W|ValueMissing^^HL70357|||Message_Query_Name_Coding_System: ValueMissing|\r"
      + "QAK|37374859|AE|\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|H95O62043^^^AIRA^MR|Hitchcock^Udaya^Demi^^^^L||20140812|F|247 Johnson Ave^^Harrison Township^MI^48045|\r"
      + "PID|||788561514^^^^LR||HITCHCOCK^UDAYA^DEMI^^^^L||20140812|F|\r"
      + "ORC|RE||116849677^NYC-CIR|||||||||^test^test|\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX|999|\r"
      + "OBX|1|CE|38890-0^Component Vaccine Type^LN|1|03^MMR^CVX||||||F|\r"
      + "OBX|2|CE|38890-0^Component Vaccine Type^LN|2|21^varicella^CVX||||||F|\r"
      + "ORC|RE||116849678^NYC-CIR|||||||||^test^test|\r"
      + "RXA|0|1|20180823|20180823|94^MMRV^CVX|999|\r"
      + "OBX|1|CE|38890-0^Component Vaccine Type^LN|1|03^MMR^CVX||||||F|\r"
      + "OBX|2|CE|38890-0^Component Vaccine Type^LN|2|21^varicella^CVX||||||F|\r"
      + "ORC|RE||Influenza^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|CE|30979-9^Vaccine due next^LN|1|88^influenza, NOS^CVX||||||F|||20180824131649|\r"
      + "OBX|2|DT|30980-7^Date vaccine due^LN|1|20180801||||||F|||20180824131649|\r"
      + "OBX|3|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||HepB^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|CE|30979-9^Vaccine due next^LN|1|08^Hep B, adolescent or pediatric^CVX||||||F|||20180824131649|\r"
      + "OBX|2|DT|30980-7^Date vaccine due^LN|1|20140812||||||F|||20180824131649|\r"
      + "OBX|3|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||Rotavirus^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|ST|30999-1^Vaccine Group Recommendation Status^NYCDOHVCGPST|1|Rotavirus^Not recommended after 8 months.^NYCDOHVCGPST||||||F|||20180824131649|\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||DTP^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|CE|30979-9^Vaccine due next^LN|1|20^DTaP^CVX||||||F|||20180824131649|\r"
      + "OBX|2|DT|30980-7^Date vaccine due^LN|1|20141012||||||F|||20180824131649|\r"
      + "OBX|3|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||Hib^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|CE|30979-9^Vaccine due next^LN|1|17^Hib, NOS^CVX||||||F|||20180824131649|\r"
      + "OBX|2|DT|30980-7^Date vaccine due^LN|1|20141012||||||F|||20180824131649|\r"
      + "OBX|3|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||Pediatric Pneumococcal (PCV \\T\\ PPSV)^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|CE|30979-9^Vaccine due next^LN|1|133^Pneumococcal, PCV-13^CVX||||||F|||20180824131649|\r"
      + "OBX|2|DT|30980-7^Date vaccine due^LN|1|20141012||||||F|||20180824131649|\r"
      + "OBX|3|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||Polio^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|CE|30979-9^Vaccine due next^LN|1|10^IPV^CVX||||||F|||20180824131649|\r"
      + "OBX|2|DT|30980-7^Date vaccine due^LN|1|20141012||||||F|||20180824131649|\r"
      + "OBX|3|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||MMR^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|ST|30999-1^Vaccine Group Recommendation Status^NYCDOHVCGPST|1|MMR^Completed Vaccine Series^NYCDOHVCGPST||||||F|||20180824131649|\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||Varicella^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|ST|30999-1^Vaccine Group Recommendation Status^NYCDOHVCGPST|1|Varicella^Completed Vaccine Series^NYCDOHVCGPST||||||F|||20180824131649|\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||HepA^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|CE|30979-9^Vaccine due next^LN|1|83^Hep A, ped/adol, 2 dose^CVX||||||F|||20180824131649|\r"
      + "OBX|2|DT|30980-7^Date vaccine due^LN|1|20150812||||||F|||20180824131649|\r"
      + "OBX|3|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||Meningococcal (MenACWY)^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|CE|30979-9^Vaccine due next^LN|1|147^MenACWY Conjugate NOS^CVX||||||F|||20180824131649|\r"
      + "OBX|2|DT|30980-7^Date vaccine due^LN|1|20250812||||||F|||20180824131649|\r"
      + "OBX|3|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||Human Papillomavirus^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|CE|30979-9^Vaccine due next^LN|1|165^Human Papillomavirus (HPV9-Gardasil 9)^CVX||||||F|||20180824131649|\r"
      + "OBX|2|DT|30980-7^Date vaccine due^LN|1|20250812||||||F|||20180824131649|\r"
      + "OBX|3|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||Adult Pneumococcal (PCV \\T\\ PPSV)^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|ST|30999-1^Vaccine Group Recommendation Status^NYCDOHVCGPST|1|Adult Pneumococcal (PCV \\T\\ PPSV)^Not recommended^NYCDOHVCGPST||||||F|||20180824131649|\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r"
      + "ORC|RE||H1N1 Influenza^NYC-CIR|\r"
      + "RXA|0|1|20180824131649|20180824131649|998^No vaccine administered^CVX|999|\r"
      + "OBX|1|ST|30999-1^Vaccine Group Recommendation Status^NYCDOHVCGPST|1|H1N1 Influenza^No longer recommended^NYCDOHVCGPST||||||F|||20180824131649|\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180824131649|\r";

  private static final String RSP_OR_ALERT_1 = ""
      + "MSH|^~\\&|ALERT IIS|ALERT IIS||AL9997|20180824101959-0700||RSP^K11^RSP_K11|23dr-QA.2.1.2-D|P|2.5.1|||||||||Z32^CDCPHINVS|ALERT IIS|AL9997\r"
      + "MSA|AA|23dr-QA.2.1.2-D||0||0^Message Accepted^HL70357\r" + "QAK|37374859|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|C68G62100^^^AIRA^MR|Union^Gisela^Annabel^^^^L||20140812|F|263 San Juan Ln^^Ironwood^MI^49938\r"
      + "PID|1||7425564^^^ORA^SR~C68G62100^^^ORA^MR||UNION^GISELA^ANNABEL^^^^L|GANNON^BURDETTE|20140812|F||2106-3|263 SAN JUAN LN^^IRONWOOD^MI^49938^^H||^PRN^PH^^^906^3005508|||||||||2186-5||N|0\r"
      + "PD1|||||||||||02|N||||A\r"
      + "NK1|1|UNION^GANNON^MARION|MTH|263 SAN JUAN LN^^IRONWOOD^MI^49938^^P|^PRN^PH^^^906^3005508\r"
      + "ORC|RE||135985297\r" + "RXA|0|1|20180823|20180823|94^MMRV^CVX|1.0|||01|||||||||||CP\r"
      + "OBX|1|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|2||||||F\r"
      + "OBX|3|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|2||||||F\r" + "ORC|RE||135985296\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX|1.0|||01|||||||||||CP\r"
      + "OBX|5|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|6|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|7|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|8|NM|30973-2^Dose number in series^LN|2|1||||||F\r" + "ORC|RE||0\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999\r"
      + "OBX|9|CE|30979-9^Vaccines Due Next^LN|0|107^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r"
      + "OBX|10|TS|30980-7^Date Vaccine Due^LN|0|20141012||||||F\r"
      + "OBX|11|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|12|TS|30981-5^Earliest date to give^LN|0|20140923||||||F\r"
      + "OBX|13|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r"
      + "OBX|14|CE|30979-9^Vaccines Due Next^LN|1|85^HepA^CVX^90730^HepA^CPT||||||F\r"
      + "OBX|15|TS|30980-7^Date Vaccine Due^LN|1|20150812||||||F\r"
      + "OBX|16|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|17|TS|30981-5^Earliest date to give^LN|1|20150812||||||F\r"
      + "OBX|18|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|19|CE|30979-9^Vaccines Due Next^LN|2|45^HepB^CVX^90731^HepB^CPT||||||F\r"
      + "OBX|20|TS|30980-7^Date Vaccine Due^LN|2|20140812||||||F\r"
      + "OBX|21|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|22|TS|30981-5^Earliest date to give^LN|2|20140812||||||F\r"
      + "OBX|23|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|24|CE|30979-9^Vaccines Due Next^LN|3|17^Hib^CVX^90737^Hib^CPT||||||F\r"
      + "OBX|25|TS|30980-7^Date Vaccine Due^LN|3|20151112||||||F\r"
      + "OBX|26|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|27|TS|30981-5^Earliest date to give^LN|3|20151112||||||F\r"
      + "OBX|28|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|29|CE|30979-9^Vaccines Due Next^LN|4|88^Influenza-seasnl^CVX^90724^Influenza-seasnl^CPT||||||F\r"
      + "OBX|30|TS|30980-7^Date Vaccine Due^LN|4|20180801||||||F\r"
      + "OBX|31|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|32|TS|30981-5^Earliest date to give^LN|4|20150212||||||F\r"
      + "OBX|33|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|34|CE|30979-9^Vaccines Due Next^LN|5|133^PneumoConjugate^CVX^90670^PneumoConjugate^CPT||||||F\r"
      + "OBX|35|TS|30980-7^Date Vaccine Due^LN|5|20141012||||||F\r"
      + "OBX|36|NM|30973-2^Vaccine due next dose number^LN|5|1||||||F\r"
      + "OBX|37|TS|30981-5^Earliest date to give^LN|5|20140923||||||F\r"
      + "OBX|38|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|39|CE|30979-9^Vaccines Due Next^LN|6|89^Polio^CVX||||||F\r"
      + "OBX|40|TS|30980-7^Date Vaccine Due^LN|6|20141012||||||F\r"
      + "OBX|41|NM|30973-2^Vaccine due next dose number^LN|6|1||||||F\r"
      + "OBX|42|TS|30981-5^Earliest date to give^LN|6|20140923||||||F\r"
      + "OBX|43|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r";


  private static final String RSP_PA_PHIL_1 = ""
      + "MSH|^~\\&|WebIZ.18.1.20180629|PH0000||PH5000|20180824131752-0400||RSP^K11^RSP_K11|PH000020180824175247|T|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|29SZ-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|F14B62161^^^AIRA^MR|Paulding^Greta^Zoey^^^^L||20140812|F|333 Grays St^^Posen^MI^49776\r"
      + "PID|1||2155228^^^PH0000^LR~F14B62161^^^PH5000^MR||PAULDING^GRETA^ZOEY^^^^L|Randall^^^^^^M|20140812|F||2106-3^White^CDCREC|333 GRAYS ST^^POSEN^MI^49776^USA^P||^PRN^PH^^^989^6038482|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|||||||||20180824\r"
      + "ORC|RE||16585105^PH0000\r"
      + "RXA|0|1|20150823|20150823|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||PR^^^PR|||||||||CP|A|20180824\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20150823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20150823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20150823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20150823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "ORC|RE||16585106^PH0000\r"
      + "RXA|0|1|20180823|20180823|94^MMRV^CVX|999|||01^Historical Information - Source Unspecified^NIP001||PR^^^PR|||||||||CP|A|20180824\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20180823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20180823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20180823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20180823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "ORC|RE||9999^PH0000\r"
      + "RXA|0|1|20180824|20180824|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|08^Hep B, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|2|DT|30981-5^Earliest date dose should be given^LN|1|20140812||||||F|||20180824\r"
      + "OBX|3|DT|30980-7^Date Vaccine Due^LN|1|20140812||||||F|||20180824\r"
      + "OBX|4|DT|59777-3^Latest date next dose should be given^LN|1|20330811||||||F|||20180824\r"
      + "OBX|5|DT|59778-1^Date dose is overdue^LN|1|20140908||||||F|||20180824\r"
      + "OBX|6|CE|59783-1^Series Status^LN|1|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|7|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|8|CE|30956-7^Vaccine Type^LN|2|133^PCV13^CVX||||||F|||20180824\r"
      + "OBX|9|DT|30981-5^Earliest date dose should be given^LN|2|20140923||||||F|||20180824\r"
      + "OBX|10|DT|30980-7^Date Vaccine Due^LN|2|20141012||||||F|||20180824\r"
      + "OBX|11|DT|59777-3^Latest date next dose should be given^LN|2|20190811||||||F|||20180824\r"
      + "OBX|12|DT|59778-1^Date dose is overdue^LN|2|20141209||||||F|||20180824\r"
      + "OBX|13|CE|59783-1^Series Status^LN|2|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|14|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|15|CE|30956-7^Vaccine Type^LN|3|10^Polio-IPV^CVX||||||F|||20180824\r"
      + "OBX|16|DT|30981-5^Earliest date dose should be given^LN|3|20140923||||||F|||20180824\r"
      + "OBX|17|DT|30980-7^Date Vaccine Due^LN|3|20141012||||||F|||20180824\r"
      + "OBX|18|DT|59777-3^Latest date next dose should be given^LN|3|20320811||||||F|||20180824\r"
      + "OBX|19|DT|59778-1^Date dose is overdue^LN|3|20141209||||||F|||20180824\r"
      + "OBX|20|CE|59783-1^Series Status^LN|3|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|21|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|22|CE|30956-7^Vaccine Type^LN|4|20^DTaP (Infanrix)^CVX||||||F|||20180824\r"
      + "OBX|23|DT|30981-5^Earliest date dose should be given^LN|4|20140923||||||F|||20180824\r"
      + "OBX|24|DT|30980-7^Date Vaccine Due^LN|4|20141012||||||F|||20180824\r"
      + "OBX|25|DT|59778-1^Date dose is overdue^LN|4|20141209||||||F|||20180824\r"
      + "OBX|26|CE|59783-1^Series Status^LN|4|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|27|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|28|CE|30956-7^Vaccine Type^LN|5|48^Hib (PRP-T)^CVX||||||F|||20180824\r"
      + "OBX|29|DT|30981-5^Earliest date dose should be given^LN|5|20140923||||||F|||20180824\r"
      + "OBX|30|DT|30980-7^Date Vaccine Due^LN|5|20141012||||||F|||20180824\r"
      + "OBX|31|DT|59777-3^Latest date next dose should be given^LN|5|20190811||||||F|||20180824\r"
      + "OBX|32|DT|59778-1^Date dose is overdue^LN|5|20141209||||||F|||20180824\r"
      + "OBX|33|CE|59783-1^Series Status^LN|5|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|34|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|35|CE|30956-7^Vaccine Type^LN|6|83^Hep A, ped/adol, 2D^CVX||||||F|||20180824\r"
      + "OBX|36|DT|30981-5^Earliest date dose should be given^LN|6|20150812||||||F|||20180824\r"
      + "OBX|37|DT|30980-7^Date Vaccine Due^LN|6|20150812||||||F|||20180824\r"
      + "OBX|38|DT|59777-3^Latest date next dose should be given^LN|6|20330811||||||F|||20180824\r"
      + "OBX|39|DT|59778-1^Date dose is overdue^LN|6|20160908||||||F|||20180824\r"
      + "OBX|40|CE|59783-1^Series Status^LN|6|LA13423-1^Overdue^LN||||||F|||20180824\r"
      + "OBX|41|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|42|CE|30956-7^Vaccine Type^LN|7|141^Influenza Inj Tri^CVX||||||F|||20180824\r"
      + "OBX|43|DT|30981-5^Earliest date dose should be given^LN|7|20180920||||||F|||20180824\r"
      + "OBX|44|DT|30980-7^Date Vaccine Due^LN|7|20180920||||||F|||20180824\r"
      + "OBX|45|CE|59783-1^Series Status^LN|7|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|46|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|47|CE|30956-7^Vaccine Type^LN|8|08^Hep B, ped/adol^CVX||||||F|||20180824\r"
      + "OBX|48|DT|30981-5^Earliest date dose should be given^LN|8|20180921||||||F|||20180824\r"
      + "OBX|49|DT|30980-7^Date Vaccine Due^LN|8|20180921||||||F|||20180824\r"
      + "OBX|50|DT|59778-1^Date dose is overdue^LN|8|20180921||||||F|||20180824\r"
      + "OBX|51|CE|59783-1^Series Status^LN|8|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|52|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|53|CE|30956-7^Vaccine Type^LN|9|10^Polio-IPV^CVX||||||F|||20180824\r"
      + "OBX|54|DT|30981-5^Earliest date dose should be given^LN|9|20180921||||||F|||20180824\r"
      + "OBX|55|DT|30980-7^Date Vaccine Due^LN|9|20180921||||||F|||20180824\r"
      + "OBX|56|DT|59777-3^Latest date next dose should be given^LN|9|20320811||||||F|||20180824\r"
      + "OBX|57|DT|59778-1^Date dose is overdue^LN|9|20180921||||||F|||20180824\r"
      + "OBX|58|CE|59783-1^Series Status^LN|9|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|59|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|60|CE|30956-7^Vaccine Type^LN|10|20^DTaP (Infanrix)^CVX||||||F|||20180824\r"
      + "OBX|61|DT|30981-5^Earliest date dose should be given^LN|10|20180921||||||F|||20180824\r"
      + "OBX|62|DT|30980-7^Date Vaccine Due^LN|10|20180921||||||F|||20180824\r"
      + "OBX|63|DT|59778-1^Date dose is overdue^LN|10|20180921||||||F|||20180824\r"
      + "OBX|64|CE|59783-1^Series Status^LN|10|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|65|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|66|CE|30956-7^Vaccine Type^LN|11|83^Hep A, ped/adol, 2D^CVX||||||F|||20180824\r"
      + "OBX|67|DT|30981-5^Earliest date dose should be given^LN|11|20190224||||||F|||20180824\r"
      + "OBX|68|DT|30980-7^Date Vaccine Due^LN|11|20190224||||||F|||20180824\r"
      + "OBX|69|DT|59778-1^Date dose is overdue^LN|11|20190224||||||F|||20180824\r"
      + "OBX|70|CE|59783-1^Series Status^LN|11|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|71|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|72|CE|30956-7^Vaccine Type^LN|12|114^MCV4 (Menactra)^CVX||||||F|||20180824\r"
      + "OBX|73|DT|30981-5^Earliest date dose should be given^LN|12|20250812||||||F|||20180824\r"
      + "OBX|74|DT|30980-7^Date Vaccine Due^LN|12|20250812||||||F|||20180824\r"
      + "OBX|75|DT|59777-3^Latest date next dose should be given^LN|12|20360811||||||F|||20180824\r"
      + "OBX|76|DT|59778-1^Date dose is overdue^LN|12|20270908||||||F|||20180824\r"
      + "OBX|77|CE|59783-1^Series Status^LN|12|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|78|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|79|CE|30956-7^Vaccine Type^LN|13|165^HPV9^CVX||||||F|||20180824\r"
      + "OBX|80|DT|30981-5^Earliest date dose should be given^LN|13|20230812||||||F|||20180824\r"
      + "OBX|81|DT|30980-7^Date Vaccine Due^LN|13|20250812||||||F|||20180824\r"
      + "OBX|82|DT|59777-3^Latest date next dose should be given^LN|13|20410811||||||F|||20180824\r"
      + "OBX|83|DT|59778-1^Date dose is overdue^LN|13|20270908||||||F|||20180824\r"
      + "OBX|84|CE|59783-1^Series Status^LN|13|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|85|CE|59779-9^Immunization Schedule Used^LN|13|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|86|CE|30956-7^Vaccine Type^LN|14|187^Recombinant Zoster^CVX||||||F|||20180824\r"
      + "OBX|87|DT|30981-5^Earliest date dose should be given^LN|14|20640812||||||F|||20180824\r"
      + "OBX|88|DT|30980-7^Date Vaccine Due^LN|14|20640812||||||F|||20180824\r"
      + "OBX|89|CE|59783-1^Series Status^LN|14|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|90|CE|59779-9^Immunization Schedule Used^LN|14|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|91|CE|30956-7^Vaccine Type^LN|15|133^PCV13^CVX||||||F|||20180824\r"
      + "OBX|92|DT|30981-5^Earliest date dose should be given^LN|15|20790812||||||F|||20180824\r"
      + "OBX|93|DT|30980-7^Date Vaccine Due^LN|15|20790812||||||F|||20180824\r"
      + "OBX|94|CE|59783-1^Series Status^LN|15|LA13422-3^On Schedule^LN||||||F|||20180824\r"
      + "OBX|95|CE|59779-9^Immunization Schedule Used^LN|15|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|96|CE|30956-7^Vaccine Type^LN|16|122^Rotavirus, UF^CVX||||||F|||20180824\r"
      + "OBX|97|CE|59783-1^Series Status^LN|16|LA13424-9^Too Old^LN||||||F|||20180824\r"
      + "OBX|98|ST|30982-3^Reason Code^LN|16|Patient has exceeded the maximum age||||||F|||20180824\r"
      + "OBX|99|CE|59779-9^Immunization Schedule Used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|100|CE|30956-7^Vaccine Type^LN|17|03^MMR^CVX||||||F|||20180824\r"
      + "OBX|101|CE|59783-1^Series Status^LN|17|LA13421-5^Complete^LN||||||F|||20180824\r"
      + "OBX|102|ST|30982-3^Reason Code^LN|17|Patient Series is complete||||||F|||20180824\r"
      + "OBX|103|CE|59779-9^Immunization Schedule Used^LN|17|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r"
      + "OBX|104|CE|30956-7^Vaccine Type^LN|18|21^Varicella^CVX||||||F|||20180824\r"
      + "OBX|105|CE|59783-1^Series Status^LN|18|LA13421-5^Complete^LN||||||F|||20180824\r"
      + "OBX|106|ST|30982-3^Reason Code^LN|18|Patient Series is complete||||||F|||20180824\r"
      + "OBX|107|CE|59779-9^Immunization Schedule Used^LN|18|VXC16^ACIP^CDCPHINVS||||||F|||20180824\r";

  private static final String RSP_PI_GUAM_1 = ""
      + "MSH|^~\\&|WebIZ.18.1.20180504|GU0000||GU2017|20180825032106+1000||RSP^K11^RSP_K11|GU000020180825210664|T|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|2g6S-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|B77J62275^^^AIRA^MR|King^Alexandra^Violet^^^^L||20140812|F|196 Grant St^^Lansing^MI^48921\r"
      + "PID|1||149173^^^GU0000^SR~B77J62275^^^GU2017^MR||KING^ALEXANDRA^VIOLET^^^^L|Marion^^^^^^M|20140812|F||2106-3^White^CDCREC|||^PRN^PH^^^517^5652037|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|||||||||20180825\r"
      + "ORC|RE||1252962^GU0000\r"
      + "RXA|0|1|20150823|20150823|94^MMRV(ProQuad)^CVX|999|||01^Historical Information - Source Unspecified^NIP001||CPD REC^^^PR|||||||||CP|A|20180825\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20150823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20150823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20150823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20150823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|1|NA^Not Applicable^HL70353|||||F|||20150823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20150823\r"
      + "ORC|RE||1252963^GU0000\r"
      + "RXA|0|1|20180823|20180823|94^MMRV(ProQuad)^CVX|999|||01^Historical Information - Source Unspecified^NIP001||CPD REC^^^PR|||||||||CP|A|20180825\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|03^MMR^CVX||||||F|||20180823\r"
      + "OBX|2|ID|59781-5^Dose Validity^LN|1|Y||||||F|||20180823\r"
      + "OBX|3|NM|30973-2^Dose Number in Series^LN|1|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|4|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "OBX|5|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|||20180823\r"
      + "OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|||20180823\r"
      + "OBX|7|NM|30973-2^Dose Number in Series^LN|2|2|NA^Not Applicable^HL70353|||||F|||20180823\r"
      + "OBX|8|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180823\r"
      + "ORC|RE||9999^GU0000\r"
      + "RXA|0|1|20180825|20180825|998^No Vaccine Administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type^LN|1|08^Hep B, ped/adol^CVX||||||F|||20180825\r"
      + "OBX|2|DT|30981-5^Earliest date dose should be given^LN|1|20140812||||||F|||20180825\r"
      + "OBX|3|DT|30980-7^Date Vaccine Due^LN|1|20140812||||||F|||20180825\r"
      + "OBX|4|DT|59777-3^Latest date next dose should be given^LN|1|20330811||||||F|||20180825\r"
      + "OBX|5|DT|59778-1^Date dose is overdue^LN|1|20140908||||||F|||20180825\r"
      + "OBX|6|CE|59783-1^Series Status^LN|1|LA13423-1^Overdue^LN||||||F|||20180825\r"
      + "OBX|7|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|8|CE|30956-7^Vaccine Type^LN|2|133^PCV-13 (PREVNAR)^CVX||||||F|||20180825\r"
      + "OBX|9|DT|30981-5^Earliest date dose should be given^LN|2|20140923||||||F|||20180825\r"
      + "OBX|10|DT|30980-7^Date Vaccine Due^LN|2|20141012||||||F|||20180825\r"
      + "OBX|11|DT|59777-3^Latest date next dose should be given^LN|2|20190811||||||F|||20180825\r"
      + "OBX|12|DT|59778-1^Date dose is overdue^LN|2|20141209||||||F|||20180825\r"
      + "OBX|13|CE|59783-1^Series Status^LN|2|LA13423-1^Overdue^LN||||||F|||20180825\r"
      + "OBX|14|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|15|CE|30956-7^Vaccine Type^LN|3|10^Polio-IPV^CVX||||||F|||20180825\r"
      + "OBX|16|DT|30981-5^Earliest date dose should be given^LN|3|20140923||||||F|||20180825\r"
      + "OBX|17|DT|30980-7^Date Vaccine Due^LN|3|20141012||||||F|||20180825\r"
      + "OBX|18|DT|59777-3^Latest date next dose should be given^LN|3|20320811||||||F|||20180825\r"
      + "OBX|19|DT|59778-1^Date dose is overdue^LN|3|20141209||||||F|||20180825\r"
      + "OBX|20|CE|59783-1^Series Status^LN|3|LA13423-1^Overdue^LN||||||F|||20180825\r"
      + "OBX|21|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|22|CE|30956-7^Vaccine Type^LN|4|20^DTaP^CVX||||||F|||20180825\r"
      + "OBX|23|DT|30981-5^Earliest date dose should be given^LN|4|20140923||||||F|||20180825\r"
      + "OBX|24|DT|30980-7^Date Vaccine Due^LN|4|20141012||||||F|||20180825\r"
      + "OBX|25|DT|59778-1^Date dose is overdue^LN|4|20141209||||||F|||20180825\r"
      + "OBX|26|CE|59783-1^Series Status^LN|4|LA13423-1^Overdue^LN||||||F|||20180825\r"
      + "OBX|27|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|28|CE|30956-7^Vaccine Type^LN|5|48^Hib (ActHIB)^CVX||||||F|||20180825\r"
      + "OBX|29|DT|30981-5^Earliest date dose should be given^LN|5|20140923||||||F|||20180825\r"
      + "OBX|30|DT|30980-7^Date Vaccine Due^LN|5|20141012||||||F|||20180825\r"
      + "OBX|31|DT|59777-3^Latest date next dose should be given^LN|5|20190811||||||F|||20180825\r"
      + "OBX|32|DT|59778-1^Date dose is overdue^LN|5|20141209||||||F|||20180825\r"
      + "OBX|33|CE|59783-1^Series Status^LN|5|LA13423-1^Overdue^LN||||||F|||20180825\r"
      + "OBX|34|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|35|CE|30956-7^Vaccine Type^LN|6|83^Hep A, ped/adol^CVX||||||F|||20180825\r"
      + "OBX|36|DT|30981-5^Earliest date dose should be given^LN|6|20150812||||||F|||20180825\r"
      + "OBX|37|DT|30980-7^Date Vaccine Due^LN|6|20150812||||||F|||20180825\r"
      + "OBX|38|DT|59777-3^Latest date next dose should be given^LN|6|20330811||||||F|||20180825\r"
      + "OBX|39|DT|59778-1^Date dose is overdue^LN|6|20160908||||||F|||20180825\r"
      + "OBX|40|CE|59783-1^Series Status^LN|6|LA13423-1^Overdue^LN||||||F|||20180825\r"
      + "OBX|41|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|42|CE|30956-7^Vaccine Type^LN|7|141^Influenza, Seasonal^CVX||||||F|||20180825\r"
      + "OBX|43|DT|30981-5^Earliest date dose should be given^LN|7|20180920||||||F|||20180825\r"
      + "OBX|44|DT|30980-7^Date Vaccine Due^LN|7|20180920||||||F|||20180825\r"
      + "OBX|45|CE|59783-1^Series Status^LN|7|LA13422-3^On Schedule^LN||||||F|||20180825\r"
      + "OBX|46|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|47|CE|30956-7^Vaccine Type^LN|8|08^Hep B, ped/adol^CVX||||||F|||20180825\r"
      + "OBX|48|DT|30981-5^Earliest date dose should be given^LN|8|20180922||||||F|||20180825\r"
      + "OBX|49|DT|30980-7^Date Vaccine Due^LN|8|20180922||||||F|||20180825\r"
      + "OBX|50|DT|59778-1^Date dose is overdue^LN|8|20180922||||||F|||20180825\r"
      + "OBX|51|CE|59783-1^Series Status^LN|8|LA13422-3^On Schedule^LN||||||F|||20180825\r"
      + "OBX|52|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|53|CE|30956-7^Vaccine Type^LN|9|10^Polio-IPV^CVX||||||F|||20180825\r"
      + "OBX|54|DT|30981-5^Earliest date dose should be given^LN|9|20180922||||||F|||20180825\r"
      + "OBX|55|DT|30980-7^Date Vaccine Due^LN|9|20180922||||||F|||20180825\r"
      + "OBX|56|DT|59777-3^Latest date next dose should be given^LN|9|20320811||||||F|||20180825\r"
      + "OBX|57|DT|59778-1^Date dose is overdue^LN|9|20180922||||||F|||20180825\r"
      + "OBX|58|CE|59783-1^Series Status^LN|9|LA13422-3^On Schedule^LN||||||F|||20180825\r"
      + "OBX|59|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|60|CE|30956-7^Vaccine Type^LN|10|20^DTaP^CVX||||||F|||20180825\r"
      + "OBX|61|DT|30981-5^Earliest date dose should be given^LN|10|20180922||||||F|||20180825\r"
      + "OBX|62|DT|30980-7^Date Vaccine Due^LN|10|20180922||||||F|||20180825\r"
      + "OBX|63|DT|59778-1^Date dose is overdue^LN|10|20180922||||||F|||20180825\r"
      + "OBX|64|CE|59783-1^Series Status^LN|10|LA13422-3^On Schedule^LN||||||F|||20180825\r"
      + "OBX|65|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|66|CE|30956-7^Vaccine Type^LN|11|83^Hep A, ped/adol^CVX||||||F|||20180825\r"
      + "OBX|67|DT|30981-5^Earliest date dose should be given^LN|11|20190225||||||F|||20180825\r"
      + "OBX|68|DT|30980-7^Date Vaccine Due^LN|11|20190225||||||F|||20180825\r"
      + "OBX|69|DT|59778-1^Date dose is overdue^LN|11|20190225||||||F|||20180825\r"
      + "OBX|70|CE|59783-1^Series Status^LN|11|LA13422-3^On Schedule^LN||||||F|||20180825\r"
      + "OBX|71|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|72|CE|30956-7^Vaccine Type^LN|12|114^Meningococcal(Menac^CVX||||||F|||20180825\r"
      + "OBX|73|DT|30981-5^Earliest date dose should be given^LN|12|20250812||||||F|||20180825\r"
      + "OBX|74|DT|30980-7^Date Vaccine Due^LN|12|20250812||||||F|||20180825\r"
      + "OBX|75|DT|59777-3^Latest date next dose should be given^LN|12|20360811||||||F|||20180825\r"
      + "OBX|76|DT|59778-1^Date dose is overdue^LN|12|20270908||||||F|||20180825\r"
      + "OBX|77|CE|59783-1^Series Status^LN|12|LA13422-3^On Schedule^LN||||||F|||20180825\r"
      + "OBX|78|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|79|CE|30956-7^Vaccine Type^LN|13|187^Recombinant Zoster^CVX||||||F|||20180825\r"
      + "OBX|80|DT|30981-5^Earliest date dose should be given^LN|13|20640812||||||F|||20180825\r"
      + "OBX|81|DT|30980-7^Date Vaccine Due^LN|13|20640812||||||F|||20180825\r"
      + "OBX|82|CE|59783-1^Series Status^LN|13|LA13422-3^On Schedule^LN||||||F|||20180825\r"
      + "OBX|83|CE|59779-9^Immunization Schedule Used^LN|13|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|84|CE|30956-7^Vaccine Type^LN|14|133^PCV-13 (PREVNAR)^CVX||||||F|||20180825\r"
      + "OBX|85|DT|30981-5^Earliest date dose should be given^LN|14|20790812||||||F|||20180825\r"
      + "OBX|86|DT|30980-7^Date Vaccine Due^LN|14|20790812||||||F|||20180825\r"
      + "OBX|87|CE|59783-1^Series Status^LN|14|LA13422-3^On Schedule^LN||||||F|||20180825\r"
      + "OBX|88|CE|59779-9^Immunization Schedule Used^LN|14|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|89|CE|30956-7^Vaccine Type^LN|15|03^MMR^CVX||||||F|||20180825\r"
      + "OBX|90|CE|59783-1^Series Status^LN|15|LA13421-5^Complete^LN||||||F|||20180825\r"
      + "OBX|91|ST|30982-3^Reason Code^LN|15|Patient Series is complete||||||F|||20180825\r"
      + "OBX|92|CE|59779-9^Immunization Schedule Used^LN|15|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|93|CE|30956-7^Vaccine Type^LN|16|122^Rotavirus, UF^CVX||||||F|||20180825\r"
      + "OBX|94|CE|59783-1^Series Status^LN|16|LA13424-9^Too Old^LN||||||F|||20180825\r"
      + "OBX|95|ST|30982-3^Reason Code^LN|16|Patient has exceeded the maximum age||||||F|||20180825\r"
      + "OBX|96|CE|59779-9^Immunization Schedule Used^LN|16|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|97|CE|30956-7^Vaccine Type^LN|17|21^Varicella^CVX||||||F|||20180825\r"
      + "OBX|98|CE|59783-1^Series Status^LN|17|LA13421-5^Complete^LN||||||F|||20180825\r"
      + "OBX|99|ST|30982-3^Reason Code^LN|17|Patient Series is complete||||||F|||20180825\r"
      + "OBX|100|CE|59779-9^Immunization Schedule Used^LN|17|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r"
      + "OBX|101|CE|30956-7^Vaccine Type^LN|18|137^HPV, UF^CVX||||||F|||20180825\r"
      + "OBX|102|CE|59783-1^Series Status^LN|18|LA13422-3^On Schedule^LN||||||F|||20180825\r"
      + "OBX|103|ST|30982-3^Reason Code^LN|18|Patient Series is not complete||||||F|||20180825\r"
      + "OBX|104|CE|59779-9^Immunization Schedule Used^LN|18|VXC16^ACIP^CDCPHINVS||||||F|||20180825\r";

  private static final String RSP_RI_KIDSNET_1 = ""
      + "MSH|^~\\&|RI_KIDSNET||||20180828143436-0400||RSP^K11^RSP_K11|340701|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AA|42qo-QA.2.1.2\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|W54L74411^^^AIRA^MR|Dawes^Nadia^Conncetta^^^^L||20140812|F|81 Washtenaw Ln^^Homer^MI^49245\r"
      + "PID|1||7570057^^^RIA^SR||DAWES^NADIA^C^^^^L||20140812|F||2106-3^WHITE^CDCREC|81 WASHTENAW LN^^HOMER^MI^49245^^H||^PRN^PH^^^517^8941356\r"
      + "ORC|RE||72351878^RIA||||||||||||||2025^AIRA INTEROPERABILITY TEST PROJECT^L\r"
      + "RXA|0|1|20150823||94^MMR/V^CVX|999|||01^Historical information - source unspecified^NIP001||||||||UNK^Unknown^MVX|||CP\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|03^MMR^CVX||||||F|||20180828\r"
      + "OBX|2|CE|59779-9^Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180828\r"
      + "OBX|3|ID|59781-5^Dose_Validity^LN|1|Y||||||F|||20180828\r"
      + "OBX|4|CE|30956-7^vaccine type^LN|2|21^Varivax^CVX||||||F|||20180828\r"
      + "OBX|5|CE|59779-9^Schedule used^LN|2|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180828\r"
      + "OBX|6|ID|59781-5^Dose_Validity^LN|2|Y||||||F|||20180828\r"
      + "ORC|RE||72351877^RIA||||||||||||||2025^AIRA INTEROPERABILITY TEST PROJECT^L\r"
      + "RXA|0|1|20180823||94^MMR/V^CVX|999|||01^Historical information - source unspecified^NIP001||||||||UNK^Unknown^MVX|||CP\r"
      + "OBX|7|CE|30956-7^vaccine type^LN|3|21^Varivax^CVX||||||F|||20180828\r"
      + "OBX|8|CE|59779-9^Schedule used^LN|3|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180828\r"
      + "OBX|9|ID|59781-5^Dose_Validity^LN|3|Y||||||F|||20180828\r"
      + "OBX|10|CE|30956-7^vaccine type^LN|4|03^MMR^CVX||||||F|||20180828\r"
      + "OBX|11|CE|59779-9^Schedule used^LN|4|VXC16^ACIP Schedule^CDCPHINVS||||||F|||20180828\r"
      + "OBX|12|ID|59781-5^Dose_Validity^LN|4|Y||||||F|||20180828\r"
      + "ORC|RE||9999^RIA||||||||||||||0^^L\r"
      + "RXA|0|1|20180828||998^no vaccine admin^CVX|999||||||||||||||NA\r"
      + "OBX|13|CE|30956-7^vaccine type^LN|1|107^DTap unsp^CVX||||||F|||20180828\r"
      + "OBX|14|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180828\r"
      + "OBX|15|DT|30981-5^Earliest date dose should be given^LN|1|20140923||||||F|||20180828\r"
      + "OBX|16|DT|30980-7^Date next dose recommended^LN|1|20141012||||||F|||20180828\r"
      + "OBX|17|CE|30956-7^vaccine type^LN|2|137^HPV UNSP^CVX||||||F|||20180828\r"
      + "OBX|18|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180828\r"
      + "OBX|19|DT|30981-5^Earliest date dose should be given^LN|2|20230812||||||F|||20180828\r"
      + "OBX|20|DT|30980-7^Date next dose recommended^LN|2|20250812||||||F|||20180828\r"
      + "OBX|21|CE|30956-7^vaccine type^LN|3|45^HepB Unsp^CVX||||||F|||20180828\r"
      + "OBX|22|CE|59779-9^Immunization Schedule used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180828\r"
      + "OBX|23|DT|30981-5^Earliest date dose should be given^LN|3|20140812||||||F|||20180828\r"
      + "OBX|24|DT|30980-7^Date next dose recommended^LN|3|20140812||||||F|||20180828\r"
      + "OBX|25|CE|30956-7^vaccine type^LN|4|17^HIB Unsp^CVX||||||F|||20180828\r"
      + "OBX|26|CE|59779-9^Immunization Schedule used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180828\r"
      + "OBX|27|DT|30981-5^Earliest date dose should be given^LN|4|20150812||||||F|||20180828\r"
      + "OBX|28|DT|30980-7^Date next dose recommended^LN|4|20150812||||||F|||20180828\r"
      + "OBX|29|CE|30956-7^vaccine type^LN|5|88^FLU UNSP^CVX||||||F|||20180828\r"
      + "OBX|30|CE|59779-9^Immunization Schedule used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180828\r"
      + "OBX|31|DT|30981-5^Earliest date dose should be given^LN|5|20180701||||||F|||20180828\r"
      + "OBX|32|DT|30980-7^Date next dose recommended^LN|5|20180701||||||F|||20180828\r"
      + "OBX|33|CE|30956-7^vaccine type^LN|6|108^Mening unsp^CVX||||||F|||20180828\r"
      + "OBX|34|CE|59779-9^Immunization Schedule used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180828\r"
      + "OBX|35|DT|30981-5^Earliest date dose should be given^LN|6|20141012||||||F|||20180828\r"
      + "OBX|36|DT|30980-7^Date next dose recommended^LN|6|20250812||||||F|||20180828\r"
      + "OBX|37|CE|30956-7^vaccine type^LN|7|164^MeningB unsp^CVX||||||F|||20180828\r"
      + "OBX|38|CE|59779-9^Immunization Schedule used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180828\r"
      + "OBX|39|DT|30981-5^Earliest date dose should be given^LN|7|20240812||||||F|||20180828\r"
      + "OBX|40|DT|30980-7^Date next dose recommended^LN|7|20300812||||||F|||20180828\r"
      + "OBX|41|CE|30956-7^vaccine type^LN|8|152^PCV-Unsp^CVX||||||F|||20180828\r"
      + "OBX|42|CE|59779-9^Immunization Schedule used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180828\r"
      + "OBX|43|DT|30981-5^Earliest date dose should be given^LN|8|20150812||||||F|||20180828\r"
      + "OBX|44|DT|30980-7^Date next dose recommended^LN|8|20150812||||||F|||20180828\r"
      + "OBX|45|CE|30956-7^vaccine type^LN|9|89^Polio unsp^CVX||||||F|||20180828\r"
      + "OBX|46|CE|59779-9^Immunization Schedule used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180828\r"
      + "OBX|47|DT|30981-5^Earliest date dose should be given^LN|9|20140923||||||F|||20180828\r"
      + "OBX|48|DT|30980-7^Date next dose recommended^LN|9|20141012||||||F|||20180828\r";

  private static final String RSP_STC = ""
      + "MSH|^~\\&|^^|^^|^^|^^|20180701172446||RSP^K11^RSP_K11|1724687711.100023793|P|2.5.1|||||||||Z42^CDCPHINVS^^|\r"
      + "MSA|AA|2aYO-GM-2.1-Q|\r"
      + "QAK|1530476519196.64097|OK|Z44^Request Evaluated History and Forecast^HL70471|\r"
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
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|107^DTAP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20170818||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20170730||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170918||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|17^HIB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20170818||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20170730||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170918||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|133^PneumoPCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20170818||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20170730||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20220618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20170918||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|88^FLU^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20171218||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20171218||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20180827||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|85^HepA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20180618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180618||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20190618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|89^POLIO^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20180728||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180728||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20180827||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|03^MMR^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|2||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20210618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180728||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20240618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|21^VARICELLA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|2||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20210618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180922||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20240618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|137^HPV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20280618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20260618||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21170618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20300618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180701172446|20180701172446|998^no vaccine administered^CVX|0||||||||||||||NA||20180701172446|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|147^MENING^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20280618||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20270618||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21370618||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20300618||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";

  private static final String RSP_TN_TIR_1 = ""
      + "MSH|^~\\&|^^|^^|AIRA^^|AIRA^^|20180828104840||RSP^K11^RSP_K11|2425965575.100019197|P|2.5.1|||||||||Z42^CDCPHINVS^^|\r"
      + "MSA|AA|pXf-QA.2.1.2|\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^HL70471|\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|37374859|V06V93125^^^AIRA^MR|Grant^Penelope^Adele||20140816|F|L^^Redford Township^MI^48240|\r"
      + "PID|1||104103^^^^SR~~~~~V06V93125||GRANT^PENELOPE^ADELE^^^^L|BREMER|20140816|F|||142 TERRELL CIR^^REDFORD TOWNSHIP^MICHIGAN^48240^United States^M^^WAYNE||3132054941^PRN^PH^^^313^2054941|||||||||2186-5^not Hispanic or Latino^HL70189||N|1|||||N|\r"
      + "ORC|RE||104103.54.20150827|\r"
      + "RXA|0|999|20150827|20150827|94^MMRV^CVX^90710^MMRV^CPT~54^MMR/Varicella^STC0292|999|||01^Historical information - source unspecified^NIP001||IRMS-216269||||||||||A|20180828104840|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20150827|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20150827|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||104103.54.20180827|\r"
      + "RXA|0|999|20180827|20180827|94^MMRV^CVX^90710^MMRV^CPT~54^MMR/Varicella^STC0292|999|||01^Historical information - source unspecified^NIP001||IRMS-216269||||||||||A|20180828104840|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20180827|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20180827|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180828104840|20180828104840|998^no vaccine administered^CVX|0||||||||||||||NA||20180828104840|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|45^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140816||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140816||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340816||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141116||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180828104840|20180828104840|998^no vaccine administered^CVX|0||||||||||||||NA||20180828104840|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|107^DTAP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141016||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140927||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340816||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141116||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180828104840|20180828104840|998^no vaccine administered^CVX|0||||||||||||||NA||20180828104840|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|17^HIB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141016||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140927||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340816||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141116||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180828104840|20180828104840|998^no vaccine administered^CVX|0||||||||||||||NA||20180828104840|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|133^PneumoPCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141016||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140927||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190816||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141116||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180828104840|20180828104840|998^no vaccine administered^CVX|0||||||||||||||NA||20180828104840|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|88^FLU^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150216||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150216||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340816||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20181024||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|D^Due Now^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180828104840|20180828104840|998^no vaccine administered^CVX|0||||||||||||||NA||20180828104840|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|85^HepA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150816||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150816||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340816||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20160816||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180828104840|20180828104840|998^no vaccine administered^CVX|0||||||||||||||NA||20180828104840|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|89^POLIO^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20180924||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20180924||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340816||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20181024||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180828104840|20180828104840|998^no vaccine administered^CVX|0||||||||||||||NA||20180828104840|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|137^HPV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250816||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20230816||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21140816||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270816||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180828104840|20180828104840|998^no vaccine administered^CVX|0||||||||||||||NA||20180828104840|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|147^MENING^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250816||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20240816||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340816||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270816||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";

  private static final String RSP_TX_IMMTRAC_1 = ""
      + "MSH|^~\\&|ImmTrac2|TEXIIS||AIRA1453|20180830063319-0500||RSP^K11^RSP_K11|HdR-QA.2.1.2-D|P|2.5.1|||||||||Z32^CDCPHINVS|TEXIIS|AIRA1453\r"
      + "MSA|AA|HdR-QA.2.1.2-D||0\r" + "QAK|37374859|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|X51K43542^^^AIRA^MR|Livingston^Idelia^Penthea^^^^L||20140811|F|35 Hamilton Pl^^Le Roy^MI^49655\r"
      + "PID|1||7173731^^^ORA^SR~X51K43542^^^ORA^PI||LIVINGSTON^IDELIA^PENTHEA^^^^L|Comanche^CATHIE|20140811|F||2106-3|35 HAMILTON PL^^LE ROY^MI^49655^^P^^TXTX||^PRN^PH^^^231^6555388|||||||||2186-5||N|0\r"
      + "PD1|||||||||||02|N||||A\r"
      + "NK1|1|LIVINGSTON^COMANCHE^MARION|MTH|35 HAMILTON PL^^LE ROY^MI^49655^^P|^PRN^PH^^^231^6555388\r"
      + "ORC|RE||0^ImmTrac2\r" + "RXA|0|1|20180830|20180830|998^No Vaccine Administered^CVX|999\r"
      + "OBX|1|CE|30979-9^Vaccines Due Next^LN|0|107^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r"
      + "OBX|2|TS|30980-7^Date Vaccine Due^LN|0|20141011||||||F\r"
      + "OBX|3|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|4|TS|30981-5^Earliest date to give^LN|0|20140922||||||F\r"
      + "OBX|5|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r"
      + "OBX|6|CE|30979-9^Vaccines Due Next^LN|1|85^HepA^CVX^90730^HepA^CPT||||||F\r"
      + "OBX|7|TS|30980-7^Date Vaccine Due^LN|1|20150811||||||F\r"
      + "OBX|8|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|9|TS|30981-5^Earliest date to give^LN|1|20150811||||||F\r"
      + "OBX|10|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|11|CE|30979-9^Vaccines Due Next^LN|2|45^HepB^CVX^90731^HepB^CPT||||||F\r"
      + "OBX|12|TS|30980-7^Date Vaccine Due^LN|2|20140811||||||F\r"
      + "OBX|13|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|14|TS|30981-5^Earliest date to give^LN|2|20140811||||||F\r"
      + "OBX|15|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|16|CE|30979-9^Vaccines Due Next^LN|3|17^Hib^CVX^90737^Hib^CPT||||||F\r"
      + "OBX|17|TS|30980-7^Date Vaccine Due^LN|3|20151111||||||F\r"
      + "OBX|18|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|19|TS|30981-5^Earliest date to give^LN|3|20151111||||||F\r"
      + "OBX|20|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|21|CE|30979-9^Vaccines Due Next^LN|4|88^Influenza-seasnl^CVX^90724^Influenza-seasnl^CPT||||||F\r"
      + "OBX|22|TS|30980-7^Date Vaccine Due^LN|4|20180701||||||F\r"
      + "OBX|23|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|4|20150211||||||F\r"
      + "OBX|25|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|26|CE|30979-9^Vaccines Due Next^LN|5|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|27|TS|30980-7^Date Vaccine Due^LN|5|20150811||||||F\r"
      + "OBX|28|NM|30973-2^Vaccine due next dose number^LN|5|0||||||F\r"
      + "OBX|29|TS|30981-5^Earliest date to give^LN|5|20150811||||||F\r"
      + "OBX|30|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|31|CE|30979-9^Vaccines Due Next^LN|6|133^PneumoConjugate^CVX^90670^PneumoConjugate^CPT||||||F\r"
      + "OBX|32|TS|30980-7^Date Vaccine Due^LN|6|20141011||||||F\r"
      + "OBX|33|NM|30973-2^Vaccine due next dose number^LN|6|1||||||F\r"
      + "OBX|34|TS|30981-5^Earliest date to give^LN|6|20140922||||||F\r"
      + "OBX|35|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r"
      + "OBX|36|CE|30979-9^Vaccines Due Next^LN|7|89^Polio^CVX||||||F\r"
      + "OBX|37|TS|30980-7^Date Vaccine Due^LN|7|20141011||||||F\r"
      + "OBX|38|NM|30973-2^Vaccine due next dose number^LN|7|1||||||F\r"
      + "OBX|39|TS|30981-5^Earliest date to give^LN|7|20140922||||||F\r"
      + "OBX|40|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|7|ACIP schedule||||||F\r"
      + "OBX|41|CE|30979-9^Vaccines Due Next^LN|8|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|42|TS|30980-7^Date Vaccine Due^LN|8|20150811||||||F\r"
      + "OBX|43|NM|30973-2^Vaccine due next dose number^LN|8|1||||||F\r"
      + "OBX|44|TS|30981-5^Earliest date to give^LN|8|20150811||||||F\r"
      + "OBX|45|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|8|ACIP schedule||||||F\r";

  private static final String RSP_VA_VIIS_1 = ""
      + "MSH|^~\\&|VIIS|VIIS||AIT|20170803130318||RSP^K11^RSP_K11|2.1.2|P|2.5.1|||||||||Z32^CDCPHINVS\r"
      + "MSA|AE|2.1.2||0||0^Message Accepted^HL70357\r" + "QAK|2.1.2|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|37374859|I34W637^^^AIRA^MR|Hardin^Aphrodite^Ciara^^^^L||20130722|F|94 Iron Ave^^Farmington Hills^MI^48333\r"
      + "PID|1||786281^^^^SR||HARDIN^APHRODITE^CIARA|WASHINGTON^JOSLIN|20130722|F||2106-3||||||||||||2186-5||N|0\r"
      + "PD1|||||||||||02|N||||A\r"
      + "NK1|1|HARDIN^WASHINGTON^MARION|MTH|94 IRON AVE^^FARMINGTON HILLS^MI^48333^^M|^^PH^^^248^6355620\r"
      + "NK1|2|HARDIN^APHRODITE^CIARA|SEL|94 IRON AVE^^FARMINGTON HILLS^MI^48333^^M|^^PH^^^248^6355620\r"
      + "ORC|RE||32818651\r"
      + "RXA|0|1|20140802|20140802|94^Proquad^CVX^90710^Proquad^CPT|999|||01\r"
      + "OBX|1|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|94^MMRV^CVX^90710^MMRV^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|3|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|94^MMRV^CVX^90710^MMRV^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|1||||||F\r" + "ORC|RE||32818652\r"
      + "RXA|0|1|20170802|20170802|94^Proquad^CVX^90710^Proquad^CPT|999|||01\r"
      + "OBX|1|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|94^MMRV^CVX^90710^MMRV^CPT||||||F\r"
      + "OBX|2|NM|30973-2^Dose number in series^LN|1|2||||||F\r"
      + "OBX|3|CE|38890-0^COMPONENT VACCINE TYPE^LN|2|94^MMRV^CVX^90710^MMRV^CPT||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|2|2||||||F\r" + "ORC|RE||0\r"
      + "RXA|0|1|20130722|20130722|998^No Vaccine Administered^CVX|999\r"
      + "OBX|1|CE|30979-9^Vaccines Due Next^LN|0|20^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r"
      + "OBX|2|TS|30980-7^Date Vaccine Due^LN|0|20130922||||||F\r"
      + "OBX|3|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|4|TS|30981-5^Earliest date to give^LN|0|20130902||||||F\r"
      + "OBX|5|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r"
      + "OBX|6|CE|30979-9^Vaccines Due Next^LN|1|85^HepA^CVX^90730^HepA^CPT||||||F\r"
      + "OBX|7|TS|30980-7^Date Vaccine Due^LN|1|20150722||||||F\r"
      + "OBX|8|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|9|TS|30981-5^Earliest date to give^LN|1|20140722||||||F\r"
      + "OBX|10|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r"
      + "OBX|11|CE|30979-9^Vaccines Due Next^LN|2|45^HepB^CVX^90731^HepB^CPT||||||F\r"
      + "OBX|12|TS|30980-7^Date Vaccine Due^LN|2|20130722||||||F\r"
      + "OBX|13|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|14|TS|30981-5^Earliest date to give^LN|2|20130722||||||F\r"
      + "OBX|15|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r"
      + "OBX|16|CE|30979-9^Vaccines Due Next^LN|3|17^Hib^CVX^90737^Hib^CPT||||||F\r"
      + "OBX|17|TS|30980-7^Date Vaccine Due^LN|3|20130922||||||F\r"
      + "OBX|18|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|19|TS|30981-5^Earliest date to give^LN|3|20130902||||||F\r"
      + "OBX|20|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r"
      + "OBX|21|CE|30979-9^Vaccines Due Next^LN|4|88^Influenza^CVX^90724^Influenza^CPT||||||F\r"
      + "OBX|22|TS|30980-7^Date Vaccine Due^LN|4|20140722||||||F\r"
      + "OBX|23|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|4|20140122||||||F\r"
      + "OBX|25|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r"
      + "OBX|26|CE|30979-9^Vaccines Due Next^LN|5|100^Pneumococcal^CVX^90669^Pneumococcal^CPT||||||F\r"
      + "OBX|27|TS|30980-7^Date Vaccine Due^LN|5|20130922||||||F\r"
      + "OBX|28|NM|30973-2^Vaccine due next dose number^LN|5|1||||||F\r"
      + "OBX|29|TS|30981-5^Earliest date to give^LN|5|20130902||||||F\r"
      + "OBX|30|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r"
      + "OBX|31|CE|30979-9^Vaccines Due Next^LN|6|89^Polio^CVX||||||F\r"
      + "OBX|32|TS|30980-7^Date Vaccine Due^LN|6|20130922||||||F\r"
      + "OBX|33|NM|30973-2^Vaccine due next dose number^LN|6|1||||||F\r"
      + "OBX|34|TS|30981-5^Earliest date to give^LN|6|20130902||||||F\r"
      + "OBX|35|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r";

  private static final String RSP_WA_WAIIS_1 = ""
      + "MSH|^~\\&|^^|^^|^^|^^|20180827120944||RSP^K11^RSP_K11|5613246782.100006573|P|2.5.1|||||||||Z42^CDCPHINVS^^|\r"
      + "MSA|AA|2Sbs-QA.2.1.2|\r"
      + "QAK|37374859|OK|Z44^Request Evaluated History and Forecast^HL70471|\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|37374859|S53F79210^^^AIRA^MR|Branch^Sashi^Pascale||20140815|F|L^^Topinabee^MI^49791|\r"
      + "PID|1||6337710^^^^SR~~~~~S53F79210^^^^MR||BRANCH^SASHI^PASCALE^^^^L|DUVAL|20140815|F|||11 LOUP PL^^TOPINABEE^MICHIGAN^49791^United States^M^^CHEBOYGAN||2315801841^PRN^PH^^^231^5801841|||||||||2186-5^not Hispanic or Latino^HL70189||N|1|||||N|\r"
      + "ORC|RE||6337710.54.20150826|\r"
      + "RXA|0|999|20150826|20150826|94^MMRV^CVX^90710^MMRV^CPT|999|||01^Historical information - source unspecified^NIP001||IRMS-1033||||||||||A|20180827120944|\r"
      + "RXR|IM^Intramuscular^CDCPHINVS|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20150826|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20150826|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||6337710.54.20180826|\r"
      + "RXA|0|999|20180826|20180826|94^MMRV^CVX^90710^MMRV^CPT|999|||01^Historical information - source unspecified^NIP001||IRMS-1033||||||||||A|20180827120944|\r"
      + "RXR|IM^Intramuscular^CDCPHINVS|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V00^Unknown^HL70064||||||F|||20180826|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V00^Unknown^HL70064||||||F|||20180826|||CVX40^per imm^CDCPHINVS|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827120944|20180827120944|998^no vaccine administered^CVX|0||||||||||||||NA||20180827120944|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|45^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20140815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827120944|20180827120944|998^no vaccine administered^CVX|0||||||||||||||NA||20180827120944|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|107^DTAP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827120944|20180827120944|998^no vaccine administered^CVX|0||||||||||||||NA||20180827120944|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|17^HIB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141215||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827120944|20180827120944|998^no vaccine administered^CVX|0||||||||||||||NA||20180827120944|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|133^PneumoPCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20190815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827120944|20180827120944|998^no vaccine administered^CVX|0||||||||||||||NA||20180827120944|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|89^POLIO^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20141015||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20140926||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20141115||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827120944|20180827120944|998^no vaccine administered^CVX|0||||||||||||||NA||20180827120944|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|88^FLU^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150215||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150215||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20150317||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827120944|20180827120944|998^no vaccine administered^CVX|0||||||||||||||NA||20180827120944|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|85^HepA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20150815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20150815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20160815||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827120944|20180827120944|998^no vaccine administered^CVX|0||||||||||||||NA||20180827120944|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|137^HPV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20230815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21140815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270815||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r"
      + "ORC|RE||9999|\r"
      + "RXA|0|999|20180827120944|20180827120944|998^no vaccine administered^CVX|0||||||||||||||NA||20180827120944|\r"
      + "OBX|1|CE|30956-7^vaccine type^LN|1|147^MENING^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20250815||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20240815||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21340815||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20270815||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";

  private static final String RSP_WI_WIR_1 = ""
      + "MSH|^~\\&|WIR|||10318|20180828132648-0500||RSP^K11^RSP_K11|889240|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AE|2peD-QA.2.1.2\r"
      + "ERR||QPD^1^8^1^7|101^Required field missing^HL70357|W||Address Type~1||Address Type is required\r"
      + "QAK|2peD-QA.2.1.2|AE|Z44^Request Immunization History^HL70471|1\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|37374859|X53X93717^^^AIRA^MR|Heffernan^Iman^Liani^^^^L||20140816|F|67 Mercer Ln^^Comerica Incorporated^MI^48264\r"
      + "PID|1||3027158^^^WIA^SR~X53X93717^^^WIA^PI||HEFFERNAN^IMAN^LIANI^^^^^L|OLSON^FALDA^^^^^^M|20140816|F|||67 MERCER LN^^COMERICA INCORPORATED^MI^48264^^H||^PRN^^^^313^2570314|||||||||2186-5^not Hispanic or Latino^CDCREC||||||||N\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N\r"
      + "NK1|1|HEFFERNAN^OLSON^MARION^^^^^L|MTH^Mother^HL70063|67 MERCER LN^^COMERICA INCORPORATED^MI^48264^^H|^PRN^^^^313^2570314\r"
      + "NK1|2|HEFFERNAN^IMAN^LIANI^^^^^L|SEL^Self^HL70063|67 MERCER LN^^COMERICA INCORPORATED^MI^48264^^H|^PRN^^^^313^2570314\r"
      + "ORC|RE||38144418^WIA||||||||||||||||||Indian Health Services^D^^^^WIA^XX^^^10318\r"
      + "RXA|0|1|20150827|20150827|94^MMRV^CVX^90710^MMRV^C4|999|||01|||||||||||||20180828\r"
      + "OBX|1|CE|38890-0^Component Vaccine Type^LN|1|03^MMR^CVX^90707^MMR^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|1^MMR^SeriesNames||||||F\r"
      + "OBX|4|NM|59782-3^Number of doses in primary series^LN|1|2||||||F\r"
      + "OBX|5|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|6|CE|59783-1^Status in immunization series^LN|1|1 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|7|CE|38890-0^Component Vaccine Type^LN|2|21^Varicella^CVX^90716^Varicella^C4||||||F\r"
      + "OBX|8|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|9|CE|59780-7^Immunization Series name^LN|2|69^Varicella^SeriesNames||||||F\r"
      + "OBX|10|NM|59782-3^Number of doses in primary series^LN|2|2||||||F\r"
      + "OBX|11|ID|59781-5^Dose validity^LN|2|Y||||||F\r"
      + "OBX|12|CE|59783-1^Status in immunization series^LN|2|1 of 2^In progress^SeriesStatus||||||F\r"
      + "ORC|RE||38144419^WIA||||||||||||||||||Indian Health Services^D^^^^WIA^XX^^^10318\r"
      + "RXA|0|1|20180827|20180827|94^MMRV^CVX^90710^MMRV^C4|999|||01|||||||||||||20180828\r"
      + "OBX|1|CE|38890-0^Component Vaccine Type^LN|1|03^MMR^CVX^90707^MMR^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|1^MMR^SeriesNames||||||F\r"
      + "OBX|4|NM|59782-3^Number of doses in primary series^LN|1|2||||||F\r"
      + "OBX|5|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|6|CE|59783-1^Status in immunization series^LN|1|2 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|7|ST|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|\\R\\\\R\\!@!\\R\\!@\\R\\!@\\R\\!@@@\\R\\!@\\R\\!@\\R\\!@\\R\\!@|rule.primaryVaccineGroupSeriesCompleted|||||F\r"
      + "OBX|8|CE|38890-0^Component Vaccine Type^LN|2|21^Varicella^CVX^90716^Varicella^C4||||||F\r"
      + "OBX|9|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|10|CE|59780-7^Immunization Series name^LN|2|69^Varicella^SeriesNames||||||F\r"
      + "OBX|11|NM|59782-3^Number of doses in primary series^LN|2|2||||||F\r"
      + "OBX|12|ID|59781-5^Dose validity^LN|2|Y||||||F\r"
      + "OBX|13|CE|59783-1^Status in immunization series^LN|2|2 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|14|ST|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|Vaccination met the minimum interval to validate which may have included a grace period.|rule.minValidInt|||||F\r"
      + "ORC|RE||9999^NA\r"
      + "RXA|0|1|20180828|20180828|998^No vaccination administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|20^DTP/aP^CVX^90700^DTaP^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|3^DTAP^SeriesNames||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|5|NM|59782-3^Number of doses in primary series^LN|1|5||||||F\r"
      + "OBX|6|TS|30981-5^Earliest date to give^LN|1|20140927||||||F\r"
      + "OBX|7|TS|30980-7^Date vaccine due^LN|1|20141016||||||F\r"
      + "OBX|8|TS|59778-1^Vaccine overdue date^LN|1|20141116||||||F\r"
      + "OBX|9|CE|59783-1^Status in immunization series^LN|1|1 of 5^In progress^SeriesStatus||||||F\r"
      + "OBX|10|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|2|85^HepA^CVX^90730^Hep A, unspecified formulation^C4||||||F\r"
      + "OBX|11|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|12|CE|59780-7^Immunization Series name^LN|2|2443^Hep A^SeriesNames||||||F\r"
      + "OBX|13|NM|30973-2^Dose number in series^LN|2|1||||||F\r"
      + "OBX|14|NM|59782-3^Number of doses in primary series^LN|2|2||||||F\r"
      + "OBX|15|TS|30981-5^Earliest date to give^LN|2|20150816||||||F\r"
      + "OBX|16|TS|30980-7^Date vaccine due^LN|2|20150816||||||F\r"
      + "OBX|17|TS|59778-1^Vaccine overdue date^LN|2|20160816||||||F\r"
      + "OBX|18|CE|59783-1^Status in immunization series^LN|2|1 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|19|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|3|45^HepB^CVX^90731^Hep B, unspecified formulation^C4||||||F\r"
      + "OBX|20|CE|59779-9^Immunization Schedule used^LN|3|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|21|CE|59780-7^Immunization Series name^LN|3|93^Hep B^SeriesNames||||||F\r"
      + "OBX|22|NM|30973-2^Dose number in series^LN|3|1||||||F\r"
      + "OBX|23|NM|59782-3^Number of doses in primary series^LN|3|3||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|3|20140816||||||F\r"
      + "OBX|25|TS|30980-7^Date vaccine due^LN|3|20140816||||||F\r"
      + "OBX|26|TS|59778-1^Vaccine overdue date^LN|3|20141116||||||F\r"
      + "OBX|27|CE|59783-1^Status in immunization series^LN|3|1 of 3^In progress^SeriesStatus||||||F\r"
      + "OBX|28|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|4|17^Hib^CVX^90737^Hib, unspecified formulation^C4||||||F\r"
      + "OBX|29|CE|59779-9^Immunization Schedule used^LN|4|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|30|CE|59780-7^Immunization Series name^LN|4|4^HIB^SeriesNames||||||F\r"
      + "OBX|31|NM|30973-2^Dose number in series^LN|4|1||||||F\r"
      + "OBX|32|NM|59782-3^Number of doses in primary series^LN|4|4||||||F\r"
      + "OBX|33|TS|30981-5^Earliest date to give^LN|4|20140927||||||F\r"
      + "OBX|34|TS|30980-7^Date vaccine due^LN|4|20141016||||||F\r"
      + "OBX|35|TS|59778-1^Vaccine overdue date^LN|4|20141116||||||F\r"
      + "OBX|36|CE|59783-1^Status in immunization series^LN|4|1 of 4^In progress^SeriesStatus||||||F\r"
      + "OBX|37|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|5|88^Influenza^CVX^90724^Influenza, unspecified formulation^C4||||||F\r"
      + "OBX|38|CE|59779-9^Immunization Schedule used^LN|5|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|39|CE|59780-7^Immunization Series name^LN|5|2482^Influenza^SeriesNames||||||F\r"
      + "OBX|40|NM|30973-2^Dose number in series^LN|5|1||||||F\r"
      + "OBX|41|NM|59782-3^Number of doses in primary series^LN|5|2||||||F\r"
      + "OBX|42|TS|30981-5^Earliest date to give^LN|5|20150216||||||F\r"
      + "OBX|43|TS|30980-7^Date vaccine due^LN|5|20180801||||||F\r"
      + "OBX|44|TS|59778-1^Vaccine overdue date^LN|5|20150816||||||F\r"
      + "OBX|45|CE|59783-1^Status in immunization series^LN|5|1 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|46|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|6|152^Pneumococcal^CVX||||||F\r"
      + "OBX|47|CE|59779-9^Immunization Schedule used^LN|6|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|48|CE|59780-7^Immunization Series name^LN|6|67^Pneumococcal^SeriesNames||||||F\r"
      + "OBX|49|NM|30973-2^Dose number in series^LN|6|1||||||F\r"
      + "OBX|50|NM|59782-3^Number of doses in primary series^LN|6|4||||||F\r"
      + "OBX|51|TS|30981-5^Earliest date to give^LN|6|20140927||||||F\r"
      + "OBX|52|TS|30980-7^Date vaccine due^LN|6|20141016||||||F\r"
      + "OBX|53|TS|59778-1^Vaccine overdue date^LN|6|20141116||||||F\r"
      + "OBX|54|CE|59783-1^Status in immunization series^LN|6|1 of 4^In progress^SeriesStatus||||||F\r"
      + "OBX|55|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|7|89^Polio^CVX||||||F\r"
      + "OBX|56|CE|59779-9^Immunization Schedule used^LN|7|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|57|CE|59780-7^Immunization Series name^LN|7|64^Polio^SeriesNames||||||F\r"
      + "OBX|58|NM|30973-2^Dose number in series^LN|7|1||||||F\r"
      + "OBX|59|NM|59782-3^Number of doses in primary series^LN|7|5||||||F\r"
      + "OBX|60|TS|30981-5^Earliest date to give^LN|7|20140927||||||F\r"
      + "OBX|61|TS|30980-7^Date vaccine due^LN|7|20141016||||||F\r"
      + "OBX|62|TS|59778-1^Vaccine overdue date^LN|7|20141116||||||F\r"
      + "OBX|63|CE|59783-1^Status in immunization series^LN|7|1 of 5^In progress^SeriesStatus||||||F\r";

  private static final String RSP_WI_WIR_3 = ""
      + "MSH|^~\\&|WIR|||10318|20190510064557-0500||RSP^K11^RSP_K11|1026822|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AE|bk4tkz\r"
      + "ERR||MSH^1^6^1|102^Data type error^HL70357|W|4^Invalid value^HL70533|23A~HL70362||Value [23A] is not valid for HL70362\r"
      + "QAK|bk4tkz|AE|Z44^Request Immunization History^HL70471|1\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|bk4tkz|bk4tkz^^^FITS^MR|Knox^Autumn^Kuldeep^^^^L|Kay^Leanne^^^^^M|20190302|F|4 Beaver Ave^^Redford^MI^48240^USA^P\r"
      + "PID|1||3080428^^^WIA^SR~BK4TKZ^^^WIA^PI||KNOX^AUTUMN^KULDEEP^^^^^L|KAY^LEANNE^^^^^^M|20190302|F|||4 BEAVER AVE^^REDFORD^MI^48240^^H||^PRN^^^^313^5018377|||||||||||||||||N\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N\r"
      + "NK1|1|NELSEN^LEANNE^^^^^^L|MTH^Mother^HL70063\r"
      + "NK1|2|KNOX^AUTUMN^KULDEEP^^^^^L|SEL^Self^HL70063|4 BEAVER AVE^^REDFORD^MI^48240^^H|^PRN^^^^313^5018377\r"
      + "ORC|RE||38217537^WIA||||||||||||||||||Indian Health Services^D^^^^WIA^XX^^^10318\r"
      + "RXA|0|1|20190410|20190410|20^DTaP^CVX^90700^DTaP^C4|999|||01|||||||||||||20190510\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|20^DTP/aP^CVX^90700^DTaP^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|3^DTAP^SeriesNames||||||F\r"
      + "OBX|4|NM|59782-3^Number of doses in primary series^LN|1|5||||||F\r"
      + "OBX|5|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|6|CE|59783-1^Status in immunization series^LN|1|1 of 5^In progress^SeriesStatus||||||F\r"
      + "ORC|RE||38217538^WIA||||||||||||||||||Indian Health Services^D^^^^WIA^XX^^^10318\r"
      + "RXA|0|1|20190506|20190506|20^DTaP^CVX^90700^DTaP^C4|999|||01|||||||||||||20190510\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|20^DTP/aP^CVX^90700^DTaP^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|3^DTAP^SeriesNames||||||F\r"
      + "OBX|4|NM|59782-3^Number of doses in primary series^LN|1|5||||||F\r"
      + "OBX|5|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|6|CE|59783-1^Status in immunization series^LN|1|2 of 5^In progress^SeriesStatus||||||F\r"
      + "ORC|RE||9999^NA\r"
      + "RXA|0|1|20190510|20190510|998^No vaccination administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|20^DTP/aP^CVX^90700^DTaP^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|3^DTAP^SeriesNames||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|1|3||||||F\r"
      + "OBX|5|NM|59782-3^Number of doses in primary series^LN|1|5||||||F\r"
      + "OBX|6|TS|30981-5^Earliest date to give^LN|1|20190603||||||F\r"
      + "OBX|7|TS|30980-7^Date vaccine due^LN|1|20190706||||||F\r"
      + "OBX|8|TS|59778-1^Vaccine overdue date^LN|1|20190806||||||F\r"
      + "OBX|9|CE|59783-1^Status in immunization series^LN|1|3 of 5^In progress^SeriesStatus||||||F\r"
      + "OBX|10|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|2|45^HepB^CVX^90731^Hep B, unspecified formulation^C4||||||F\r"
      + "OBX|11|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|12|CE|59780-7^Immunization Series name^LN|2|93^Hep B^SeriesNames||||||F\r"
      + "OBX|13|NM|30973-2^Dose number in series^LN|2|1||||||F\r"
      + "OBX|14|NM|59782-3^Number of doses in primary series^LN|2|3||||||F\r"
      + "OBX|15|TS|30981-5^Earliest date to give^LN|2|20190302||||||F\r"
      + "OBX|16|TS|30980-7^Date vaccine due^LN|2|20190302||||||F\r"
      + "OBX|17|TS|59778-1^Vaccine overdue date^LN|2|20190602||||||F\r"
      + "OBX|18|CE|59783-1^Status in immunization series^LN|2|1 of 3^In progress^SeriesStatus||||||F\r"
      + "OBX|19|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|3|17^Hib^CVX^90737^Hib, unspecified formulation^C4||||||F\r"
      + "OBX|20|CE|59779-9^Immunization Schedule used^LN|3|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|21|CE|59780-7^Immunization Series name^LN|3|4^HIB^SeriesNames||||||F\r"
      + "OBX|22|NM|30973-2^Dose number in series^LN|3|1||||||F\r"
      + "OBX|23|NM|59782-3^Number of doses in primary series^LN|3|4||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|3|20190413||||||F\r"
      + "OBX|25|TS|30980-7^Date vaccine due^LN|3|20190502||||||F\r"
      + "OBX|26|TS|59778-1^Vaccine overdue date^LN|3|20190602||||||F\r"
      + "OBX|27|CE|59783-1^Status in immunization series^LN|3|1 of 4^In progress^SeriesStatus||||||F\r"
      + "OBX|28|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|4|03^MMR^CVX^90707^MMR^C4||||||F\r"
      + "OBX|29|CE|59779-9^Immunization Schedule used^LN|4|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|30|CE|59780-7^Immunization Series name^LN|4|1^MMR^SeriesNames||||||F\r"
      + "OBX|31|NM|30973-2^Dose number in series^LN|4|1||||||F\r"
      + "OBX|32|NM|59782-3^Number of doses in primary series^LN|4|2||||||F\r"
      + "OBX|33|TS|30981-5^Earliest date to give^LN|4|20200302||||||F\r"
      + "OBX|34|TS|30980-7^Date vaccine due^LN|4|20200302||||||F\r"
      + "OBX|35|TS|59778-1^Vaccine overdue date^LN|4|20200702||||||F\r"
      + "OBX|36|CE|59783-1^Status in immunization series^LN|4|1 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|37|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|5|152^Pneumococcal^CVX||||||F\r"
      + "OBX|38|CE|59779-9^Immunization Schedule used^LN|5|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|39|CE|59780-7^Immunization Series name^LN|5|67^Pneumococcal^SeriesNames||||||F\r"
      + "OBX|40|NM|30973-2^Dose number in series^LN|5|1||||||F\r"
      + "OBX|41|NM|59782-3^Number of doses in primary series^LN|5|4||||||F\r"
      + "OBX|42|TS|30981-5^Earliest date to give^LN|5|20190413||||||F\r"
      + "OBX|43|TS|30980-7^Date vaccine due^LN|5|20190502||||||F\r"
      + "OBX|44|TS|59778-1^Vaccine overdue date^LN|5|20190602||||||F\r"
      + "OBX|45|CE|59783-1^Status in immunization series^LN|5|1 of 4^In progress^SeriesStatus||||||F\r"
      + "OBX|46|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|6|89^Polio^CVX||||||F\r"
      + "OBX|47|CE|59779-9^Immunization Schedule used^LN|6|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|48|CE|59780-7^Immunization Series name^LN|6|64^Polio^SeriesNames||||||F\r"
      + "OBX|49|NM|30973-2^Dose number in series^LN|6|1||||||F\r"
      + "OBX|50|NM|59782-3^Number of doses in primary series^LN|6|5||||||F\r"
      + "OBX|51|TS|30981-5^Earliest date to give^LN|6|20190413||||||F\r"
      + "OBX|52|TS|30980-7^Date vaccine due^LN|6|20190502||||||F\r"
      + "OBX|53|TS|59778-1^Vaccine overdue date^LN|6|20190602||||||F\r"
      + "OBX|54|CE|59783-1^Status in immunization series^LN|6|1 of 5^In progress^SeriesStatus||||||F\r"
      + "OBX|55|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|7|122^Rotavirus^CVX||||||F\r"
      + "OBX|56|CE|59779-9^Immunization Schedule used^LN|7|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|57|CE|59780-7^Immunization Series name^LN|7|2462^Rotavirus^SeriesNames||||||F\r"
      + "OBX|58|NM|30973-2^Dose number in series^LN|7|1||||||F\r"
      + "OBX|59|NM|59782-3^Number of doses in primary series^LN|7|3||||||F\r"
      + "OBX|60|TS|30981-5^Earliest date to give^LN|7|20190413||||||F\r"
      + "OBX|61|TS|30980-7^Date vaccine due^LN|7|20190502||||||F\r"
      + "OBX|62|TS|59778-1^Vaccine overdue date^LN|7|20190502||||||F\r"
      + "OBX|63|CE|59783-1^Status in immunization series^LN|7|1 of 3^In progress^SeriesStatus||||||F\r"
      + "OBX|64|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|8|21^Varicella^CVX^90716^Varicella^C4||||||F\r"
      + "OBX|65|CE|59779-9^Immunization Schedule used^LN|8|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|66|CE|59780-7^Immunization Series name^LN|8|69^Varicella^SeriesNames||||||F\r"
      + "OBX|67|NM|30973-2^Dose number in series^LN|8|1||||||F\r"
      + "OBX|68|NM|59782-3^Number of doses in primary series^LN|8|2||||||F\r"
      + "OBX|69|TS|30981-5^Earliest date to give^LN|8|20200302||||||F\r"
      + "OBX|70|TS|30980-7^Date vaccine due^LN|8|20200302||||||F\r"
      + "OBX|71|TS|59778-1^Vaccine overdue date^LN|8|20230302||||||F\r"
      + "OBX|72|CE|59783-1^Status in immunization series^LN|8|1 of 2^In progress^SeriesStatus||||||F\r"
      + "\r";

  @Test
  public void testRSP_WI_WIR_3() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_WI_WIR_3);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "20",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "20",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Not all forecasts read", 10, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "06/03/2019",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "07/06/2019",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/06/2019",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "06/03/2019",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "07/06/2019",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/06/2019",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "03/02/2019",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "03/02/2019",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "06/02/2019",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "04/13/2019",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "05/02/2019",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "06/02/2019",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "03/02/2020",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "03/02/2020",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/02/2020",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "04/13/2019",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "05/02/2019",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "06/02/2019",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "04/13/2019",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "05/02/2019",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "06/02/2019",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "04/13/2019",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "05/02/2019",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "06/02/2019",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "04/13/2019",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "05/02/2019",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "05/02/2019",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "03/02/2020",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "03/02/2020",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "03/02/2023",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
  }

  private static final String RSP_WI_WIR_4 = ""
      + "MSH|^~\\&|WIR|||10318|20190515084633-0500||RSP^K11^RSP_K11|1028770|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AE|to6qiu\r"
      + "ERR||MSH^1^6^1|102^Data type error^HL70357|W|4^Invalid value^HL70533|23A~HL70362||Value [23A] is not valid for HL70362\r"
      + "QAK|to6qiu|AE|Z44^Request Immunization History^HL70471|1\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|to6qiu|to6qiu^^^FITS^MR|Juniata^Matilda^Galahad^^^^L|Breckinridge^Carenza^^^^^M|20060903|F|305 Huntingdon St^^Brighton Township^MI^48114^USA^P\r"
      + "PID|1||3081592^^^WIA^SR~TO6QIU^^^WIA^PI||JUNIATA^MATILDA^GALAHAD^^^^^L|BRECKINRIDGE^CARENZA^^^^^^M|20060903|F|||305 HUNTINGDON ST^^BRIGHTON TOWNSHIP^MI^48114^^H||^PRN^^^^810^9400217|||||||||||||||||N\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N\r"
      + "NK1|1|CASS^CARENZA^^^^^^L|MTH^Mother^HL70063\r"
      + "NK1|2|JUNIATA^MATILDA^GALAHAD^^^^^L|SEL^Self^HL70063|305 HUNTINGDON ST^^BRIGHTON TOWNSHIP^MI^48114^^H|^PRN^^^^810^9400217\r"
      + "ORC|RE||38218644^WIA||||||||||||||||||Indian Health Services^D^^^^WIA^XX^^^10318\r"
      + "RXA|0|1|20061103|20061103|20^DTaP^CVX^90700^DTaP^C4|999|||01|||||||||||||20190515\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|20^DTP/aP^CVX^90700^DTaP^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|3^DTAP^SeriesNames||||||F\r"
      + "OBX|4|NM|59782-3^Number of doses in primary series^LN|1|5||||||F\r"
      + "OBX|5|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|6|CE|59783-1^Status in immunization series^LN|1|1 of 5^In progress^SeriesStatus||||||F\r"
      + "ORC|RE||38218645^WIA||||||||||||||||||Indian Health Services^D^^^^WIA^XX^^^10318\r"
      + "RXA|0|1|20070103|20070103|20^DTaP^CVX^90700^DTaP^C4|999|||01|||||||||||||20190515\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|20^DTP/aP^CVX^90700^DTaP^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|3^DTAP^SeriesNames||||||F\r"
      + "OBX|4|NM|59782-3^Number of doses in primary series^LN|1|5||||||F\r"
      + "OBX|5|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|6|CE|59783-1^Status in immunization series^LN|1|2 of 5^In progress^SeriesStatus||||||F\r"
      + "ORC|RE||38218646^WIA||||||||||||||||||Indian Health Services^D^^^^WIA^XX^^^10318\r"
      + "RXA|0|1|20070303|20070303|20^DTaP^CVX^90700^DTaP^C4|999|||01|||||||||||||20190515\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|20^DTP/aP^CVX^90700^DTaP^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|3^DTAP^SeriesNames||||||F\r"
      + "OBX|4|NM|59782-3^Number of doses in primary series^LN|1|5||||||F\r"
      + "OBX|5|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|6|CE|59783-1^Status in immunization series^LN|1|3 of 5^In progress^SeriesStatus||||||F\r"
      + "ORC|RE||38218647^WIA||||||||||||||||||Indian Health Services^D^^^^WIA^XX^^^10318\r"
      + "RXA|0|1|20071206|20071206|20^DTaP^CVX^90700^DTaP^C4|999|||01|||||||||||||20190515\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|20^DTP/aP^CVX^90700^DTaP^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|3^DTAP^SeriesNames||||||F\r"
      + "OBX|4|NM|59782-3^Number of doses in primary series^LN|1|5||||||F\r"
      + "OBX|5|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|6|CE|59783-1^Status in immunization series^LN|1|4 of 5^In progress^SeriesStatus||||||F\r"
      + "OBX|7|ST|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|Vaccination met the minimum interval to validate which may have included a grace period.|rule.minValidInt|||||F\r"
      + "ORC|RE||38218648^WIA||||||||||||||||||Indian Health Services^D^^^^WIA^XX^^^10318\r"
      + "RXA|0|1|20111201|20111201|20^DTaP^CVX^90700^DTaP^C4|999|||01|||||||||||||20190515\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|20^DTP/aP^CVX^90700^DTaP^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|3^DTAP^SeriesNames||||||F\r"
      + "OBX|4|NM|59782-3^Number of doses in primary series^LN|1|5||||||F\r"
      + "OBX|5|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|6|CE|59783-1^Status in immunization series^LN|1|5 of 5^In progress^SeriesStatus||||||F\r"
      + "OBX|7|ST|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|Vaccination met the minimum interval to validate which may have included a grace period.|rule.minValidInt|||||F\r"
      + "ORC|RE||38218649^WIA||||||||||||||||||Indian Health Services^D^^^^WIA^XX^^^10318\r"
      + "RXA|0|1|20190511|20190511|09^Td^CVX^90718^Td^C4|999|||01|||||||||||||20190515\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|139^Td^CVX||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|8^Td^SeriesNames||||||F\r"
      + "OBX|4|NM|59782-3^Number of doses in primary series^LN|1|0||||||F\r"
      + "OBX|5|ID|59781-5^Dose validity^LN|1|Y||||||F\r"
      + "OBX|6|CE|59783-1^Status in immunization series^LN|1|Booster^In progress^SeriesStatus||||||F\r"
      + "OBX|7|ST|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|Vaccination met the minimum interval to validate which may have included a grace period.|rule.minValidInt|||||F\r"
      + "ORC|RE||9999^NA\r"
      + "RXA|0|1|20190515|20190515|998^No vaccination administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|85^HepA^CVX^90730^Hep A, unspecified formulation^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|2443^Hep A^SeriesNames||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|5|NM|59782-3^Number of doses in primary series^LN|1|2||||||F\r"
      + "OBX|6|TS|30981-5^Earliest date to give^LN|1|20070903||||||F\r"
      + "OBX|7|TS|30980-7^Date vaccine due^LN|1|20070903||||||F\r"
      + "OBX|8|TS|59778-1^Vaccine overdue date^LN|1|20080903||||||F\r"
      + "OBX|9|CE|59783-1^Status in immunization series^LN|1|1 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|10|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|2|45^HepB^CVX^90731^Hep B, unspecified formulation^C4||||||F\r"
      + "OBX|11|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|12|CE|59780-7^Immunization Series name^LN|2|93^Hep B^SeriesNames||||||F\r"
      + "OBX|13|NM|30973-2^Dose number in series^LN|2|1||||||F\r"
      + "OBX|14|NM|59782-3^Number of doses in primary series^LN|2|3||||||F\r"
      + "OBX|15|TS|30981-5^Earliest date to give^LN|2|20060903||||||F\r"
      + "OBX|16|TS|30980-7^Date vaccine due^LN|2|20060903||||||F\r"
      + "OBX|17|TS|59778-1^Vaccine overdue date^LN|2|20061203||||||F\r"
      + "OBX|18|CE|59783-1^Status in immunization series^LN|2|1 of 3^In progress^SeriesStatus||||||F\r"
      + "OBX|19|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|3|137^Human Papilloma Virus^CVX||||||F\r"
      + "OBX|20|CE|59779-9^Immunization Schedule used^LN|3|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|21|CE|59780-7^Immunization Series name^LN|3|16092^HPV 2 Dose^SeriesNames||||||F\r"
      + "OBX|22|NM|30973-2^Dose number in series^LN|3|1||||||F\r"
      + "OBX|23|NM|59782-3^Number of doses in primary series^LN|3|2||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|3|20150903||||||F\r"
      + "OBX|25|TS|30980-7^Date vaccine due^LN|3|20170903||||||F\r"
      + "OBX|26|TS|59778-1^Vaccine overdue date^LN|3|20190903||||||F\r"
      + "OBX|27|CE|59783-1^Status in immunization series^LN|3|1 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|28|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|4|88^Influenza^CVX^90724^Influenza, unspecified formulation^C4||||||F\r"
      + "OBX|29|CE|59779-9^Immunization Schedule used^LN|4|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|30|CE|59780-7^Immunization Series name^LN|4|2482^Influenza^SeriesNames||||||F\r"
      + "OBX|31|NM|30973-2^Dose number in series^LN|4|1||||||F\r"
      + "OBX|32|NM|59782-3^Number of doses in primary series^LN|4|2||||||F\r"
      + "OBX|33|TS|30981-5^Earliest date to give^LN|4|20070303||||||F\r"
      + "OBX|34|TS|30980-7^Date vaccine due^LN|4|20180801||||||F\r"
      + "OBX|35|TS|59778-1^Vaccine overdue date^LN|4|20070903||||||F\r"
      + "OBX|36|CE|59783-1^Status in immunization series^LN|4|1 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|37|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|5|108^Meningo^CVX||||||F\r"
      + "OBX|38|CE|59779-9^Immunization Schedule used^LN|5|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|39|CE|59780-7^Immunization Series name^LN|5|3362^Meningococcal^SeriesNames||||||F\r"
      + "OBX|40|NM|30973-2^Dose number in series^LN|5|1||||||F\r"
      + "OBX|41|NM|59782-3^Number of doses in primary series^LN|5|2||||||F\r"
      + "OBX|42|TS|30981-5^Earliest date to give^LN|5|20170903||||||F\r"
      + "OBX|43|TS|30980-7^Date vaccine due^LN|5|20170903||||||F\r"
      + "OBX|44|TS|59778-1^Vaccine overdue date^LN|5|20190903||||||F\r"
      + "OBX|45|CE|59783-1^Status in immunization series^LN|5|1 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|46|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|6|03^MMR^CVX^90707^MMR^C4||||||F\r"
      + "OBX|47|CE|59779-9^Immunization Schedule used^LN|6|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|48|CE|59780-7^Immunization Series name^LN|6|1^MMR^SeriesNames||||||F\r"
      + "OBX|49|NM|30973-2^Dose number in series^LN|6|1||||||F\r"
      + "OBX|50|NM|59782-3^Number of doses in primary series^LN|6|2||||||F\r"
      + "OBX|51|TS|30981-5^Earliest date to give^LN|6|20070903||||||F\r"
      + "OBX|52|TS|30980-7^Date vaccine due^LN|6|20070903||||||F\r"
      + "OBX|53|TS|59778-1^Vaccine overdue date^LN|6|20080103||||||F\r"
      + "OBX|54|CE|59783-1^Status in immunization series^LN|6|1 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|55|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|7|115^Pertussis^CVX^90715^Tdap^C4||||||F\r"
      + "OBX|56|CE|59779-9^Immunization Schedule used^LN|7|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|57|CE|59780-7^Immunization Series name^LN|7|2079^Pertussis / TdaP^SeriesNames||||||F\r"
      + "OBX|58|NM|30973-2^Dose number in series^LN|7|1||||||F\r"
      + "OBX|59|NM|59782-3^Number of doses in primary series^LN|7|1||||||F\r"
      + "OBX|60|TS|30981-5^Earliest date to give^LN|7|20190512||||||F\r"
      + "OBX|61|TS|30980-7^Date vaccine due^LN|7|20190512||||||F\r"
      + "OBX|62|TS|59778-1^Vaccine overdue date^LN|7|20190903||||||F\r"
      + "OBX|63|CE|59783-1^Status in immunization series^LN|7|1 of 1^In progress^SeriesStatus||||||F\r"
      + "OBX|64|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|8|89^Polio^CVX||||||F\r"
      + "OBX|65|CE|59779-9^Immunization Schedule used^LN|8|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|66|CE|59780-7^Immunization Series name^LN|8|64^Polio^SeriesNames||||||F\r"
      + "OBX|67|NM|30973-2^Dose number in series^LN|8|1||||||F\r"
      + "OBX|68|NM|59782-3^Number of doses in primary series^LN|8|5||||||F\r"
      + "OBX|69|TS|30981-5^Earliest date to give^LN|8|20061015||||||F\r"
      + "OBX|70|TS|30980-7^Date vaccine due^LN|8|20061103||||||F\r"
      + "OBX|71|TS|59778-1^Vaccine overdue date^LN|8|20061203||||||F\r"
      + "OBX|72|CE|59783-1^Status in immunization series^LN|8|1 of 5^In progress^SeriesStatus||||||F\r"
      + "OBX|73|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|9|139^Td^CVX||||||F\r"
      + "OBX|74|CE|59779-9^Immunization Schedule used^LN|9|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|75|CE|59780-7^Immunization Series name^LN|9|8^Td^SeriesNames||||||F\r"
      + "OBX|76|NM|30973-2^Dose number in series^LN|9|4||||||F\r"
      + "OBX|77|NM|59782-3^Number of doses in primary series^LN|9|3||||||F\r"
      + "OBX|78|TS|30981-5^Earliest date to give^LN|9|20240511||||||F\r"
      + "OBX|79|TS|30980-7^Date vaccine due^LN|9|20290511||||||F\r"
      + "OBX|80|TS|59778-1^Vaccine overdue date^LN|9|20290711||||||F\r"
      + "OBX|81|CE|59783-1^Status in immunization series^LN|9|4 of 3^In progress^SeriesStatus||||||F\r"
      + "OBX|82|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|10|21^Varicella^CVX^90716^Varicella^C4||||||F\r"
      + "OBX|83|CE|59779-9^Immunization Schedule used^LN|10|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|84|CE|59780-7^Immunization Series name^LN|10|69^Varicella^SeriesNames||||||F\r"
      + "OBX|85|NM|30973-2^Dose number in series^LN|10|1||||||F\r"
      + "OBX|86|NM|59782-3^Number of doses in primary series^LN|10|2||||||F\r"
      + "OBX|87|TS|30981-5^Earliest date to give^LN|10|20070903||||||F\r"
      + "OBX|88|TS|30980-7^Date vaccine due^LN|10|20070903||||||F\r"
      + "OBX|89|TS|59778-1^Vaccine overdue date^LN|10|20100903||||||F\r"
      + "OBX|90|CE|59783-1^Status in immunization series^LN|10|1 of 2^In progress^SeriesStatus||||||F\r";


  @Test
  public void testRSP_WI_WIR_4() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_WI_WIR_4);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 6, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "20",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "20",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(2).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "20",
        testCase.getTestEventList().get(2).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(2).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(3).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "20",
        testCase.getTestEventList().get(3).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(3).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(4).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "20",
        testCase.getTestEventList().get(4).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(4).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(5).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "139",
        testCase.getTestEventList().get(5).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(5).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Not all forecasts read", 11, forecastActualList.size());
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/03/2015",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "09/03/2017",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/03/2019",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/03/2007",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "09/03/2007",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/03/2008",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "09/03/2006",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "09/03/2006",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/03/2006",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "03/03/2007",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/01/2018",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/03/2007",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "09/03/2007",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "09/03/2007",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "01/03/2008",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "09/03/2017",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "09/03/2017",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/03/2019",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "10/15/2006",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "11/03/2006",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/03/2006",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Td Only",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "05/11/2024",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "05/11/2029",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/11/2029",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "Td or Tdap",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "05/11/2024",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "05/11/2029",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/11/2029",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Tdap Only",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "05/12/2019",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "05/12/2019",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/03/2019",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/03/2007",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "09/03/2007",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/03/2010",
        sdf.format(forecastActualList.get(10).getOverdueDate()));
  }

  private Software createSoftware() {
    Software software = new Software();
    software.setServiceUrl("http://localhost/iis-kernel/soap");
    software.setService(Service.IIS);
    software.setServiceUserid("Mercy");
    software.setServicePassword("password1234");
    software.setServiceFacilityid("Mercy Healthcare");
    return software;
  }

  private Software createSoftwareNotAuthenticated() {
    Software software = new Software();
    software.setServiceUrl("http://localhost/iis-kernel/soap");
    software.setService(Service.IIS);
    software.setServiceUserid("Mercy");
    software.setServicePassword("passwordWrong");
    software.setServiceFacilityid("Mercy Healthcare");
    return software;
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

  private TestCase run(List<ForecastActual> forecastActualList, String rsp)
      throws IOException, ParseException {
    Software software = createSoftware();
    IISConnector c = new IISConnector(software, VaccineGroup.getForecastItemList());
    TestCase testCase = IISConnector.recreateTestCase(rsp);
    SoftwareResult softwareResult = new SoftwareResult();
    c.readRSP(forecastActualList, testCase, softwareResult, rsp);
    return testCase;
  }

  public void testConnectIIS() throws Exception {
    if (!ENABLE_LIVE_TESTS) {
      return;
    }

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

    ConnectorInterface connector =
        ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
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
    System.out.println(softwareResult.getLogText());
    assertTrue("HepB forecast not found", foundHepB);

  }

  public void testConnectIISNotAuthenticated() throws Exception {

    if (ENABLE_LIVE_TESTS) {
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

      ConnectorInterface connector =
          ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
      connector.setLogText(true);
      SoftwareResult softwareResult = new SoftwareResult();
      List<ForecastActual> forecastActualList =
          connector.queryForForecast(testCase, softwareResult);
      System.out.println(softwareResult.getLogText());
      assertEquals(SoftwareResultStatus.NOT_AUTHENTICATED,
          softwareResult.getSoftwareResultStatus());
    }
  }

  public void testConnectSTC() throws Exception {
    if (!ENABLE_LIVE_TESTS) {
      return;
    }

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

    ConnectorInterface connector =
        ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
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

  public void testReadRSP() throws Exception {
    Software software = createSoftware();
    SoftwareResult softwareResult = new SoftwareResult();
    TestCase testCase = new TestCase();
    IISConnector c = new IISConnector(software, VaccineGroup.getForecastItemList());
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();

    c.readRSP(forecastActualList, testCase, softwareResult, RSP);

    assertTrue(forecastActualList.size() > 5);

  }

  @Test
  public void testReadRSPEnvision() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_ENVISION);

    assertEquals("", 2, testCase.getTestEventList().size());
    assertTrue(forecastActualList.size() > 5);
    PrintStream out = System.out;
    ForecastResultPrinter.printOutResultInFixedWidth(forecastActualList, testCase, out);

  }

  public void testReadRSPNotFound() throws Exception {
    Software software = createSoftware();
    SoftwareResult softwareResult = new SoftwareResult();
    TestCase testCase = new TestCase();
    IISConnector c = new IISConnector(software, VaccineGroup.getForecastItemList());
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();

    c.readRSP(forecastActualList, testCase, softwareResult, RSP_NOT_FOUND);

    assertTrue(forecastActualList.size() == 0);
    assertEquals(SoftwareResultStatus.NOT_FOUND, softwareResult.getSoftwareResultStatus());
  }

  @Test
  public void testReadRSPSTC() throws Exception {
    List forecastActualList = new ArrayList();
    TestCase testCase = run(forecastActualList, RSP_STC);

    assertEquals("Not all test events read", 1, testCase.getTestEventList().size());
  }

  @Test
  public void testRSP_AK_VacTrAK_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_AK_VACTRAK_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 11, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2023",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/12/2027",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/12/2016",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "02/12/2015",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "02/12/2015",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "03/14/2015",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2024",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/12/2027",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(10).getOverdueDate()));
  }

  @Test
  public void testRSP_AL_ImmPrint_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_AL_IMMPRINT_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 13, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(0).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(1).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(2).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(3).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(4).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "11/12/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(5).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(5).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(5).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(6).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(7).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "PPSV",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(8).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(9).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(10).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "G", forecastActualList.get(11).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(11).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(11).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(12).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(12).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(12).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(12).getOverdueDate());
  }

  @Test
  public void testRSP_AR_WebIZ_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_AR_WEBIZ_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 22, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2023",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2027",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2016",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(11).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(11).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(11).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2027",
        sdf.format(forecastActualList.get(12).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(13).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(13).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(13).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(13).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(13).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(14).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(14).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(14).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(14).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(14).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(15).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(15).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(15).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(15).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(15).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(15).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(15).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(15).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(16).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(16).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(16).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(16).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(16).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(17).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(17).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(17).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(17).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(17).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(17).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(17).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(17).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(18).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(18).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(18).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(18).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(18).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getOverdueDate()));
    assertEquals("Forecast not found", "RZV (Shingrix)",
        forecastActualList.get(19).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(19).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(19).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(19).getDueDate());
    assertEquals("Wrong due date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(19).getOverdueDate());
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(20).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "G", forecastActualList.get(20).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(20).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(20).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(20).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(21).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(21).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(21).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(21).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(21).getOverdueDate());
  }

  @Test
  public void testRSP_AZ_ASIIS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_AZ_ASIIS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 11, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2023",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/15/2025",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/15/2027",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2015",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/15/2015",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/15/2016",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/15/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/15/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "02/15/2015",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "02/15/2015",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "03/17/2015",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2024",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/15/2025",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/15/2027",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(10).getOverdueDate()));
  }

  @Test
  public void testRSP_CA_CAIR2_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_CA_CAIR2_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 9, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "11/12/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "11/12/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "02/12/2018",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "09/01/2018",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "08/11/2016",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/11/2016",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/11/2016",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/11/2016",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
  }

  @Test
  public void testRSP_CT_WIZ_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_CT_WIZ_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 22, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/24/2018",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "09/24/2018",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/24/2018",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/24/2018",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "09/24/2018",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/24/2018",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2023",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/15/2025",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/11/2027",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/15/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/11/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/24/2018",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "09/24/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/24/2018",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2015",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/15/2015",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/11/2016",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "02/27/2019",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "02/27/2019",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "02/27/2019",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2018",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "09/23/2018",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(11).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(11).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(11).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2025",
        sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "08/15/2025",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/11/2027",
        sdf.format(forecastActualList.get(12).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(13).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(13).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(13).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(13).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(13).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(14).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(14).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(14).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2079",
        sdf.format(forecastActualList.get(14).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(14).getDueDate());
    assertEquals("Wrong due date found", "08/15/2079",
        sdf.format(forecastActualList.get(14).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(14).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(15).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(15).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(15).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(15).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(15).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(15).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(15).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(15).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(16).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(16).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(16).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2079",
        sdf.format(forecastActualList.get(16).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(16).getDueDate());
    assertEquals("Wrong due date found", "08/15/2079",
        sdf.format(forecastActualList.get(16).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(16).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(17).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(17).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(17).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(17).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(17).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(17).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(17).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(17).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(18).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(18).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(18).getValidDate());
    assertEquals("Wrong earliest date found", "09/24/2018",
        sdf.format(forecastActualList.get(18).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(18).getDueDate());
    assertEquals("Wrong due date found", "09/24/2018",
        sdf.format(forecastActualList.get(18).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(18).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/24/2018",
        sdf.format(forecastActualList.get(18).getOverdueDate()));
    assertEquals("Forecast not found", "RZV (Shingrix)",
        forecastActualList.get(19).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(19).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(19).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2064",
        sdf.format(forecastActualList.get(19).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(19).getDueDate());
    assertEquals("Wrong due date found", "08/15/2064",
        sdf.format(forecastActualList.get(19).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(19).getOverdueDate());
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(20).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "G", forecastActualList.get(20).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(20).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(20).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(20).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(21).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(21).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(21).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(21).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(21).getOverdueDate());
  }

  @Test
  public void testRSP_DE_DelVAX_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_DE_DELVAX_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 22, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2023",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2027",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2016",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(11).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(11).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(11).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2027",
        sdf.format(forecastActualList.get(12).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(13).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(13).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(13).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(13).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(13).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(14).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(14).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(14).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(14).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(14).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(15).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(15).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(15).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(15).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(15).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(15).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(15).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(15).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(16).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(16).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(16).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(16).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(16).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(17).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(17).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(17).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(17).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(17).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(17).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(17).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(17).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(18).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(18).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(18).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(18).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(18).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getOverdueDate()));
    assertEquals("Forecast not found", "RZV (Shingrix)",
        forecastActualList.get(19).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(19).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(19).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(19).getDueDate());
    assertEquals("Wrong due date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(19).getOverdueDate());
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(20).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "G", forecastActualList.get(20).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(20).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(20).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(20).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(21).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(21).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(21).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(21).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(21).getOverdueDate());
  }

  @Test
  public void testRSP_Envision_Dev_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_ENVISION_DEV_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 16, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(0).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "02/24/2017",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(1).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "03/24/2017",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(2).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "02/24/2017",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(3).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "03/24/2017",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(4).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "02/12/2024",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(5).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "02/24/2017",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(6).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "03/24/2017",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(7).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "02/24/2017",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(8).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "08/24/2017",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(9).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(9).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "02/24/2017",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(10).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(10).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "03/23/2017",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(11).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(11).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(11).getDueDate());
    assertEquals("Wrong due date found", "02/12/2024",
        sdf.format(forecastActualList.get(11).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(12).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(12).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "02/24/2017",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(13).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(13).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "02/24/2017",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(13).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(14).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(14).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(14).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(14).getDueDate());
    assertEquals("Wrong due date found", "02/24/2017",
        sdf.format(forecastActualList.get(14).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(14).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(15).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(15).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(15).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(15).getDueDate());
    assertEquals("Wrong due date found", "03/24/2017",
        sdf.format(forecastActualList.get(15).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(15).getOverdueDate());
  }

  @Test
  public void testRSP_FL_SHOTS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_FL_SHOTS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 4,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "VZV",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "MEASLES",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong CVX found", "MUMPS",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(2).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(2).getDoseValid());
    assertEquals("Wrong CVX found", "RUBELLA",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(3).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(3).getDoseValid());
    assertEquals("Wrong number of evaluations", 4,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "VZV",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "MEASLES",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong CVX found", "MUMPS",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(2).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(2).getDoseValid());
    assertEquals("Wrong CVX found", "RUBELLA",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(3).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(3).getDoseValid());
    assertEquals("Not all forecasts read", 13, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2023",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "11/12/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "11/12/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(5).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(5).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(5).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "Measles Only",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(6).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(6).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(6).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Mumps Only",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(7).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(7).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(7).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2016",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "08/12/2016",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(10).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(10).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(10).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "Rubella Only",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(11).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(11).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(11).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(12).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(12).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(12).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(12).getOverdueDate());
  }

  @Test
  public void testRSP_GA_GRITS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_GA_GRITS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 8, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "02/12/2015",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "02/12/2015",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
  }

  @Test
  public void testRSP_HI_HIR_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_HI_HIR_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 6, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "06/08/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "06/27/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "06/08/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "06/27/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "04/27/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "04/27/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "04/27/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "04/27/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "07/27/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "07/27/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "10/27/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "10/27/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
  }

  @Test
  public void testRSP_IA_IRIS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_IA_IRIS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 8, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/16/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/16/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/16/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/16/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "11/16/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "11/16/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "02/16/2015",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "07/01/2018",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "08/16/2016",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/16/2016",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
  }

  @Test
  public void testRSP_ID_IRIS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_ID_IRIS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 13, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2023",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "02/12/2015",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/01/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2025",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Forecast not found", "Td Only",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2021",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "08/12/2021",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "Td or Tdap",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(11).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(11).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2021",
        sdf.format(forecastActualList.get(11).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(11).getDueDate());
    assertEquals("Wrong due date found", "08/12/2021",
        sdf.format(forecastActualList.get(11).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "Tdap Only",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2021",
        sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(12).getOverdueDate());
  }

  @Test
  public void testRSP_IL_I_CARE_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_IL_I_CARE_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 9, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "10/13/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "10/13/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/13/2015",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/13/2014",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "10/12/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "10/13/2014",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "09/01/2018",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "09/01/2018",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/02/2018",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2025",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/13/2025",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "10/12/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "10/13/2014",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "10/12/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "10/13/2014",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
  }

  @Test
  public void testRSP_KS_WebIZ_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_KS_WEBIZ_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 22, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2023",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2027",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2016",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(11).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(11).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(11).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2027",
        sdf.format(forecastActualList.get(12).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(13).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(13).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(13).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(13).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(13).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(14).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(14).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(14).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(14).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(14).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(15).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(15).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(15).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(15).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(15).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(15).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(15).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(15).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(16).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(16).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(16).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(16).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(16).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(17).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(17).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(17).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(17).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(17).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(17).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(17).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(17).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(18).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(18).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(18).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(18).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(18).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getOverdueDate()));
    assertEquals("Forecast not found", "RZV (Shingrix)",
        forecastActualList.get(19).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(19).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(19).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(19).getDueDate());
    assertEquals("Wrong due date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(19).getOverdueDate());
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(20).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "G", forecastActualList.get(20).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(20).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(20).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(20).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(21).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(21).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(21).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(21).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(21).getOverdueDate());
  }

  @Test
  public void testRSP_KY_KYIR_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_KY_KYIR_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 23, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/28/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/17/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/14/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2018",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "09/26/2018",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/26/2018",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "09/28/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "10/17/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/14/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2018",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "09/26/2018",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/26/2018",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(4).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(4).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/17/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/17/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/13/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2018",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "09/26/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/26/2018",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/17/2015",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/17/2015",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/13/2016",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "03/01/2019",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "03/01/2019",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "03/01/2019",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/28/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/17/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/14/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "07/01/2018",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "07/01/2018",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(11).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(11).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2018",
        sdf.format(forecastActualList.get(11).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(11).getDueDate());
    assertEquals("Wrong due date found", "09/26/2018",
        sdf.format(forecastActualList.get(11).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(12).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(12).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(12).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(13).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(13).getValidDate());
    assertEquals("Wrong earliest date found", "08/17/2025",
        sdf.format(forecastActualList.get(13).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "08/17/2025",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(13).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/13/2027",
        sdf.format(forecastActualList.get(13).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(14).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(14).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(14).getValidDate());
    assertEquals("Wrong earliest date found", "09/28/2014",
        sdf.format(forecastActualList.get(14).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(14).getDueDate());
    assertEquals("Wrong due date found", "10/17/2014",
        sdf.format(forecastActualList.get(14).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(14).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/14/2014",
        sdf.format(forecastActualList.get(14).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(15).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(15).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(15).getValidDate());
    assertEquals("Wrong earliest date found", "08/17/2079",
        sdf.format(forecastActualList.get(15).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(15).getDueDate());
    assertEquals("Wrong due date found", "08/17/2079",
        sdf.format(forecastActualList.get(15).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(15).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(16).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(16).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(16).getValidDate());
    assertEquals("Wrong earliest date found", "09/28/2014",
        sdf.format(forecastActualList.get(16).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(16).getDueDate());
    assertEquals("Wrong due date found", "10/17/2014",
        sdf.format(forecastActualList.get(16).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(16).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/14/2014",
        sdf.format(forecastActualList.get(16).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(17).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(17).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(17).getValidDate());
    assertEquals("Wrong earliest date found", "08/17/2079",
        sdf.format(forecastActualList.get(17).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(17).getDueDate());
    assertEquals("Wrong due date found", "08/17/2079",
        sdf.format(forecastActualList.get(17).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(17).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(18).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(18).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(18).getValidDate());
    assertEquals("Wrong earliest date found", "09/28/2014",
        sdf.format(forecastActualList.get(18).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(18).getDueDate());
    assertEquals("Wrong due date found", "10/17/2014",
        sdf.format(forecastActualList.get(18).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(18).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/14/2014",
        sdf.format(forecastActualList.get(18).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(19).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(19).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(19).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2018",
        sdf.format(forecastActualList.get(19).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(19).getDueDate());
    assertEquals("Wrong due date found", "09/26/2018",
        sdf.format(forecastActualList.get(19).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(19).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/26/2018",
        sdf.format(forecastActualList.get(19).getOverdueDate()));
    assertEquals("Forecast not found", "RZV (Shingrix)",
        forecastActualList.get(20).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(20).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(20).getValidDate());
    assertEquals("Wrong earliest date found", "08/17/2064",
        sdf.format(forecastActualList.get(20).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(20).getDueDate());
    assertEquals("Wrong due date found", "08/17/2064",
        sdf.format(forecastActualList.get(20).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(20).getOverdueDate());
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(21).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "G", forecastActualList.get(21).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(21).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(21).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(21).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(22).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(22).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(22).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(22).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(22).getOverdueDate());
  }

  @Test
  public void testRSP_LA_LINKS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_LA_LINKS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 7, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "02/12/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "02/12/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "03/14/2015",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2025",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/12/2025",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
  }

  @Test
  public void testRSP_MA_MIIS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_MA_MIIS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 1, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "133",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Not all forecasts read", 16, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "06/11/2017",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "06/30/2017",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/30/2017",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "06/11/2017",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "06/30/2017",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/30/2017",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "04/30/2026",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "04/30/2028",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/30/2030",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "04/30/2018",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "04/30/2018",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/30/2019",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "04/30/2017",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "04/30/2017",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "05/14/2017",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "06/11/2017",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "06/30/2017",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/30/2017",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "08/01/2018",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/01/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/01/2018",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "04/30/2018",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "04/30/2018",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/30/2018",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "04/30/2028",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "04/30/2028",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/30/2029",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "12/25/2018",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "12/25/2018",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "01/25/2019",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "PPSV",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "04/30/2082",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "04/30/2082",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Wrong overdue date found", "05/30/2082",
        sdf.format(forecastActualList.get(10).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(11).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(11).getValidDate());
    assertEquals("Wrong earliest date found", "12/25/2018",
        sdf.format(forecastActualList.get(11).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(11).getDueDate());
    assertEquals("Wrong due date found", "12/25/2018",
        sdf.format(forecastActualList.get(11).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Wrong overdue date found", "01/25/2019",
        sdf.format(forecastActualList.get(11).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "06/11/2017",
        sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "06/30/2017",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/30/2017",
        sdf.format(forecastActualList.get(12).getOverdueDate()));
    assertEquals("Forecast not found", "RZV (Shingrix)",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(13).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(13).getValidDate());
    assertEquals("Wrong earliest date found", "04/30/2067",
        sdf.format(forecastActualList.get(13).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "04/30/2067",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(13).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/30/2068",
        sdf.format(forecastActualList.get(13).getOverdueDate()));
    assertEquals("Forecast not found", "Tdap Only",
        forecastActualList.get(14).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(14).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(14).getValidDate());
    assertEquals("Wrong earliest date found", "04/30/2024",
        sdf.format(forecastActualList.get(14).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(14).getDueDate());
    assertEquals("Wrong due date found", "04/30/2024",
        sdf.format(forecastActualList.get(14).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(14).getOverdueDate());
    assertEquals("Wrong overdue date found", "05/30/2024",
        sdf.format(forecastActualList.get(14).getOverdueDate()));
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(15).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(15).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(15).getValidDate());
    assertEquals("Wrong earliest date found", "04/30/2018",
        sdf.format(forecastActualList.get(15).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(15).getDueDate());
    assertEquals("Wrong due date found", "04/30/2018",
        sdf.format(forecastActualList.get(15).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(15).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/30/2018",
        sdf.format(forecastActualList.get(15).getOverdueDate()));
  }

  @Test
  public void testRSP_MD_IMMUNET_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_MD_IMMUNET_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 7, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "02/12/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "02/12/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/12/2015",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/12/2014",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
  }

  @Test
  public void testRSP_ME_ImmPact2_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_ME_IMMPACT2_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 9, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "11/12/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "11/12/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "02/12/2015",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/01/2018",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "08/11/2016",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/11/2016",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/11/2016",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/11/2016",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
  }

  @Test
  public void testRSP_MI_MCIR_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_MI_MCIR_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 3,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(2).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(2).getDoseValid());
    assertEquals("Wrong number of evaluations", 3,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(2).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(2).getDoseValid());
    assertEquals("Not all forecasts read", 10, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "06/30/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "07/19/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "06/30/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "07/19/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "05/19/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "05/19/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "06/30/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "07/19/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "07/01/2017",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/01/2017",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(5).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(5).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(5).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "06/30/2014",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "07/19/2014",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "06/30/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "07/19/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "06/30/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "07/19/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(9).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(9).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(9).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(9).getOverdueDate());
  }

  @Test
  public void testRSP_MN_MIIC_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_MN_MIIC_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 8, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/15/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/15/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "11/15/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "11/15/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "02/15/2015",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "07/01/2018",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2016",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/15/2016",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
  }

  @Test
  public void testRSP_MS_MIIX_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_MS_MIIX_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 10, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2023",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/15/2025",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/15/2027",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2015",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/15/2015",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/15/2016",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/15/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/15/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2024",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/15/2025",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/15/2027",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
  }

  @Test
  public void testRSP_MT_imMTrax_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_MT_IMMTRAX_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 0, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 0, forecastActualList.size());
  }

  @Test
  public void testRSP_NC_NCIR_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_NC_NCIR_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 8, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/16/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/16/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/16/2016",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/16/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/16/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "11/16/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "11/16/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/16/2015",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "02/16/2015",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "02/16/2015",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/16/2015",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
  }

  @Test
  public void testRSP_ND_SIIS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_ND_SIIS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Not all forecasts read", 14, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2023",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/01/2018",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/01/2018",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
//    assertEquals("Forecast not found", "Meningococcal",
//        forecastActualList.get(6).getVaccineGroup().getLabel());
//    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
//    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
//    assertEquals("Wrong earliest date found", "08/12/2025",
//        sdf.format(forecastActualList.get(6).getValidDate()));
//    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
//    assertEquals("Wrong due date found", "08/12/2025",
//        sdf.format(forecastActualList.get(6).getDueDate()));
//    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
//    assertEquals("Forecast not found", "Meningococcal",
//        forecastActualList.get(7).getVaccineGroup().getLabel());
//    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
//    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
//    assertEquals("Wrong earliest date found", "08/12/2030",
//        sdf.format(forecastActualList.get(7).getValidDate()));
//    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
//    assertEquals("Wrong due date found", "08/12/2030",
//        sdf.format(forecastActualList.get(7).getDueDate()));
//    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Forecast not found", "PPSV",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(11).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(11).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(11).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(11).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(11).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "RZV (Shingrix)",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2064",
        sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "08/12/2064",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Forecast not found", "Tdap Only",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(13).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(13).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2021",
        sdf.format(forecastActualList.get(13).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "08/12/2021",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(13).getOverdueDate());
  }

  @Test
  public void testRSP_NE_NESIIS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_NE_NESIIS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 9, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/29/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/18/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/18/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/29/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/18/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/18/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/18/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/18/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/18/2016",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/18/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/18/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/15/2014",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "09/29/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "10/18/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/18/2014",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "02/18/2015",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/01/2018",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/18/2015",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "08/18/2025",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/18/2025",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/18/2027",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/29/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "10/18/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/18/2014",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/29/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/18/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/18/2014",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
  }

  @Test
  public void testRSP_NH_VaxNH_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_NH_VAXNH_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 9, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "06/07/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "06/26/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/26/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "06/07/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "06/26/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/26/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "04/26/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "04/26/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "05/26/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "06/07/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "06/26/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/26/2014",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "10/26/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "10/26/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/04/2018",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "04/26/2024",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "04/26/2025",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/26/2027",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "06/07/2014",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "06/26/2014",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/26/2014",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "06/07/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "06/26/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/26/2014",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "06/07/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "06/26/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "07/26/2014",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
  }

  @Test
  public void testRSP_NM_NMSIIS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_NM_NMSIIS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 22, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2023",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2027",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2016",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(11).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(11).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(11).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2027",
        sdf.format(forecastActualList.get(12).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(13).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(13).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(13).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(13).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(13).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(14).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(14).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(14).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(14).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(14).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(15).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(15).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(15).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(15).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(15).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(15).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(15).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(15).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(16).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(16).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(16).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(16).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(16).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(17).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(17).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(17).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(17).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(17).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(17).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(17).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(17).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(18).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(18).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(18).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(18).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(18).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getOverdueDate()));
    assertEquals("Forecast not found", "RZV (Shingrix)",
        forecastActualList.get(19).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(19).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(19).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(19).getDueDate());
    assertEquals("Wrong due date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(19).getOverdueDate());
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(20).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "G", forecastActualList.get(20).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(20).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(20).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(20).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(21).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(21).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(21).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(21).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(21).getOverdueDate());
  }

  @Test
  public void testRSP_NV_WebIZ_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_NV_WEBIZ_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 21, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/24/2018",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "09/24/2018",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/24/2018",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/24/2018",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "09/24/2018",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/24/2018",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2023",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/15/2025",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/11/2027",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/15/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/11/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/24/2018",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "09/24/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/24/2018",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2018",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "09/23/2018",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/23/2018",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2018",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "09/23/2018",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(10).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(10).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(10).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(11).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(11).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2025",
        sdf.format(forecastActualList.get(11).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(11).getDueDate());
    assertEquals("Wrong due date found", "08/15/2025",
        sdf.format(forecastActualList.get(11).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/11/2027",
        sdf.format(forecastActualList.get(11).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(12).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(13).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(13).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2079",
        sdf.format(forecastActualList.get(13).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "08/15/2079",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(13).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(14).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(14).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(14).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(14).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(14).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(14).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(14).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(14).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(15).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(15).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(15).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2079",
        sdf.format(forecastActualList.get(15).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(15).getDueDate());
    assertEquals("Wrong due date found", "08/15/2079",
        sdf.format(forecastActualList.get(15).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(15).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(16).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(16).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(16).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(16).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(16).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(16).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(16).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/12/2014",
        sdf.format(forecastActualList.get(16).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(17).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(17).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(17).getValidDate());
    assertEquals("Wrong earliest date found", "09/24/2018",
        sdf.format(forecastActualList.get(17).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(17).getDueDate());
    assertEquals("Wrong due date found", "09/24/2018",
        sdf.format(forecastActualList.get(17).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(17).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/24/2018",
        sdf.format(forecastActualList.get(17).getOverdueDate()));
    assertEquals("Forecast not found", "RZV (Shingrix)",
        forecastActualList.get(18).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(18).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(18).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2064",
        sdf.format(forecastActualList.get(18).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(18).getDueDate());
    assertEquals("Wrong due date found", "08/15/2064",
        sdf.format(forecastActualList.get(18).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(18).getOverdueDate());
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(19).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "G", forecastActualList.get(19).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(19).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(19).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(19).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(20).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(20).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(20).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(20).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(20).getOverdueDate());
  }

  @Test
  public void testRSP_NY_CIR_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_NY_CIR_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 11, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(0).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(1).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(2).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(3).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(4).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(5).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(6).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/01/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(7).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(8).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(9).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(9).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(10).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(10).getValidDate());
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
  }

  @Test
  public void testRSP_OR_ALERT_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_OR_ALERT_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 9, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "11/12/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "11/12/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "02/12/2015",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/01/2018",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
  }

  @Test
  public void testRSP_PA_PHIL_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_PA_PHIL_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 22, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2023",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2027",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2016",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(11).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(11).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(11).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2027",
        sdf.format(forecastActualList.get(12).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(13).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(13).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(13).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(13).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(13).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(14).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(14).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(14).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(14).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(14).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(15).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(15).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(15).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(15).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(15).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(15).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(15).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(15).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(16).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(16).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(16).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(16).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(16).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(17).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(17).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(17).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(17).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(17).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(17).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(17).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(17).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(18).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(18).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(18).getValidDate());
    assertEquals("Wrong earliest date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(18).getDueDate());
    assertEquals("Wrong due date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(18).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/21/2018",
        sdf.format(forecastActualList.get(18).getOverdueDate()));
    assertEquals("Forecast not found", "RZV (Shingrix)",
        forecastActualList.get(19).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(19).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(19).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(19).getDueDate());
    assertEquals("Wrong due date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(19).getOverdueDate());
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(20).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "G", forecastActualList.get(20).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(20).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(20).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(20).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(21).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(21).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(21).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(21).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(21).getOverdueDate());
  }

  @Test
  public void testRSP_PI_Guam_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_PI_GUAM_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 22, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/22/2018",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "09/22/2018",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/22/2018",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "09/22/2018",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "09/22/2018",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/22/2018",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(4).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(4).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Hep B 3 Dose Only",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/22/2018",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "09/22/2018",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/22/2018",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2016",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "02/25/2019",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "02/25/2019",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "02/25/2019",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "09/20/2018",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(11).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(11).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(11).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(11).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(11).getOverdueDate());
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(12).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(12).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(12).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(12).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(12).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(12).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/08/2027",
        sdf.format(forecastActualList.get(12).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(13).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(13).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(13).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(13).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(13).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(13).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(13).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(13).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(14).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(14).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(14).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(14).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(14).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(14).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(15).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(15).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(15).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(15).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(15).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(15).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(15).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(15).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(16).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(16).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(16).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(16).getDueDate());
    assertEquals("Wrong due date found", "08/12/2079",
        sdf.format(forecastActualList.get(16).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(16).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(17).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(17).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(17).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(17).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(17).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(17).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(17).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/09/2014",
        sdf.format(forecastActualList.get(17).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(18).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(18).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(18).getValidDate());
    assertEquals("Wrong earliest date found", "09/22/2018",
        sdf.format(forecastActualList.get(18).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(18).getDueDate());
    assertEquals("Wrong due date found", "09/22/2018",
        sdf.format(forecastActualList.get(18).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(18).getOverdueDate());
    assertEquals("Wrong overdue date found", "09/22/2018",
        sdf.format(forecastActualList.get(18).getOverdueDate()));
    assertEquals("Forecast not found", "RZV (Shingrix)",
        forecastActualList.get(19).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(19).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(19).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(19).getDueDate());
    assertEquals("Wrong due date found", "08/12/2064",
        sdf.format(forecastActualList.get(19).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(19).getOverdueDate());
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(20).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "G", forecastActualList.get(20).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(20).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(20).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(20).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(21).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "C", forecastActualList.get(21).getAdminStatus());
    assertNull("Valid date should be null", forecastActualList.get(21).getValidDate());
    assertNull("Due date should be null", forecastActualList.get(21).getDueDate());
    assertNull("Overdue date should be null", forecastActualList.get(21).getOverdueDate());
  }

  @Test
  public void testRSP_RI_KIDSNET_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_RI_KIDSNET_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 11, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2023",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/12/2025",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/12/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "07/01/2018",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "07/01/2018",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
//    assertEquals("Forecast not found", "Meningococcal",
//        forecastActualList.get(6).getVaccineGroup().getLabel());
//    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
//    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
//    assertEquals("Wrong earliest date found", "08/12/2024",
//        sdf.format(forecastActualList.get(6).getValidDate()));
//    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
//    assertEquals("Wrong due date found", "08/12/2030",
//        sdf.format(forecastActualList.get(6).getDueDate()));
//    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
//    assertEquals("Forecast not found", "Meningococcal",
//        forecastActualList.get(7).getVaccineGroup().getLabel());
//    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
//    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
//    assertEquals("Wrong earliest date found", "08/12/2014",
//        sdf.format(forecastActualList.get(7).getValidDate()));
//    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
//    assertEquals("Wrong due date found", "08/12/2030",
//        sdf.format(forecastActualList.get(7).getDueDate()));
//    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "08/12/2015",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "08/12/2015",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/23/2014",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "10/12/2014",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
  }

  @Test
  public void testRSP_TN_TIR_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_TN_TIR_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 11, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/16/2023",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/16/2025",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/16/2027",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/16/2015",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/16/2015",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/16/2016",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/16/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/16/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "02/16/2015",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "02/16/2015",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "10/24/2018",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/16/2024",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/16/2025",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/16/2027",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/24/2018",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "09/24/2018",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Wrong overdue date found", "10/24/2018",
        sdf.format(forecastActualList.get(10).getOverdueDate()));
  }

  @Test
  public void testRSP_TX_ImmTrac_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_TX_IMMTRAC_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 0, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 11, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/22/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/11/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/22/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/11/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/11/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/11/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/11/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/11/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "11/11/2015",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "11/11/2015",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "02/11/2015",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "07/01/2018",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "08/11/2015",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "08/11/2015",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/22/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "10/11/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/22/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/11/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/22/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/11/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "08/11/2015",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "08/11/2015",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(10).getOverdueDate());
  }

  @Test
  public void testRSP_VA_VIIS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_VA_VIIS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "94",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 8, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/02/2013",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "09/22/2013",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/02/2013",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "09/22/2013",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "07/22/2014",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "07/22/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "07/22/2013",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "07/22/2013",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "09/02/2013",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "09/22/2013",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "01/22/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "07/22/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/02/2013",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "09/22/2013",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/02/2013",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "09/22/2013",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNull("Overdue date should be null", forecastActualList.get(7).getOverdueDate());
  }

  @Test
  public void testRSP_WA_WAIIS_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_WA_WAIIS_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Not all forecasts read", 11, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HPV",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2023",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/15/2025",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/15/2027",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2015",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/15/2015",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/15/2016",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "08/15/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "12/15/2014",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "02/15/2015",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "02/15/2015",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "03/17/2015",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Meningococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "08/15/2024",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "08/15/2025",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/15/2027",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(10).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "N", forecastActualList.get(10).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(10).getValidDate());
    assertEquals("Wrong earliest date found", "09/26/2014",
        sdf.format(forecastActualList.get(10).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(10).getDueDate());
    assertEquals("Wrong due date found", "10/15/2014",
        sdf.format(forecastActualList.get(10).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(10).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/15/2014",
        sdf.format(forecastActualList.get(10).getOverdueDate()));
  }

  @Test
  public void testRSP_WI_WIR_1() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_WI_WIR_1);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 2, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Wrong number of evaluations", 2,
        testCase.getTestEventList().get(1).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "03",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Wrong CVX found", "21",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getVaccineCvx());
    assertEquals("Wrong validity found", "Y",
        testCase.getTestEventList().get(1).getEvaluationActualList().get(1).getDoseValid());
    assertEquals("Not all forecasts read", 9, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HepA",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "08/16/2015",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "08/16/2015",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/16/2016",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "08/16/2014",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "08/16/2014",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "02/16/2015",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "08/01/2018",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "08/16/2015",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "09/27/2014",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "10/16/2014",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "11/16/2014",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
  }


  private static final String RSP_WI_WIR_2 = ""
      + "MSH|^~\\&|WIR|||10318|20190221154837-0600||RSP^K11^RSP_K11|963787|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS\r"
      + "MSA|AE|9wHBlE\r"
      + "ERR||MSH^1^6^1|102^Data type error^HL70357|W|4^Invalid value^HL70533|23A~HL70362||Value [23A] is not valid for HL70362\r"
      + "QAK|9wHBlE|AE|Z44^Request Immunization History^HL70471|1\r"
      + "QPD|Z44^Request Evaluated History and Forecast^HL70471|9wHBlE|9wHBlE^^^FITS^MR|Saint Clair^Sunita^Enos^^^^L|Antelope^Lemuela^^^^^M|20190113|F|308 Morrey St^^Wyandotte^MI^48192^USA^P\r"
      + "PID|1||3060383^^^WIA^SR~9WHBLE^^^WIA^PI||SAINT CLAIR^SUNITA^ENOS^^^^^L|ANTELOPE^LEMUELA^^^^^^M|20190113|F|||308 MORREY ST^^WYANDOTTE^MI^48192^^H||^PRN^^^^734^4516369|||||||||||||||||N\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N\r"
      + "NK1|1|WERSTAD^LEMUELA^^^^^^L|MTH^Mother^HL70063\r"
      + "NK1|2|SAINT CLAIR^SUNITA^ENOS^^^^^L|SEL^Self^HL70063|308 MORREY ST^^WYANDOTTE^MI^48192^^H|^PRN^^^^734^4516369\r"
      + "ORC|RE||38197889^WIA||||||||||||||||||Indian Health Services^D^^^^WIA^XX^^^10318\r"
      + "RXA|0|1|20190217|20190217|10^Polio-Inject^CVX^90713^Polio-Inject^C4|999|||01|||||||||||||20190221\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|89^Polio^CVX||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|64^Polio^SeriesNames||||||F\r"
      + "OBX|4|NM|59782-3^Number of doses in primary series^LN|1|0||||||F\r"
      + "OBX|5|ID|59781-5^Dose validity^LN|1|N||||||F\r"
      + "OBX|6|CE|59783-1^Status in immunization series^LN|1|Not Valid^In progress^SeriesStatus||||||F\r"
      + "OBX|7|ST|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|Dose was given before the earliest acceptable date.|rule.age.min|||||F\r"
      + "ORC|RE||9999^NA\r"
      + "RXA|0|1|20190221|20190221|998^No vaccination administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|1|20^DTP/aP^CVX^90700^DTaP^C4||||||F\r"
      + "OBX|2|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|3|CE|59780-7^Immunization Series name^LN|1|3^DTAP^SeriesNames||||||F\r"
      + "OBX|4|NM|30973-2^Dose number in series^LN|1|1||||||F\r"
      + "OBX|5|NM|59782-3^Number of doses in primary series^LN|1|5||||||F\r"
      + "OBX|6|TS|30981-5^Earliest date to give^LN|1|20190224||||||F\r"
      + "OBX|7|TS|30980-7^Date vaccine due^LN|1|20190313||||||F\r"
      + "OBX|8|TS|59778-1^Vaccine overdue date^LN|1|20190413||||||F\r"
      + "OBX|9|CE|59783-1^Status in immunization series^LN|1|1 of 5^In progress^SeriesStatus||||||F\r"
      + "OBX|10|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|2|45^HepB^CVX^90731^Hep B, unspecified formulation^C4||||||F\r"
      + "OBX|11|CE|59779-9^Immunization Schedule used^LN|2|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|12|CE|59780-7^Immunization Series name^LN|2|93^Hep B^SeriesNames||||||F\r"
      + "OBX|13|NM|30973-2^Dose number in series^LN|2|1||||||F\r"
      + "OBX|14|NM|59782-3^Number of doses in primary series^LN|2|3||||||F\r"
      + "OBX|15|TS|30981-5^Earliest date to give^LN|2|20190113||||||F\r"
      + "OBX|16|TS|30980-7^Date vaccine due^LN|2|20190113||||||F\r"
      + "OBX|17|TS|59778-1^Vaccine overdue date^LN|2|20190413||||||F\r"
      + "OBX|18|CE|59783-1^Status in immunization series^LN|2|1 of 3^In progress^SeriesStatus||||||F\r"
      + "OBX|19|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|3|17^Hib^CVX^90737^Hib, unspecified formulation^C4||||||F\r"
      + "OBX|20|CE|59779-9^Immunization Schedule used^LN|3|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|21|CE|59780-7^Immunization Series name^LN|3|4^HIB^SeriesNames||||||F\r"
      + "OBX|22|NM|30973-2^Dose number in series^LN|3|1||||||F\r"
      + "OBX|23|NM|59782-3^Number of doses in primary series^LN|3|4||||||F\r"
      + "OBX|24|TS|30981-5^Earliest date to give^LN|3|20190224||||||F\r"
      + "OBX|25|TS|30980-7^Date vaccine due^LN|3|20190313||||||F\r"
      + "OBX|26|TS|59778-1^Vaccine overdue date^LN|3|20190413||||||F\r"
      + "OBX|27|CE|59783-1^Status in immunization series^LN|3|1 of 4^In progress^SeriesStatus||||||F\r"
      + "OBX|28|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|4|03^MMR^CVX^90707^MMR^C4||||||F\r"
      + "OBX|29|CE|59779-9^Immunization Schedule used^LN|4|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|30|CE|59780-7^Immunization Series name^LN|4|1^MMR^SeriesNames||||||F\r"
      + "OBX|31|NM|30973-2^Dose number in series^LN|4|1||||||F\r"
      + "OBX|32|NM|59782-3^Number of doses in primary series^LN|4|2||||||F\r"
      + "OBX|33|TS|30981-5^Earliest date to give^LN|4|20200113||||||F\r"
      + "OBX|34|TS|30980-7^Date vaccine due^LN|4|20200113||||||F\r"
      + "OBX|35|TS|59778-1^Vaccine overdue date^LN|4|20200513||||||F\r"
      + "OBX|36|CE|59783-1^Status in immunization series^LN|4|1 of 2^In progress^SeriesStatus||||||F\r"
      + "OBX|37|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|5|152^Pneumococcal^CVX||||||F\r"
      + "OBX|38|CE|59779-9^Immunization Schedule used^LN|5|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|39|CE|59780-7^Immunization Series name^LN|5|67^Pneumococcal^SeriesNames||||||F\r"
      + "OBX|40|NM|30973-2^Dose number in series^LN|5|1||||||F\r"
      + "OBX|41|NM|59782-3^Number of doses in primary series^LN|5|4||||||F\r"
      + "OBX|42|TS|30981-5^Earliest date to give^LN|5|20190224||||||F\r"
      + "OBX|43|TS|30980-7^Date vaccine due^LN|5|20190313||||||F\r"
      + "OBX|44|TS|59778-1^Vaccine overdue date^LN|5|20190413||||||F\r"
      + "OBX|45|CE|59783-1^Status in immunization series^LN|5|1 of 4^In progress^SeriesStatus||||||F\r"
      + "OBX|46|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|6|89^Polio^CVX||||||F\r"
      + "OBX|47|CE|59779-9^Immunization Schedule used^LN|6|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|48|CE|59780-7^Immunization Series name^LN|6|64^Polio^SeriesNames||||||F\r"
      + "OBX|49|NM|30973-2^Dose number in series^LN|6|1||||||F\r"
      + "OBX|50|NM|59782-3^Number of doses in primary series^LN|6|5||||||F\r"
      + "OBX|51|TS|30981-5^Earliest date to give^LN|6|20190224||||||F\r"
      + "OBX|52|TS|30980-7^Date vaccine due^LN|6|20190313||||||F\r"
      + "OBX|53|TS|59778-1^Vaccine overdue date^LN|6|20190413||||||F\r"
      + "OBX|54|CE|59783-1^Status in immunization series^LN|6|1 of 5^In progress^SeriesStatus||||||F\r"
      + "OBX|55|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|7|122^Rotavirus^CVX||||||F\r"
      + "OBX|56|CE|59779-9^Immunization Schedule used^LN|7|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|57|CE|59780-7^Immunization Series name^LN|7|2462^Rotavirus^SeriesNames||||||F\r"
      + "OBX|58|NM|30973-2^Dose number in series^LN|7|1||||||F\r"
      + "OBX|59|NM|59782-3^Number of doses in primary series^LN|7|3||||||F\r"
      + "OBX|60|TS|30981-5^Earliest date to give^LN|7|20190224||||||F\r"
      + "OBX|61|TS|30980-7^Date vaccine due^LN|7|20190313||||||F\r"
      + "OBX|62|TS|59778-1^Vaccine overdue date^LN|7|20190313||||||F\r"
      + "OBX|63|CE|59783-1^Status in immunization series^LN|7|1 of 3^In progress^SeriesStatus||||||F\r"
      + "OBX|64|CE|30956-7^Vaccine Type (Vaccine group or family)^LN|8|21^Varicella^CVX^90716^Varicella^C4||||||F\r"
      + "OBX|65|CE|59779-9^Immunization Schedule used^LN|8|VXC16^ACIP Schedule^CDCPHINVS||||||F\r"
      + "OBX|66|CE|59780-7^Immunization Series name^LN|8|69^Varicella^SeriesNames||||||F\r"
      + "OBX|67|NM|30973-2^Dose number in series^LN|8|1||||||F\r"
      + "OBX|68|NM|59782-3^Number of doses in primary series^LN|8|2||||||F\r"
      + "OBX|69|TS|30981-5^Earliest date to give^LN|8|20200113||||||F\r"
      + "OBX|70|TS|30980-7^Date vaccine due^LN|8|20200113||||||F\r"
      + "OBX|71|TS|59778-1^Vaccine overdue date^LN|8|20230113||||||F\r"
      + "OBX|72|CE|59783-1^Status in immunization series^LN|8|1 of 2^In progress^SeriesStatus||||||F\r";

  @Test
  public void testRSP_WI_WIR_2() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_WI_WIR_2);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 1, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "89",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "N",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Not all forecasts read", 10, forecastActualList.size());
    assertEquals("Forecast not found", "DTaP",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "03/13/2019",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/13/2019",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "DTaP, Tdap or Td",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "03/13/2019",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/13/2019",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "HepB",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "01/13/2019",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "01/13/2019",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/13/2019",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "Hib",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "03/13/2019",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/13/2019",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "01/13/2020",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "01/13/2020",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "05/13/2020",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "PCV",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "03/13/2019",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/13/2019",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Pneumococcal",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "03/13/2019",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/13/2019",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
    assertEquals("Forecast not found", "Polio",
        forecastActualList.get(7).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(7).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(7).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(7).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(7).getDueDate());
    assertEquals("Wrong due date found", "03/13/2019",
        sdf.format(forecastActualList.get(7).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(7).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/13/2019",
        sdf.format(forecastActualList.get(7).getOverdueDate()));
    assertEquals("Forecast not found", "Rotavirus",
        forecastActualList.get(8).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(8).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(8).getValidDate());
    assertEquals("Wrong earliest date found", "02/24/2019",
        sdf.format(forecastActualList.get(8).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(8).getDueDate());
    assertEquals("Wrong due date found", "03/13/2019",
        sdf.format(forecastActualList.get(8).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(8).getOverdueDate());
    assertEquals("Wrong overdue date found", "03/13/2019",
        sdf.format(forecastActualList.get(8).getOverdueDate()));
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(9).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(9).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(9).getValidDate());
    assertEquals("Wrong earliest date found", "01/13/2020",
        sdf.format(forecastActualList.get(9).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(9).getDueDate());
    assertEquals("Wrong due date found", "01/13/2020",
        sdf.format(forecastActualList.get(9).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(9).getOverdueDate());
    assertEquals("Wrong overdue date found", "01/13/2023",
        sdf.format(forecastActualList.get(9).getOverdueDate()));
  }

  private static final String RSP_NE_NESIIS_2 = ""
      + "MSH|^~\\&|NESIIS|NESIIS||WPIH|20190318||RSP^K11^RSP_K11|993473.1|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS||WPIH\r"
      + "MSA|AA|FvQExK||0||0^Message Accepted^HL70357\r" + "QAK|FvQExK|OK|Z44\r"
      + "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|FvQExK|FvQExK^^^FITS^MR|Santa Clara^Amoke^Freeman^^^^L|Gogebic^Delyth^^^^^M|19690312|F|267 Gray Ave^^Gwinn^MI^49841^USA^P\r"
      + "PID|1||7395036^^^NEA^SR~FvQExK^^^FITS^MR||SANTA CLARA^AMOKE^FREEMAN|GOGEBIC^DELYTH|19690312|F|||267 GRAY AVE^^GWINN^MI^49841^^C||^^PH^^^906^5803226|||||||||||N|0\r"
      + "ORC|RE||41787932\r"
      + "RXA|0|1|20190312|20190312|121^Zoster (shingles), live^CVX^Varicella^^WVGC|1.0|||01\r"
      + "OBX|1|CE|30956-7^VACCINE TYPE^LN|1|188^Zoster, unspecified formulation^CVX^Zoster^^WVGC||||||F\r"
      + "OBX|2|CE|59779-9^Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|3|NM|30973-2^Dose number in series^LN|1|1||||||F\r" + "ORC|RE||0\r"
      + "RXA|0|1|19690312|19690312|998^No Vaccine Administered^CVX|999\r"
      + "OBX|4|CE|30956-7^VACCINE_TYPE^LN|0|88^Influenza^CVX^90724^Influenza^CPT||||||F\r"
      + "OBX|5|TS|30980-7^Date Vaccine Due^LN|0|20180801||||||F\r"
      + "OBX|6|TS|59778-1^Date Dose Is Overdue^LN|0|19700312||||||F\r"
      + "OBX|7|NM|30973-2^Vaccine due next dose number^LN|0|1||||||F\r"
      + "OBX|8|TS|30981-5^Earliest date to give^LN|0|19690912||||||F\r"
      + "OBX|9|CE|59779-9^Schedule Used^LN|0|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|10|CE|30956-7^VACCINE_TYPE^LN|1|03^MMR^CVX^90707^MMR^CPT||||||F\r"
      + "OBX|11|TS|30980-7^Date Vaccine Due^LN|1|20190409||||||F\r"
      + "OBX|12|TS|59778-1^Date Dose Is Overdue^LN|1|20190409||||||F\r"
      + "OBX|13|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r"
      + "OBX|14|TS|30981-5^Earliest date to give^LN|1|20190409||||||F\r"
      + "OBX|15|CE|59779-9^Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|16|CE|30956-7^VACCINE_TYPE^LN|2|115^Pertussis(Tdap)^CVX^90715^Pertussis(Tdap)^CPT||||||F\r"
      + "OBX|17|TS|30980-7^Date Vaccine Due^LN|2|19800312||||||F\r"
      + "OBX|18|TS|59778-1^Date Dose Is Overdue^LN|2|19820312||||||F\r"
      + "OBX|19|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r"
      + "OBX|20|TS|30981-5^Earliest date to give^LN|2|19760312||||||F\r"
      + "OBX|21|CE|59779-9^Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|22|CE|30956-7^VACCINE_TYPE^LN|3|139^Td^CVX^90714^Td^CPT||||||F\r"
      + "OBX|23|TS|30980-7^Date Vaccine Due^LN|3|19760312||||||F\r"
      + "OBX|24|TS|59778-1^Date Dose Is Overdue^LN|3|19760412||||||F\r"
      + "OBX|25|NM|30973-2^Vaccine due next dose number^LN|3|1||||||F\r"
      + "OBX|26|TS|30981-5^Earliest date to give^LN|3|19760312||||||F\r"
      + "OBX|27|CE|59779-9^Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|28|CE|30956-7^VACCINE_TYPE^LN|4|21^Varicella^CVX^90716^Varicella^CPT||||||F\r"
      + "OBX|29|TS|30980-7^Date Vaccine Due^LN|4|20190409||||||F\r"
      + "OBX|30|TS|59778-1^Date Dose Is Overdue^LN|4|20190409||||||F\r"
      + "OBX|31|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r"
      + "OBX|32|TS|30981-5^Earliest date to give^LN|4|20190409||||||F\r"
      + "OBX|33|CE|59779-9^Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F\r"
      + "OBX|34|CE|30956-7^VACCINE_TYPE^LN|5|188^Zoster^CVX||||||F\r"
      + "OBX|35|TS|30980-7^Date Vaccine Due^LN|5|20190507||||||F\r"
      + "OBX|36|TS|59778-1^Date Dose Is Overdue^LN|5|20190604||||||F\r"
      + "OBX|37|NM|30973-2^Vaccine due next dose number^LN|5|2||||||F\r"
      + "OBX|38|TS|30981-5^Earliest date to give^LN|5|20190507||||||F\r"
      + "OBX|39|CE|59779-9^Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F\r";

  @Test
  public void testRSP_NE_NESIIS_2() throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    TestCase testCase = run(forecastActualList, RSP_NE_NESIIS_2);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    assertEquals("Not all test events read", 1, testCase.getTestEventList().size());
    assertEquals("Wrong number of evaluations", 1,
        testCase.getTestEventList().get(0).getEvaluationActualList().size());
    assertEquals("Wrong CVX found", "188",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getVaccineCvx());
    assertEquals("Wrong validity found", "",
        testCase.getTestEventList().get(0).getEvaluationActualList().get(0).getDoseValid());
    assertEquals("Not all forecasts read", 7, forecastActualList.size());
    assertEquals("Forecast not found", "HerpesZoster",
        forecastActualList.get(0).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(0).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(0).getValidDate());
    assertEquals("Wrong earliest date found", "05/07/2019",
        sdf.format(forecastActualList.get(0).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(0).getDueDate());
    assertEquals("Wrong due date found", "05/07/2019",
        sdf.format(forecastActualList.get(0).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(0).getOverdueDate());
    assertEquals("Wrong overdue date found", "06/04/2019",
        sdf.format(forecastActualList.get(0).getOverdueDate()));
    assertEquals("Forecast not found", "Influenza",
        forecastActualList.get(1).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(1).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(1).getValidDate());
    assertEquals("Wrong earliest date found", "09/12/1969",
        sdf.format(forecastActualList.get(1).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(1).getDueDate());
    assertEquals("Wrong due date found", "08/01/2018",
        sdf.format(forecastActualList.get(1).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(1).getOverdueDate());
    assertEquals("Wrong overdue date found", "03/12/1970",
        sdf.format(forecastActualList.get(1).getOverdueDate()));
    assertEquals("Forecast not found", "MMR",
        forecastActualList.get(2).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(2).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(2).getValidDate());
    assertEquals("Wrong earliest date found", "04/09/2019",
        sdf.format(forecastActualList.get(2).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(2).getDueDate());
    assertEquals("Wrong due date found", "04/09/2019",
        sdf.format(forecastActualList.get(2).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(2).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/09/2019",
        sdf.format(forecastActualList.get(2).getOverdueDate()));
    assertEquals("Forecast not found", "Td Only",
        forecastActualList.get(3).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(3).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(3).getValidDate());
    assertEquals("Wrong earliest date found", "03/12/1976",
        sdf.format(forecastActualList.get(3).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(3).getDueDate());
    assertEquals("Wrong due date found", "03/12/1976",
        sdf.format(forecastActualList.get(3).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(3).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/12/1976",
        sdf.format(forecastActualList.get(3).getOverdueDate()));
    assertEquals("Forecast not found", "Td or Tdap",
        forecastActualList.get(4).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(4).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(4).getValidDate());
    assertEquals("Wrong earliest date found", "03/12/1976",
        sdf.format(forecastActualList.get(4).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(4).getDueDate());
    assertEquals("Wrong due date found", "03/12/1976",
        sdf.format(forecastActualList.get(4).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(4).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/12/1976",
        sdf.format(forecastActualList.get(4).getOverdueDate()));
    assertEquals("Forecast not found", "Tdap Only",
        forecastActualList.get(5).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(5).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(5).getValidDate());
    assertEquals("Wrong earliest date found", "03/12/1976",
        sdf.format(forecastActualList.get(5).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(5).getDueDate());
    assertEquals("Wrong due date found", "03/12/1980",
        sdf.format(forecastActualList.get(5).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(5).getOverdueDate());
    assertEquals("Wrong overdue date found", "03/12/1982",
        sdf.format(forecastActualList.get(5).getOverdueDate()));
    assertEquals("Forecast not found", "Varicella",
        forecastActualList.get(6).getVaccineGroup().getLabel());
    assertEquals("Wrong status found", "", forecastActualList.get(6).getAdminStatus());
    assertNotNull("Valid date should not be null", forecastActualList.get(6).getValidDate());
    assertEquals("Wrong earliest date found", "04/09/2019",
        sdf.format(forecastActualList.get(6).getValidDate()));
    assertNotNull("Due date should not be null", forecastActualList.get(6).getDueDate());
    assertEquals("Wrong due date found", "04/09/2019",
        sdf.format(forecastActualList.get(6).getDueDate()));
    assertNotNull("Overdue date should not be null", forecastActualList.get(6).getOverdueDate());
    assertEquals("Wrong overdue date found", "04/09/2019",
        sdf.format(forecastActualList.get(6).getOverdueDate()));
  }
}
