package org.tch.fc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Fake
{
  
  private static Fake singleton = null;
  public static Fake getFake()
  {
    if (singleton == null){
      singleton = new Fake();
    }
    return singleton;
  }
  
  private Map<String, List<String[]>> conceptMap = null;
  private Random random = new Random();


  public Random getRandom() {
    return random;
  }


  protected Fake() {
    try {
      BufferedReader in = new BufferedReader(
          new InputStreamReader(getClass().getResourceAsStream("fake.txt")));
      conceptMap = readDataIn(in);
    } catch (IOException e) {
      e.printStackTrace();
      conceptMap = new HashMap<String, List<String[]>>();
    }
  }
  
  
  public String getRandomValue(String concept) {
    try {
      return getValue(concept, 0);
    } catch (IOException ioe) {
      return "Unable to get value: " + ioe.getMessage();
    }
  }
  
  public String[] getValueArray(String concept, int size) {
    String[] valueSourceList = null;
    List<String[]> valueList = null;
    if (valueList == null) {
      valueList = conceptMap.get(concept);
    }
    if (valueList != null) {
      valueSourceList = valueList.get(random.nextInt(valueList.size()));
    }
    String[] values = new String[size];
    for (int i = 0; i < values.length; i++) {
      if (valueSourceList != null && i < valueSourceList.length) {
        values[i] = valueSourceList[i];
      } else {
        values[i] = "";
      }
    }
    return values;
  }
  
  public String getValue(String concept, int pos) throws IOException {
    List<String[]> valueList = conceptMap.get(concept);
    if (valueList != null) {
      return getRandomValue(pos, valueList);
    }
    return "";
  }


  public String getRandomValue(int pos, List<String[]> valueList) {
    String[] values = valueList.get(random.nextInt(valueList.size()));
    if (pos < values.length) {
      return values[pos];
    }
    return "";
  }


  public HashMap<String, List<String[]>> readDataIn(BufferedReader in) throws IOException {
    HashMap<String, List<String[]>> map = new HashMap<String, List<String[]>>();
    String line;
    while ((line = in.readLine()) != null) {
      int equals = line.indexOf("=");
      if (equals != -1) {
        String concept = line.substring(0, equals);
        String[] values = line.substring(equals + 1).split("\\,");
        List<String[]> valueList = map.get(concept);
        if (valueList == null) {
          valueList = new ArrayList<String[]>();
          map.put(concept, valueList);
        }
        valueList.add(values);
      }
    }
    return map;
  }


}
