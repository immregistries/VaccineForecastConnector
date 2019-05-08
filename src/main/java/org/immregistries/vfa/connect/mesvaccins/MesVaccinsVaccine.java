package org.immregistries.vfa.connect.mesvaccins;

import java.util.HashSet;
import java.util.Set;

public class MesVaccinsVaccine {
  private int id = 0;
  private String name;
  private Set<MesVaccinsDisease> diseasesSet = new HashSet<>();
  private MesVaccinsDisease disease = null;
  
  public Set<MesVaccinsDisease> getDiseasesSet() {
    return diseasesSet;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public MesVaccinsVaccine()
  {
    // default
  }

  public MesVaccinsDisease getDisease() {
    return disease;
  }

  public void setDisease(MesVaccinsDisease disease) {
    this.disease = disease;
    diseasesSet.add(disease);
  }
}
