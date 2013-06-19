package org.tch.fc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ForecastItem implements Serializable {
  
  public ForecastItem()
  {
    // default;
  }
  
  public ForecastItem(int forecastItemId, String label)
  {
    this.forecastItemId = forecastItemId;
    this.label = label;
  }
  
  private static final long serialVersionUID = 1L;

  private int forecastItemId = 0;
  private String label = "";

  private static final int ID_DTAP = 2;
  private static final int ID_INFLUENZA = 3;
  private static final int ID_HEPA = 4;
  private static final int ID_HEPB = 5;
  private static final int ID_HIB = 6;
  private static final int ID_HPV = 7;
  private static final int ID_MENING = 8;
  private static final int ID_MMR = 9;
  private static final int ID_PNEUMO = 10;
  private static final int ID_POLIO = 11;
  private static final int ID_ROTA = 12;
  private static final int ID_VAR = 13;
  private static final int ID_ZOSTER = 14;
  private static final int ID_TDAP = 15;
  
  private static List<ForecastItem> forecastItemList = null; 
  
  public static final List<ForecastItem> getForecastItemList()
  {
    if (forecastItemList == null)
    {
      forecastItemList = new ArrayList<ForecastItem>();
      forecastItemList.add(new ForecastItem(ID_DTAP, "DTaP"));
      forecastItemList.add(new ForecastItem(ID_INFLUENZA, "Influenza"));
      forecastItemList.add(new ForecastItem(ID_HEPA, "HepA"));
      forecastItemList.add(new ForecastItem(ID_HEPB, "HepB"));
      forecastItemList.add(new ForecastItem(ID_HIB, "Hib"));
      forecastItemList.add(new ForecastItem(ID_HPV, "HPV"));
      forecastItemList.add(new ForecastItem(ID_MENING, "Meningococcal"));
      forecastItemList.add(new ForecastItem(ID_MMR, "MMR"));
      forecastItemList.add(new ForecastItem(ID_PNEUMO, "Pneumococcal"));
      forecastItemList.add(new ForecastItem(ID_POLIO, "Polio"));
      forecastItemList.add(new ForecastItem(ID_ROTA, "Rotavirus"));
      forecastItemList.add(new ForecastItem(ID_VAR, "Varicella"));
      forecastItemList.add(new ForecastItem(ID_ZOSTER, "HerpesZoster"));
      forecastItemList.add(new ForecastItem(ID_TDAP, "Td/Tdap"));
    }
    return forecastItemList;
  }
  
  private int[] typicalGivenYear;

  private int[] getTypicalGivenYear()
  {
    if (typicalGivenYear == null)
    {
      typicalGivenYear = initTypicalGivenYear();
    }
    return typicalGivenYear;
  }
  private int[] initTypicalGivenYear() {
    switch (forecastItemId) {
    case ID_DTAP:
      return new int[] { 0, 6 };
    case ID_INFLUENZA:
      return new int[] { 0, 120 };
    case ID_HEPA:
      return new int[] { 1, 18 };
    case ID_HEPB:
      return new int[] { 0, 18 };
    case ID_HIB:
      return new int[] { 0, 4 };
    case ID_HPV:
      return new int[] { 9, 26 };
    case ID_MENING:
      return new int[] { 11, 26 };
    case ID_MMR:
      return new int[] { 0, 18 };
    case ID_PNEUMO:
      return new int[] { 0, 6 };
    case ID_POLIO:
      return new int[] { 0, 18 };
    case ID_ROTA:
      return new int[] { 0, 1 };
    case ID_VAR:
      return new int[] { 0, 120 };
    case ID_ZOSTER:
      return new int[] { 60, 120 };
    case ID_TDAP:
      return new int[] { 7, 120 };
    }
    return new int[] { 0, 120 };
  }

  public int getTypicallyGivenYearStart() {
    return getTypicalGivenYear()[0];
  }

  public int getTypicallyGivenYearEnd() {
    return getTypicalGivenYear()[1];
  }

  public int getForecastItemId() {
    return forecastItemId;
  }

  public void setForecastItemId(int forecastItemId) {
    this.forecastItemId = forecastItemId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj instanceof ForecastItem) {
      return ((ForecastItem) obj).getForecastItemId() == this.getForecastItemId();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return forecastItemId == 0 ? super.hashCode() : forecastItemId;
  }

  @Override
  public String toString() {
    return label;
  }
  
  

}
