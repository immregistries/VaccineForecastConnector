package org.tch.fc;

import java.util.List;

import org.tch.fc.model.VaccineGroup;
import org.tch.fc.model.Service;
import org.tch.fc.model.Software;

public class ConnectFactory {
  
  
  public static ConnectorInterface createConnecter(Software software) throws Exception
  {
    return createConnecter(software, VaccineGroup.getForecastItemList());
  }
  
  public static ConnectorInterface createConnecter(Software software, List<VaccineGroup> forecastItemList) throws Exception
  {
    
    if (software.getService() == Service.TCH)
    {
      return new TCHConnector(software, forecastItemList);
    }
    if (software.getService() == Service.WEB1)
    {
      
    }
    if (software.getService() == Service.SWP)
    {
      return new SWPConnector(software, forecastItemList);
    }
    if (software.getService() == Service.STC)
    {
      return new STCConnector(software, forecastItemList);
    }
    if (software.getService() == Service.ICE)
    {
      return new ICEConnector(software, forecastItemList);
    }
    if (software.getService() == Service.IIS)
    {
      return new IISConnector(software, forecastItemList);
    }
    return null;
  }
  

}
