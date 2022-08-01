package org.immregistries.vfa.connect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.immregistries.vfa.connect.ICEConnector;
import org.immregistries.vfa.connect.model.Event;
import org.immregistries.vfa.connect.model.ForecastActual;
import org.immregistries.vfa.connect.model.Software;
import org.immregistries.vfa.connect.model.SoftwareResult;
import org.immregistries.vfa.connect.model.TestCase;
import org.immregistries.vfa.connect.model.TestEvent;
import org.immregistries.vfa.connect.model.VaccineGroup;
import org.junit.Test;

public class TestICEConnector extends junit.framework.TestCase
{

  @Test
  public void testForecast() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    TestCase testCase = createTestCase(sdf);

    Software software = new Software();
    //software.setServiceUrl("http://immlab.pagekite.me/opencds-decision-support-service/evaluate");
    software.setServiceUrl("https://cds.hln.com/opencds-decision-support-service/evaluate");
    ICEConnector iceConnector = new ICEConnector(software, VaccineGroup.getForecastItemList());
    iceConnector.setLogText(true);
    SoftwareResult softwareResult = new SoftwareResult();
    List<ForecastActual> forecastActualList = iceConnector.queryForForecast(testCase, softwareResult);
    assertEquals(19, forecastActualList.size());
    for (ForecastActual forecastActual : forecastActualList) {
      System.out.print("--> " + (forecastActual.getVaccineGroup().getLabel() + "               ").substring(0, 15) + " "
          + (forecastActual.getAdmin() + "          ").substring(0, 10));
      if (forecastActual.getValidDate() == null) {
        System.out.print("           ");
      } else {
        System.out.print(" " + sdf.format(forecastActual.getValidDate()));
      }
      if (forecastActual.getDueDate() == null) {
        System.out.print("           ");
      } else {
        System.out.print(" " + sdf.format(forecastActual.getDueDate()));
      }
      if (forecastActual.getOverdueDate() == null) {
        System.out.print("           ");
      } else {
        System.out.print(" " + sdf.format(forecastActual.getOverdueDate()));
      }
      System.out.println();
    }
    System.out.println(softwareResult.getLogText());
  }

  public void testReadVMR() throws Exception {
    StringBuilder sb = new StringBuilder();
    BufferedReader in = new BufferedReader(
        new InputStreamReader(this.getClass().getResourceAsStream("exampleReturn.xml")));
    String line;
    while ((line = in.readLine()) != null) {
      sb.append(line);
      sb.append("\n");
    }
    String exampleReturn = sb.toString();

    Software software = new Software();
    ICEConnector iceConnector = new ICEConnector(software, VaccineGroup.getForecastItemList());
    SoftwareResult softwareResult = new SoftwareResult();
    List<ForecastActual> forecastActualList = iceConnector.readVMR(exampleReturn, softwareResult);
    assertEquals(9, forecastActualList.size());
  }

  private static final String EXAMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
      + "<ns4:cdsInput xmlns:ns2=\"org.opencds\" xmlns:ns3=\"org.opencds.vmr.v1_0.schema.vmr\" xmlns:ns4=\"org.opencds.vmr.v1_0.schema.cdsinput\" xmlns:ns5=\"org.opencds.vmr.v1_0.schema.cdsoutput\">\n"
      + "    <templateId root=\"2.16.840.1.113883.3.795.11.1.1\"/>\n" + "    <cdsContext>\n"
      + "        <cdsSystemUserPreferredLanguage code=\"en\" codeSystem=\"2.16.840.1.113883.6.99\" displayName=\"English\"/>\n"
      + "    </cdsContext>\n" + "    <vmrInput>\n" + "        <templateId root=\"2.16.840.1.113883.3.795.11.1.1\"/>\n"
      + "        <patient>\n" + "            <templateId root=\"2.16.840.1.113883.3.795.11.2.1.1\"/>\n"
      + "            <id root=\"2.16.840.1.113883.3.795.12.100.11\" extension=\"92\"/>\n"
      + "            <demographics>\n" + "                <birthTime value=\"19900101\"/>\n"
      + "                <gender code=\"M\" codeSystem=\"2.16.840.1.113883.5.1\" displayName=\"Male\" originalText=\"M\"/>\n"
      + "            </demographics>\n" + "            <clinicalStatements>\n" +
      // "                <observationResults>\n" +
      // "                    <observationResult>\n" +
      // "                        <templateId root=\"2.16.840.1.113883.3.795.11.6.3.1\"/>\n"
      // +
      // "                        <id root=\"617478b8-b6eb-4988-853a-b5f5c2441eb8\"/>\n"
      // +
      // "                        <observationFocus code=\"070.30\" codeSystem=\"2.16.840.1.113883.3.795.12.100.7\" displayName=\"Hepatitis B\" originalText=\"070.30\"/>\n"
      // +
      // "                        <observationEventTime low=\"19960315\" high=\"19960315\"/>\n"
      // +
      // "                        <observationValue>\n" +
      // "                            <concept code=\"DISEASE_DOCUMENTED\" codeSystem=\"2.16.840.1.113883.3.795.12.100.8\" displayName=\"Disease Documented\" originalText=\"DISEASE_DOCUMENTED\"/>\n"
      // +
      // "                        </observationValue>\n" +
      // "                        <interpretation code=\"IS_IMMUNE\" codeSystem=\"2.16.840.1.113883.3.795.12.100.9\" displayName=\"Is Immune\" originalText=\"IS_IMMUNE\"/>\n"
      // +
      // "                    </observationResult>\n" +
      // "                </observationResults>\n" +
      "                <substanceAdministrationEvents>\n" + "                    <substanceAdministrationEvent>\n"
      + "                        <templateId root=\"2.16.840.1.113883.3.795.11.9.1.1\"/>\n"
      + "                        <id root=\"2.16.840.1.113883.3.795.12.100.10\" extension=\"230\"/>\n"
      + "                        <substanceAdministrationGeneralPurpose code=\"384810002\" codeSystem=\"2.16.840.1.113883.6.5\"/>\n"
      + "                        <substance>\n"
      + "                            <id root=\"6095733e-a576-44a2-b314-26a23e1ff6b6\"/>\n"
      + "                            <substanceCode code=\"45\" codeSystem=\"2.16.840.1.113883.12.292\" displayName=\"Hep B, unspecified formulation\" originalText=\"45\"/>\n"
      + "                        </substance>\n"
      + "                        <administrationTimeInterval low=\"19900315\" high=\"19900315\"/>\n"
      + "                    </substanceAdministrationEvent>\n" + "                    <substanceAdministrationEvent>\n"
      + "                        <templateId root=\"2.16.840.1.113883.3.795.11.9.1.1\"/>\n"
      + "                        <id root=\"2.16.840.1.113883.3.795.12.100.10\" extension=\"229\"/>\n"
      + "                        <substanceAdministrationGeneralPurpose code=\"384810002\" codeSystem=\"2.16.840.1.113883.6.5\"/>\n"
      + "                        <substance>\n"
      + "                            <id root=\"c4361cf7-4387-4072-a55e-5bac066813ad\"/>\n"
      + "                            <substanceCode code=\"45\" codeSystem=\"2.16.840.1.113883.12.292\" displayName=\"Hep B, unspecified formulation\" originalText=\"45\"/>\n"
      + "                        </substance>\n"
      + "                        <administrationTimeInterval low=\"19900401\" high=\"19900401\"/>\n"
      + "                    </substanceAdministrationEvent>\n" + "                    <substanceAdministrationEvent>\n"
      + "                        <templateId root=\"2.16.840.1.113883.3.795.11.9.1.1\"/>\n"
      + "                        <id root=\"2.16.840.1.113883.3.795.12.100.10\" extension=\"228\"/>\n"
      + "                        <substanceAdministrationGeneralPurpose code=\"384810002\" codeSystem=\"2.16.840.1.113883.6.5\"/>\n"
      + "                        <substance>\n"
      + "                            <id root=\"84e18c21-1a07-4347-b7fd-96f052a39ef6\"/>\n"
      + "                            <substanceCode code=\"08\" codeSystem=\"2.16.840.1.113883.12.292\" displayName=\"Hep B, adolescent or pediatric\" originalText=\"08\"/>\n"
      + "                        </substance>\n"
      + "                        <administrationTimeInterval low=\"19960315\" high=\"19960315\"/>\n"
      + "                    </substanceAdministrationEvent>\n" + "                    <substanceAdministrationEvent>\n"
      + "                        <templateId root=\"2.16.840.1.113883.3.795.11.9.1.1\"/>\n"
      + "                        <id root=\"2.16.840.1.113883.3.795.12.100.10\" extension=\"227\"/>\n"
      + "                        <substanceAdministrationGeneralPurpose code=\"384810002\" codeSystem=\"2.16.840.1.113883.6.5\"/>\n"
      + "                        <substance>\n"
      + "                            <id root=\"fca8d517-9541-4f80-adbd-1528b3963360\"/>\n"
      + "                            <substanceCode code=\"08\" codeSystem=\"2.16.840.1.113883.12.292\" displayName=\"Hep B, adolescent or pediatric\" originalText=\"08\"/>\n"
      + "                        </substance>\n"
      + "                        <administrationTimeInterval low=\"20100201\" high=\"20100201\"/>\n"
      + "                    </substanceAdministrationEvent>\n" + "                </substanceAdministrationEvents>\n"
      + "            </clinicalStatements>\n" + "        </patient>\n" + "    </vmrInput>\n" + "</ns4:cdsInput>\n";

  @Test
  public void testMakeVMR() throws ParseException {

    assertEquals("&gt;", ICEConnector.fix(">"));
    assertEquals("&lt;", ICEConnector.fix("<"));
    assertEquals("&quot;", ICEConnector.fix("\""));
    assertEquals("&amp;", ICEConnector.fix("&"));
    assertEquals("&apos;", ICEConnector.fix("'"));
    assertEquals("&lt;&gt;&apos;&amp;&quot;", ICEConnector.fix("<>'&\""));

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    TestCase testCase = createTestCase(sdf);

    Software software = new Software();
    List<VaccineGroup> forecastItemList = new ArrayList<VaccineGroup>();
    ICEConnector iceConnector = new ICEConnector(software, forecastItemList);
    assertEquals(EXAMPLE, iceConnector.makeVMR(testCase));
  }

  public TestCase createTestCase(SimpleDateFormat sdf) throws ParseException {
    TestCase testCase = new TestCase();
    testCase.setPatientSex("M");
    testCase.setPatientDob(sdf.parse("01/01/1990"));
    List<TestEvent> testEventList = new ArrayList<TestEvent>();
    testCase.setTestEventList(testEventList);
    {
      TestEvent testEvent = new TestEvent();
      testEvent.setEvent(Event.getEvent(45));
      testEvent.setEventDate(sdf.parse("03/15/1990"));
      testEvent.setTestCase(testCase);
      testEventList.add(testEvent);
    }
    {
      TestEvent testEvent = new TestEvent();
      testEvent.setEvent(Event.getEvent(45));
      testEvent.setEventDate(sdf.parse("04/01/1990"));
      testEvent.setTestCase(testCase);
      testEventList.add(testEvent);
    }
    {
      TestEvent testEvent = new TestEvent();
      testEvent.setEvent(Event.getEvent(8));
      testEvent.setEventDate(sdf.parse("03/15/1996"));
      testEvent.setTestCase(testCase);
      testEventList.add(testEvent);
    }
    {
      TestEvent testEvent = new TestEvent();
      testEvent.setEvent(Event.getEvent(8));
      testEvent.setEventDate(sdf.parse("02/01/2010"));
      testEvent.setTestCase(testCase);
      testEventList.add(testEvent);
    }
    return testCase;
  }

}
