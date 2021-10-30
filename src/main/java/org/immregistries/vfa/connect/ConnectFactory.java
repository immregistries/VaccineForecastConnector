package org.immregistries.vfa.connect;

import java.util.List;
import org.immregistries.vfa.connect.model.Service;
import org.immregistries.vfa.connect.model.Software;
import org.immregistries.vfa.connect.model.VaccineGroup;

public class ConnectFactory {


  public static ConnectorInterface createConnecter(Software software) throws Exception {
    return createConnecter(software, VaccineGroup.getForecastItemList());
  }

  public static ConnectorInterface createConnecter(Software software,
      List<VaccineGroup> forecastItemList) throws Exception {

    if (software.getService() == Service.LSVF) {
      return new LSVFConnector(software, forecastItemList);
    }
    if (software.getService() == Service.WEB1) {

    }
    if (software.getService() == Service.SWP) {
      return new SWPConnector(software, forecastItemList);
    }
    if (software.getService() == Service.STC) {
      return new STCConnector(software, forecastItemList);
    }
    if (software.getService() == Service.ICE) {
      return new ICEConnector(software, forecastItemList);
    }
    if (software.getService() == Service.IIS) {
      return new IISConnector(software, forecastItemList);
    }
    if (software.getService() == Service.MES_VACCINE) {
      return new MesVaccinsConnector(software, forecastItemList);
    }
    return null;
  }


}
