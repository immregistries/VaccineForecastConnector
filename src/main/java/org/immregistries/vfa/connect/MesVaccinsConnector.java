package org.immregistries.vfa.connect;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.immregistries.vfa.connect.mesvaccins.MesVaccinsDisease;
import org.immregistries.vfa.connect.mesvaccins.MesVaccinsVaccine;
import org.immregistries.vfa.connect.model.Admin;
import org.immregistries.vfa.connect.model.ForecastActual;
import org.immregistries.vfa.connect.model.Software;
import org.immregistries.vfa.connect.model.SoftwareResult;
import org.immregistries.vfa.connect.model.TestCase;
import org.immregistries.vfa.connect.model.TestEvent;
import org.immregistries.vfa.connect.model.VaccineGroup;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class MesVaccinsConnector implements ConnectorInterface {

  private Software software = null;

  private static Map<Integer, String> mesVaccinsVaccineIdMap = new HashMap<>();

  static {
    mesVaccinsVaccineIdMap.put(107, "136");
    mesVaccinsVaccineIdMap.put(137, "156");
    mesVaccinsVaccineIdMap.put(188, "160");
    mesVaccinsVaccineIdMap.put(17, "143");
    mesVaccinsVaccineIdMap.put(88, "110");
    mesVaccinsVaccineIdMap.put(45, "101");
    mesVaccinsVaccineIdMap.put(85, "102");
    mesVaccinsVaccineIdMap.put(21, "159");
    mesVaccinsVaccineIdMap.put(03, "130");
    mesVaccinsVaccineIdMap.put(122, "158");
    mesVaccinsVaccineIdMap.put(152, "157");
    mesVaccinsVaccineIdMap.put(111, "233");
    mesVaccinsVaccineIdMap.put(89, "135");
    mesVaccinsVaccineIdMap.put(121, "160");
    mesVaccinsVaccineIdMap.put(33, "24");
    mesVaccinsVaccineIdMap.put(9, "137");
    mesVaccinsVaccineIdMap.put(115, "136");
    mesVaccinsVaccineIdMap.put(108, "152");
    mesVaccinsVaccineIdMap.put(114, "69");
    mesVaccinsVaccineIdMap.put(147, "103");
  }

  private static Map<Integer, Integer> mesVaccinsDiseaseMap = new HashMap<>();
  private static Set<Integer> mesVaccinsDiseaseIgnoredSet = new HashSet<>();

  static {
    mesVaccinsDiseaseMap.put(1, 19);
    mesVaccinsDiseaseIgnoredSet.add(2);
    mesVaccinsDiseaseIgnoredSet.add(3);
    mesVaccinsDiseaseMap.put(4, 11);
    mesVaccinsDiseaseMap.put(5, 6);
    mesVaccinsDiseaseMap.put(6, 5);
    mesVaccinsDiseaseMap.put(8, 9);
    mesVaccinsDiseaseIgnoredSet.add(9);
    mesVaccinsDiseaseIgnoredSet.add(10);
    mesVaccinsDiseaseMap.put(11, 13);
    mesVaccinsDiseaseIgnoredSet.add(14);
    mesVaccinsDiseaseMap.put(15, 3);
    mesVaccinsDiseaseMap.put(16, 4);
    mesVaccinsDiseaseIgnoredSet.add(17);
    mesVaccinsDiseaseIgnoredSet.add(18);
    mesVaccinsDiseaseMap.put(25, 10);
    mesVaccinsDiseaseIgnoredSet.add(27);
    mesVaccinsDiseaseIgnoredSet.add(28);
    mesVaccinsDiseaseIgnoredSet.add(29);
    mesVaccinsDiseaseIgnoredSet.add(30);
    mesVaccinsDiseaseMap.put(32, 12);
    mesVaccinsDiseaseMap.put(33, 14);
    mesVaccinsDiseaseIgnoredSet.add(34);
    mesVaccinsDiseaseIgnoredSet.add(35);
    mesVaccinsDiseaseIgnoredSet.add(36);
    mesVaccinsDiseaseMap.put(38, 7);
    mesVaccinsDiseaseMap.put(39, 8);
    mesVaccinsDiseaseIgnoredSet.add(40);
    mesVaccinsDiseaseIgnoredSet.add(41);
    mesVaccinsDiseaseIgnoredSet.add(42);
    mesVaccinsDiseaseIgnoredSet.add(43);
    mesVaccinsDiseaseIgnoredSet.add(44);
    mesVaccinsDiseaseIgnoredSet.add(45);
    mesVaccinsDiseaseIgnoredSet.add(47);
    mesVaccinsDiseaseIgnoredSet.add(49);
    mesVaccinsDiseaseIgnoredSet.add(50);
    mesVaccinsDiseaseIgnoredSet.add(51);
    mesVaccinsDiseaseIgnoredSet.add(52);
    mesVaccinsDiseaseIgnoredSet.add(53);
    mesVaccinsDiseaseIgnoredSet.add(54);
  }



  public static class Vaccine {
    private int id = 0;
    private String name;

    public void setId(int id) {
      this.id = id;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public Vaccine() {
      // default
    }
  }

  public List<MesVaccinsVaccine> getVaccines() throws IOException {
    List<MesVaccinsVaccine> vaccineList = new ArrayList<>();
    HttpURLConnection urlConn;
    InputStreamReader input = null;
    String query = software.getServiceUrl() + "/vaccines.json?include_foreigns=1";
    URL url = new URL(query);

    urlConn = (HttpURLConnection) url.openConnection();
    urlConn.setRequestMethod("GET");
    String authorization = software.getServiceUserid() + ":" + software.getServicePassword();
    authorization = Base64.getEncoder().encodeToString((authorization).getBytes());
    urlConn.setRequestProperty("Authorization", "Basic " + authorization);
    urlConn.setDoInput(true);
    urlConn.setDoOutput(false);
    urlConn.setUseCaches(false);
    System.out.println("--> sending request");

    try {
      input = new InputStreamReader(urlConn.getInputStream(), "UTF-8");
      JsonReader reader = new JsonReader(input);
      handleArray(reader, vaccineList, null);
      input.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
      input = new InputStreamReader(urlConn.getErrorStream(), "UTF-8");
      StringBuilder response = getResponse(input);
      input.close();
      System.out.println(response);
    }
    return vaccineList;
  }

  /**
   * Handle a json array. The first token would be JsonToken.BEGIN_ARRAY.
   * Arrays may contain objects or primitives.
   *
   * @param reader
   * @throws IOException
   */
  private static void handleArray(JsonReader reader, List<MesVaccinsVaccine> vaccineList,
      String flowsUnder) throws IOException {
    reader.beginArray();
    while (true) {
      JsonToken token = reader.peek();
      if (token.equals(JsonToken.END_ARRAY)) {
        reader.endArray();
        break;
      } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
        handleObject(reader, vaccineList, flowsUnder);
      } else if (token.equals(JsonToken.END_OBJECT)) {
        reader.endObject();
      } else
        handleNonArrayToken(reader, token, vaccineList, null, flowsUnder);
    }
  }

  /**
   * Handle an Object. Consume the first token which is BEGIN_OBJECT. Within
   * the Object there could be array or non array tokens. We write handler
   * methods for both. Noe the peek() method. It is used to find out the type
   * of the next token without actually consuming it.
   *
   * @param reader
   * @throws IOException
   */
  private static void handleObject(JsonReader reader, List<MesVaccinsVaccine> vaccineList,
      String flowsUnder) throws IOException {
    String flowsUnderNext = flowsUnder;
    reader.beginObject();
    String name = null;
    while (reader.hasNext()) {
      JsonToken token = reader.peek();
      if (token.equals(JsonToken.BEGIN_ARRAY)) {
        handleArray(reader, vaccineList, flowsUnderNext);
      } else if (token.equals(JsonToken.END_OBJECT)) {
        reader.endObject();
        return;
      } else {
        name = handleNonArrayToken(reader, token, vaccineList, name, flowsUnder);
        flowsUnderNext = name;
      }
    }
  }

  private static final String MES_VACCINS_NAME = "name";
  private static final String MES_VACCINS_ID = "id";

  private static final String MES_VACCINS_DISEASES = "diseases";

  /**
   * Handle non array non object tokens
   *
   * @param reader
   * @param token
   * @throws IOException
   */
  private static String handleNonArrayToken(JsonReader reader, JsonToken token,
      List<MesVaccinsVaccine> vaccineList, String name, String flowsUnder) throws IOException {
    if (token.equals(JsonToken.NAME)) {
      name = reader.nextName();
      if (name.equals(MES_VACCINS_ID)) {
        if (flowsUnder == null) {
          MesVaccinsVaccine v = new MesVaccinsVaccine();
          vaccineList.add(v);
        } else if (flowsUnder.equals(MES_VACCINS_DISEASES)) {
          if (vaccineList.size() > 0) {
            MesVaccinsVaccine v = vaccineList.get(vaccineList.size() - 1);
            MesVaccinsDisease disease = new MesVaccinsDisease();
            v.setDisease(disease);
          }
        }
      }
    } else if (token.equals(JsonToken.STRING)) {
      String s = reader.nextString();
      if (vaccineList.size() > 0) {
        MesVaccinsVaccine v = vaccineList.get(vaccineList.size() - 1);
        if (MES_VACCINS_NAME.equals(name)) {
          if (flowsUnder == null) {
            v.setName(s);
          } else if (flowsUnder.equals(MES_VACCINS_DISEASES)) {
            MesVaccinsDisease disease = v.getDisease();
            disease.setName(s);
          }
        }
      }
    } else if (token.equals(JsonToken.NUMBER)) {
      Double d = reader.nextDouble();
      if (vaccineList.size() > 0) {
        MesVaccinsVaccine v = vaccineList.get(vaccineList.size() - 1);
        if (MES_VACCINS_ID.equals(name)) {
          if (flowsUnder == null) {
            v.setId(d.intValue());
          } else if (flowsUnder.equals(MES_VACCINS_DISEASES)) {
            MesVaccinsDisease disease = v.getDisease();
            disease.setId(d.intValue());
          }
        }
      }
    } else {
      reader.skipValue();
    }
    return name;
  }


  public MesVaccinsConnector(Software software, List<VaccineGroup> forecastItemList) {
    this.software = software;
  }

  @Override
  public List<ForecastActual> queryForForecast(TestCase testCase, SoftwareResult softwareResult)
      throws Exception {

    List<ForecastActual> forecastActualList = new ArrayList<>();
    HttpURLConnection urlConn;
    InputStreamReader input = null;
    String query = software.getServiceUrl() + "/decision_support/immunisation_assessment.json";
    URL url = new URL(query);

    urlConn = (HttpURLConnection) url.openConnection();
    urlConn.setRequestMethod("POST");
    String authorization = software.getServiceUserid() + ":" + software.getServicePassword();
    authorization = Base64.getEncoder().encodeToString((authorization).getBytes());
    urlConn.setRequestProperty("Authorization", "Basic " + authorization);
    urlConn.setDoInput(true);
    urlConn.setDoOutput(true);
    urlConn.setUseCaches(false);


    DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
    StringWriter stringWriter = new StringWriter();
    PrintWriter out = new PrintWriter(stringWriter);
    out.println("{");
    {
      SimpleDateFormat sdfDateOnly = new SimpleDateFormat("yyyy-MM-dd");
      out.println("    \"birth_date\": \"" + sdfDateOnly.format(testCase.getPatientDob()) + "\",");
    }
    out.println("    \"gender\": \""
        + (testCase.getPatientSex().equalsIgnoreCase("M") ? "male" : "female") + "\",");
    out.println("    \"condition_ids\": [],");
    out.println("    \"vaccinations\": [");
    boolean first = true;
    for (TestEvent testEvent : testCase.getTestEventList()) {
      String mesVaccinsVaccineId = mesVaccinsVaccineIdMap.get(testEvent.getEvent().getEventId());
      if (mesVaccinsVaccineId != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        if (first) {
          first = false;
        } else {
          out.println("      ,");
        }
        out.println("      {");
        out.println("        \"date\": \"" + sdf.format(testEvent.getEventDate()) + "\",");
        out.println("        \"vaccine_id\": " + mesVaccinsVaccineId + ",");
        out.println("        \"booster\": false");
        out.println("      }");
      }
    }
    out.println("    ],");
    out.println("    \"user_type\": \"professional\"");
    out.println("}");
    String messageBeingSent = stringWriter.toString();
    printout.writeBytes(messageBeingSent);
    printout.flush();
    printout.close();

    System.out.println("--> sending request");

    try {
      input = new InputStreamReader(urlConn.getInputStream(), "UTF-8");
      JsonReader reader = new JsonReader(input);
      responseObject(reader, forecastActualList, null);
      input.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
      input = new InputStreamReader(urlConn.getErrorStream(), "UTF-8");
      StringBuilder response = getResponse(input);
      input.close();
      System.out.println(response);
    }

    for (Iterator<ForecastActual> it = forecastActualList.iterator(); it.hasNext();) {
      ForecastActual forecastActual = it.next();
      if (forecastActual.getVaccineGroup() == null) {
        it.remove();
      }
    }

    return forecastActualList;
  }

  public StringBuilder getResponse(InputStreamReader input) throws IOException {
    StringBuilder response = new StringBuilder();
    BufferedReader in = new BufferedReader(input);
    String line;
    while ((line = in.readLine()) != null) {
      response.append(line);
      response.append("\n");
    }
    return response;
  }

  @Override
  public void setLogText(boolean logText) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isLogText() {
    // TODO Auto-generated method stub
    return false;
  }



  /**
   * Handle a json array. The first token would be JsonToken.BEGIN_ARRAY.
   * Arrays may contain objects or primitives.
   *
   * @param reader
   * @throws IOException
   */
  private static void responseArray(JsonReader reader, List<ForecastActual> forecastActualList,
      String flowsUnder) throws IOException {
    reader.beginArray();
    while (true) {
      JsonToken token = reader.peek();
      if (token.equals(JsonToken.END_ARRAY)) {
        reader.endArray();
        break;
      } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
        responseObject(reader, forecastActualList, flowsUnder);
      } else if (token.equals(JsonToken.END_OBJECT)) {
        reader.endObject();
      } else
        responseNonArrayToken(reader, token, forecastActualList, null, flowsUnder);
    }
  }

  /**
   * Handle an Object. Consume the first token which is BEGIN_OBJECT. Within
   * the Object there could be array or non array tokens. We write handler
   * methods for both. Noe the peek() method. It is used to find out the type
   * of the next token without actually consuming it.
   *
   * @param reader
   * @throws IOException
   */
  private static void responseObject(JsonReader reader, List<ForecastActual> forecastActualList,
      String flowsUnder) throws IOException {
    String flowsUnderNext = flowsUnder;
    reader.beginObject();
    String name = null;
    while (reader.hasNext()) {
      JsonToken token = reader.peek();
      if (token.equals(JsonToken.BEGIN_ARRAY)) {
        responseArray(reader, forecastActualList, flowsUnderNext);
      } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
        responseObject(reader, forecastActualList, flowsUnderNext);
      } else if (token.equals(JsonToken.END_OBJECT)) {
        reader.endObject();
        return;
      } else {
        name = responseNonArrayToken(reader, token, forecastActualList, name, flowsUnder);
        flowsUnderNext = name;
      }
    }
  }

  private static final String MES_VACCINS_DISEASE = "disease";
  private static final String MES_VACCINS_DOSE_COUNT = "dose_count";
  private static final String MES_VACCINS_SCHEDULE_STATUS = "schedule_status";
  private static final String MES_VACCINS_NEXT_VACCINATION_DATE = "next_vaccination_date";


  /**
   * Handle non array non object tokens
   *
   * @param reader
   * @param token
   * @throws IOException
   */
  private static String responseNonArrayToken(JsonReader reader, JsonToken token,
      List<ForecastActual> forecastActualList, String name, String flowsUnder) throws IOException {
    if (token.equals(JsonToken.NAME)) {
      name = reader.nextName();
      if (name.equals(MES_VACCINS_ID)) {
        if (MES_VACCINS_DISEASE.equals(flowsUnder)) {
          ForecastActual forecastActual = new ForecastActual();
          forecastActualList.add(forecastActual);
        }
      }
    } else if (token.equals(JsonToken.STRING)) {
      System.out.println("--> ");
      String s = reader.nextString();
      if (forecastActualList.size() > 0) {
        ForecastActual forecastActual = forecastActualList.get(forecastActualList.size() - 1);
        if (MES_VACCINS_NEXT_VACCINATION_DATE.equals(name)) {
          try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            forecastActual.setDueDate(sdf.parse(s));
          } catch (ParseException pe) {
            // ignore 
          }
        } else if (MES_VACCINS_SCHEDULE_STATUS.equals(name)) {
          if (s.equals("up-to-date")) {
            // forecastActual.setAdmin(Admin.DUE_LATER);
          }
        }
      }
    } else if (token.equals(JsonToken.NUMBER)) {
      Double d = reader.nextDouble();
      if (forecastActualList.size() > 0) {
        ForecastActual forecastActual = forecastActualList.get(forecastActualList.size() - 1);
        if (MES_VACCINS_ID.equals(name)) {
          if (MES_VACCINS_DISEASE.equals(flowsUnder)) {
            if (!mesVaccinsDiseaseIgnoredSet.contains(d.intValue())) {
              Integer id = mesVaccinsDiseaseMap.get(d.intValue());
              if (id == null) {
                System.err.println("Unrecognized disease id " + d.intValue());
              } else {
                VaccineGroup forecastItem = VaccineGroup.getForecastItem(id);
                forecastActual.setVaccineGroup(forecastItem);
              }
            }
          }
        } else if (MES_VACCINS_DOSE_COUNT.equals(name)) {
          forecastActual.setDoseNumber("" + (d.intValue() + 1));
        }
      }
    } else {
      reader.skipValue();
    }
    return name;
  }
}
