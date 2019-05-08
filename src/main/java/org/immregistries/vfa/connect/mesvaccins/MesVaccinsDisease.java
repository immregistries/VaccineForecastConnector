package org.immregistries.vfa.connect.mesvaccins;

public class MesVaccinsDisease implements Comparable<MesVaccinsDisease> {
  private int id = 0;
  private String name;

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

  public MesVaccinsDisease() {
    // default
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MesVaccinsDisease) {
      MesVaccinsDisease d = (MesVaccinsDisease) obj;
      return d.getId() == getId();
    }
    return super.equals(obj);
  }


  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public int compareTo(MesVaccinsDisease o) {
    if (id < o.id) {
      return -1;
    } else if (id > o.id) {
      return 1;
    }
    return 0;
  }
}
