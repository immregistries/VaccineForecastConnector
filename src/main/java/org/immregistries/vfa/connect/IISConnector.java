package org.immregistries.vfa.connect;

import static org.immregistries.vfa.connect.model.ForecastEngineIssueLevel.ERROR;
import static org.immregistries.vfa.connect.model.ForecastEngineIssueLevel.WARNING;
import static org.immregistries.vfa.connect.model.ForecastEngineIssueType.UNEXPECTED_FORMAT;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.immregistries.vfa.connect.model.Admin;
import org.immregistries.vfa.connect.model.EvaluationActual;
import org.immregistries.vfa.connect.model.Event;
import org.immregistries.vfa.connect.model.ForecastActual;
import org.immregistries.vfa.connect.model.ForecastEngineIssue;
import org.immregistries.vfa.connect.model.Software;
import org.immregistries.vfa.connect.model.SoftwareResult;
import org.immregistries.vfa.connect.model.SoftwareResultStatus;
import org.immregistries.vfa.connect.model.TestCase;
import org.immregistries.vfa.connect.model.TestEvent;
import org.immregistries.vfa.connect.model.VaccineGroup;
import org.immregistries.vfa.connect.util.FakePatient;

public class IISConnector implements ConnectorInterface {
  private Software software = null;
  private boolean logText = false;
  private String uniqueId;

  public IISConnector(Software software, List<VaccineGroup> forecastItemList) {
    this.software = software;
    this.forecastItemList = forecastItemList;
    map("3", VaccineGroup.ID_MMR, INVALID_CVX_CODE);
    map("9", VaccineGroup.ID_DTAP_TDAP_TD, INVALID_CVX_CODE);
    map("9", VaccineGroup.ID_TDAP_ONLY, INVALID_CVX_CODE);
    map("9", VaccineGroup.ID_TDAP_TD, INVALID_CVX_CODE);
    map("09", VaccineGroup.ID_DTAP_TDAP_TD);
    map("09", VaccineGroup.ID_TDAP_ONLY);
    map("09", VaccineGroup.ID_TDAP_TD);
    map("10", VaccineGroup.ID_POLIO);
    map("010", VaccineGroup.ID_POLIO, INVALID_CVX_CODE);
    map("017", VaccineGroup.ID_HIB, INVALID_CVX_CODE);
    map("16", VaccineGroup.ID_INFLUENZA);
    map("020", VaccineGroup.ID_DTAP, INVALID_CVX_CODE);
    map("22", VaccineGroup.ID_HIB);
    map("28", VaccineGroup.ID_DTAP);
    map("28", VaccineGroup.ID_DTAP_TDAP_TD);
    map("028", VaccineGroup.ID_DTAP, INVALID_CVX_CODE);
    map("028", VaccineGroup.ID_DTAP_TDAP_TD, INVALID_CVX_CODE);
    map("31", VaccineGroup.ID_HEPA);
    map("033", VaccineGroup.ID_PPSV, INVALID_CVX_CODE);
    map("42", VaccineGroup.ID_HEPB);
    map("045", VaccineGroup.ID_HEPB, INVALID_CVX_CODE);
    map("48", VaccineGroup.ID_HIB);
    map("49", VaccineGroup.ID_HIB);
    map("52", VaccineGroup.ID_HEPA);
    map("062", VaccineGroup.ID_HPV, INVALID_CVX_CODE);
    map("62", VaccineGroup.ID_HPV);
    map("83", VaccineGroup.ID_HEPA);
    map("085", VaccineGroup.ID_HEPA, INVALID_CVX_CODE);
    map("088", VaccineGroup.ID_INFLUENZA, INVALID_CVX_CODE);
    map("100", VaccineGroup.ID_PNEUMO);
    map("106", VaccineGroup.ID_DTAP);
    map("106", VaccineGroup.ID_DTAP_TDAP_TD);
    map("107", VaccineGroup.ID_DTAP);
    map("107", VaccineGroup.ID_DTAP_TDAP_TD);
    map("108", VaccineGroup.ID_MENING);
    map("136", VaccineGroup.ID_MENING);
    map("109", VaccineGroup.ID_PNEUMO);
    map("114", VaccineGroup.ID_MENING);
    map("116", VaccineGroup.ID_ROTA);
    map("119", VaccineGroup.ID_ROTA);
    map("133", VaccineGroup.ID_PCV);
    map("215", VaccineGroup.ID_PCV);
    map("216", VaccineGroup.ID_PCV);
    map("133", VaccineGroup.ID_PNEUMO);
    map("141", VaccineGroup.ID_INFLUENZA);
    map("147", VaccineGroup.ID_MENING);
    map("150", VaccineGroup.ID_INFLUENZA);
    map("150", VaccineGroup.ID_INFLUENZA_IIV);
    map("162", VaccineGroup.ID_MENINGB);
    map("163", VaccineGroup.ID_MENINGB);
    map("164", VaccineGroup.ID_MENINGB);
    map("165", VaccineGroup.ID_HPV);
    map("188", VaccineGroup.ID_ZOSTER);
    map("189", VaccineGroup.ID_HEPB);
    map("HEP B", VaccineGroup.ID_HEPB, INVALID_CVX_CODE); // FL SHOTS - HEP B^HEPATITIS B
    map("HIB", VaccineGroup.ID_HIB, INVALID_CVX_CODE); // FL SHOTS - HIB^H INFLUENZA TYPE B
    map("POLIO", VaccineGroup.ID_POLIO, INVALID_CVX_CODE); // FL SHOTS - POLIO^POLIO
    map("VZV", VaccineGroup.ID_VAR, INVALID_CVX_CODE); // FL SHOTS - VZV^CHICKEN POX
    map("PNEUCON", VaccineGroup.ID_PNEUMO, INVALID_CVX_CODE); // FL SHOTS - PNEUCON^PNEUMOCOCCAL CONJUGATE
    map("MEASLES", VaccineGroup.ID_MEASLES_ONLY, INVALID_CVX_CODE); // FL SHOTS - MEASLES^MEASLES
    map("MEASLES", VaccineGroup.ID_MMR, INVALID_CVX_CODE); // FL SHOTS - MEASLES^MEASLES
    map("MUMPS", VaccineGroup.ID_MUMPS_ONLY, INVALID_CVX_CODE); // FL SHOTS - MUMPS^MUMPS
    map("RUBELLA", VaccineGroup.ID_RUBELLA_ONLY, INVALID_CVX_CODE); // FL SHOTS - RUBELLA^RUBELLA
    map("ROTAVIRUS", VaccineGroup.ID_ROTA, INVALID_CVX_CODE); // FL SHOTS - ROTAVIRUS^ROTAVIRUS
    map("HPV", VaccineGroup.ID_HPV, INVALID_CVX_CODE); // FL SHOTS - HPV^HPV
    map("DIPHTHERIA", VaccineGroup.ID_DTAP, INVALID_CVX_CODE); // FL SHOTS - DIPHTHERIA^DIPHTHERIA
    map("DIPHTHERIA", VaccineGroup.ID_DTAP_TDAP_TD, INVALID_CVX_CODE); // FL SHOTS - DIPHTHERIA^DIPHTHERIA
    map("203", VaccineGroup.ID_MENING);
    map("207", VaccineGroup.ID_COVID);
    map("208", VaccineGroup.ID_COVID);
    map("210", VaccineGroup.ID_COVID);
    map("211", VaccineGroup.ID_COVID);
    map("212", VaccineGroup.ID_COVID);
    map("213", VaccineGroup.ID_COVID);
    map("217", VaccineGroup.ID_COVID);
    map("218", VaccineGroup.ID_COVID);
    map("219", VaccineGroup.ID_COVID);
    map("221", VaccineGroup.ID_COVID);
    map("225", VaccineGroup.ID_COVID);
    map("226", VaccineGroup.ID_COVID);
    map("227", VaccineGroup.ID_COVID);
    map("228", VaccineGroup.ID_COVID);
    map("229", VaccineGroup.ID_COVID);
    map("230", VaccineGroup.ID_COVID);
    map("300", VaccineGroup.ID_COVID);
    map("301", VaccineGroup.ID_COVID);
    map("302", VaccineGroup.ID_COVID);
    map("303", VaccineGroup.ID_RSV);
    map("304", VaccineGroup.ID_RSV);
    map("305", VaccineGroup.ID_RSV);
    map("306", VaccineGroup.ID_RSV);
    map("307", VaccineGroup.ID_RSV);
    map("308", VaccineGroup.ID_COVID);
    map("309", VaccineGroup.ID_COVID);
    map("310", VaccineGroup.ID_COVID);
    map("311", VaccineGroup.ID_COVID);
    map("312", VaccineGroup.ID_COVID);
    map("313", VaccineGroup.ID_COVID);
    map("314", VaccineGroup.ID_RSV);
    map("315", VaccineGroup.ID_RSV);
    map("500", VaccineGroup.ID_COVID);
    map("501", VaccineGroup.ID_COVID);
    map("502", VaccineGroup.ID_COVID);
    map("503", VaccineGroup.ID_COVID);
    map("504", VaccineGroup.ID_COVID);
    map("505", VaccineGroup.ID_COVID);
    map("506", VaccineGroup.ID_COVID);
    map("507", VaccineGroup.ID_COVID);
    map("508", VaccineGroup.ID_COVID);
    map("509", VaccineGroup.ID_COVID);
    map("510", VaccineGroup.ID_COVID);
    map("511", VaccineGroup.ID_COVID);
    map("512", VaccineGroup.ID_COVID);
    map("513", VaccineGroup.ID_COVID);
    map("514", VaccineGroup.ID_COVID);
    map("515", VaccineGroup.ID_COVID);
    map("516", VaccineGroup.ID_COVID);
    map("517", VaccineGroup.ID_COVID);
    map("518", VaccineGroup.ID_COVID);
    map("519", VaccineGroup.ID_COVID);
    map("520", VaccineGroup.ID_COVID);
    map("521", VaccineGroup.ID_COVID);
    for (VaccineGroup forecastItem : forecastItemList) {
      map(forecastItem.getVaccineCvx(), forecastItem.getVaccineGroupId());
    }
  }

  private List<VaccineGroup> forecastItemList = null;
  private Map<String, List<VaccineGroup>> familyMapping = new HashMap<String, List<VaccineGroup>>();
  private Set<String> invalidCvxCodes = new HashSet<>();

  private static final boolean INVALID_CVX_CODE = true;

  public Map<String, List<VaccineGroup>> getFamilyMapping() {
    return familyMapping;
  }

  private void map(String familyName, int forecastItemId) {
    map(familyName, forecastItemId, false);
  }

  private void map(String familyName, int forecastItemId, boolean incorrectCvxCode) {
    if (incorrectCvxCode) {
      invalidCvxCodes.add(familyName);
    }

    for (VaccineGroup forecastItem : forecastItemList) {
      if (forecastItem != null && forecastItem.getVaccineGroupId() == forecastItemId) {
        List<VaccineGroup> forecastItemListFromMap = familyMapping.get(familyName);
        if (forecastItemListFromMap == null) {
          forecastItemListFromMap = new ArrayList<VaccineGroup>();
          familyMapping.put(familyName, forecastItemListFromMap);
        }
        if (!forecastItemListFromMap.contains(forecastItem)) {
          forecastItemListFromMap.add(forecastItem);
        }
        return;
      }
    }
  }

  public List<ForecastActual> queryForForecast(TestCase testCase, SoftwareResult softwareResult)
      throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    StringWriter sw = new StringWriter();
    PrintWriter logOut = logText ? new PrintWriter(sw) : null;

    logMessage(logOut, "Starting HL7 connection",
        "This service will attempt to send a fake VXU with the vaccination history and then request the forecast back using a QBP. ");
    try {
      if (testCase.getTestEventList().size() == 0) {
        logMessage(logOut, "No vaccinations found, so adding a Typhoid to list, "
            + "some IIS cannot accept a patient without at least one vaccination.", "");
        // Adding typhoid given today
        TestEvent testEvent = new TestEvent();
        testEvent.setEvent(Event.getEvent(91));
        Date evalDate = testCase.getEvalDate();
        if (evalDate == null) {
          evalDate = new Date();
          logMessage(logOut, "Evaluation date not set, so defaulting to today", "");
        }
        testEvent.setEventDate(evalDate);
        testEvent.setTestCase(testCase);
        testCase.getTestEventList().add(testEvent);
        logMessage(logOut, "Typhoid added for " + evalDate, "");
      }
      createUniqueId(testCase.getTestCaseNumber());

      FakePatient fakePatient = new FakePatient(testCase, uniqueId);
      String vxu = buildVXU(testCase, fakePatient);
      logMessage(logOut, "Sending VXU",
          "Sending update to IIS to create a fake record with test case vaccination history");
      if (logText) {
        logOut.println(vxu);
      }
      String ack = sendRequest(vxu);
      logMessage(logOut, "Reading ACK", "IIS returned a response that should contain an HL7 ACK");
      if (logText) {
        logOut.println(ack);
      }

      String qbp = buildQBP(fakePatient);
      logMessage(logOut, "QBP prepped", "Will try to send this query to the IIS");
      if (logText) {
        logOut.println(qbp);
      }
      boolean lookingForMatch = true;
      int delay = 0;
      while (lookingForMatch) {
        logMessage(logOut, "Sending QBP", "Sending query to IIS");
        String rsp = sendRequest(qbp);
        logMessage(logOut, "RSP Received", "IIS returned a response that needs to be inspected");
        if (logText) {
          logOut.println(rsp);
        }
        readRSP(forecastActualList, testCase, softwareResult, rsp);
        if (softwareResult.getSoftwareResultStatus() == SoftwareResultStatus.NOT_FOUND
            || forecastActualList.size() == 0) {
          logMessage(logOut, "Match or forecasts not found",
              "Unable to find matching patient or forecasts in response");
          if (delay == 0) {
            delay = 10;
          } else if (delay == 10) {
            delay = 20;
          } else if (delay == 20) {
            delay = 40;
          } else if (delay == 40) {
            lookingForMatch = false;
          }
        } else {
          lookingForMatch = false;
        }
        if (lookingForMatch) {
          logMessage(logOut, "Waiting", "Will wait for " + delay + "s and query again");
          synchronized (this) {
            this.wait(delay * 1000);
          }
        }
      }
    } catch (NotAuthenticated na) {
      logMessage(logOut, "Unable to Authenticate",
          "Unable to authenticate with IIS, please check credentials and URL");
      softwareResult.setSoftwareResultStatus(SoftwareResultStatus.NOT_AUTHENTICATED);
    } catch (Exception e) {
      softwareResult.setSoftwareResultStatus(SoftwareResultStatus.PROBLEM);
      logMessage(logOut, "Exception ocurred", "Unable to get forecast results from IIS");
      if (logOut != null) {
        e.printStackTrace(logOut);
      } else {
        e.printStackTrace();
      }
      throw new Exception("Unable to get forecast results", e);
    } finally {
      logMessage(logOut, "Finished process",
          "The HL7 connector for IIS is finished, results are being returned to FITS");
      if (logOut != null) {
        logOut.close();
        softwareResult.setLogText(sw.toString());
      }
    }
    return forecastActualList;
  }

  public void logMessage(PrintWriter logOut, String message, String details) {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS Z");
    if (logText) {
      logOut.println("[" + sdf.format(System.currentTimeMillis()) + "] " + message);
      logOut.println(details);
    }
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
    boolean firstOBX = true;
    boolean okayToRead597807inEvaluation = true;
    boolean okayToRead597807inForecast = true;
    Set<String> cvxForecastSet = new HashSet<>();
    Set<VaccineGroup> vaccineGroupSet = new HashSet<>();
    Map<String, Integer> segmentCountMap = new HashMap<>();
    while ((line = in.readLine()) != null) {
      if (line.length() == 0) {
        continue;
      }
      if (parseDebugLineList != null) {
        parseDebugLine = new ParseDebugLine(line);
        parseDebugLineList.add(parseDebugLine);
      }
      String[] f = line.split("\\|");

      if (f == null || f.length <= 1 || f[0] == null || f[0].length() != 3) {
        if (parseDebugLine != null) {
          parseDebugLine.setLineStatus(ParseDebugStatus.NOT_READ);
          parseDebugLine
              .setLineStatusReason("HL7 segment has too few fields, or is not formatted properly");
        }
        issuesList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, WARNING,
            "Invalid HL7 format found in segment", ""));
        continue;
      }
      {
        String segmentName = f[0];
        String path = segmentName;
        {
          Integer count = segmentCountMap.get(segmentName);
          if (count == null) {
            count = 1;
          } else {
            count = count + 1;
            path += "[" + count + "]";
          }
          segmentCountMap.put(segmentName, count);
        }
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
          firstOBX = true;
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
                obsLabel, softwareResult, okayToRead597807inForecast, cvxForecastSet,
                vaccineGroupSet, path);
            if (firstOBX) {
              firstOBX = false;
              okayToRead597807inForecast = obsCode.equals("59780-7");
            }

          } else {
            evaluationActual = handleEvaluation(softwareResult, testEvent, evaluationActual,
                parseDebugLine, obsCode, obsValue, path, okayToRead597807inEvaluation);
            if (firstOBX) {
              firstOBX = false;
              okayToRead597807inEvaluation = obsCode.equals("59780-7");
            }
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
    Collections.sort(forecastActualList, new Comparator<ForecastActual>() {
      @Override
      public int compare(ForecastActual fa1, ForecastActual fa2) {
        if (fa1.getVaccineGroup().equals(fa2.getVaccineGroup())) {
          if (fa1.getValidDate() != null && fa2.getValidDate() != null) {
            return fa1.getValidDate().compareTo(fa2.getValidDate());
          } else if (fa1.getDueDate() != null && fa2.getDueDate() != null) {
            return fa1.getDueDate().compareTo(fa2.getDueDate());
          } else if (fa1.getAdminStatus() != null && fa2.getAdminStatus() != null) {
            return fa1.getAdminStatus().compareTo(fa2.getAdminStatus());
          }
        } else {
          return fa1.getVaccineGroup().getLabel().compareTo(fa2.getVaccineGroup().getLabel());
        }
        return 0;
      }
    });

  }

  private EvaluationActual handleEvaluation(SoftwareResult softwareResult, TestEvent testEvent,
      EvaluationActual evaluationActual, ParseDebugLine parseDebugLine, String obsCode,
      String obsValue, String path, boolean okayToRead597807) {
    List<ForecastEngineIssue> issuesList = softwareResult.getIssueList();
    if (obsCode.equals("30956-7") || obsCode.equals("38890-0")
        || (okayToRead597807 && obsCode.equals("59780-7"))) {
      if (testEvent == null) {
        if (parseDebugLine != null) {
          parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
          parseDebugLine.setLineStatusReason("No test event was found, unable to link");
        }
        issuesList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, ERROR,
            "OBX " + obsCode + " found, but not able to link with vaccine administered",
            path + "-3"));
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
        logEvaluationReasonNotSetup(parseDebugLine, issuesList, path + "-3");
      } else {
        evaluationActual.setDoseValid(obsValue);
        if (parseDebugLine != null) {
          parseDebugLine.setLineStatus(ParseDebugStatus.OK);
        }
      }
    } else if (obsCode.equals("30982-3")) {
      if (evaluationActual == null) {
        logEvaluationReasonNotSetup(parseDebugLine, issuesList, path + "-3");
      } else {
        evaluationActual.setReasonText(obsValue);
        if (parseDebugLine != null) {
          parseDebugLine.setLineStatus(ParseDebugStatus.OK);
        }
      }
    } else if (obsCode.equals("30973-2")) {
      if (evaluationActual == null) {
        logEvaluationReasonNotSetup(parseDebugLine, issuesList, path + "-3");
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
      String obsLabel, SoftwareResult softwareResult, boolean okayToRead597807,
      Set<String> cvxForecastSet, Set<VaccineGroup> vaccineGroupSet, String path) {
    List<ForecastEngineIssue> issueList = softwareResult.getIssueList();
    if (obsCode.equals("30956-7") || obsCode.equals("30979-9")
        || (okayToRead597807 && obsCode.equals("59780-7"))) {

      if (obsCode.equals("59780-7")) {
        issueList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, ERROR,
            "LOINC 59780-7 should not be used to indicate which vaccination to forecast for",
            path + "-3"));
      }
      if (obsValue.equals("")) {
        issueList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, ERROR,
            "No vaccine CVX was sent in OBX-5", path + "-5"));
      } else {
        if (cvxForecastSet.contains(obsValue)) {
          issueList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, WARNING,
              "CVX " + obsValue
                  + " has already been forecasted in this message, duplicate forecasts might confuse the reader",
              path + "-5"));
        }
      }
      cvxForecastSet.add(obsValue);
      if (invalidCvxCodes.contains(obsValue)) {
        issueList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, ERROR,
            "Value '" + obsValue + "' is not a valid CVX code", path + "-5"));
      }
      List<VaccineGroup> forecastItemListFromMap = familyMapping.get(obsValue);
      fal = new ArrayList<ForecastActual>();
      if (forecastItemListFromMap != null) {
        for (VaccineGroup vaccineGroup : forecastItemListFromMap) {
          if (vaccineGroupSet.contains(vaccineGroup)) {
            issueList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, WARNING,
                "CVX " + obsCode + " is a close match to another " + vaccineGroup.getLabel()
                    + " forecast in this message, this may be a potential duplicate that may confuse the reader",
                path + "-5"));
          }
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
              .setLineStatusReason("Unable to find mapping to one or more vaccine groups");
        }
        issueList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, ERROR,
            "Unable to find mapping to one or more vaccine groups", path + "-5"));
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
    } else if (obsCode.equals("30999-1")) {
      if (parseDebugLine != null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.NOT_READ);
        parseDebugLine.setLineStatusReason("LOINC 30999-1 is not supported");
      }
      issueList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, ERROR,
          "LOINC 30999-1 is not supported, information may be lost of ignored", path + "-3"));
    } else if (obsCode.equals("59780-7")) {
      if (parseDebugLine != null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.EXPECTED_BUT_NOT_READ);
        parseDebugLine.setLineStatusReason("Not currently supported");
      }
    } else if (obsCode.equals("59783-1")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList, path + "-3");
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
              "Unrecognized series status code: " + obsValue, path + "-5"));
        }
        for (ForecastActual forecastActual : fal) {
          forecastActual.setAdmin(admin);
        }
      }
    } else if (obsCode.equals("30981-5")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList, path + "-3");
      } else {
        if (fal.size() == 0) {
          logNotRead(parseDebugLine);
        } else {
          Date date = parseDate(obsValue);
          logDateNotParsedProblem(parseDebugLine, date, issueList, path + "-5");
          for (ForecastActual forecastActual : fal) {
            forecastActual.setValidDate(date);
          }
        }
      }
    } else if (obsCode.equals("30980-7")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList, path + "-3");
      } else {
        if (fal.size() == 0) {
          logNotRead(parseDebugLine);
        } else {
          Date date = parseDate(obsValue);
          logDateNotParsedProblem(parseDebugLine, date, issueList, path + "-5");
          for (ForecastActual forecastActual : fal) {
            forecastActual.setDueDate(date);
          }
        }
      }
    } else if (obsCode.equals("59777-3")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList, path + "-3");
      } else {
        if (fal.size() == 0) {
          logNotRead(parseDebugLine);
        } else {
          Date date = parseDate(obsValue);
          logDateNotParsedProblem(parseDebugLine, date, issueList, path + "-5");
          for (ForecastActual forecastActual : fal) {
            forecastActual.setFinishedDate(date);
          }
        }
      }
    } else if (obsCode.equals("59778-1")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList, path + "-3");
      } else {
        if (fal.size() == 0) {
          logNotRead(parseDebugLine);
        } else {
          Date date = parseDate(obsValue);
          logDateNotParsedProblem(parseDebugLine, date, issueList, path + "-5");
          for (ForecastActual forecastActual : fal) {
            forecastActual.setOverdueDate(date);
          }
        }
      }
    } else if (obsCode.equals("30973-2")) {
      if (fal == null) {
        logOutofSequence(parseDebugLine, issueList, path + "-3");
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
        logOutofSequence(parseDebugLine, issueList, path + "-3");
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
      List<ForecastEngineIssue> issuesList, String path) {
    if (parseDebugLine != null) {
      parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
      parseDebugLine.setLineStatusReason("No evaluation actual setup");
    }
    issuesList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, WARNING,
        "Evaluation was not setup properly, cannot link concept to evaluation ", path));
  }

  private void logDateNotParsedProblem(ParseDebugLine parseDebugLine, Date date,
      List<ForecastEngineIssue> issuesList, String path) {
    if (date == null) {
      if (parseDebugLine != null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
        parseDebugLine.setLineStatusReason("Date not parseable");
      }
      issuesList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, ERROR, "Date not parseable", path));
    } else {
      if (parseDebugLine != null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.OK);
      }
    }
  }

  private void logOutofSequence(ParseDebugLine parseDebugLine, List<ForecastEngineIssue> issuesList,
      String path) {
    if (parseDebugLine != null) {
      parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
      parseDebugLine.setLineStatusReason(
          "Missing a previous OBX that defines the vaccines being forecast for");
      issuesList.add(new ForecastEngineIssue(UNEXPECTED_FORMAT, ERROR,
          "Missing a previous OBX that defines the vaccines being forecast for", path));
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
        if (testEvent.getEvent().getVaccineMvx() != null
            && !testEvent.getEvent().getVaccineMvx().equals("null")) {
          sb.append(testEvent.getEvent().getVaccineMvx() + "^"
              + testEvent.getEvent().getVaccineMvx() + "^MVX");
        }
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
