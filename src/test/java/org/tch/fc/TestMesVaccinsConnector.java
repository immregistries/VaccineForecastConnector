package org.tch.fc;

import java.io.IOException;
import org.junit.Test;
import org.tch.fc.model.Software;
import org.tch.fc.model.VaccineGroup;

public class TestMesVaccinsConnector {

  @Test
  public void test() throws IOException {
    String uid = "";
    String secret = "";
    Software software = new Software();
    software.setServiceUserid(uid);
    software.setServicePassword(secret);
    software.setServiceUrl("https://test.mesvaccins.net/api/v1");
    MesVaccinsConnector mesVaccinsConnector = new MesVaccinsConnector(software, VaccineGroup.getForecastItemList());
    mesVaccinsConnector.getVaccines();
  }

}
