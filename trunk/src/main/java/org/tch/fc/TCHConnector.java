package org.tch.fc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tch.fc.model.Admin;
import org.tch.fc.model.EvaluationActual;
import org.tch.fc.model.EventType;
import org.tch.fc.model.ForecastActual;
import org.tch.fc.model.Software;
import org.tch.fc.model.SoftwareResult;
import org.tch.fc.model.SoftwareSetting;
import org.tch.fc.model.TestCase;
import org.tch.fc.model.TestCaseSetting;
import org.tch.fc.model.TestEvent;
import org.tch.fc.model.VaccineGroup;

public class TCHConnector implements ConnectorInterface
{

  private static final String STATUS = " status ";
  private static final String FINISHED = " finished ";
  private static final String OVERDUE = " overdue ";
  private static final String VALID = " valid ";
  private static final String DUE = " due ";
  private static final String DOSE = " dose ";
  private static final String FORECASTING = "Forecasting ";
  private static final String VACCINATION_LINE_PREFIX = "Vaccination #";
  private static final String DETAILS_FOR_PREFIX = "DETAILS FOR: ";

  private static final String STATUS_DESCRIPTION_DUE_LATER = "due later";
  private static final String STATUS_DESCRIPTION_DUE = "due";
  private static final String STATUS_DESCRIPTION_OVERDUE = "overdue";
  private static final String STATUS_DESCRIPTION_FINISHED = "finished";
  private static final String STATUS_DESCRIPTION_COMPLETE = "complete";
  private static final String STATUS_DESCRIPTION_CONTRAINDICATED = "contraindicated";
  private static final String STATUS_DESCRIPTION_COMPLETE_FOR_SEASON = "complete for season";
  private static final String STATUS_DESCRIPTION_ASSUMED_COMPLETE_OR_IMMUNIE = "assumed complete or immune";

  private Map<String, List<VaccineGroup>> familyMapping = new HashMap<String, List<VaccineGroup>>();

  private Map<String, String> evaluationToCvxMapping = new HashMap<String, String>();

  private Map<String, Admin> adminStatusMapping = new HashMap<String, Admin>();

  private Software software = null;
  private boolean logText = false;

  public boolean isLogText() {
    return logText;
  }

  public void setLogText(boolean logText) {
    this.logText = logText;
  }

  public TCHConnector(Software software, List<VaccineGroup> forecastItemList) {
    this.software = software;
    addForcastItem(forecastItemList, "Hib", VaccineGroup.ID_HIB);
    addForcastItem(forecastItemList, "HepB", VaccineGroup.ID_HEPB);
    addForcastItem(forecastItemList, "DTaP", VaccineGroup.ID_DTAP);
    addForcastItem(forecastItemList, "DTaP", VaccineGroup.ID_DTAP_TDAP_TD);
    addForcastItem(forecastItemList, "Td", VaccineGroup.ID_TD_ONLY);
    addForcastItem(forecastItemList, "Td", VaccineGroup.ID_TDAP_TD);
    addForcastItem(forecastItemList, "Td", VaccineGroup.ID_DTAP_TDAP_TD);
    addForcastItem(forecastItemList, "Tdap", VaccineGroup.ID_TDAP_ONLY);
    addForcastItem(forecastItemList, "Tdap", VaccineGroup.ID_TDAP_TD);
    addForcastItem(forecastItemList, "Tdap", VaccineGroup.ID_DTAP_TDAP_TD);
    addForcastItem(forecastItemList, "IPV", VaccineGroup.ID_POLIO);
    addForcastItem(forecastItemList, "HepA", VaccineGroup.ID_HEPA);
    addForcastItem(forecastItemList, "MMR", VaccineGroup.ID_MMR);
    addForcastItem(forecastItemList, "Measles", VaccineGroup.ID_MEASLES_ONLY);
    addForcastItem(forecastItemList, "Mumps", VaccineGroup.ID_MUMPS_ONLY);
    addForcastItem(forecastItemList, "Rubella", VaccineGroup.ID_RUBELLA_ONLY);
    addForcastItem(forecastItemList, "Var", VaccineGroup.ID_VAR);
    addForcastItem(forecastItemList, "Influenza", VaccineGroup.ID_INFLUENZA);
    addForcastItem(forecastItemList, "Influenza", VaccineGroup.ID_INFLUENZA_IIV);
    addForcastItem(forecastItemList, "Influenza", VaccineGroup.ID_INFLUENZA_LAIV);
    addForcastItem(forecastItemList, "Influenza IIV", VaccineGroup.ID_INFLUENZA_IIV);
    addForcastItem(forecastItemList, "Influenza IIV", VaccineGroup.ID_INFLUENZA);
    addForcastItem(forecastItemList, "Influenza LAIV", VaccineGroup.ID_INFLUENZA_LAIV);
    addForcastItem(forecastItemList, "MCV4", VaccineGroup.ID_MENING);
    addForcastItem(forecastItemList, "HPV", VaccineGroup.ID_HPV);
    addForcastItem(forecastItemList, "Rota", VaccineGroup.ID_ROTA);
    addForcastItem(forecastItemList, "PCV13", VaccineGroup.ID_PNEUMO);
    addForcastItem(forecastItemList, "PCV13", VaccineGroup.ID_PCV);
    addForcastItem(forecastItemList, "Zoster", VaccineGroup.ID_ZOSTER);
    addForcastItem(forecastItemList, "PPSV", VaccineGroup.ID_PPSV);

    evaluationToCvxMapping.put("Varicella", "21");
    evaluationToCvxMapping.put("Rubella", "06");
    evaluationToCvxMapping.put("Measles", "05");
    evaluationToCvxMapping.put("Influenza", "88");
    evaluationToCvxMapping.put("Hib", "17");
    evaluationToCvxMapping.put("HPV", "137");
    evaluationToCvxMapping.put("HepB", "45");
    evaluationToCvxMapping.put("HepA", "85");
    evaluationToCvxMapping.put("Diphtheria", "107");
    evaluationToCvxMapping.put("Mening", "147");
    evaluationToCvxMapping.put("Mumps", "07");
    evaluationToCvxMapping.put("Pertussis", "11");
    evaluationToCvxMapping.put("Pneumo", "152");
    evaluationToCvxMapping.put("Polio", "89");
    evaluationToCvxMapping.put("Rotavirus", "122");
    evaluationToCvxMapping.put("Zoster", "121");
    evaluationToCvxMapping.put("PPSV", "33");

    adminStatusMapping.put(STATUS_DESCRIPTION_DUE_LATER, Admin.DUE_LATER);
    adminStatusMapping.put(STATUS_DESCRIPTION_DUE, Admin.DUE);
    adminStatusMapping.put(STATUS_DESCRIPTION_OVERDUE, Admin.OVERDUE);
    adminStatusMapping.put(STATUS_DESCRIPTION_FINISHED, Admin.FINISHED);
    adminStatusMapping.put(STATUS_DESCRIPTION_COMPLETE, Admin.COMPLETE);
    adminStatusMapping.put(STATUS_DESCRIPTION_CONTRAINDICATED, Admin.CONTRAINDICATED);
    adminStatusMapping.put(STATUS_DESCRIPTION_COMPLETE_FOR_SEASON, Admin.COMPLETE_FOR_SEASON);
    adminStatusMapping.put(STATUS_DESCRIPTION_ASSUMED_COMPLETE_OR_IMMUNIE, Admin.ASSUMED_COMPLETE_OR_IMMUNE);
  }

  private void addForcastItem(List<VaccineGroup> forecastItemList, String familyName, int forecastItemId) {
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

  public List<ForecastActual> queryForForecast(TestCase testCase) throws Exception {

    Map<String, ForecastActual> forecastActualMap = new HashMap<String, ForecastActual>();

    SoftwareResult softwareResult = new SoftwareResult();
    softwareResult.setSoftware(software);
    StringWriter sw = new StringWriter();
    PrintWriter logOut = null;
    if (logText) {
      logOut = new PrintWriter(sw);
    }
    String queryString = createQueryString(testCase, software, "text");
    if (logOut != null) {
      logOut.println("TCH Forecaster");
      logOut.println();
      logOut.println("Current time " + new Date());
      logOut.println("Connecting to " + software.getServiceUrl());
      logOut.println("Query " + software.getServiceUrl() + queryString);
      logOut.println();
    }
    URLConnection urlConn;
    URL url = new URL(software.getServiceUrl() + queryString);
    urlConn = url.openConnection();
    urlConn.setDoInput(true);
    urlConn.setDoOutput(true);
    urlConn.setUseCaches(true);
    urlConn.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
    urlConn.connect();

    InputStreamReader input = null;
    input = new InputStreamReader(urlConn.getInputStream());
    List<ForecastActual> list = new ArrayList<ForecastActual>();
    BufferedReader in = new BufferedReader(input);
    String line;
    if (logOut != null) {
      logOut.println("Results:");
    }
    while ((line = in.readLine()) != null) {
      if (logOut != null) {
        logOut.println(line);
      }
      line = line.trim();
      if (line.startsWith(FORECASTING)) {
        line = line.substring(FORECASTING.length());
        // Example line
        // Forecasting Influenza IIV status overdue dose 1 due 08/01/2013 valid 08/01/2013 overdue 12/01/2013 finished 01/14/2159
        int statusPos = line.indexOf(STATUS);
        if (statusPos > 0) {
          String vaccineType = line.substring(0, statusPos);
          List<VaccineGroup> forecastItemListFromMap = familyMapping.get(vaccineType);

          int dosePos = line.indexOf(DOSE);
          if (dosePos == -1) {
            dosePos = line.length();
          }
          String status = line.substring(statusPos + STATUS.length(), dosePos);
          String dose = "";
          String due = "";
          String valid = "";
          String overdue = "";
          String finished = "";
          if (dosePos < line.length()) {
            line = line.substring(dosePos + DOSE.length());
            int duePos = line.indexOf(DUE);
            int validPos = line.indexOf(VALID);
            int overduePos = line.indexOf(OVERDUE);
            int finishedPos = line.indexOf(FINISHED);
            if (duePos > 0) {
              dose = line.substring(0, duePos);
              if (validPos > duePos) {
                due = line.substring(duePos + DUE.length(), validPos);
                if (overduePos > validPos) {
                  valid = line.substring(validPos + VALID.length(), overduePos);
                  if (finishedPos > overduePos) {
                    overdue = line.substring(overduePos + OVERDUE.length(), finishedPos);
                    finished = line.substring(finishedPos + FINISHED.length());
                  }
                }
              }
            }
          }

          Admin adminStatus = adminStatusMapping.get(status);
          if (adminStatus == null) {
            adminStatus = Admin.UNKNOWN;
          }
          Date dueDate = null;
          Date validDate = null;
          Date overdueDate = null;
          Date finishedDate = null;
          if (due.length() == 10) {
            dueDate = parseDate(due);
          }
          if (valid.length() == 10) {
            validDate = parseDate(valid);
          }
          if (overdue.length() == 10) {
            overdueDate = parseDate(overdue);
          }
          if (finished.length() == 10) {
            finishedDate = parseDate(finished);
          }

          if (forecastItemListFromMap != null) {
            for (VaccineGroup forecastItem : forecastItemListFromMap) {
              if (forecastItem != null) {
                ForecastActual forecastActual = new ForecastActual();
                forecastActual.setSoftwareResult(softwareResult);
                forecastActual.setVaccineGroup(forecastItem);
                forecastActual.setAdmin(adminStatus);
                if (adminStatus != Admin.COMPLETE && adminStatus != Admin.COMPLETE_FOR_SEASON
                    && adminStatus != Admin.FINISHED) {
                  forecastActual.setDoseNumber(dose);
                  forecastActual.setDueDate(dueDate);
                  forecastActual.setValidDate(validDate);
                  forecastActual.setOverdueDate(overdueDate);
                  forecastActual.setFinishedDate(finishedDate);
                }
                list.add(forecastActual);
                forecastActualMap.put(vaccineType, forecastActual);
              }
            }
          }
        }

      } else if (line.startsWith(VACCINATION_LINE_PREFIX)) {
        line = line.substring(VACCINATION_LINE_PREFIX.length());
        int pos = line.indexOf(':');
        if (pos > 0) {
          try {
            int i = Integer.parseInt(line.substring(0, pos).trim());
            int count = 0;
            for (TestEvent testEvent : testCase.getTestEventList()) {
              if (testEvent.getEvent().getEventType() == EventType.VACCINE) {
                count++;
                if (count == i) {
                  line = line.substring(pos + 1).trim();
                  int isValidPos = line.indexOf(" is a valid ");
                  int isInvalidPos = line.indexOf(" is an invalid ");
                  if (isValidPos > 0 || isInvalidPos > 0) {
                    boolean isValid = isInvalidPos == -1 && isValidPos > -1;
                    pos = isInvalidPos + 15;
                    if (isInvalidPos == -1) {
                      pos = isValidPos + 12;
                    }
                    int startPos = line.indexOf(DOSE, pos);
                    String doseName = "";
                    String vaccineCvx = "";
                    String doseNumber = "";
                    if (startPos > 0) {
                      doseName = line.substring(pos, startPos).trim();
                      vaccineCvx = evaluationToCvxMapping.get(doseName);
                      if (vaccineCvx == null) {
                        vaccineCvx = "";
                      }
                      startPos += 6;
                      int endPos = line.indexOf(".", startPos);
                      if (endPos > 0) {
                        doseNumber = line.substring(startPos, endPos);
                      }
                    }
                    EvaluationActual evaluationActual = new EvaluationActual();
                    evaluationActual.setSoftwareResult(softwareResult);
                    evaluationActual.getSoftwareResult().setSoftware(software);
                    evaluationActual.setTestEvent(testEvent);
                    evaluationActual.setDoseNumber(doseNumber);
                    evaluationActual.setDoseValid(isValid ? "Y" : "N");
                    //evaluationActual.setReasonCode(reasonCode);
                    evaluationActual.setReasonText(line);
                    evaluationActual.setSeriesUsedCode(vaccineCvx);
                    evaluationActual.setSeriesUsedText(doseName);
                    evaluationActual.setVaccineCvx(vaccineCvx);

                    if (testEvent.getEvaluationActualList() == null) {
                      testEvent.setEvaluationActualList(new ArrayList<EvaluationActual>());
                    }
                    testEvent.getEvaluationActualList().add(evaluationActual);

                  }
                  break;
                }
              }
            }
          } catch (NumberFormatException nfe) {
            // ignore
          }
        }
      } else if (line.startsWith(DETAILS_FOR_PREFIX)) {
        String vaccineType = line.substring(DETAILS_FOR_PREFIX.length());
        ForecastActual forecastActual = forecastActualMap.get(vaccineType);
        String html = in.readLine();
        if (forecastActual != null && html != null) {
          forecastActual.setExplanationHtml(html);
        }
      }
    }
    input.close();
    if (logOut != null) {
      logOut.close();
      softwareResult.setLogText(sw.toString());
    }

    return list;
  }

  public static String createQueryString(TestCase testCase, Software software, String format) {
    StringBuilder sb = new StringBuilder();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    sb.append("?evalDate=" + sdf.format(testCase.getEvalDate()));
    sb.append("&evalSchedule=");
    sb.append("&resultFormat=" + format);
    sb.append("&patientDob=" + sdf.format(testCase.getPatientDob()) + "");
    sb.append("&patientSex=" + testCase.getPatientSex() + "");
    int count = 0;
    for (TestEvent testEvent : testCase.getTestEventList()) {
      if (testEvent.getEvent() != null && testEvent.getEvent().getEventType() == EventType.VACCINE) {
        count++;
        sb.append("&vaccineDate" + count + "=" + sdf.format(testEvent.getEventDate()));
        sb.append("&vaccineCvx" + count + "=" + testEvent.getEvent().getVaccineCvx());
        sb.append("&vaccineMvx" + count + "=" + testEvent.getEvent().getVaccineMvx());
      }
    }

    Map<String, String> settingsMap = new HashMap<String, String>();
    if (software.getSoftwareSettingList() != null) {
      for (SoftwareSetting softwareSetting : software.getSoftwareSettingList()) {
        settingsMap.put(softwareSetting.getServiceOption().getOptionName(), softwareSetting.getOptionValue());
      }
    }
    if (testCase.getTestCaseSettingList() != null) {
      for (TestCaseSetting testCaseSetting : testCase.getTestCaseSettingList()) {
        settingsMap.put(testCaseSetting.getServiceOption().getOptionName(), testCaseSetting.getOptionValue());
      }
    }

    try {
      for (String key : settingsMap.keySet()) {
        sb.append("&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(settingsMap.get(key), "UTF-8"));
      }
    } catch (UnsupportedEncodingException uee) {
      uee.printStackTrace();
    }

    return sb.toString();
  }

  private Date parseDate(String s) {
    Date date = null;
    if (s == null) {
      return null;
    }

    if (s.length() > 0) {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      try {
        date = sdf.parse(s);
      } catch (ParseException pe) {
        // ignore
      }
    }
    return date;
  }

}
