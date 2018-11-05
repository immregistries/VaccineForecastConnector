package org.tch.fc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.tch.fc.model.Admin;
import org.tch.fc.model.EvaluationActual;
import org.tch.fc.model.Event;
import org.tch.fc.model.ForecastActual;
import org.tch.fc.model.ForecastEngineIssue;
import org.tch.fc.model.Software;
import org.tch.fc.model.SoftwareResult;
import org.tch.fc.model.SoftwareResultStatus;
import org.tch.fc.model.TestCase;
import org.tch.fc.model.TestEvent;
import org.tch.fc.model.VaccineGroup;
import org.tch.fc.util.FakePatient;
import static org.tch.fc.model.ForecastEngineIssueLevel.*;
import static org.tch.fc.model.ForecastEngineIssueType.*;

public class IISConnector implements ConnectorInterface {
  private Software software = null;
  private boolean logText = false;
  private String uniqueId;

  public IISConnector(Software software, List<VaccineGroup> forecastItemList) {
    this.software = software;
    this.forecastItemList = forecastItemList;
    map("10", VaccineGroup.ID_POLIO);
    map("010", VaccineGroup.ID_POLIO);
    map("017", VaccineGroup.ID_HIB);
    map("16", VaccineGroup.ID_INFLUENZA);
    map("22", VaccineGroup.ID_HIB);
    map("28", VaccineGroup.ID_DTAP);
    map("28", VaccineGroup.ID_DTAP_TDAP_TD);
    map("028", VaccineGroup.ID_DTAP);
    map("028", VaccineGroup.ID_DTAP_TDAP_TD);
    map("31", VaccineGroup.ID_HEPA);
    map("033", VaccineGroup.ID_PPSV);
    map("42", VaccineGroup.ID_HEPB);
    map("045", VaccineGroup.ID_HEPB);
    map("48", VaccineGroup.ID_HIB);
    map("52", VaccineGroup.ID_HEPA);
    map("062", VaccineGroup.ID_HPV);
    map("83", VaccineGroup.ID_HEPA);
    map("085", VaccineGroup.ID_HEPA);
    map("088", VaccineGroup.ID_INFLUENZA);
    map("100", VaccineGroup.ID_PNEUMO);
    map("106", VaccineGroup.ID_DTAP);
    map("106", VaccineGroup.ID_DTAP_TDAP_TD);
    map("107", VaccineGroup.ID_DTAP);
    map("107", VaccineGroup.ID_DTAP_TDAP_TD);
    map("109", VaccineGroup.ID_PNEUMO);
    map("114", VaccineGroup.ID_MENING);
    map("116", VaccineGroup.ID_ROTA);
    map("133", VaccineGroup.ID_PCV);
    map("133", VaccineGroup.ID_PNEUMO);
    map("141", VaccineGroup.ID_INFLUENZA);
    map("147", VaccineGroup.ID_MENING);
    map("150", VaccineGroup.ID_INFLUENZA);
    map("150", VaccineGroup.ID_INFLUENZA_IIV);
    map("164", VaccineGroup.ID_MENINGB);
    map("164", VaccineGroup.ID_MENING);
    map("165", VaccineGroup.ID_HPV);
    map("HEP B", VaccineGroup.ID_HEPB); // FL SHOTS - HEP B^HEPATITIS B
    map("HIB", VaccineGroup.ID_HIB); // FL SHOTS - HIB^H INFLUENZA TYPE B
    map("POLIO", VaccineGroup.ID_POLIO); // FL SHOTS - POLIO^POLIO
    map("VZV", VaccineGroup.ID_VAR); // FL SHOTS - VZV^CHICKEN POX
    map("PNEUCON", VaccineGroup.ID_PNEUMO); // FL SHOTS - PNEUCON^PNEUMOCOCCAL CONJUGATE
    map("MEASLES", VaccineGroup.ID_MEASLES_ONLY); // FL SHOTS - MEASLES^MEASLES
    map("MEASLES", VaccineGroup.ID_MMR); // FL SHOTS - MEASLES^MEASLES
    map("MUMPS", VaccineGroup.ID_MUMPS_ONLY); // FL SHOTS - MUMPS^MUMPS
    map("RUBELLA", VaccineGroup.ID_RUBELLA_ONLY); // FL SHOTS - RUBELLA^RUBELLA
    map("ROTAVIRUS", VaccineGroup.ID_ROTA); // FL SHOTS - ROTAVIRUS^ROTAVIRUS
    map("HPV", VaccineGroup.ID_HPV); // FL SHOTS - HPV^HPV
    map("DIPHTHERIA", VaccineGroup.ID_DTAP); // FL SHOTS - DIPHTHERIA^DIPHTHERIA
    map("DIPHTHERIA", VaccineGroup.ID_DTAP_TDAP_TD); // FL SHOTS - DIPHTHERIA^DIPHTHERIA
    // map("PERTUSSIS", VaccineGroup.ID_); // FL SHOTS - PERTUSSIS^PERTUSSIS
    // map("TETANUS", VaccineGroup.ID); // FL SHOTS - TETANUS^TETANUS
    // IL I-CARE - 
    for (VaccineGroup forecastItem : forecastItemList) {
      map(forecastItem.getVaccineCvx(), forecastItem.getVaccineGroupId());
    }
  }

  private List<VaccineGroup> forecastItemList = null;
  private Map<String, List<VaccineGroup>> familyMapping = new HashMap<String, List<VaccineGroup>>();

  public Map<String, List<VaccineGroup>> getFamilyMapping() {
    return familyMapping;
  }

  private void map(String familyName, int forecastItemId) {
    for (VaccineGroup forecastItem : forecastItemList) {
      if (forecastItem.getVaccineGroupId() == forecastItemId) {
        List<VaccineGroup> forecastItemListFromMap = familyMapping.get(familyName);
        if (forecastItemListFromMap == null) {
          forecastItemListFromMap = new ArrayList<VaccineGroup>();
          familyMapping.put(familyName, forecastItemListFromMap);
        }
        forecastItemListFromMap.add(forecastItem);
        return;
      }
    }
  }

  public List<ForecastActual> queryForForecast(TestCase testCase, SoftwareResult softwareResult)
      throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    StringWriter sw = new StringWriter();
    PrintWriter logOut = logText ? new PrintWriter(sw) : null;

    if (logText) {
      logOut.println(
          "This service will attempt to send a fake VXU with the vaccination history and then request the forecast back using a QBP. ");
    }
    try {
      createUniqueId(testCase.getTestCaseNumber());

      FakePatient fakePatient = new FakePatient(testCase, uniqueId);
      String vxu = buildVXU(testCase, fakePatient);
      if (logText) {
        logOut.println();
        logOut.println("VXU SENT: ");
        logOut.println(vxu);
      }
      String ack = sendRequest(vxu);
      if (logText) {
        logOut.println();
        logOut.println("ACK received back ...");
        logOut.println(ack);
      }

      String qbp = buildQBP(fakePatient);
      if (logText) {
        logOut.println();
        logOut.println("Sending QBP ...");
        logOut.println(qbp);
      }
      boolean lookingForMatch = true;
      int delay = 0;
      while (lookingForMatch) {
        String rsp = sendRequest(qbp);
        if (logText) {
          logOut.println();
          logOut.println("RSP received back ...");
          logOut.println(rsp);
        }
        readRSP(forecastActualList, testCase, softwareResult, rsp);
        if (softwareResult.getSoftwareResultStatus() == SoftwareResultStatus.NOT_FOUND) {
          if (delay == 0) {
            delay = 10;
          } else if (delay == 10) {
            delay = 20;
          } else if (delay == 20) {
            delay = 30;
          } else if (delay == 30) {
            lookingForMatch = false;
          }
        } else {
          lookingForMatch = false;
        }
        if (lookingForMatch) {
          synchronized (this) {
            this.wait(delay * 1000);
          }
        }
      }
    } catch (NotAuthenticated na) {
      if (logOut != null) {
        logOut.println("Unable to authenticate");
      }
      softwareResult.setSoftwareResultStatus(SoftwareResultStatus.NOT_AUTHENTICATED);
    } catch (Exception e) {
      softwareResult.setSoftwareResultStatus(SoftwareResultStatus.PROBLEM);
      if (logOut != null) {
        logOut.println("Unable to get forecast results from IIS");
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
    return forecastActualList;
  }

  private static char[] encoding = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
      'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
      'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',};

  private void createUniqueId(String testId) {
    String num = "" + System.currentTimeMillis();
    num = nextIncrement() + num.substring(num.length() - 10);
    long i = Long.parseLong(num);
    num = "";
    while (i >= encoding.length) {
      int r = (int) (i % encoding.length);
      num = encoding[r] + num;
      i = i / encoding.length;
    }
    num = encoding[(int) i] + num;
    if (testId.length() > 9) {
      testId = testId.substring(testId.length() - 9);
    }
    if (num.length() > 6) {
      num = num.substring(num.length() - 6);
    }
    uniqueId = testId + num;
  }

  public static enum ParseDebugStatus {
                                       OK,
                                       OK_FORECAST,
                                       OK_EVALUATION,
                                       EXPECTED_BUT_NOT_READ,
                                       NOT_READ,
                                       PROBLEM
  }

  public static class ParseDebugLine {
    protected ParseDebugLine(String line) {
      this.line = line;
    }

    private String line;
    private String segmentName;
    private ParseDebugStatus lineStatus = ParseDebugStatus.NOT_READ;
    private String lineStatusReason = "";
    private String obsCode;
    private ParseDebugStatus obsCodeStatus = ParseDebugStatus.NOT_READ;
    private String obsValue;
    private ParseDebugStatus obsValueStatus = ParseDebugStatus.NOT_READ;
    private String cvxCode = "";
    private Date adminDate = null;
    private TestEvent testEvent = null;
    private EvaluationActual evaluationActual = null;
    private ArrayList<ForecastActual> forecastActualList = null;

    public String getLineStatusReason() {
      return lineStatusReason;
    }

    public void setLineStatusReason(String lineStatusReason) {
      this.lineStatusReason = lineStatusReason;
    }

    public String getCvxCode() {
      return cvxCode;
    }

    public void setCvxCode(String cvxCode) {
      this.cvxCode = cvxCode;
    }

    public Date getAdminDate() {
      return adminDate;
    }

    public void setAdminDate(Date adminDate) {
      this.adminDate = adminDate;
    }

    public TestEvent getTestEvent() {
      return testEvent;
    }

    public void setTestEvent(TestEvent testEvent) {
      this.testEvent = testEvent;
    }

    public EvaluationActual getEvaluationActual() {
      return evaluationActual;
    }

    public void setEvaluationActual(EvaluationActual evaluationActual) {
      this.evaluationActual = evaluationActual;
    }

    public ArrayList<ForecastActual> getForecastActual() {
      return forecastActualList;
    }

    public void setForecastActual(ArrayList<ForecastActual> forecastActualList) {
      this.forecastActualList = forecastActualList;
    }

    public String getSegmentName() {
      return segmentName;
    }

    public void setSegmentName(String segmentName) {
      this.segmentName = segmentName;
    }

    public ParseDebugStatus getLineStatus() {
      return lineStatus;
    }

    public void setLineStatus(ParseDebugStatus lineStatus) {
      this.lineStatus = lineStatus;
    }

    public String getObsCode() {
      return obsCode;
    }

    public void setObsCode(String obsCode) {
      this.obsCode = obsCode;
    }

    public ParseDebugStatus getObsCodeStatus() {
      return obsCodeStatus;
    }

    public void setObsCodeStatus(ParseDebugStatus obsCodeStatus) {
      this.obsCodeStatus = obsCodeStatus;
    }

    public String getObsValue() {
      return obsValue;
    }

    public void setObsValue(String obsValue) {
      this.obsValue = obsValue;
    }

    public ParseDebugStatus getObsValueStatus() {
      return obsValueStatus;
    }

    public void setObsValueStatus(ParseDebugStatus obsValueStatus) {
      this.obsValueStatus = obsValueStatus;
    }

    public String getLine() {
      return line;
    }

  }

  public void readRSP(List<ForecastActual> forecastActualList, TestCase testCase,
      SoftwareResult softwareResult, String rsp) throws IOException, ParseException {
    readRSP(forecastActualList, testCase, softwareResult, rsp, null);
  }

  public void readRSP(List<ForecastActual> forecastActualList, TestCase testCase,
      SoftwareResult softwareResult, String rsp, List<ParseDebugLine> parseDebugLineList)
      throws IOException, ParseException {
    SoftwareResultStatus softwareResultStatus = SoftwareResultStatus.OK;
    List<ForecastEngineIssue> issuesList = softwareResult.getIssueList();
    BufferedReader in = new BufferedReader(new StringReader(rsp));
    String line;
    List<ForecastActual> fal = null;
    TestEvent testEvent = null;
    EvaluationActual evaluationActual = null;
    String cvxCodeInRxa = "";
    String adminDateInRxa = "";
    ParseDebugLine parseDebugLine = null;
    while ((line = in.readLine()) != null) {
      if (parseDebugLineList != null) {
        parseDebugLine = new ParseDebugLine(line);
        parseDebugLineList.add(parseDebugLine);
      }
      String[] f = line.split("\\|");

      if (f == null || f.length <= 1 || f[0] == null || f[0].length() != 3) {
        if (parseDebugLine != null) {
          parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
          parseDebugLine
              .setLineStatusReason("HL7 segment has too few fields, or is not formatted properly");
        }
        issuesList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, WARNING,
            "Invalid HL7 format found in segment"));
        continue;
      }
      {
        String segmentName = f[0];
        if (parseDebugLine != null) {
          parseDebugLine.setSegmentName(segmentName);
        }
        if (segmentName.equals("BHS") || segmentName.equals("FHS") || segmentName.equals("FTS")
            || segmentName.equals("BTS") || segmentName.equals("MSH") || segmentName.equals("MSA")
            || segmentName.equals("ERR") || segmentName.equals("QPD") || segmentName.equals("PID")
            || segmentName.equals("ORC") || segmentName.equals("RXR") || segmentName.equals("NK1")
            || segmentName.equals("PD1")) {
          if (parseDebugLine != null) {
            parseDebugLine.setLineStatus(ParseDebugStatus.EXPECTED_BUT_NOT_READ);
          }
        } else if (segmentName.equals("QAK")) {
          String queryResponseStatus = readFieldValue(f, 2);
          if (queryResponseStatus.equals("NF")) {
            softwareResultStatus = SoftwareResultStatus.NOT_FOUND;
          }
          if (parseDebugLine != null) {
            parseDebugLine.setLineStatus(ParseDebugStatus.OK);
          }
        } else if (segmentName.equals("RXA")) {
          adminDateInRxa = readFieldValue(f, 3);
          cvxCodeInRxa = readFieldValue(f, 5);
          testEvent = null;
          evaluationActual = null;
          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
          if (testCase.getTestEventList() != null) {
            for (TestEvent te : testCase.getTestEventList()) {
              if (te.getEvent().getVaccineCvx().equals(cvxCodeInRxa) && te.getEventDate() != null
                  && sdf.format(te.getEventDate()).equals(adminDateInRxa)) {
                testEvent = te;
                break;
              }
            }
          }
          if (parseDebugLine != null) {
            try {
              parseDebugLine.setAdminDate(sdf.parse(adminDateInRxa));
            } catch (ParseException pe) {
              // ignore
            }
            parseDebugLine.setCvxCode(cvxCodeInRxa);
            parseDebugLine.setTestEvent(testEvent);
            parseDebugLine.setLineStatus(ParseDebugStatus.OK);
          }
        } else if (segmentName.equals("OBX")) {
          String obsCode = readValue(f, 3);
          String obsValue = readValue(f, 5);
          String obsLabel = readValue(f, 5, 2);
          if (cvxCodeInRxa == null) {
            if (parseDebugLine != null) {
              parseDebugLine.setLineStatus(ParseDebugStatus.NOT_READ);
              parseDebugLine
                  .setLineStatusReason("OBX found before a valid RXA segment, unable to read");
            }
            continue;
          } else if (cvxCodeInRxa.equals("998")) {
            fal = handleForecast(forecastActualList, fal, parseDebugLine, obsCode, obsValue,
                obsLabel, softwareResult);
          } else {
            evaluationActual = handleEvaluation(softwareResult, testEvent, evaluationActual,
                parseDebugLine, obsCode, obsValue);
          }

        } else {
          if (parseDebugLine != null) {
            parseDebugLine.setLineStatus(ParseDebugStatus.NOT_READ);
            parseDebugLine.setLineStatusReason("Unrecognized segment");
          }
        }

      }
    }
    softwareResult.setSoftwareResultStatus(softwareResultStatus);
  }

  private EvaluationActual handleEvaluation(SoftwareResult softwareResult, TestEvent testEvent,
      EvaluationActual evaluationActual, ParseDebugLine parseDebugLine, String obsCode,
      String obsValue) {
    List<ForecastEngineIssue> issuesList = softwareResult.getIssueList();
    if (obsCode.equals("30956-7") || obsCode.equals("38890-0") || obsCode.equals("59780-7")) {
      if (testEvent == null) {
        if (parseDebugLine != null) {
          parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
          parseDebugLine.setLineStatusReason("No test event was found, unable to link");
        }
        issuesList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, ERROR,
            "OBX " + obsCode + " found, but not able to link with vaccine administered"));
      } else {
        evaluationActual = new EvaluationActual();
        evaluationActual.setSoftwareResult(softwareResult);
        evaluationActual.getSoftwareResult().setSoftware(software);
        evaluationActual.setTestEvent(testEvent);
        evaluationActual.setVaccineCvx(obsValue);
        evaluationActual.setSeriesUsedCode(obsValue);

        if (testEvent.getEvaluationActualList() == null) {
          testEvent.setEvaluationActualList(new ArrayList<EvaluationActual>());
        }
        testEvent.getEvaluationActualList().add(evaluationActual);
        if (parseDebugLine != null) {
          SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
          parseDebugLine.setLineStatus(ParseDebugStatus.OK_EVALUATION);
          if (testEvent.getEvent() != null && testEvent.getEventDate() != null) {
            parseDebugLine.setLineStatusReason("CVX " + testEvent.getEvent().getVaccineCvx() + " - "
                + sdf.format(testEvent.getEventDate()));
          }
        }
      }
    } else if (obsCode.equals("59781-5")) {
      if (evaluationActual == null) {
        logEvaluationReasonNotSetup(parseDebugLine, issuesList);
      } else {
        evaluationActual.setDoseValid(obsValue);
        if (parseDebugLine != null) {
          parseDebugLine.setLineStatus(ParseDebugStatus.OK);
        }
      }
    } else if (obsCode.equals("30982-3")) {
      if (evaluationActual == null) {
        logEvaluationReasonNotSetup(parseDebugLine, issuesList);
      } else {
        evaluationActual.setReasonText(obsValue);
        if (parseDebugLine != null) {
          parseDebugLine.setLineStatus(ParseDebugStatus.OK);
        }
      }
    } else if (obsCode.equals("30973-2")) {
      if (evaluationActual == null) {
        logEvaluationReasonNotSetup(parseDebugLine, issuesList);
      } else {
        evaluationActual.setDoseNumber(obsValue);
        if (parseDebugLine != null) {
          parseDebugLine.setLineStatus(ParseDebugStatus.OK);
        }
      }
    } else if (obsCode.equals("59779-9")) {
      if (parseDebugLine != null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.EXPECTED_BUT_NOT_READ);
      }
    } else if (obsCode.equals("29769-7") || obsCode.equals("VFC-STATUS")
        || obsCode.equals("64994-7") || obsCode.equals("29768-9")) {
      if (parseDebugLine != null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.EXPECTED_BUT_NOT_READ);
      }
    } else {
      if (parseDebugLine != null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.NOT_READ);
      }
    }
    // evaluationActual.setDoseNumber(doseNumber);
    // evaluationActual.setEvaluationReason(evaluationReason);
    // evaluationActual.setReasonCode(reasonCode);
    // evaluationActual.setSeriesUsedText(doseName);
    return evaluationActual;
  }

  private List<ForecastActual> handleForecast(List<ForecastActual> forecastActualList,
      List<ForecastActual> fal, ParseDebugLine parseDebugLine, String obsCode, String obsValue,
      String obsLabel, SoftwareResult softwareResult) {
    List<ForecastEngineIssue> issueList = softwareResult.getIssueList();
    if (obsCode.equals("30956-7") || obsCode.equals("30979-9") || obsCode.equals("59780-7")) {
      List<VaccineGroup> forecastItemListFromMap = familyMapping.get(obsValue);
      fal = new ArrayList<ForecastActual>();
      if (forecastItemListFromMap != null) {
        for (VaccineGroup vaccineGroup : forecastItemListFromMap) {
          ForecastActual forecastActual = new ForecastActual();
          forecastActualList.add(forecastActual);
          fal.add(forecastActual);
          forecastActual.setVaccineGroup(vaccineGroup);
          forecastActual.setVaccineCvx(obsValue);
        }
      }
      if (fal.size() == 0) {
        if (parseDebugLine != null) {
          parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
          parseDebugLine
              .setLineStatusReason("Unable to find mapping to one ore more vaccine groups");
        }
        issueList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, WARNING,
            "Unable to find mapping to one ore more vaccine groups"));
      } else {
        if (parseDebugLine != null) {
          parseDebugLine.setLineStatus(ParseDebugStatus.OK_FORECAST);
          String s = "";
          boolean first = true;
          for (ForecastActual fa : fal) {
            if (!first) {
              s += " / ";
            }
            s += fa.getVaccineGroup().getLabel();
            first = false;
          }
          parseDebugLine.setLineStatusReason(s);
        }
      }
    } else if (obsCode.equals("59783-1")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList);
      } else {
        Admin admin = mapAdmin(obsValue, obsLabel);
        if (parseDebugLine != null) {
          if (admin == Admin.UNKNOWN) {
            parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
            parseDebugLine.setLineStatusReason("Unrecognized value received ");
          } else {
            parseDebugLine.setLineStatus(ParseDebugStatus.OK);
          }
        }
        if (admin == Admin.UNKNOWN) {
          issueList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, WARNING,
              "Unrecognized series status code: " + obsValue));
        }
        for (ForecastActual forecastActual : fal) {
          forecastActual.setAdmin(admin);
        }
      }
    } else if (obsCode.equals("30981-5")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList);
      } else {
        if (fal.size() == 0) {
          logNotRead(parseDebugLine);
        } else {
          Date date = parseDate(obsValue);
          logDateNotParsedProblem(parseDebugLine, date, issueList);
          for (ForecastActual forecastActual : fal) {
            forecastActual.setValidDate(date);
          }
        }
      }
    } else if (obsCode.equals("30980-7")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList);
      } else {
        if (fal.size() == 0) {
          logNotRead(parseDebugLine);
        } else {
          Date date = parseDate(obsValue);
          logDateNotParsedProblem(parseDebugLine, date, issueList);
          for (ForecastActual forecastActual : fal) {
            forecastActual.setDueDate(date);
          }
        }
      }
    } else if (obsCode.equals("59777-3")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList);
      } else {
        if (fal.size() == 0) {
          logNotRead(parseDebugLine);
        } else {
          Date date = parseDate(obsValue);
          logDateNotParsedProblem(parseDebugLine, date, issueList);
          for (ForecastActual forecastActual : fal) {
            forecastActual.setFinishedDate(date);
          }
        }
      }
    } else if (obsCode.equals("59778-1")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList);
      } else {
        if (fal.size() == 0) {
          logNotRead(parseDebugLine);
        } else {
          Date date = parseDate(obsValue);
          logDateNotParsedProblem(parseDebugLine, date, issueList);
          for (ForecastActual forecastActual : fal) {
            forecastActual.setOverdueDate(date);
          }
        }
      }
    } else if (obsCode.equals("30973-2")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList);
      } else {
        if (fal.size() == 0) {
          logNotRead(parseDebugLine);
        } else {
          if (parseDebugLine != null) {
            parseDebugLine.setLineStatus(ParseDebugStatus.OK);
          }
          for (ForecastActual forecastActual : fal) {
            forecastActual.setDoseNumber(obsValue);
          }
        }
      }
    } else if (obsCode.equals("30982-3")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList);
      } else {
        if (fal.size() == 0) {
          logNotRead(parseDebugLine);
        } else {
          if (parseDebugLine != null) {
            parseDebugLine.setLineStatus(ParseDebugStatus.OK);
          }
          for (ForecastActual forecastActual : fal) {
            forecastActual.setForecastReason(obsValue);
          }
        }
      }

    } else if (obsCode.equals("59779-9")) {
      if (parseDebugLine != null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.EXPECTED_BUT_NOT_READ);
      }
    } else {
      if (parseDebugLine != null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.NOT_READ);
      }
    }
    return fal;
  }

  private void logEvaluationReasonNotSetup(ParseDebugLine parseDebugLine,
      List<ForecastEngineIssue> issuesList) {
    if (parseDebugLine != null) {
      parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
      parseDebugLine.setLineStatusReason("No evaluation actual setup");
    }
    issuesList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, WARNING,
        "Evaluation was not setup properly, cannot link concept to evaluation "));
  }

  private void logDateNotParsedProblem(ParseDebugLine parseDebugLine, Date date,
      List<ForecastEngineIssue> issuesList) {
    if (date == null) {
      if (parseDebugLine != null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
        parseDebugLine.setLineStatusReason("Date not parseable");
      }
      issuesList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, ERROR, "Date not parseable"));
    } else {
      if (parseDebugLine != null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.OK);
      }
    }
  }

  private void logOutofSequence(ParseDebugLine parseDebugLine,
      List<ForecastEngineIssue> issuesList) {
    if (parseDebugLine != null) {
      parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
      parseDebugLine.setLineStatusReason(
          "Missing a previous OBX that defines the vaccines being forecast for");
      issuesList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, ERROR,
          "Missing a previous OBX that defines the vaccines being forecast for"));
    }
  }

  private void logNotRead(ParseDebugLine parseDebugLine) {
    if (parseDebugLine != null) {
      parseDebugLine.setLineStatus(ParseDebugStatus.NOT_READ);
    }
  }

  private static String readFieldValue(String[] f, int p) {
    String s;
    if (f.length > p && f[p] != null) {
      s = f[p];
    } else {
      s = "";
    }
    int pos = s.indexOf("^");
    if (pos > 0) {
      s = s.substring(0, pos).trim();
    }
    return s;
  }

  private static final Map<String, Admin> adminStatusMap = new HashMap<String, Admin>();
  private static final Map<String, Admin> adminStatusLabelMap = new HashMap<String, Admin>();

  public static Map<String, Admin> getAdminstatusmap() {
    return adminStatusMap;
  }

  public static Map<String, Admin> getAdminstatuslabelmap() {
    return adminStatusLabelMap;
  }

  static {
    // Here are the official codes to map to
    //    Admin.AGED_OUT;
    //    Admin.COMPLETE;
    //    Admin.CONTRAINDICATED;
    //    Admin.IMMUNE;
    //    Admin.NOT_COMPLETE;
    //    Admin.NOT_RECOMMENDED;
    adminStatusMap.put("LA13423-1", Admin.OVERDUE); // Envision LA13423-1^Overdue^LN
    adminStatusMap.put("LA13422-3", Admin.NOT_COMPLETE); // Envision LA13422-3^On Schedule^LN
    adminStatusMap.put("LA13421-5", Admin.COMPLETE); // AL_ImmPrint LA13421-5^Not Recommended - Series Complete^LN
    adminStatusMap.put("LA13424-9", Admin.AGED_OUT); // AL_ImmPrint LA13424-9^Not Recommended - Too old^LN
    adminStatusMap.put("P", Admin.NOT_COMPLETE); // STC P^Past Due^STC0002
    adminStatusMap.put("D", Admin.NOT_COMPLETE); // STC D^Due Now^STC0002
    adminStatusMap.put("U", Admin.NOT_COMPLETE); // STC U^Up to Date^STC0002
    adminStatusMap.put("Overdue", Admin.NOT_COMPLETE); // FL SHOTS - Overdue^Overdue
    adminStatusMap.put("Up to Date", Admin.NOT_COMPLETE); // FL SHOTS - Up to Date^Up to Dat
    adminStatusMap.put("Complete", Admin.COMPLETE); // FL SHOTS - Complete^Complete

    adminStatusLabelMap.put("OVERDUE", Admin.NOT_COMPLETE); // MI MCIR - 4^Overdue^eval_result_id
    adminStatusLabelMap.put("COMPLETE", Admin.COMPLETE); // MI MCIR - 1^Complete^eval_result_id
  }

  private static Admin mapAdmin(String value, String label) {
    Admin admin = Admin.UNKNOWN;
    if (value != null) {
      admin = adminStatusMap.get(value);
      if (admin == null && label != null & !label.equals("")) {
        label = label.toUpperCase();
        admin = adminStatusLabelMap.get(label);
      }
    }
    return admin;
  }

  private Date parseDate(String value) {
    if (value.length() > 8) {
      value = value.substring(0, 8);
    }
    if (value.length() == 8) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      try {
        return sdf.parse(value);
      } catch (ParseException e) {
        return null;
      }
    }
    return null;
  }

  private String readValue(String[] f, int pos) {
    String s = f[pos];
    int i = s.indexOf("^");
    if (i > 0) {
      s = s.substring(0, i);
    }
    return s;
  }

  private String readValue(String[] f, int pos, int subpos) {
    String s = f[pos];
    while (subpos > 1) {
      int i = s.indexOf("^");
      if (i == -1) {
        return "";
      }
      s = s.substring(i + 1);
      subpos--;
    }
    int i = s.indexOf("^");
    if (i > 0) {
      s = s.substring(0, i);
    }
    return s;
  }

  private static Integer increment = new Integer(1);

  private static int nextIncrement() {
    synchronized (increment) {
      if (increment < Integer.MAX_VALUE) {
        increment = increment + 1;
      } else {
        increment = 1;
      }
      return increment;
    }
  }

  private void createMSHforVXU(StringBuilder sb) {
    String sendingApp = software.getServiceFacilityid();
    String sendingFac = software.getServiceFacilityid();
    String receivingApp = "";
    String receivingFac = "";
    String sendingDateString;
    {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmssZ");
      sendingDateString = simpleDateFormat.format(new Date());
    }
    // build MSH
    sb.append("MSH|^~\\&|");
    sb.append(receivingApp + "|");
    sb.append(receivingFac + "|");
    sb.append(sendingApp + "|");
    sb.append(sendingFac + "|");
    sb.append(sendingDateString + "|");
    sb.append("|");
    sb.append("VXU^V04^VXU_V04|");
    sb.append(uniqueId + "|");
    sb.append("P|");
    sb.append("2.5.1|");
    sb.append("|");
    sb.append("|");
    sb.append("ER|");
    sb.append("AL|");
    sb.append("|");
    sb.append("|");
    sb.append("|");
    sb.append("|");
    sb.append("Z22^CDCPHINVS\r");
  }

  private void createMSHforQBP(StringBuilder sb) {
    String sendingApp = software.getServiceFacilityid();
    String sendingFac = software.getServiceFacilityid();
    String receivingApp = "";
    String receivingFac = "";
    String sendingDateString;
    {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmssZ");
      sendingDateString = simpleDateFormat.format(new Date());
    }
    // build MSH
    sb.append("MSH|^~\\&|");
    sb.append(receivingApp + "|");
    sb.append(receivingFac + "|");
    sb.append(sendingApp + "|");
    sb.append(sendingFac + "|");
    sb.append(sendingDateString + "|");
    sb.append("|");
    sb.append("QBP^Q11^QBP_Q11|");
    sb.append(uniqueId + "|");
    sb.append("P|");
    sb.append("2.5.1|");
    sb.append("|");
    sb.append("|");
    sb.append("ER|");
    sb.append("AL|");
    sb.append("|");
    sb.append("|");
    sb.append("|");
    sb.append("|");
    sb.append("Z44^CDCPHINVS\r");
  }

  public void printORC(StringBuilder sb, int count) {
    sb.append("ORC");
    // ORC-1
    sb.append("|RE");
    // ORC-2
    sb.append("|");
    // ORC-3
    sb.append("|");
    sb.append(uniqueId + "." + count + "^FITS");
    sb.append("\r");
  }

  public String buildVXU(TestCase testCase, FakePatient fakePatient) {

    StringBuilder sb = new StringBuilder();
    createMSHforVXU(sb);
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      // PID
      sb.append("PID");
      // PID-1
      sb.append("|1");
      // PID-2
      sb.append("|");
      // PID-3
      sb.append("|" + fakePatient.getMrn() + "^^^FITS^MR");
      // PID-4
      sb.append("|");
      // PID-5
      sb.append("|" + fakePatient.getNameLast() + "^" + fakePatient.getNameFirst() + "^"
          + fakePatient.getNameMiddle() + "^^^^L");
      // PID-6
      sb.append("|" + fakePatient.getMaidenLast() + "^" + fakePatient.getMaidenFirst() + "^^^^^M");
      // PID-7
      sb.append("|" + sdf.format(fakePatient.getPatientDob()));
      {
        // PID-8
        sb.append("|" + (fakePatient.getPatientSex().toUpperCase().equals("M") ? "M" : "F"));
        // PID-9
        sb.append("|");
        // PID-10
        sb.append("|");
        // PID-11
        sb.append("|" + fakePatient.getAddressLine1() + "^^" + fakePatient.getAddressCity() + "^"
            + fakePatient.getAddressState() + "^" + fakePatient.getAddressZip() + "^USA");
        // PID-12
        sb.append("|");
        // PID-13
        sb.append("|");
        if (fakePatient.getPhone().length() == 10) {
          sb.append("^PRN^PH^^^" + fakePatient.getPhone().substring(0, 3) + "^"
              + fakePatient.getPhone().substring(3, 10));
        }
      }
      sb.append("\r");

      sb.append("NK1");
      sb.append("|1");
      sb.append("|" + fakePatient.getMotherLast() + "^" + fakePatient.getMotherFirst() + "^^^^^L");
      sb.append("|MTH^Mother^HL70063");
      sb.append("\r");
      int count = 0;
      for (TestEvent testEvent : testCase.getTestEventList()) {
        count++;
        printORC(sb, count);
        sb.append("RXA");
        // RXA-1
        sb.append("|0");
        // RXA-2
        sb.append("|1");
        // RXA-3
        sb.append("|" + sdf.format(testEvent.getEventDate()));
        // RXA-4
        sb.append("|");
        // RXA-5
        String cvxLabel = testEvent.getEvent().getLabel();
        if (cvxLabel == null || cvxLabel.equals("")) {
          cvxLabel = testEvent.getEvent().getVaccineCvx();
        }
        sb.append("|" + testEvent.getEvent().getVaccineCvx() + "^" + cvxLabel + "^CVX");
        {
          // RXA-6
          sb.append("|");
          sb.append("999");
          // RXA-7
          sb.append("|");
        }

        // RXA-8
        sb.append("|");
        // RXA-9
        sb.append("|");
        sb.append("01");
        // RXA-10
        sb.append("|");
        // RXA-11
        sb.append("|");
        // RXA-12
        sb.append("|");
        // RXA-13
        sb.append("|");
        // RXA-14
        sb.append("|");
        // RXA-15
        sb.append("|");
        // RXA-16
        sb.append("|");
        // RXA-17
        sb.append("|");
        sb.append(testEvent.getEvent().getVaccineMvx() + "^" + testEvent.getEvent().getVaccineMvx()
            + "^MVX");
        // RXA-18
        sb.append("|");
        // RXA-19
        sb.append("|");
        // RXA-20
        sb.append("|");
        // RXA-21
        sb.append("|A");
        sb.append("\r");
      }
    }

    return sb.toString();
  }

  public String buildQBP(FakePatient fakePatient) {

    StringBuilder sb = new StringBuilder();
    createMSHforQBP(sb);
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      // QPD
      sb.append("QPD");
      // QPD-1
      sb.append("|Z44^Request Evaluated History and Forecast^CDCPHINVS");
      // QPD-2
      sb.append("|" + uniqueId);
      // QPD-3
      sb.append("|" + fakePatient.getMrn() + "^^^FITS^MR");
      // QPD-4
      sb.append("|" + fakePatient.getNameLast() + "^" + fakePatient.getNameFirst() + "^"
          + fakePatient.getNameMiddle() + "^^^^L");
      // QPD-5
      sb.append("|" + fakePatient.getMaidenLast() + "^" + fakePatient.getMaidenFirst() + "^^^^^M");
      // QPD-6
      sb.append("|" + sdf.format(fakePatient.getPatientDob()));
      // QPD-7
      sb.append("|" + fakePatient.getPatientSex());
      // PID-8
      sb.append("|" + fakePatient.getAddressLine1() + "^^" + fakePatient.getAddressCity() + "^"
          + fakePatient.getAddressState() + "^" + fakePatient.getAddressZip() + "^USA^P");
      sb.append("\r");
      sb.append("RCP|I|1^RD&Records&HL70126");
      sb.append("\r");
    }
    return sb.toString();
  }

  public boolean isLogText() {
    return logText;
  }

  public void setLogText(boolean logText) {
    this.logText = logText;
  }

  @SuppressWarnings("serial")
  private class NotAuthenticated extends Exception {

  }

  private String sendRequest(String request) throws IOException, NotAuthenticated {
    URLConnection urlConn;
    DataOutputStream printout;
    InputStreamReader input = null;
    URL url = new URL(software.getServiceUrl());
    urlConn = url.openConnection();
    urlConn.setDoInput(true);
    urlConn.setDoOutput(true);
    urlConn.setUseCaches(false);
    urlConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
    urlConn.setRequestProperty("SOAPAction", "\"http://tempuri.org/ExecuteHL7Message\"");
    printout = new DataOutputStream(urlConn.getOutputStream());
    StringWriter stringWriter = new StringWriter();
    PrintWriter out = new PrintWriter(stringWriter);
    request = request.replaceAll("\\&", "&amp;");
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("   <Header />");
    out.println("   <Body>");
    out.println("      <submitSingleMessage xmlns=\"urn:cdc:iisb:2011\">");
    out.println("         <username>" + software.getServiceUserid() + "</username>");
    out.println("         <password>" + software.getServicePassword() + "</password>");
    out.println("         <facilityID>" + software.getServiceFacilityid() + "</facilityID>");
    out.println("         <hl7Message>" + request + "</hl7Message>");
    out.println("      </submitSingleMessage>");
    out.println("   </Body>");
    out.println("</Envelope>");

    printout.writeBytes(stringWriter.toString());
    printout.flush();
    printout.close();
    input = new InputStreamReader(urlConn.getInputStream());
    StringBuilder response = new StringBuilder();
    BufferedReader in = new BufferedReader(input);
    String line;
    while ((line = in.readLine()) != null) {
      response.append(line);
      response.append('\r');
    }
    input.close();
    String s = response.toString();

    int start = s.indexOf("MSH|");
    if (s.indexOf("SecurityFault") > 0 && start < 0) {
      throw new NotAuthenticated();
    }

    if (start > 0) {
      s = s.substring(start);
      int e = s.indexOf("</");
      if (e > 0) {
        s = s.substring(0, e);
      }
      s = s.replace("&amp;", "\\&");
      if (s.endsWith("]]>")) {
        s = s.substring(0, s.length() - 3);
      }
    } else {
      BufferedReader stringIn = new BufferedReader(new StringReader(s));
      s = "";
      String l;
      while ((l = stringIn.readLine()) != null) {
        s += l.trim() + "\r";
      }
    }
    return s;
  }

  public static TestCase recreateTestCase(String rsp) throws IOException, ParseException {
    TestCase testCase = new TestCase();
    testCase.setTestEventList(new ArrayList<TestEvent>());
    BufferedReader in = new BufferedReader(new StringReader(rsp));
    String line;
    while ((line = in.readLine()) != null) {
      String[] f = line.split("\\|");
      if (f == null || f.length <= 1 || f[0] == null || f[0].length() != 3) {
        continue;
      }
      {
        String segmentName = f[0];
        if (segmentName.equals("RXA")) {
          String adminDate = readFieldValue(f, 3);
          String cvxCode = readFieldValue(f, 5);
          if (!cvxCode.equals("998")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
              TestEvent testEvent = new TestEvent();
              Event event = new Event();
              event.setVaccineCvx(cvxCode);
              testEvent.setEventDate(sdf.parse(adminDate));
              testEvent.setEvent(event);
              testCase.getTestEventList().add(testEvent);
            } catch (ParseException pe) {
              // keep going
            }
          }
        }
      }
    }
    return testCase;
  }
}
