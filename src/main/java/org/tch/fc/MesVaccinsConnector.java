package org.tch.fc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.tch.fc.mesvaccins.MesVaccinsVaccine;
import org.tch.fc.model.ForecastActual;
import org.tch.fc.model.Software;
import org.tch.fc.model.SoftwareResult;
import org.tch.fc.model.TestCase;
import org.tch.fc.model.VaccineGroup;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class MesVaccinsConnector implements ConnectorInterface {

  private Software software = null;
  /*
   * 
   * 
   * [
  {
    "id": 175,
    "name": "A.D.T.",
    "marketing_start": null,
    "marketing_end": null,
    "french": false,
    "alternative_names": [
      "ADT",
      "A.D.T."
    ],
    "ucd_code": null,
    "cip_codes": [],
    "diseases": [
      {
        "id": 1,
        "name": "Dipht\u00e9rie"
      },
      {
        "id": 2,
        "name": "T\u00e9tanos"
      }
    ],
    "valences": [
      {"id": 2},
      {"id": 3}
    ]
  },
  "..."
  ]
   */

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
    String query = software.getServiceUrl() + "/vaccines/search.json?query=DTP&include_foreigns=1";
    URL url = new URL(query);

    urlConn = (HttpURLConnection) url.openConnection();
    urlConn.setRequestMethod("GET");
    String authorization = software.getServiceUserid() + ":" + software.getServicePassword();
    // authorization = Base64.getEncoder().encodeToString((authorization).getBytes());
    urlConn.setRequestProperty("Authorization", "Basic " + authorization);
    urlConn.setDoInput(true);
    urlConn.setDoOutput(false);
    urlConn.setUseCaches(false);
    System.out.println("--> sending request");

    input = new InputStreamReader(urlConn.getInputStream(), "UTF-8");
    BufferedReader in = new BufferedReader(input);
    String line;

    StringBuilder response = new StringBuilder();
    while ((line = in.readLine()) != null) {
      System.out.println("!!!!");
     // System.out.println("|" + line + "|");
      System.out.println("!!!!");
      response.append(line);
      response.append("\n");
    }
    input.close();
    System.out.println("--> response received");
    System.out.println("--> response length = " + response.toString().length());
    JsonReader reader = new JsonReader(new StringReader(response.toString()));
    handleObject(reader);
    return vaccineList;
  }

  /**
   * Handle a json array. The first token would be JsonToken.BEGIN_ARRAY.
   * Arrays may contain objects or primitives.
   *
   * @param reader
   * @throws IOException
   */
  private static void handleArray(JsonReader reader) throws IOException {
    reader.beginArray();
    while (true) {
      JsonToken token = reader.peek();
      if (token.equals(JsonToken.END_ARRAY)) {
        reader.endArray();
        break;
      } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
        handleObject(reader);
      } else if (token.equals(JsonToken.END_OBJECT)) {
        reader.endObject();
      } else
        handleNonArrayToken(reader, token);
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
  private static void handleObject(JsonReader reader) throws IOException {
    reader.beginObject();
    while (reader.hasNext()) {
      JsonToken token = reader.peek();
      if (token.equals(JsonToken.BEGIN_ARRAY)) {
        handleArray(reader);
      } else if (token.equals(JsonToken.END_OBJECT)) {
        reader.endObject();
        return;
      } else {
        handleNonArrayToken(reader, token);
      }
    }

  }

  /**
   * Handle non array non object tokens
   *
   * @param reader
   * @param token
   * @throws IOException
   */
  private static void handleNonArrayToken(JsonReader reader, JsonToken token) throws IOException {
    if (token.equals(JsonToken.NAME)) {
      System.out.print("--> " + reader.nextName() + " = ");
    } else if (token.equals(JsonToken.STRING)) {
      System.out.println(reader.nextString());
    } else if (token.equals(JsonToken.NUMBER)) {
      System.out.println(reader.nextDouble());
    } else {
      System.out.println();
      reader.skipValue();
    }
  }


  public MesVaccinsConnector(Software software, List<VaccineGroup> forecastItemList) {
    this.software = software;
  }

  @Override
  public List<ForecastActual> queryForForecast(TestCase testCase, SoftwareResult softwareResult)
      throws Exception {
    // TODO Auto-generated method stub
    return null;
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

}
