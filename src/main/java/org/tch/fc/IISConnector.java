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
import org.tch.fc.model.Software;
import org.tch.fc.model.SoftwareResult;
import org.tch.fc.model.TestCase;
import org.tch.fc.model.TestEvent;
import org.tch.fc.model.VaccineGroup;
import org.tch.fc.util.FakePatient;

public class IISConnector implements ConnectorInterface
{
  private Software software = null;
  private boolean logText = false;
  private String uniqueId;

  public IISConnector(Software software, List<VaccineGroup> forecastItemList) {
    this.software = software;
    this.forecastItemList = forecastItemList;
    map("10", VaccineGroup.ID_POLIO);
    map("31", VaccineGroup.ID_HEPA);
    map("48", VaccineGroup.ID_HIB);
    map("83", VaccineGroup.ID_HEPA);
    map("107", VaccineGroup.ID_DTAP);
    map("107", VaccineGroup.ID_DTAP_TDAP_TD);
    map("109", VaccineGroup.ID_PNEUMO);
    map("114", VaccineGroup.ID_MENING);
    map("116", VaccineGroup.ID_ROTA);
    map("133", VaccineGroup.ID_PCV);
    map("133", VaccineGroup.ID_PNEUMO);
    map("147", VaccineGroup.ID_MENING);
    map("150", VaccineGroup.ID_INFLUENZA);
    map("150", VaccineGroup.ID_INFLUENZA_IIV);
    map("164", VaccineGroup.ID_MENINGB);
    map("164", VaccineGroup.ID_MENING);
    map("165", VaccineGroup.ID_HPV);
    for (VaccineGroup forecastItem : forecastItemList) {
      map(forecastItem.getVaccineCvx(), forecastItem.getVaccineGroupId());
    }
  }

  private List<VaccineGroup> forecastItemList = null;
  private Map<String, List<VaccineGroup>> familyMapping = new HashMap<String, List<VaccineGroup>>();

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

  public List<ForecastActual> queryForForecast(TestCase testCase, SoftwareResult softwareResult) throws Exception {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    StringWriter sw = new StringWriter();
    PrintWriter logOut = logText ? new PrintWriter(sw) : null;

    if (logText) {
      logOut.println(
          "This service will attempt to send a fake VXU with the vaccination history and then request the forecast back using a QBP. ");
    }
    try {
      uniqueId = "" + System.currentTimeMillis() + nextIncrement();

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

      uniqueId = "" + System.currentTimeMillis() + nextIncrement();
      String qbp = buildQBP(fakePatient);
      if (logText) {
        logOut.println();
        logOut.println("Sending QBP ...");
        logOut.println(qbp);
      }
      String rsp = sendRequest(qbp);
      if (logText) {
        logOut.println();
        logOut.println("RSP received back ...");
        logOut.println(rsp);
      }

      readRSP(forecastActualList, testCase, softwareResult, rsp);
    } catch (Exception e) {
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

  public static enum ParseDebugStatus {
    OK, EXPECTED_BUT_NOT_READ, NOT_READ, PROBLEM
  }

  public static class ParseDebugLine
  {
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

  public void readRSP(List<ForecastActual> forecastActualList, TestCase testCase, SoftwareResult softwareResult,
      String rsp) throws IOException, ParseException {
    readRSP(forecastActualList, testCase, softwareResult, rsp, null);
  }

  public void readRSP(List<ForecastActual> forecastActualList, TestCase testCase, SoftwareResult softwareResult,
      String rsp, List<ParseDebugLine> parseDebugLineList) throws IOException, ParseException {
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
          parseDebugLine.setLineStatusReason("HL7 segment has too few fields, or is not formatted properly");
        }
        continue;
      }
      {
        String segmentName = f[0];
        if (parseDebugLine != null) {
          parseDebugLine.setSegmentName(segmentName);
        }
        if (segmentName.equals("BHS") || segmentName.equals("FHS") || segmentName.equals("FTS")
            || segmentName.equals("BTS") || segmentName.equals("MSH") || segmentName.equals("MSA")
            || segmentName.equals("ERR") || segmentName.equals("QAK") || segmentName.equals("QPD")
            || segmentName.equals("PID") || segmentName.equals("ORC") || segmentName.equals("RXR")
            || segmentName.equals("NK1") || segmentName.equals("PD1")) {
          if (parseDebugLine != null) {
            parseDebugLine.setLineStatus(ParseDebugStatus.EXPECTED_BUT_NOT_READ);
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
          if (cvxCodeInRxa == null) {
            if (parseDebugLine != null) {
              parseDebugLine.setLineStatus(ParseDebugStatus.NOT_READ);
              parseDebugLine.setLineStatusReason("OBX found before a valid RXA segment, unable to read");
            }
            continue;
          } else if (cvxCodeInRxa.equals("998")) {
            if (obsCode.equals("30956-7") || obsCode.equals("30979-9")) {
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
              if (parseDebugLine != null) {
                if (fal.size() == 0) {
                  parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
                  parseDebugLine.setLineStatusReason("Unable to find mapping to one ore more vaccine groups");
                } else {
                  parseDebugLine.setLineStatus(ParseDebugStatus.OK);
                }
              }
            } else if (obsCode.equals("59783-1")) {
              if (fal == null) {
                logOutofSequence(parseDebugLine);
              } else {
                Admin admin = mapAdmin(obsValue);
                if (parseDebugLine != null) {
                  if (admin == Admin.UNKNOWN) {
                    parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
                    parseDebugLine.setLineStatusReason("Unrecognized value received ");
                  } else {
                    parseDebugLine.setLineStatus(ParseDebugStatus.OK);
                  }
                }
                for (ForecastActual forecastActual : fal) {
                  forecastActual.setAdmin(admin);
                }
              }
            } else if (obsCode.equals("30981-5")) {
              if (fal == null) {
                logOutofSequence(parseDebugLine);
              } else {
                if (fal.size() == 0) {
                  logNotRead(parseDebugLine);
                } else {
                  Date date = parseDate(obsValue);
                  logDateNotParsedProblem(parseDebugLine, date);
                  for (ForecastActual forecastActual : fal) {
                    forecastActual.setValidDate(date);
                  }
                }
              }
            } else if (obsCode.equals("30980-7")) {
              if (fal == null) {
                logOutofSequence(parseDebugLine);
              } else {
                if (fal.size() == 0) {
                  logNotRead(parseDebugLine);
                } else {
                  Date date = parseDate(obsValue);
                  logDateNotParsedProblem(parseDebugLine, date);
                  for (ForecastActual forecastActual : fal) {
                    forecastActual.setDueDate(date);
                  }
                }
              }
            } else if (obsCode.equals("59777-3")) {
              if (fal == null) {
                logOutofSequence(parseDebugLine);
              } else {
                if (fal.size() == 0) {
                  logNotRead(parseDebugLine);
                } else {
                  Date date = parseDate(obsValue);
                  logDateNotParsedProblem(parseDebugLine, date);
                  for (ForecastActual forecastActual : fal) {
                    forecastActual.setFinishedDate(date);
                  }
                }
              }
            } else if (obsCode.equals("59778-1")) {
              if (fal == null) {
                logOutofSequence(parseDebugLine);
              } else {
                if (fal.size() == 0) {
                  logNotRead(parseDebugLine);
                } else {
                  Date date = parseDate(obsValue);
                  logDateNotParsedProblem(parseDebugLine, date);
                  for (ForecastActual forecastActual : fal) {
                    forecastActual.setOverdueDate(date);
                  }
                }
              }
            } else if (obsCode.equals("30973-2")) {
              if (fal == null) {
                logOutofSequence(parseDebugLine);
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
                logOutofSequence(parseDebugLine);
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
          } else {
            if (obsCode.equals("30956-7")) {

              if (testEvent == null) {
                if (parseDebugLine != null) {
                  parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
                  parseDebugLine.setLineStatusReason("No test event was found, unable to link");
                }
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
                  parseDebugLine.setLineStatus(ParseDebugStatus.OK);
                }
              }
            } else if (obsCode.equals("59781-5")) {
              if (evaluationActual == null) {
                logEvaluationReasonNotSetup(parseDebugLine);
              } else {
                evaluationActual.setDoseValid(obsValue);
                if (parseDebugLine != null) {
                  parseDebugLine.setLineStatus(ParseDebugStatus.OK);
                }
              }
            } else if (obsCode.equals("30982-3")) {
              if (evaluationActual == null) {
                logEvaluationReasonNotSetup(parseDebugLine);
              } else {
                evaluationActual.setReasonText(obsValue);
                if (parseDebugLine != null) {
                  parseDebugLine.setLineStatus(ParseDebugStatus.OK);
                }
              }
            } else if (obsCode.equals("30973-2")) {
              if (evaluationActual == null) {
                logEvaluationReasonNotSetup(parseDebugLine);
              } else {
                evaluationActual.setDoseNumber(obsValue);
                if (parseDebugLine != null) {
                  parseDebugLine.setLineStatus(ParseDebugStatus.OK);
                }
              }
            } else if (obsCode.equals("29769-7") || obsCode.equals("VFC-STATUS") || obsCode.equals("64994-7")
                || obsCode.equals("29768-9")) {
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
          }

        } else {
          if (parseDebugLine != null) {
            parseDebugLine.setLineStatus(ParseDebugStatus.NOT_READ);
            parseDebugLine.setLineStatusReason("Unrecognized segment");
          }
        }

      }
    }

  }

  private void logEvaluationReasonNotSetup(ParseDebugLine parseDebugLine) {
    if (parseDebugLine != null) {
      parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
      parseDebugLine.setLineStatusReason("No evaluation actual setup");
    }
  }

  private void logDateNotParsedProblem(ParseDebugLine parseDebugLine, Date date) {
    if (parseDebugLine != null) {
      if (date == null) {
        parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
        parseDebugLine.setLineStatusReason("Date not parseable");
      } else {
        parseDebugLine.setLineStatus(ParseDebugStatus.OK);
      }
    }
  }

  private void logOutofSequence(ParseDebugLine parseDebugLine) {
    if (parseDebugLine != null) {
      parseDebugLine.setLineStatus(ParseDebugStatus.PROBLEM);
      parseDebugLine.setLineStatusReason("Missing a previous OBX that defines the vaccines being forecast for");
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

  private static final Map<String, Admin> adminStatusMap = new HashMap();
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
    adminStatusMap.put("P", Admin.NOT_COMPLETE); // STC P^Past Due^STC0002
    adminStatusMap.put("D", Admin.NOT_COMPLETE); // STC D^Due Now^STC0002
    adminStatusMap.put("U", Admin.NOT_COMPLETE); // STC U^Up to Date^STC0002
  }

  private static Admin mapAdmin(String value) {
    if (value == null) {
      return Admin.UNKNOWN;
    }
    Admin admin = adminStatusMap.get(value);
    if (admin == null) {
      return Admin.UNKNOWN;
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
    String obsCode = s;
    return obsCode;
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
      sb.append("|" + fakePatient.getNameLast() + "^" + fakePatient.getNameFirst() + "^" + fakePatient.getNameMiddle()
          + "^^^^L");
      // PID-6
      sb.append("|" + fakePatient.getMaidenLast() + "^" + fakePatient.getMaidenFirst() + "^^^^^M");
      // PID-7
      sb.append("|" + sdf.format(fakePatient.getPatientDob()));
      {
        // PID-8
        sb.append("|" + fakePatient.getPatientSex());
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
          sb.append(
              "^PRN^PH^^^" + fakePatient.getPhone().substring(0, 3) + "^" + fakePatient.getPhone().substring(3, 10));
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
        sb.append("|" + testEvent.getEvent().getVaccineCvx() + "^" + testEvent.getEvent().getLabel() + "^CVX");
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
        sb.append(testEvent.getEvent().getVaccineMvx() + "^" + testEvent.getEvent().getVaccineMvx() + "^MVX");
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
      sb.append("|" + fakePatient.getNameLast() + "^" + fakePatient.getNameFirst() + "^" + fakePatient.getNameMiddle()
          + "^^^^L");
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

  private String sendRequest(String request) throws IOException {
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
    if (start > 0) {
      s = s.substring(start);
      int e = s.indexOf("</");
      if (e > 0) {
        s = s.substring(0, e);
      }
    }
    s = s.replace("&amp;", "\\&");
    if (s.endsWith("]]>")) {
      s = s.substring(0, s.length() - 3);
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
