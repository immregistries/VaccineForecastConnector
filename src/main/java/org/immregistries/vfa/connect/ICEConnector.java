package org.immregistries.vfa.connect;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axis.encoding.Base64;
import org.immregistries.vfa.connect.model.Admin;
import org.immregistries.vfa.connect.model.ForecastActual;
import org.immregistries.vfa.connect.model.Software;
import org.immregistries.vfa.connect.model.SoftwareResult;
import org.immregistries.vfa.connect.model.SoftwareResultStatus;
import org.immregistries.vfa.connect.model.TestCase;
import org.immregistries.vfa.connect.model.TestEvent;
import org.immregistries.vfa.connect.model.VaccineGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ICEConnector implements ConnectorInterface
{

  private static final String LOG_PARSE_ID_END = "\"/>";
  private static final String LOG_PARSE_ID_START = "<id root=\"";

  private static final String BASE64_ENCODED_PAYLOAD_TAG_START = "<base64EncodedPayload>";
  private static final String BASE64_ENCODED_PAYLOAD_TAG_END = "</base64EncodedPayload>";

  private Map<String, VaccineGroup[]> familyMapping = new HashMap<String, VaccineGroup[]>();

  private Software software = null;

  private boolean logText = false;

  public boolean isLogText() {
    return logText;
  }

  public void setLogText(boolean logText) {
    this.logText = logText;
  }

  public ICEConnector(Software software, List<VaccineGroup> forecastItemList) {
    this.software = software;

    // 100 Hep B Vaccine Group
    addForcastItem(forecastItemList, "100", new int[] { VaccineGroup.ID_HEPB });
    // 810 Hep A Vaccine Group
    addForcastItem(forecastItemList, "810", new int[] { VaccineGroup.ID_HEPA });
    // 200_107 DTaP NOS Vaccine Group
    addForcastItem(forecastItemList, "107", new int[] { VaccineGroup.ID_DTAP, VaccineGroup.ID_DTAP_TDAP_TD });
    // 200_09 TD Vaccine Group
    addForcastItem(forecastItemList, "09",
        new int[] { VaccineGroup.ID_TD_ONLY, VaccineGroup.ID_DTAP_TDAP_TD, VaccineGroup.ID_TDAP_TD });
    // 200_115 DTP Vaccine Group
    addForcastItem(forecastItemList, "115",
        new int[] { VaccineGroup.ID_TDAP_ONLY, VaccineGroup.ID_DTAP_TDAP_TD, VaccineGroup.ID_TDAP_TD });
    // 200 DTP Vaccine Group
    addForcastItem(forecastItemList, "200", new int[] { VaccineGroup.ID_DTAP_TDAP_TD });
    // 300 Hib Vaccine Group
    addForcastItem(forecastItemList, "300", new int[] { VaccineGroup.ID_HIB });
    // 400 Polio Vaccine Group
    addForcastItem(forecastItemList, "400", new int[] { VaccineGroup.ID_POLIO });
    // 500 MMR Vaccine Group
    addForcastItem(forecastItemList, "500", new int[] { VaccineGroup.ID_MMR });
    // 600 Varicella Vaccine Group
    addForcastItem(forecastItemList, "600", new int[] { VaccineGroup.ID_VAR });
    // 700 Pneumococcal Conjugate Vaccine Group
    addForcastItem(forecastItemList, "700", new int[] { VaccineGroup.ID_PCV, VaccineGroup.ID_PNEUMO });
    // 720 Pneumococcal Polysaccharid Vaccine Group
    addForcastItem(forecastItemList, "720", new int[] { VaccineGroup.ID_PPSV });
    // 800 Influenza
    addForcastItem(forecastItemList, "800", new int[] { VaccineGroup.ID_INFLUENZA });
    // 820 Rotavirus Vaccine Group
    addForcastItem(forecastItemList, "820", new int[] { VaccineGroup.ID_ROTA });
    // 830 Meningococcal Vaccine Group
    addForcastItem(forecastItemList, "830", new int[] { VaccineGroup.ID_MENING });
    // 840 Human Papillomavirus Vaccine Group
    addForcastItem(forecastItemList, "840", new int[] { VaccineGroup.ID_HPV });
    // 890 H1N1 Influenza
    addForcastItem(forecastItemList, "890", new int[] { VaccineGroup.ID_NOVEL_H1N1 });

  }

  private static int[] supportedItems = new int[] { VaccineGroup.ID_NOVEL_H1N1,
      VaccineGroup.ID_HEPA, VaccineGroup.ID_HEPB, VaccineGroup.ID_HIB, VaccineGroup.ID_HPV, VaccineGroup.ID_INFLUENZA,
      VaccineGroup.ID_MENING, VaccineGroup.ID_MMR, VaccineGroup.ID_PNEUMO, VaccineGroup.ID_PCV, VaccineGroup.ID_PPSV,
      VaccineGroup.ID_POLIO, VaccineGroup.ID_ROTA, VaccineGroup.ID_VAR };
  private static int[] notSupportedItems = new int[] { VaccineGroup.ID_ZOSTER, VaccineGroup.ID_HEPB_2_ONLY,
      VaccineGroup.ID_HEPB_3_ONLY, VaccineGroup.ID_MEASLES_ONLY, VaccineGroup.ID_MUMPS_ONLY,
      VaccineGroup.ID_RUBELLA_ONLY, VaccineGroup.ID_ANTHRAX, VaccineGroup.ID_SMALLPOX_SHOT_OR_READING,
      VaccineGroup.ID_TYPHOID };

  private void addForcastItem(List<VaccineGroup> forecastItemList, String familyName, int[] forecastItemIds) {
    VaccineGroup[] forecastItems = new VaccineGroup[forecastItemIds.length];
    for (int i = 0; i < forecastItemIds.length; i++) {
      forecastItems[i] = VaccineGroup.getForecastItem(forecastItemIds[i]);
    }
    familyMapping.put(familyName, forecastItems);
    return;
  }

  private StringWriter sw;
  private PrintWriter logOut;

  public List<ForecastActual> queryForForecast(TestCase testCase, SoftwareResult softwareResult) throws Exception {

    sw = new StringWriter();
    logOut = logText ? new PrintWriter(sw) : null;
    List<ForecastActual> list = new ArrayList<ForecastActual>();

    try {
      if (logOut != null) {
        logOut.println("ICE");
        logOut.println();
        logOut.println("Current time " + new Date());
        logOut.println("Connecting to " + software.getServiceUrl());
        logOut.println();
      }
      URLConnection urlConn;
      URL url = new URL(software.getServiceUrl());
      urlConn = url.openConnection();
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      urlConn.setUseCaches(true);
      ((HttpURLConnection) urlConn).setRequestMethod("POST");
      urlConn.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
      urlConn.setRequestProperty("SoapAction",
          "http://www.omg.org/spec/CDSS/201105/dssWsdl:operation:evaluateAtSpecifiedTime");

      DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
      printout.writeBytes(makeRequest(testCase));
      printout.flush();
      printout.close();

      InputStreamReader input = null;
      input = new InputStreamReader(urlConn.getInputStream());
      BufferedReader in = new BufferedReader(input);
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = in.readLine()) != null) {
        sb.append(line);
      }
      input.close();
      line = sb.toString();

      int posStart = line.indexOf(BASE64_ENCODED_PAYLOAD_TAG_START);
      int posEnd = line.indexOf(BASE64_ENCODED_PAYLOAD_TAG_END);
      if (posStart == -1 || posEnd == -1 || posEnd < posStart) {
        if (logOut != null) {
          logOut.println("Unable to read results, couldn't find base 64 contents");
          logOut.println(line);
        }
      } else {
        line = line.substring(posStart + BASE64_ENCODED_PAYLOAD_TAG_START.length(), posEnd);
        line = new String(Base64.decode(line));
        if (logOut != null) {
          logOut.print(line);
        }
        list = readVMR(line, softwareResult);
        for (int forecastItemId : notSupportedItems) {
          boolean found = false;
          for (ForecastActual forecastActual : list) {
            if (forecastActual.getVaccineGroup().getVaccineGroupId() == forecastItemId) {
              found = true;
              break;
            }
          }
          if (!found) {
            VaccineGroup forecastItem = VaccineGroup.getForecastItem(forecastItemId);
            if (forecastItem != null) {
              ForecastActual forecastActual = new ForecastActual();
              forecastActual.setSoftwareResult(softwareResult);
              forecastActual.setAdmin(Admin.NO_RESULTS);
              forecastActual.setDoseNumber("NS");
              forecastActual.setVaccineGroup(forecastItem);
              if (logOut != null) {
                logOut.println("ICE Forecaster does not support " + forecastItem.getLabel() + ". No results returned.");
              }
              list.add(forecastActual);
            }
          }
        }
        for (int forecastItemId : supportedItems) {
          boolean found = false;
          for (ForecastActual forecastActual : list) {
            if (forecastActual.getVaccineGroup().getVaccineGroupId() == forecastItemId) {
              found = true;
              break;
            }
          }
          if (!found) {
            VaccineGroup forecastItem = VaccineGroup.getForecastItem(forecastItemId);
            if (forecastItem != null) {
              ForecastActual forecastActual = new ForecastActual();
              forecastActual.setSoftwareResult(softwareResult);
              forecastActual.setAdmin(Admin.NO_RESULTS);
              forecastActual.setVaccineGroup(forecastItem);
              if (logOut != null) {
                logOut.println("ICE Forecaster did not return results " + forecastItem.getLabel()
                    + ". Results assumed to be complete. ");
              }
              list.add(forecastActual);
            }
          }
        }
      }
    } catch (Exception e) {
      softwareResult.setSoftwareResultStatus(SoftwareResultStatus.PROBLEM);
      if (logOut != null) {
        logOut.println("Unable to get forecast results");
        e.printStackTrace(logOut);
      } else {
        e.printStackTrace();
      }
      throw new Exception("Unable to get forecast results", e);
    } finally {
      if (logOut != null) {
        logOut.close();
        softwareResult.setLogText(sw.toString());
      }
    }
    return list;
  }

  private Map<String, String> xmlLogMap = new HashMap<String, String>();

  protected List<ForecastActual> readVMR(String s, SoftwareResult softwareResult) throws Exception {

    {
      BufferedReader in = new BufferedReader(new StringReader(s));
      String line;
      StringBuilder sb = null;
      String id = null;
      while ((line = in.readLine()) != null) {
        if (line.indexOf("<substanceAdministrationProposal>") != -1) {
          sb = new StringBuilder();
        } else if (line.indexOf("</substanceAdministrationProposal>") != -1) {
          if (id != null) {
            xmlLogMap.put(id, sb.toString());
          }
          sb = null;
          id = null;
        } else if (sb != null) {
          int pos = line.indexOf(LOG_PARSE_ID_START);
          if (id == null && pos != -1) {
            int endPos = line.indexOf(LOG_PARSE_ID_END);
            if (endPos != -1) {
              id = line.substring(pos + LOG_PARSE_ID_START.length(), endPos);
            }
          } else {
            if (line.indexOf("<templateId") == -1) {
              sb.append(line.trim());
              sb.append("\n");
            }
          }
        }

      }
      in.close();
    }

    InputStream in = new ByteArrayInputStream(s.getBytes());
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(in);
    doc.getDocumentElement().normalize();
    NodeList nList = doc.getElementsByTagName("ns5:cdsOutput");
    if (nList.getLength() == 0) {
      nList = doc.getElementsByTagName("ns4:cdsOutput");
    }
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        readVmrOutput(forecastActualList, eElement, softwareResult);
      }
    }
    return forecastActualList;
  }

  private void readVmrOutput(List<ForecastActual> forecastActualList, Element pElement, SoftwareResult softwareResult) {
    NodeList nList = pElement.getElementsByTagName("vmrOutput");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        readPatient(forecastActualList, eElement, softwareResult);
      }
    }
  }

  private void readPatient(List<ForecastActual> forecastActualList, Element pElement, SoftwareResult softwareResult) {
    NodeList nList = pElement.getElementsByTagName("patient");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        readClinicalStatements(forecastActualList, eElement, softwareResult);
      }
    }
  }

  private void readClinicalStatements(List<ForecastActual> forecastActualList, Element pElement,
      SoftwareResult softwareResult) {
    NodeList nList = pElement.getElementsByTagName("clinicalStatements");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        readSubstanceAdministrationProposals(forecastActualList, eElement, softwareResult);
      }
    }
  }

  private void readSubstanceAdministrationProposals(List<ForecastActual> forecastActualList, Element pElement,
      SoftwareResult softwareResult) {
    NodeList nList = pElement.getElementsByTagName("substanceAdministrationProposals");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        readSubstanceAdministrationProposal(forecastActualList, eElement, softwareResult);
      }
    }
  }

  private void readSubstanceAdministrationProposal(List<ForecastActual> forecastActualList, Element pElement,
      SoftwareResult softwareResult) {
    NodeList nList = pElement.getElementsByTagName("substanceAdministrationProposal");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        processSubstanceAdministrationProposal(forecastActualList, eElement, softwareResult);
      }
    }
  }

  private void processSubstanceAdministrationProposal(List<ForecastActual> forecastActualList, Element pElement,
      SoftwareResult softwareResult) {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    String substanceCode = readSubstanceCode(pElement);
    VaccineGroup[] forecastItems = familyMapping.get(substanceCode);
    if (forecastItems != null && forecastItems.length > 0) {
      Date earliestDate = readValidTimeLow(pElement);
      Date recommendDate = readProposedTimeLow(pElement);
      Date overdueDate = readProposedTimeHigh(pElement);
      String id = readId(pElement);
      NodeList nList = pElement.getElementsByTagName("relatedClinicalStatement");
      for (int i = 0; i < nList.getLength(); i++) {
        StringBuilder sb = new StringBuilder();
        Node nNode = nList.item(i);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) nNode;
          String recommendationValue = readObservationValue(eElement);
          sb.append("Found forecast results for " + forecastItems[0].getLabel() + "\n");
          sb.append(" + RecommendationValue = " + recommendationValue + "\n");
          RecommendationReason recommendationReason = readInterpretation(eElement);
          sb.append(" + RecommendationReason = " + recommendationReason + "\n");
          if (recommendDate != null) {
            sb.append(" + Recommended Date = " + sdf.format(recommendDate) + "\n");
          }
          if (earliestDate != null)
          {
            sb.append(" + Earliest Date = " + sdf.format(earliestDate) + "\n");
          }
          if (overdueDate != null) {
            sb.append(" + Overdue Date = " + sdf.format(overdueDate) + "\n");
          }
          for (VaccineGroup forecastItem : forecastItems) {
            ForecastActual forecastActual = new ForecastActual();
            forecastActual.setSoftwareResult(softwareResult);
            forecastActual.setVaccineGroup(forecastItem);
            forecastActual.setDueDate(recommendDate);
            forecastActual.setOverdueDate(overdueDate);
            forecastActual.setValidDate(earliestDate);
            forecastActual.setDoseNumber("*");

            switch (recommendationReason) {
            case ABOVE_AGE_MAY_COMPLETE:
              forecastActual.setAdmin(Admin.FINISHED);
              sb.append(" + Above age to complete, so setting as recommendation as complete\n");
              break;
            case COMPLETE:
              forecastActual.setAdmin(Admin.COMPLETE);
              sb.append(" + Completed, so setting as recommendation as complete\n");
              break;
            case COMPLETE_HIGH_RISK:
              forecastActual.setAdmin(Admin.COMPLETE);
              sb.append(" + High risk series complete so setting as recommendation as complete\n");
              break;
            case DISEASE_DOCUMENTED:
              forecastActual.setAdmin(Admin.IMMUNE);
              sb.append(" + Disease was documented so setting as recommendation as complete\n");
              break;
            case DUE_IN_FUTURE:
              forecastActual.setAdmin(Admin.DUE_LATER);
              forecastActual.setDoseNumber("*");
              sb.append(" + Due in the future\n");
              break;
            case DUE_NOW:
              forecastActual.setAdmin(Admin.DUE);
              forecastActual.setDoseNumber("*");
              sb.append(" + Due now\n");
              break;
            case HIGH_RISK:
              forecastActual.setAdmin(Admin.DUE);
              forecastActual.setDoseNumber("*");
              sb.append(" + Is due because of high risk\n");
              break;
            case IGNORE:
              forecastActual.setAdmin(Admin.UNKNOWN);
              sb.append(" + Ignoring this result\n");
              continue;
            case NOT_SPECIFIED:
              forecastActual.setAdmin(Admin.UNKNOWN);
              sb.append(" + Result not specified\n");
              continue;
            case OUTSIDE_FLU_SEASON:
              forecastActual.setAdmin(Admin.COMPLETE_FOR_SEASON);
              sb.append(" + Outsize of flu season so not recommending, marking as complete\n");
              break;
            case PROOF_OF_IMMUNITY:
              forecastActual.setAdmin(Admin.IMMUNE);
              sb.append(" + Patient has proof of immunity, marking as complete\n");
              break;
            case TOO_OLD:
            case TOO_OLD_TO_INITIATE:
              forecastActual.setAdmin(Admin.FINISHED);
              sb.append(" + Patient is too old, marking as complete\n");
              break;
            case TOO_OLD_HIGH_RISK:
              forecastActual.setAdmin(Admin.FINISHED);
              sb.append(" + Patient is too old for high risk vaccination\n");
              break;
            case WRONG_GENDER:
              forecastActual.setAdmin(Admin.COMPLETE);
              sb.append(" + Patient is wrong gender to receive this vaccination, marking as complete\n");
              break;
            }
            String xmlLog = null;
            if (id != null) {
              xmlLog = xmlLogMap.get(id);
            }
            forecastActualList.add(forecastActual);
          }
        }
      }

    }
  }

  private static enum RecommendationReason {
    ABOVE_AGE_MAY_COMPLETE, COMPLETE, COMPLETE_HIGH_RISK, DISEASE_DOCUMENTED, DUE_IN_FUTURE, DUE_NOW, HIGH_RISK, IGNORE, NOT_SPECIFIED, OUTSIDE_FLU_SEASON, PROOF_OF_IMMUNITY, TOO_OLD, TOO_OLD_HIGH_RISK, WRONG_GENDER, TOO_OLD_TO_INITIATE, CLINICAL_PATIENT_DISCRETION
  };

  private static String readObservationValue(Element pElement) {
    NodeList nList = pElement.getElementsByTagName("observationResult");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        NodeList cList = eElement.getElementsByTagName("observationValue");
        for (int j = 0; j < cList.getLength(); j++) {
          Node cNode = cList.item(j);
          if (cNode.getNodeType() == Node.ELEMENT_NODE) {
            Element cElement = (Element) cNode;
            NodeList gList = cElement.getElementsByTagName("concept");
            for (int k = 0; k < gList.getLength(); k++) {
              Node gNode = gList.item(k);
              if (gNode.getNodeType() == Node.ELEMENT_NODE) {
                Element gElement = (Element) gNode;
                return gElement.getAttribute("code");
              }
            }
          }
        }
      }
    }
    return "";
  }

  private static RecommendationReason readInterpretation(Element pElement) {
    NodeList nList = pElement.getElementsByTagName("observationResult");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        NodeList cList = eElement.getElementsByTagName("interpretation");
        for (int j = 0; j < cList.getLength(); j++) {
          Node cNode = cList.item(j);
          if (cNode.getNodeType() == Node.ELEMENT_NODE) {
            Element cElement = (Element) cNode;
            String code = cElement.getAttribute("code");
            return RecommendationReason.valueOf(code);
          }
        }
      }
    }
    return RecommendationReason.NOT_SPECIFIED;
  }

  private static String readSubstanceCode(Element pElement) {
    NodeList nList = pElement.getElementsByTagName("substance");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        NodeList cList = eElement.getElementsByTagName("substanceCode");
        for (int j = 0; j < cList.getLength(); j++) {
          Node cNode = cList.item(j);
          if (cNode.getNodeType() == Node.ELEMENT_NODE) {
            Element cElement = (Element) cNode;
            return cElement.getAttribute("code");
          }
        }
      }
    }
    return "";
  }

  private static Date readProposedTimeHigh(Element pElement) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    NodeList nList = pElement.getElementsByTagName("proposedAdministrationTimeInterval");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        String dateStringHigh = eElement.getAttribute("high");
        if (dateStringHigh != null) {
          if (dateStringHigh.length() > 8) {
            dateStringHigh = dateStringHigh.substring(0, 8);
          }
          try {
            return sdf.parse(dateStringHigh);
          } catch (ParseException pe) {
            return null;
          }
        }
      }
    }
    return null;
  }

  private static Date readProposedTimeLow(Element pElement) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    NodeList nList = pElement.getElementsByTagName("proposedAdministrationTimeInterval");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        String dateStringLow = eElement.getAttribute("low");
        if (dateStringLow != null) {
          if (dateStringLow.length() > 8) {
            dateStringLow = dateStringLow.substring(0, 8);
          }
          try {
            return sdf.parse(dateStringLow);
          } catch (ParseException pe) {
            return null;
          }
        }
      }
    }
    return null;
  }

  private static Date readValidTimeLow(Element pElement) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    NodeList nList = pElement.getElementsByTagName("validAdministrationTimeInterval");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        String dateStringLow = eElement.getAttribute("low");
        if (dateStringLow != null) {
          if (dateStringLow.length() > 8) {
            dateStringLow = dateStringLow.substring(0, 8);
          }
          try {
            return sdf.parse(dateStringLow);
          } catch (ParseException pe) {
            return null;
          }
        }
      }
    }
    return null;
  }

  private static String readId(Element pElement) {
    NodeList nList = pElement.getElementsByTagName("id");
    for (int i = 0; i < nList.getLength(); i++) {
      Node nNode = nList.item(i);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        return eElement.getAttribute("root");
      }
    }
    return null;
  }

  protected static final boolean SUPPORT_HISTORY_OF_DISEASE = false;

  private String makeRequest(TestCase testCase) {
    StringBuilder sb = new StringBuilder();

    String content = makeVMR(testCase);
    String contentEncoded = Base64.encode(content.getBytes());

    sb.append("<?xml version='1.0' encoding='UTF-8'?>\n");
    sb.append("<S:Envelope xmlns:S=\"http://www.w3.org/2003/05/soap-envelope\">\n");
    sb.append("    <S:Body>\n");
    sb.append("        <ns2:evaluateAtSpecifiedTime xmlns:ns2=\"http://www.omg.org/spec/CDSS/201105/dss\">\n");
    sb.append("            <interactionId scopingEntityId=\"gov.nyc.health\" interactionId=\"123456\"/>\n");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date evalDate = testCase.getEvalDate();
    if (evalDate == null) {
      evalDate = new Date();
    }
    // TODO get the timezone right
    sb.append("            <specifiedTime>" + sdf.format(evalDate) + "T00:00:00.000-05:00</specifiedTime>\n");
    sb.append("            <evaluationRequest clientLanguage=\"\" clientTimeZoneOffset=\"\">\n");
    sb.append("                <kmEvaluationRequest>\n");
    sb.append("                    <kmId scopingEntityId=\"org.nyc.cir\" businessId=\"ICE\" version=\"1.0.0\"/>\n");
    sb.append("                </kmEvaluationRequest>\n");
    sb.append("                <dataRequirementItemData>\n");
    sb.append("                    <driId itemId=\"cdsPayload\">\n");
    sb.append(
        "                        <containingEntityId scopingEntityId=\"gov.nyc.health\" businessId=\"ICEData\" version=\"1.0.0.0\"/>\n");
    sb.append("                    </driId>\n");
    sb.append("                    <data>\n");
    sb.append(
        "                        <informationModelSSId scopingEntityId=\"org.opencds.vmr\" businessId=\"VMR\" version=\"1.0\"/>\n");
    sb.append("                        <base64EncodedPayload>" + contentEncoded + "</base64EncodedPayload>\n");
    sb.append("                    </data>\n");
    sb.append("                </dataRequirementItemData>\n");
    sb.append("            </evaluationRequest>\n");
    sb.append("        </ns2:evaluateAtSpecifiedTime>\n");
    sb.append("    </S:Body>\n");
    sb.append("</S:Envelope>\n");

    return sb.toString();
  }

  protected String makeVMR(TestCase testCase) {
    StringBuilder sb = new StringBuilder();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    // Message Begins
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
    // CDSInput Section Begins (mandatory)
    {
      sb.append(
          "<ns4:cdsInput xmlns:ns2=\"org.opencds\" xmlns:ns3=\"org.opencds.vmr.v1_0.schema.vmr\" xmlns:ns4=\"org.opencds.vmr.v1_0.schema.cdsinput\" xmlns:ns5=\"org.opencds.vmr.v1_0.schema.cdsoutput\">\n");
      sb.append("    <templateId root=\"2.16.840.1.113883.3.795.11.1.1\"/>\n");
      // CDSContext Section Begins (mandatory)
      {
        sb.append("    <cdsContext>\n");
        // Specify user Preferred Language
        sb.append(
            "        <cdsSystemUserPreferredLanguage code=\"en\" codeSystem=\"2.16.840.1.113883.6.99\" displayName=\"English\"/>\n");
        sb.append("    </cdsContext>\n");
        // CDSContext Section Ends
      }
      // VMR Input Section Begins (mandatory)
      {
        sb.append("    <vmrInput>\n");
        sb.append("        <templateId root=\"2.16.840.1.113883.3.795.11.1.1\"/>\n");
        // Patient Input Section Begins (mandatory)
        {
          String uniqueRoot = "2.16.840.1.113883.3.795.12.100.11";
          String uniqueExtension = "92";
          // TODO set unique Root
          // TODO set unique Extension

          sb.append("        <patient>\n");
          sb.append("            <templateId root=\"2.16.840.1.113883.3.795.11.2.1.1\"/>\n");
          // root & extension attributes appended together must be unique across
          // all root & root/extension values for the entire message. The unique
          // identifier cannot be repeated anywhere in the message. Suggestion:
          // use the Globally Unique Identifer (GUID) algorithm to generate the
          // root attribute value only and do not bother specifying the
          // extension. Example GUID value: 0368a1b4-0f93-402e-841d-e0b02943300d
          sb.append("            <id root=\"" + uniqueRoot + "\" extension=\"" + uniqueExtension + "\"/>\n");
          // Patient Birthdate and Gender Section Begins (mandatory)
          {
            sb.append("            <demographics>\n");
            // February 29, 2012 would be specified by 20120229
            sb.append("                <birthTime value=\"" + sdf.format(testCase.getPatientDob()) + "\"/>\n");
            sb.append("                <gender code=\"" + testCase.getPatientSex()
                + "\" codeSystem=\"2.16.840.1.113883.5.1\" displayName=\""
                + (testCase.getPatientSex().equals("M") ? "Male" : "Female") + "\" originalText=\""
                + testCase.getPatientSex() + "\"/>\n");
            sb.append("            </demographics>\n");
            // Patient Birthdate and Gender Section Ends
          }
          sb.append("            <clinicalStatements>\n");
          // Patient Disease Immunity Section Begins (optional)
          if (SUPPORT_HISTORY_OF_DISEASE) {
            sb.append("                <observationResults>\n");
            sb.append("                    <observationResult>\n");
            sb.append("                        <templateId root=\"2.16.840.1.113883.3.795.11.6.3.1\"/>\n");
            // Suggestion: Use Globally Unique Identifier algorithm (GUID)
            String observationResultUniqueId = "617478b8-b6eb-4988-853a-b5f5c2441eb8";
            sb.append("                        <id root=\"" + observationResultUniqueId + "\"/>\n");
            sb.append(
                "                        <observationFocus code=\"{DISEASE_IMMUNITY_FOCUS_CODE}\" codeSystem=\"2.16.840.1.113883.3.795.12.100.7\" displayName=\"..\" originalText=\"..\"/>\n");
            sb.append("                 \n");
            sb.append(
                "                        <!-- ObservationEventTime low and high attributes are dates in YYYYMMDD format, and they must be the same value -->\n");
            sb.append("                        <observationEventTime low=\"{YYYYMMDD}\" high=\"{YYYYMMDD}\"/>\n");
            sb.append("                        <observationValue>                             \n");
            sb.append(
                "                            <concept code=\"{DISEASE_DOCUMENTATION_CODE}\" codeSystem=\"2.16.840.1.113883.3.795.12.100.8\" displayName=\"..\" originalText=\"..\"/>\n");
            sb.append("                        </observationValue>\n");
            sb.append(
                "                        <interpretation code=\"{DISEASE_IMMUNITY_INTERPRETATION_CODE}\" codeSystem=\"2.16.840.1.113883.3.795.12.100.9\" displayName=\"..\" originalText=\"..\"/>\n");
            sb.append("                    </observationResult>\n");
            sb.append("                    <observationResult>\n");
            sb.append("                        [Record another disease immunity information here if necessary ]\n");
            sb.append("                    </observationResult>\n");
            sb.append("                    <observationResult>\n");
            sb.append("                        [Record another disease immunity information here if necessary ]\n");
            sb.append("                    </observationResult>\n");
            sb.append("                </observationResults>\n");
            // Patient Disease Immunity Section Ends
          }
          // List of Vaccines Administered Begins (optional)
          String[] guids = { "6095733e-a576-44a2-b314-26a23e1ff6b6", "c4361cf7-4387-4072-a55e-5bac066813ad",
              "84e18c21-1a07-4347-b7fd-96f052a39ef6", "fca8d517-9541-4f80-adbd-1528b3963360" };
          {
            sb.append("                <substanceAdministrationEvents>\n");
            String substanceAdministrationUniqueId = "2.16.840.1.113883.3.795.12.100.10";
            int id = 231;
            int pos = -1;
            for (TestEvent testEvent : testCase.getTestEventList()) {
              id--;
              pos++;
              sb.append("                    <substanceAdministrationEvent>\n");
              sb.append("                        <templateId root=\"2.16.840.1.113883.3.795.11.9.1.1\"/>\n");
              sb.append("                        <id root=\"" + substanceAdministrationUniqueId + "\" extension=\"" + id
                  + "\"/>\n");
              sb.append(
                  "                        <substanceAdministrationGeneralPurpose code=\"384810002\" codeSystem=\"2.16.840.1.113883.6.5\"/>\n");
              sb.append("                        <substance>\n");
              String guid;
              if (pos < guids.length) {
                guid = guids[pos];
              } else {
                guid = guids[0] + "-" + id;
              }

              sb.append("                            <id root=\"" + guid + "\"/>\n");
              sb.append("                            <substanceCode code=\"" + testEvent.getEvent().getVaccineCvx()
                  + "\" codeSystem=\"2.16.840.1.113883.12.292\" displayName=\"" + fix(testEvent.getEvent().getLabel())
                  + "\" originalText=\"" + testEvent.getEvent().getVaccineCvx() + "\"/>\n");
              sb.append("                        </substance>\n");
              sb.append(
                  "                        <administrationTimeInterval low=\"" + sdf.format(testEvent.getEventDate())
                      + "\" high=\"" + sdf.format(testEvent.getEventDate()) + "\"/>\n");
              sb.append("                    </substanceAdministrationEvent>\n");
            }
            sb.append("                </substanceAdministrationEvents>\n");
            // List of Vaccines Administered Ends
          }
          sb.append("            </clinicalStatements>\n");
          sb.append("        </patient>\n");
          // Patient Input Section Ends -->
        }
        sb.append("    </vmrInput>\n");
        // VMR Input Section Ends
      }
      sb.append("</ns4:cdsInput>\n");
      // CDSInput Section Ends
    }
    // Message Ends

    return sb.toString();
  }

  protected static String fix(String s) {
    return s.replaceAll("\\&", "&amp;").replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;").replaceAll("\\'", "&apos;")
        .replaceAll("\\\"", "&quot;");
  }
}
