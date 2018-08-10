package org.tch.fc.util;

import java.util.Date;

import org.tch.fc.model.TestCase;

public class FakePatient
{
  private String mrn = "";
  private String nameLast = "";
  private String nameFirst = "";
  private String nameMiddle = "";
  private String maidenLast = "";
  private String maidenFirst = "";
  private String motherLast = "";
  private String motherFirst = "";
  private Date patientDob = null;
  private String patientSex = "";
  private String addressLine1 = "";
  private String addressCity = "";
  private String addressState = "";
  private String addressZip = "";
  private String phone = "";

  public String getMrn() {
    return mrn;
  }
  
  public String getNameLast() {
    return nameLast;
  }

  public String getNameFirst() {
    return nameFirst;
  }

  public String getNameMiddle() {
    return nameMiddle;
  }

  public String getMaidenLast() {
    return maidenLast;
  }

  public String getMaidenFirst() {
    return maidenFirst;
  }

  public String getMotherLast() {
    return motherLast;
  }

  public String getMotherFirst() {
    return motherFirst;
  }

  public Date getPatientDob() {
    return patientDob;
  }

  public String getPatientSex() {
    return patientSex;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public String getAddressCity() {
    return addressCity;
  }

  public String getAddressState() {
    return addressState;
  }

  public String getAddressZip() {
    return addressZip;
  }

  public String getPhone() {
    return phone;
  }

  public FakePatient(TestCase testCase, String mrn) {
    Fake fake = Fake.getFake();
    this.mrn = mrn;
    nameLast = fake.getRandomValue("LAST_NAME");
    nameFirst = fake.getRandomValue("GIRL");
    nameMiddle = fake.getRandomValue("BOY");
    motherFirst = fake.getRandomValue("GIRL");
    maidenFirst = motherFirst;
    maidenLast = fake.getRandomValue("LAST_NAME");
    motherLast = fake.getRandomValue("LAST_NAME");
    patientDob = testCase.getPatientDob();
    patientSex = testCase.getPatientSex();
    String address[] = fake.getValueArray("ADDRESS", 4);
    addressLine1 = (fake.getRandom().nextInt(400) + 1) + " " + fake.getRandomValue("LAST_NAME") + " "
        + fake.getRandomValue("STREET_ABBREVIATION");
    addressCity = address[0];
    addressState = address[1];
    addressZip = address[2];
    phone = address[3] + (fake.getRandom().nextInt(8) + 2) + fake.getRandom().nextInt(10) + fake.getRandom().nextInt(10)
        + fake.getRandom().nextInt(10) + fake.getRandom().nextInt(10) + fake.getRandom().nextInt(10)
        + fake.getRandom().nextInt(10);

  }
}
